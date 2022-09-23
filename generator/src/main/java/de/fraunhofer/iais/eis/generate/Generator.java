/*
 * Copyright (c) 2021 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e. V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.iais.eis.generate;

import fr.inria.acacia.corese.exceptions.EngineException;
import fr.inria.edelweiss.kgraph.core.Graph;
import fr.inria.edelweiss.kgraph.query.QueryProcess;
import fr.inria.edelweiss.kgtool.load.Load;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class Generator {

    private final Collection<String> vocabDirectories;
    private final Collection<String> templateDirectories;
    private final Collection<String> vocabDirPathPatterns;
    private final int maxDepth;
    private final Graph graph = Graph.create();

    public static void main(String[] args) throws EngineException {
        CommandLineParser parser = new DefaultParser();
        Options options = createOptions();

        try {
            CommandLine line = parser.parse( options, args );
            Collection<String> vocabDirs = Arrays.asList(line.getOptionValues("vocabdirectory"));
            Collection<String> templateDirs = Arrays.asList(line.getOptionValues("templatedirectory"));
            Collection<String> vocabdirpathpattern = Arrays.asList(line.getOptionValues("vocabdirpathpattern"));
            int maxdepth = line.hasOption("maxdepth") ? Integer.parseInt(line.getOptionValue("maxdepth")) : 5;

            new Generator(vocabDirs, templateDirs, vocabdirpathpattern, maxdepth).generate();
        }
        catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "Generator", options );
        }
    }

    private static Options createOptions() {
        Options options = new Options();

        Option vocabDirs  = Option.builder("vd").argName("directory")
                .longOpt("vocabdirectory")
                .hasArg()
                .required()
                .desc( "a directory containing vocabulary files")
                .build();
        Option templateDirs   = Option.builder("td").argName("directory")
                .longOpt("templatedirectory")
                .hasArg()
                .required()
                .desc( "a directory containing sparql template files")
                .build();
        Option pathPattern   = Option.builder("pp").argName("vocab directory path pattern")
                .longOpt("vocabdirpathpattern")
                .hasArg()
                .desc( "glob pattern used to select matching vocabulary files, e.g. '**/*.ttl'")
                .build();
        Option maxDepth   = Option.builder("md").argName("maximum depth for vocab directory file search")
                .longOpt("maxdepth")
                .hasArg()
                .desc( "integer number defining the maximum depth (hierarchy levels) to descend into the vocabulary directory for finding files")
                .build();

        options.addOption(vocabDirs);
        options.addOption(templateDirs);
        options.addOption(pathPattern);
        options.addOption(maxDepth);

        return options;
    }

    private Generator(Collection<String> vocabDirectories,
                      Collection<String> templateDirectories,
                      Collection<String> vocabDirPathPatterns,
                      int maxDepth)
    {
        this.vocabDirectories = vocabDirectories;
        this.templateDirectories = templateDirectories;
        this.vocabDirPathPatterns = vocabDirPathPatterns;
        this.maxDepth = maxDepth;
    }

    private void generate() throws EngineException {
        Iterator<String> dirIterator = vocabDirectories.iterator();
        Iterator<String> dirPatternIterator = vocabDirPathPatterns.iterator();

        while (dirIterator.hasNext()) {
            String currentDir = dirIterator.next();

            while (dirPatternIterator.hasNext()) {
                String currentPattern = dirPatternIterator.next();
                try {
                    loadVocabularyFiles(currentDir, currentPattern);
                } catch (IOException e) {
                    System.err.println("Recursively getting vocabulary files failed. Skipping directory '" + currentDir + "' with pattern '" + currentPattern + "'. Reason: " + e.getMessage());
                }
            }

        }

        for (String templateDir : templateDirectories) {
            doQuery("template {st:apply-templates-with(\"" +templateDir+ "\")} where {}");
        }
    }

    private void loadVocabularyFiles(String rootDir, String pathPattern) throws IOException {
        Collection<File> files = recursivelyGetRdfFiles(rootDir, pathPattern);
        Load ld = Load.create(graph);
        files.forEach(file -> ld.load(file.getAbsolutePath()));
    }

    private Collection<File> recursivelyGetRdfFiles(String rootDir, String pathPattern) throws IOException {
        Collection<File> files = new ArrayList<>();

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:"+pathPattern);

        Files.find(Paths.get(rootDir),
                maxDepth,
                (filePath, fileAttr) -> matcher.matches(filePath)
        )
                .forEach(filePath -> files.add(filePath.toFile()));

        return files;
    }

    private void doQuery(String query) throws EngineException {
        QueryProcess exec = QueryProcess.create(graph);
        exec.query(query);
    }

}
