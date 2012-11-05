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
import java.io.InputStream;
import java.util.Set;
import java.util.regex.Pattern;

import org.opf_labs.fmts.corpora.Corpora;
import org.opf_labs.fmts.corpora.CorpusDetails;

import com.google.common.base.Preconditions;

/**
 * Abstract base class for GovDocsCorpora Corpora implementations.
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 2 Nov 2012:19:01:26
 */

abstract class AbstractGovDocs implements GovDocsCorpora {
	/** RegEx string for GovDocsDirectories directory names */
	public static final String DIR_REGEX = "^\\d{3}";
	protected static final String FILE_REGEX_SUFFIX = "\\.?.*";
	/** RegEx string for GovDocsDirectories file names */
	public static final String FILE_REGEX = "^\\d{6}" + FILE_REGEX_SUFFIX;
	protected static final Pattern DIR_PATTERN = Pattern.compile(DIR_REGEX);
	protected static final Pattern FILE_PATTERN = Pattern.compile(FILE_REGEX);
	protected static final int MAX_FOLDER_NUM = 999;
	protected static final int MAX_FILE_NUM = 999999;
	protected final File root;
	protected final CorpusDetails details;
	private final int folderCount;

	protected AbstractGovDocs(final File root) {
		this(root, Corpora.canonicalDetails(), 0);
	}

	protected AbstractGovDocs(final File root, final CorpusDetails details, int folderCount) {
		assert(root != null);
		assert(root.isDirectory());
		assert(details != null);
		assert(folderCount>=0);
		this.root = root;
		this.details = details;
		this.folderCount = folderCount;
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getName()
	 */
	@Override
	public String getName() {
		return this.details.getName();
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getType()
	 */
	@Override
	public CorporaType getType() {
		return this.details.getType();
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getCount()
	 */
	@Override
	public int getCount() {
		return this.details.getCount();
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getSize()
	 */
	@Override
	public long getSize() {
		return this.details.getSize();
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getExtensions()
	 */
	@Override
	public Set<String> getExtensions() {
		return this.details.getExtensions();
	}

	/**
	 * @see org.opf_labs.fmts.corpora.govdocs.GovDocsCorpora#folderCount()
	 */
	@Override
	public int folderCount() {
		return this.folderCount;
	}

	/**
	 * @see org.opf_labs.fmts.corpora.govdocs.GovDocsCorpora#getItem(int)
	 */
	@Override
	public final InputStream getItem(final int itemNum) throws FileNotFoundException {
		Preconditions.checkArgument(
				((itemNum >= 0) && (itemNum <= MAX_FILE_NUM)),
				"Invalid item number should be (0 <= itemNumber <= "
						+ MAX_FILE_NUM + ") NOT: " + itemNum);
		return getItemImpl(itemNum);
	}
	
	abstract protected InputStream getItemImpl(final int itemNum) throws FileNotFoundException;

	/**
	 * @see org.opf_labs.fmts.corpora.govdocs.GovDocsCorpora#getItem(int, int)
	 */
	@Override
	public final InputStream getItem(final int folderNum, final int itemNum)
			throws FileNotFoundException {
		Preconditions.checkArgument(
				((folderNum >= 0) && (folderNum <= MAX_FOLDER_NUM)),
				"Invalid folder number should be (0 <= folderNum <= "
						+ MAX_FOLDER_NUM + ") NOT: " + folderNum);
		Preconditions.checkArgument(
				((itemNum >= 0) && (itemNum <= MAX_FOLDER_NUM)),
				"Invalid file number should be (0 <= itemNumber <= "
						+ MAX_FOLDER_NUM + ") NOT: " + itemNum);
		return getItemImpl(folderNum, itemNum);
	}

	abstract protected InputStream getItemImpl(final int folderNum, final int itemNum) throws FileNotFoundException;

	/**
	 * @see org.opf_labs.fmts.corpora.govdocs.GovDocsCorpora#getItemName(int)
	 */
	@Override
	public final String getItemName(int number) throws FileNotFoundException {
		Preconditions.checkArgument(
				((number >= 0) && (number <= MAX_FILE_NUM)),
				"Invalid item number should be (0 <= itemNumber <= "
						+ MAX_FILE_NUM + ") NOT: " + number);
		return getItemNameImpl(number);
	}
	
	abstract protected String getItemNameImpl(final int number) throws FileNotFoundException;

	/**
	 * @see org.opf_labs.fmts.corpora.govdocs.GovDocsCorpora#getItemName(int, int)
	 */
	@Override
	public final String getItemName(final int folderNum, final int itemNum) throws FileNotFoundException {
		Preconditions.checkArgument(
				((folderNum >= 0) && (folderNum <= MAX_FOLDER_NUM)),
				"Invalid folder number should be (0 <= folderNum <= "
						+ MAX_FOLDER_NUM + ") NOT: " + folderNum);
		Preconditions.checkArgument(
				((itemNum >= 0) && (itemNum <= MAX_FOLDER_NUM)),
				"Invalid file number should be (0 <= itemNumber <= "
						+ MAX_FOLDER_NUM + ") NOT: " + itemNum);
		return getItemNameImpl(folderNum, itemNum);
	}

	abstract protected String getItemNameImpl(final int folderNum, final int itemNum) throws FileNotFoundException;

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.details == null) ? 0 : this.details.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractGovDocs)) {
			return false;
		}
		AbstractGovDocs other = (AbstractGovDocs) obj;
		if (this.details == null) {
			if (other.details != null) {
				return false;
			}
		} else if (!this.details.equals(other.details)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractGovDocs [details=" + this.details + "]";
	}

	protected final String baseName(final int fileNum) {
		assert ((fileNum <= MAX_FILE_NUM) && (fileNum >= 0));
		return (fileNum > (MAX_FILE_NUM / 10)) ? String.valueOf(fileNum) : String
				.format("%06d", fileNum);
	}

	protected final String folderName(final int folderNum) {
		assert (folderNum >= 0 && folderNum <= MAX_FOLDER_NUM);
		return (folderNum > (MAX_FOLDER_NUM / 10)) ? String.valueOf(folderNum) : String
				.format("%3d", folderNum);
	}
	
	protected final int folderNumber(final int fileNum) {
		return Integer.valueOf(baseName(fileNum).substring(0,3));
	}

	protected final String fileName(final int folderNum, final int fileNum) {
		assert (folderNum >= 0 && folderNum <= MAX_FOLDER_NUM);
		assert (fileNum >= 0 && fileNum <= MAX_FILE_NUM);
		return String.format("%3d%3d", folderNum, fileNum);
	}
}
