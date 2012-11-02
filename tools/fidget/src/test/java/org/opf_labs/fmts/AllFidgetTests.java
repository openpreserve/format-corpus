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
package org.opf_labs.fmts;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.opf_labs.fmts.fidget.OldTikaSigTesterTest;
import org.opf_labs.fmts.fidget.TikaResourceHelperTest;

/**
 * Test suite class to run all tests, plus holds test helper methods
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:11:54:06
 */
@RunWith(Suite.class)
@SuiteClasses({ OldTikaSigTesterTest.class, TikaResourceHelperTest.class })
public class AllFidgetTests {
	/** Path to the Tika sig file resource dir */
	public static final String TIKA_MIME_PATH = "org/apache/tika/mime"; 
	private static final String XML_EXT = "xml";
	/**
	 * @return all of the files
	 * @throws URISyntaxException
	 */
	public static final Collection<File> getCustomSigTestFile() throws URISyntaxException {
		return getResourceFilesByExt(TIKA_MIME_PATH, true, XML_EXT);
	}
	/**
	 * @param resName
	 *            the name of the resource to retrieve a file for
	 * @return the java.io.File for the named resource
	 * @throws URISyntaxException
	 *             if the named resource can't be converted to a URI
	 */
	public final static File getResourceAsFile(String resName)
			throws URISyntaxException {
		return new File(ClassLoader.getSystemResource(resName).toURI());
	}

	@SuppressWarnings("unused")
	private final static Collection<File> getResourceFiles(String resName,
			boolean recurse) throws URISyntaxException {
		return getResourceFilesByExt(resName, recurse, null);
	}

	private final static Collection<File> getResourceFilesByExt(String resName,
			boolean recurse, String ext) throws URISyntaxException {
		File root = getResourceAsFile(resName);
		return FileUtils.listFiles(root, new String[]{ext}, recurse);
	}

}
