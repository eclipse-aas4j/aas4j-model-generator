# Eclipse AAS4J - AAS Code Generation Tool for Java

This is a tool for the automated creation of a Java library containing all classes, properties and enumerations of the Asset Administration Shell (AAS) Metamodel. The tool itself is completly written in Java and uses the shapes constraint language (SHACL) to specify annotations and cardinalities for the Java classes.

# Build and Use

Before running the tool, a profile needs to be created. This profile contains information about where the ontology sources reside. In the [pom.xml](pom.xml), add your own profile with a (local) path to your ontology. There are multiple profiles present already which can be used as reference.

Once the profile has been created, you can build the project using Maven by simply executing at the repository root:

`mvn clean package -P <Profile Name>`

# Project Structure

The project contains several modules:

- `aas` Module containing shapes translating RDF into Java classes matching specific requirements of projects regarding the Asset Administration Shell.
- `common` Module containing shapes translating RDF into Java classes.
- `util` Module containing some generic utility classes, such as custom annotations.
- `generator` This module loads all data required for the translation process from RDF ontologies to Java classes, including the shapes and the ontology.

# How to Contribute

We always look for contributions, bug reports, feature requests etc. Simply open an [issue](https://github.com/eclipse-digitaltwin/aas4j-model-generator/issues) or - even better - directly propose a change through a [pull request](https://github.com/eclipse-digitaltwin/aas4j-model-generator/pulls).

# Documentation

A detailed documentation how to use the tool and how to prepare the input and the environment is provided [here](./aas/README.md).

# Contributors

An updated list of the committers can be found here: https://projects.eclipse.org/projects/dt.aas4j/who

This project was initiated by SAP and Fraunhofer to provide a foundation for the
AAS development and to foster its dissemination.

## License
Copyright (c) 2021 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e. V.

Copyright 2021 SAP SE or an SAP affiliate company and Eclipse AAS4J contributors. 

The serializers contained in this repository provide the functionalities to serialize and deserialize instances of the Asset Administration Shell (AAS) data model from and to the AAS Java Model library. It is licensed under the Apache License
2.0 (see [LICENSE](https://github.com/eclipse-aas4j/aas4j-model-generator/blob/main/LICENSE)).

The Model uses the concepts of the document "Specifications of Asset Administration Shell" published on [Industrial Digital Twin Association (IDTA)](https://industrialdigitaltwin.org) which is licensed under Creative Commons [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/).
