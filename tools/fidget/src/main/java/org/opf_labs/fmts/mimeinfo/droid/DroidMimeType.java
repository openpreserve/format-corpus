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
package org.opf_labs.fmts.mimeinfo.droid;

import java.util.List;

import org.opf_labs.fmts.mimeinfo.Magic;
import org.opf_labs.fmts.mimeinfo.MimeType;

/**
 * @author pmay
 *
 */
public class DroidMimeType extends MimeType {

	private List<String> sigIds;
	
	/**
	 * @return the sigIds
	 */
	public List<String> getSigIds() {
		return this.sigIds;
	}

	/**
	 * @param sigIds the sigIds to set
	 */
	public void setSigIds(List<String> sigIds) {
		this.sigIds = sigIds;
	}
}
