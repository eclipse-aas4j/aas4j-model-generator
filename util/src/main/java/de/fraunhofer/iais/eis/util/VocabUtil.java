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

package de.fraunhofer.iais.eis.util;

import de.fraunhofer.iais.eis.spi.BeanSerializer;
import de.fraunhofer.iais.eis.spi.BeanValidator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.UUID;

/**
 * Created by christian on 31.01.17.
 */
public class VocabUtil {

    private final ServiceLoader<BeanSerializer> serializerLoader;
    private final ServiceLoader<BeanValidator> validatorLoader;

    private final static String PROTOCOL = "https";
    private final static String HOST = "w3id.org";

    public static String randomUrlBase;

    public VocabUtil() {
        serializerLoader = ServiceLoader.load(BeanSerializer.class);
        validatorLoader = ServiceLoader.load(BeanValidator.class);
    }

    /**
     * This function used to be the sole access point to the private constructor in a singleton class
     * Seeing the issues regarding thread safety, this approach was abandoned, making this function unnecessary
     * @return new VocabUtil instance
     * @deprecated use constructor directly instead
     */
    @Deprecated
    public static VocabUtil getInstance() {
        return new VocabUtil();
    }

    /**
     * Generates a random URL starting with https://w3id.org/idsa/autogen/{path}/RANDOM-STRING
     * This behaviour can be altered by setting the static variable randomUrlBase. Then, the URL will be randomUrlBase/{path}/RANDOM-STRING
     * @param path Path to be appended to base URL
     * @return randomized URL according to predefined schema
     */
    public URI createRandomUrl(String path) {
        try {
            if(randomUrlBase != null)
            {
                if(!randomUrlBase.endsWith("/"))
                {
                    randomUrlBase += "/";
                }
                return new URL(randomUrlBase + path + "/" + UUID.randomUUID()).toURI();
            }
            return new URL(PROTOCOL, HOST, "/idsa/autogen/" + path + "/" + UUID.randomUUID()).toURI();
        }
        catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void validate(T objToValidate) throws ConstraintViolationException {
        for (BeanValidator beanValidator : validatorLoader) {
            beanValidator.validate(objToValidate);
        }
    }

    public String toRdf(Object obj) {
        Iterator<BeanSerializer> iterator = serializerLoader.iterator();
        return iterator.hasNext() ? iterator.next().toRdf(obj) : "";
    }

    public <T> T fromRdf(String rdf, Class<T> valueType) {
        Iterator<BeanSerializer> iterator = serializerLoader.iterator();
        return iterator.hasNext() ? iterator.next().fromRdf(rdf, valueType) : null;
    }

    public <T> T getByString(T[] values, String label) {
        for (T value : values) {
            if (value.toString().equals(label)) return value;
        }
        return null;
    }

}
