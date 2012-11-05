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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.opf_labs.fmts.corpora.CorpusDetails;

/**
 * Quick utility knocked up in a hurry to run the TikaSigTester over the
 * GovDocsDirectories corpus. Initially done for a the corpus in 1000 zip form,
 * as that's how I've got it stored. Extending to files should be easier.
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 2 Nov 2012:13:35:11
 */
public class GovDocsDirectories extends AbstractGovDocs {
	private GovDocsDirectories(final File root) {
		super(root);
	}

	private GovDocsDirectories(final File root, final CorpusDetails details,
			final int folderCount) {
		super(root, details, folderCount);
	}

	/**
	 * @param root
	 *            the root directory for the corpus form
	 * @param details
	 *            the corpus details
	 * @param folderCount
	 *            the number of folders in the corpus
	 * @return a new GovDocsDirectories instance rooted on the directory passed
	 */
	static final GovDocsDirectories newInstance(final File root,
			final CorpusDetails details, final int folderCount) {
		return new GovDocsDirectories(root, details, folderCount);
	}

	@Override
	protected InputStream getItemImpl(int itemNum) throws FileNotFoundException {
		return new FileInputStream(getItemFile(itemNum));
	}

	@Override
	public InputStream getItemImpl(final int folderNum, final int itemNum)
			throws FileNotFoundException {
		return new FileInputStream(getItemFile((folderNum * 1000) + itemNum));
	}

	@Override
	public String getItemNameImpl(final int number) throws FileNotFoundException {
		return getItemFile(number).getName();
	}

	@Override
	public String getItemNameImpl(final int folderNum, final int itemNum) throws FileNotFoundException {
		return getItemFile((folderNum * 1000) + itemNum).getName();
	}

	private File getItemFile(int number) throws FileNotFoundException {
		FilenameFilter itemFilter = new RegexFileFilter("^\\"
				+ baseName(number) + FILE_REGEX_SUFFIX);
		int folderNum = folderNumber(number);
		File folder = new File(this.root.getAbsolutePath() + File.separator
				+ folderName(folderNum));
		File[] files = folder.listFiles(itemFilter);
		if (files.length < 1)
			throw new FileNotFoundException("Could not file file for number: "
					+ number);
		if (files.length > 1)
			System.err.println("Duplicated entries for number: " + number);
		return files[0];
	}
}
