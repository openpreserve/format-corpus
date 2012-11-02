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
package org.opf_labs.fmts.fidget;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opf_labs.fmts.AllFidgetTests;

/**
 * TODO JavaDoc for TikaResourceHelperTest.</p> TODO Tests for
 * TikaResourceHelperTest.</p> TODO Implementation for
 * TikaResourceHelperTest.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 2 Nov 2012:09:35:56
 */

public class TikaResourceHelperTest {
	/**
	 * Test method for
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#concat(Object[], Object[])}
	 * . Tests the ordered concatenation of arrays for Tika sig def loading
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testConcat() {
		// Create two arrays and concatenate them, then test that the result is
		// in the expected order
		Integer[] first = new Integer[] { Integer.valueOf(1),
				Integer.valueOf(2), Integer.valueOf(3) };
		Integer[] second = new Integer[] { Integer.valueOf(4),
				Integer.valueOf(5), Integer.valueOf(6) };

		Integer[] concatRes = TikaResourceHelper.concat(first, second);
		Integer counter = Integer.valueOf(0);
		for (Integer frmRes : concatRes) {
			assertTrue("Expected elements in order, frmRes:" + frmRes
					+ " <= counter:" + counter, frmRes > counter);
			counter++;
		}
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#getCoreUrl()}. Tests
	 * we just get the core Tika defs file called
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#TIKA_MIMETYPES}
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testGetCoreUrl() {
		// Just get the core URL from the helper, there can be only 1
		URL coreUrl = TikaResourceHelper.getCoreUrl();
		// Check it's name is core
		assertTrue("expecting tika mime types NOT:" + coreUrl, coreUrl
				.getPath().contains(TikaResourceHelper.TIKA_MIMETYPES));
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#getCustomUrls()}. Test
	 * that we get just ONE and it's our custom file, this SHOULDN'T pick up the
	 * test resource custom files I've added to resources that aren't called
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#CUSTOM_MIMETYPES}
	 * @throws IOException if the custom file can't be gotten
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testGetCustomUrls() throws IOException {
		// Just get the core URL from the helper, there can be only 1
		List<URL> customUrls = TikaResourceHelper.getCustomUrls();
		// There should be only one
		assertTrue("", customUrls.size() == 1);
		// Check it's name is core
		assertTrue("expecting custom mime types NOT:" + customUrls.get(0), customUrls.get(0)
				.getPath().contains(TikaResourceHelper.CUSTOM_MIMETYPES));
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#getVanillaUrls()}. Test
	 * that we get just two files, this SHOULDN'T pick up the
	 * test resource custom files I've added to resources that aren't called
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#CUSTOM_MIMETYPES}
	 * @throws IOException if the vanilla urls can't be gotten
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testGetVanillaUrls() throws IOException {
		// Just get the core URL from the helper, there can be only 1
		List<URL> vanillaUrls = TikaResourceHelper.getVanillaUrls();
		// There should be onl
		assertTrue("", vanillaUrls.size() == 2);
		// Check it's name is core
		assertTrue("expecting core mime types NOT:" + vanillaUrls.get(0), vanillaUrls.get(0)
				.getPath().contains(TikaResourceHelper.TIKA_MIMETYPES));
		assertTrue("expecting custom mime types NOT:" + vanillaUrls.get(1), vanillaUrls.get(1)
				.getPath().contains(TikaResourceHelper.CUSTOM_MIMETYPES));
	}
	
	/**
	 * Test method for
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#streamsFromFiles(java.io.File...)}.
	 * @throws URISyntaxException when the test resource lookup goes wrong.
	 * @throws IOException 
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testStreamsFromFiles() throws URISyntaxException, IOException {
		// Get the test sig-def files from the resource directory 
		List<File> custSigFileLst = new ArrayList<File>(AllFidgetTests.getCustomSigTestFile());
		File[] custSigFiles = custSigFileLst.toArray(new File[custSigFileLst.size()]);
		// OK going to collect the 64K hashes in order
		List<String> sigFileHashes = new ArrayList<String>();
		for (File sigFile : custSigFiles) {
			FileInputStream fis = new FileInputStream(sigFile);
			sigFileHashes.add(IdentificationResult.hash64K(fis));
			fis.close();
		}
		// Now convert to an array of Streams using streamsFromFiles()
		InputStream[] custSigStreams = TikaResourceHelper.streamsFromFiles(custSigFiles);
		// And again get the hashes
		List<String> sigStrHashes = new ArrayList<String>();
		for (InputStream sigStr : custSigStreams) {
			sigStrHashes.add(IdentificationResult.hash64K(sigStr));
		}
		// Make sure we got something
		assertTrue("Expected some test data", sigStrHashes.size() > 0);
		// Check that the lists are equal
		assertTrue("Hash lists should be equal", sigFileHashes.equals(sigStrHashes));
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.fmts.fidget.TikaResourceHelper#streamsFromUrls(List)}.
	 * @throws URISyntaxException when the test resource lookup goes wrong.
	 * @throws IOException 
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testStreamsFromUrls() throws URISyntaxException, IOException {
		// Get the test sig-def files from the resource directory 
		List<URL> custSigUrls = new ArrayList<URL>();
		for (File csFile : AllFidgetTests.getCustomSigTestFile()) {
			custSigUrls.add(csFile.toURI().toURL());
		}
		// OK going to collect the 64K hashes in order
		List<String> sigUrlHashes = new ArrayList<String>();
		for (URL sigUrl : custSigUrls) {
			InputStream urlStr = sigUrl.openStream();
			sigUrlHashes.add(IdentificationResult.hash64K(urlStr));
			urlStr.close();
		}
		// Now convert to an array of Streams using streamsFromFiles()
		InputStream[] custSigStreams = TikaResourceHelper.streamsFromUrls(custSigUrls);
		// And again get the hashes
		List<String> sigStrHashes = new ArrayList<String>();
		for (InputStream sigStr : custSigStreams) {
			sigStrHashes.add(IdentificationResult.hash64K(sigStr));
		}
		// Make sure we got something
		assertTrue("Expected some test data", sigStrHashes.size() > 0);
		// Check that the lists are equal
		assertTrue("Hash lists should be equal", sigUrlHashes.equals(sigStrHashes));
	}
}
