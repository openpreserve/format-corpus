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

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.opf_labs.fmts.corpora.Corpora;
import org.opf_labs.fmts.corpora.Corpora.Details;
import org.opf_labs.fmts.corpora.CorpusDetails;

/**
 * TODO JavaDoc for GovDocsZipped.</p>
 * TODO Tests for GovDocsZipped.</p>
 * TODO Implementation for GovDocsZipped.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:19:23:06
 */

public final class GovDocsZipped extends AbstractGovDocs {
	public static final String ZIP_EXT = "zip";
	public static final String ZIP_REGEX = "\\d{3}\\." + ZIP_EXT;
	private static final Pattern ZIP_PATTERN = Pattern.compile(ZIP_REGEX);

	private GovDocsZipped(File root) {
		super(root);
	}

	private GovDocsZipped(File root, CorpusDetails details) {
		super(root, details);
	}
	
	
	static final GovDocsZipped fromdDir(final File root) {
		File[] zipFiles = getZipFiles(root);
		int count = 0;
		long size = 0L;
		for (File zipFile : zipFiles) {
			count++;
			size+=zipFile.length();
		}
		Details detsBuild = Corpora.details(count, size);
		return new GovDocsZipped(root, detsBuild.build());
	}

	private static final File[] getZipFiles(final File root) {
		return root.listFiles(new FilenameFilter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public boolean accept(File dir, String name) {
				return ZIP_PATTERN.matcher(name).matches();
			}
		});
	}

	static boolean isZip(final File root) {
		/**
		 * TODO a really lazy check for zip form, could do better
		 */
		return FileUtils.listFiles(root, new String[] { ZIP_EXT }, false)
				.size() > 0;
	}

}
