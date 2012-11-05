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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.opf_labs.fmts.corpora.CorpusDetails;

/**
 * Class that covers zipped version of GovDocsDirectories.
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:19:23:06
 */

public final class GovDocsZipped extends AbstractGovDocs {
	/** File extension for zip "folder" in the package */
	public static final String ZIP_EXT = "zip";
	/** RegEx pattern for GovDocsDirectories zip "folder" name */
	public static final String ZIP_REGEX = "\\d{3}\\." + ZIP_EXT;
	static final Pattern ZIP_PATTERN = Pattern.compile(ZIP_REGEX);
	static final FilenameFilter ZIP_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return ZIP_PATTERN.matcher(name).matches();
		}
	};

	private ZipFile currFolder;
	private int current = -1;

	private GovDocsZipped(final File root) {
		super(root);
	}

	private GovDocsZipped(final File root, final CorpusDetails details, final int folderCount) {
		super(root, details, folderCount);
	}

	/**
	 * @param root
	 *            the root directory for the corpus
	 *            form
	 * @param details the corpus details
	 * @param folderCount the number of folders in the corpus
	 * @return a new GovDocsDirectories instance rooted on the directory passed
	 */
	static final GovDocsZipped newInstance(final File root, final CorpusDetails details, final int folderCount) {
		return new GovDocsZipped(root, details, folderCount);
	}

	@Override
	protected InputStream getItemImpl(final int number) throws FileNotFoundException {
			try {
				return this.currFolder.getInputStream(this.getItemEntry(number));
			} catch (IOException excep) {
				throw new FileNotFoundException("Could not file file for number: "
						+ number);
			}
	}

	@Override
	protected InputStream getItemImpl(final int folderNum, final int fileNum) throws FileNotFoundException {
		return this.getItemImpl((folderNum * 1000) + fileNum);
	}

	@Override
	protected String getItemNameImpl(final int number) throws FileNotFoundException {
		return this.getItemEntry(number).getName();
	}

	@Override
	protected final String getItemNameImpl(final int folderNum, final int fileNum) throws FileNotFoundException {
		return this.getItemEntry((folderNum * 1000) + fileNum).getName();
	}

	private ZipEntry getItemEntry(final int number) throws FileNotFoundException {
		int folderNum = folderNumber(number);
		if (folderNum != this.current) {
			try {
				this.currFolder.close();
			} catch (IOException excep) {
				// Do nothing
			}
			try {
				this.currFolder = new ZipFile(this.root.getAbsolutePath() + File.separator
						+ folderName(folderNum));
			} catch (IOException excep) {
				throw new FileNotFoundException("Could not file file for number: "
						+ number);
			}
			this.current = folderNum;
		}
		String baseName = baseName(number);
		Enumeration<? extends ZipEntry> entries = this.currFolder.entries();
		ZipEntry entry = null;
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (!entry.isDirectory()) {
				if (FilenameUtils.getBaseName(entry.getName()).equals(baseName)) return entry;
			}
		}
		throw new FileNotFoundException("Could not file file for number: "
				+ number);
	}
}
