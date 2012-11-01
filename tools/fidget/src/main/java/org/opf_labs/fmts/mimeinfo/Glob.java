/**
 * Copyright (C) 2012 Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * glob elements have a pattern attribute. Any file whose name matches this
 * pattern will be given this MIME type (subject to conflicting rules in other
 * files, of course). There is also an optional weight attribute which is used
 * when resolving conflicts with other glob matches. The default weight value is
 * 50, and the maximum is 100.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Glob {

	// Of the form *.ext'
	@XmlAttribute
	private String pattern;
	
	// Integer 0-100, default 50
	@XmlAttribute
	private String weight;

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return this.pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return this.weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
}
