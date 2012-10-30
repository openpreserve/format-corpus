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
package org.opf_labs.fmts.fidget.droid;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class used to create to the PRONOM sig generation service.
 * 
 * It has been declared with example entries, but these should of course be overwritten.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class SigDefSubmission {

	public String name = "Development Signature";
	public String version = "1.0";
	public String puid = "dev/1";
	public String extension = "ext";
	public String mimetype = "text/x-test-signature";
	public List<InternalSigSubmission> signatures = new ArrayList<InternalSigSubmission>();
		
}
