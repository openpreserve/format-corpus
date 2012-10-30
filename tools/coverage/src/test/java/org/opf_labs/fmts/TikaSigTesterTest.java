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
package org.opf_labs.fmts;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.SortedSet;

import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.Test;
import org.opf_labs.fmts.mimeinfo.MimeInfoUtils;
import org.opf_labs.fmts.mimeinfo.MimeInfoUtilsTest;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class TikaSigTesterTest {

	/**
	 * Test method for {@link org.opf_labs.fmts.TikaSigTester#TikaSigTester(java.io.File, boolean)}.
	 * @throws IOException 
	 * @throws MimeTypeException 
	 */
	@Test
	public void testTikaSigTesterFileBoolean() throws MimeTypeException, IOException {
		// Test loading a single signature, alone.
		TikaSigTester tst = new TikaSigTester(new File(MimeInfoUtilsTest.PERCIPIO_XML), false);
		//this.printMimeTypes(tst.getMimeTypes());
		SortedSet<MediaType> types = tst.getMimeTypes().getMediaTypeRegistry().getTypes();
		int size = types.size();
		assertEquals("Unexpected number of signatures: "+size, size, 4 );
		// Test using the trial signature
		System.out.println("Try: "+tst.identify( new FileInputStream( "../.././save-as/OpenOffice.org 3.2.0 OSX/pdf-features/simple.pdf" ) ));
		
		// Test loading all signatures:
		tst = new TikaSigTester( new File(MimeInfoUtilsTest.PERCIPIO_XML), true);
		//this.printMimeTypes(tst.getMimeTypes());
		types = tst.getMimeTypes().getMediaTypeRegistry().getTypes();
		size = types.size();
		assertTrue("Unexpected number of signatures: "+size, size > 4 );
		
	}
	
	public void printMimeTypes(MimeTypes mimeTypes) {
		System.out.println("Total: "+mimeTypes.getMediaTypeRegistry().getTypes().size());
		for( MediaType mt : mimeTypes.getMediaTypeRegistry().getTypes()) {
			System.out.println("MediaType: "+mt.toString());
		}
	}

}
