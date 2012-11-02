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
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;
import org.opf_labs.fmts.AllFidgetTests;
import org.opf_labs.fmts.corpora.govdocs.GovDocs;

/**
 * TODO JavaDoc for GovDocsTestBadATM.</p>
 * TODO Tests for GovDocsTestBadATM.</p>
 * TODO Implementation for GovDocsTestBadATM.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:15:13:35
 */

public class GovDocsTestBadATM {

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocs#getNewInstance(java.io.File)}.
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetNullDir() {
		GovDocs.getNewInstance(null);
	}

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocs#getNewInstance(java.io.File)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testGetNotDir() {
		GovDocs.getNewInstance(new File("doesnt exist at all"));
	}

	/**
	 * README
	 * S:/govdocs is the govdocs dir on MY machine. This test will not fire if the directory doesn't exist
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocs#getCount()}.
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetCount() throws URISyntaxException {
		File realRoot = new File("S:/govdocs/");
		if (!realRoot.isDirectory()) return;
		
		GovDocs govDocs = GovDocs.getNewInstance(realRoot);
		System.out.println(govDocs.getCount());
		assertTrue("missing test data? count: " + govDocs.getCount(), govDocs.getCount() > 0);
	}

	/**
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetCountZip() throws URISyntaxException {
		GovDocs govDocs = GovDocs.getNewInstance(AllFidgetTests.getGovDocsZip());
		assertTrue("missing test data? count: " + govDocs.getCount(), govDocs.getCount() == 3);
	}

	/**
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetCountDir() throws URISyntaxException {
		GovDocs govDocs = GovDocs.getNewInstance(AllFidgetTests.getGovDocsDir());
		assertTrue("missing test data? count: " + govDocs.getCount(), govDocs.getCount() == 11);
	}

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocs#getSize()}.
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetSize() throws URISyntaxException {
		GovDocs govDocs = GovDocs.getNewInstance(AllFidgetTests.getGovDocsDir());
		assertTrue("No data found size: " + govDocs.getSize(), govDocs.getSize() > 0);
	}

	/**
	 * Test method for {@link org.opf_labs.fmts.corpora.govdocs.GovDocs#isZip(java.io.File)}.
	 * @throws URISyntaxException when resource lookup fails
	 */
	@SuppressWarnings("javadoc")
	@Test
	public final void testIsZip() throws URISyntaxException {
		assertTrue(GovDocsZipped.isZip(AllFidgetTests.getGovDocsZip()));
		assertFalse(GovDocsZipped.isZip(AllFidgetTests.getGovDocsDir()));
	}

}
