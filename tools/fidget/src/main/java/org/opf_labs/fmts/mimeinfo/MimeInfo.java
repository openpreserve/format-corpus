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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Based on the MIME Info spec. source XML files: 
 *   http://standards.freedesktop.org/shared-mime-info-spec/shared-mime-info-spec-0.18.html#id2604543
 *  
 * TODO Decide whether to use the http://www.freedesktop.org/standards/shared-mime-info namespace.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
@XmlRootElement(name="mime-info")
@XmlAccessorType(XmlAccessType.FIELD)
public class MimeInfo {

    @XmlElement(name = "mime-type")
	private List<MimeType> mimetypes;

	/**
	 * @return the mimetypes
	 */
	public List<MimeType> getMimetypes() {
		return this.mimetypes;
	}

	/**
	 * @param mimetypes the mimetypes to set
	 */
	public void setMimetypes(List<MimeType> mimetypes) {
		this.mimetypes = mimetypes;
	}
	
}
