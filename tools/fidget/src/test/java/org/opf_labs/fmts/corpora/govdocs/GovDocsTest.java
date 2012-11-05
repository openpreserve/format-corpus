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
package org.opf_labs.fmts.corpora.govdocs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;
import org.opf_labs.fmts.AllFidgetTests;

/**
 * TODO JavaDoc for GovDocsTest.</p>
 * TODO Tests for GovDocsTest.</p>
 * TODO Implementation for GovDocsTest.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:15:13:35
 */

public class GovDocsTest {

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocs#newInstance(java.io.File)}.
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetNullDir() {
		GovDocs.newInstance(null);
	}

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocs#newInstance(java.io.File)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testGetNotDir() {
		GovDocs.newInstance(new File("c@n't exist?*"));
	}

	/**
	 * README
	 * S:/govdocs is the govdocs dir on MY machine. This test will not fire if the directory doesn't exist
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocsDirectories#getCount()}.
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetCount() throws URISyntaxException {
		File realRoot = new File("S:/govdocs/");
		if (!realRoot.isDirectory()) return;
		
		GovDocsCorpora govDocs = GovDocs.newInstance(realRoot);
		System.out.println(govDocs.getCount());
		assertTrue("missing test data? count: " + govDocs.getCount(), govDocs.getCount() > 0);
	}

	/**
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetCountZip() throws URISyntaxException {
		GovDocsCorpora govDocs = GovDocs.newInstance(AllFidgetTests.getGovDocsZip());
		assertTrue("missing test data? count: " + govDocs.getCount(), govDocs.getCount() == 11);
	}

	/**
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetCountDir() throws URISyntaxException {
		GovDocsCorpora govDocs = GovDocs.newInstance(AllFidgetTests.getGovDocsDir());
		assertTrue("missing test data? count: " + govDocs.getCount(), govDocs.getCount() == 11);
	}

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocsDirectories#getSize()}.
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetSize() throws URISyntaxException {
		GovDocsCorpora govDocs = GovDocs.newInstance(AllFidgetTests.getGovDocsDir());
		assertTrue("No data found size: " + govDocs.getSize(), govDocs.getSize() > 0);
	}

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocsDirectories#isZip(java.io.File)}.
	 * @throws URISyntaxException when resource lookup fails
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testIsZip() throws URISyntaxException {
		assertTrue("GovDocsZipped.isZip() != true for zip test directory??", GovDocs.isZip(AllFidgetTests.getGovDocsZip()));
		assertFalse("GovDocsZipped.isZip() == true for dir based test directory??", GovDocs.isZip(AllFidgetTests.getGovDocsDir()));
	}

}
