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

import org.opf_labs.fmts.corpora.Corpora;
import org.opf_labs.fmts.corpora.Corpora.Details;
import org.opf_labs.fmts.corpora.CorpusDetails;

import com.google.common.base.Preconditions;

/**
 * Quick utility knocked up in a hurry to run the TikaSigTester over the GovDocs
 * corpus. Initially done for a the corpus in 1000 zip form, as that's how I've
 * got it stored. Extending to files should be easier.
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 2 Nov 2012:13:35:11
 */
public class GovDocs extends AbstractGovDocs {
	private GovDocs(final File root) {
		super(root);
	}

	private GovDocs(final File root, CorpusDetails details) {
		super(root, details);
	}

	/**
	 * @param root
	 *            the root directory for the corpus, can be in zip or unpacked
	 *            form
	 * @return a new GovDocs instance rooted on the directory passed
	 */
	public static final GovDocs getNewInstance(final File root) {
		Preconditions.checkNotNull(root, "root==null");
		Preconditions.checkArgument(root.isDirectory(),
				"root must be an existing directory.");
		// Iterate over the files in the directory
		return fromDirs(root);
	}

	private static final GovDocs fromDirs(final File root) {
		File[] corpFiles = root.listFiles(new FilenameFilter() {
			private Pattern pattern = null;

			@Override
			public boolean accept(File dir, String name) {
				System.out.println(this.pattern.matcher(name).matches());
				System.out.println("name: " + name);
				System.out.println("pattern: " + this.pattern.pattern());
				return this.pattern.matcher(name).matches();
			}
		});
		int count = 0;
		long size = 0L;
		for (File corpFile : corpFiles) {
			count++;
			size += corpFile.length();
		}
		Details detBuild = Corpora.details(count, size);

		return new GovDocs(root, detBuild.build());
	}

	private static final File[] getFilesFromRoot(final File root) {
		return null;
	}
}
