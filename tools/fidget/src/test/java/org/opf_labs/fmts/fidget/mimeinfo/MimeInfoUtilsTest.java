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
package org.opf_labs.fmts.fidget.mimeinfo;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.opf_labs.fmts.mimeinfo.MimeInfo;
import org.opf_labs.fmts.mimeinfo.MimeInfoUtils;

/**
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class MimeInfoUtilsTest {
	
	public static String PERCIPIO_XML = "src/test/resources/percipio.pdf.xml";
	public static String TIKA_XML = "src/main/resources/org/apache/tika/mime/custom-mimetypes.xml";

	@Test
	public void testPercipioParser() throws FileNotFoundException, JAXBException {
		MimeInfo mi = MimeInfoUtils.parser(new FileInputStream(PERCIPIO_XML));
		MimeInfoUtils.printer(mi);
		assertTrue("Failed to parse MimeInfo object.", mi !=  null);
		//
		mi = MimeInfoUtils.parser(new FileInputStream(TIKA_XML));
		MimeInfoUtils.printer(mi);
		//
	}

}
