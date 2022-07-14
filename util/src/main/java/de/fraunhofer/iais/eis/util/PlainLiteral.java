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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Deprecated
/**
 * This class is not reflecting the characteristics of rdf:Literal properly and therefore must be replaced soon.
 * Use TypedLiteral or LocalizableString instead.
 *
 * @author sbader
 *
 */
public class PlainLiteral extends TypedLiteral {

    public PlainLiteral() {
        super();
    }

    public PlainLiteral(String valueAndLanguage) {
        super(valueAndLanguage);
    }

    public PlainLiteral(String value, String language) {
        super(value, language);
    }
    //Getters and Setters inherited from parent class

}
