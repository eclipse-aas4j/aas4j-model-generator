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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.net.URI;

//Prevent empty values from being printed - @language AND @type in combination is forbidden
//Note that the Serializer uses this class by name. If this class is renamed, make sure to adapt the MessageParser class accordingly!
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypedLiteral extends RdfResource implements Serializable {

	@JsonProperty("@language")
	private String language = null;

	public TypedLiteral() {
		super();
	}


	public TypedLiteral(String valueAndTypeOrLanguage) {
		if(valueAndTypeOrLanguage.contains("@"))
		{
			String[] splitString = valueAndTypeOrLanguage.split("@");
			this.value = splitString[0];
			this.language = splitString[1];
		}
		//.contains expects character sequence. Passing "^^"
		else if(valueAndTypeOrLanguage.contains("^^"))
		{
			//.split expects regex. "^" is meta character for "start of line", so it needs to be escaped
			String[] splitString = valueAndTypeOrLanguage.split("\\^\\^");
			this.value = splitString[0].replace("\"", "");
			this.type = splitString[1];
		}
		else
		{
			this.value = valueAndTypeOrLanguage;
			this.type = "http://www.w3.org/2001/XMLSchema#string";
		}
	}

	public TypedLiteral(String value, URI type) { super(value, type); }

	public TypedLiteral(String value, String language) {
		this.value = value;
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	//This override doesn't seem to do much...
	@Override
	public String toString() {
		String result = this.value;
		if (this.language != null && !this.language.isEmpty())
		{
			return "\"" + result + "\"@" + this.language;
		}
		if (this.type != null && !this.type.isEmpty()) return "\"" + result + "\"^^" + this.type;
		return result;
	}
}
