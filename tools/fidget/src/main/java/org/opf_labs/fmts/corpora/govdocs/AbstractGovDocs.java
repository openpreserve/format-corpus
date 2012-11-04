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
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import org.opf_labs.fmts.corpora.Corpora;
import org.opf_labs.fmts.corpora.CorpusDetails;

/**
 * TODO JavaDoc for AbstractGovDocs.</p>
 * TODO Tests for AbstractGovDocs.</p>
 * TODO Implementation for AbstractGovDocs.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:19:01:26
 */

abstract class AbstractGovDocs implements GovDocs, CorpusDetails {
	public static final String NAME = "GovDocsDirectories";
	/** RegEx string for GovDocsDirectories directory names */
	public static final String DIR_REGEX = "^\\d{3}";
	/** RegEx string for GovDocsDirectories file names */
	public static final String FILE_REGEX = "^\\d{6\\..*}";
	protected static final Pattern DIR_PATTERN = Pattern.compile(DIR_REGEX);
	protected static final Pattern FILE_PATTERN = Pattern.compile(FILE_REGEX);
	private static final int MAX_FOLDER_NUM = 999;
	private static final int MAX_FILE_NUM = 999999;
	protected final File root;
	protected final CorpusDetails details;
	
	protected AbstractGovDocs(final File root) {
		this(root, Corpora.canonicalDetails());
	}
	protected AbstractGovDocs(final File root, final CorpusDetails details) {
		assert(root!=null);
		assert(root.isDirectory());
		assert(details!=null);
		this.root = root;
		this.details = details;
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

	protected String getStringBaseName(int num) {
		assert((num <= MAX_FILE_NUM) && (num>=0));
		return (num > (MAX_FILE_NUM / 10)) ? String.valueOf(num) : String.format("%06d", num);
	}
	
	protected String getFolderName(int num) {
		assert(num >= 0 && num <= MAX_FOLDER_NUM);
		return (num > (MAX_FOLDER_NUM / 10)) ? String.valueOf(num) : String.format("%3d", num);
	}
	
	protected String makeFileName(int folderNum, int fileNum) {
		assert(folderNum >= 0 && folderNum <= MAX_FOLDER_NUM);
		assert(fileNum >= 0 && fileNum <= MAX_FILE_NUM);
		return String.format("%3d%3d", folderNum, fileNum);
	}
	
	protected static class FolderDetails {
		final int count;
		final long size;
		final Set<String> exts;
		
		FolderDetails(final int count, final long size, final Set<String> exts) {
			assert(count > 0);
			assert(size > 0);
			assert(exts!=null);
			this.count = count;
			this.size = size;
			this.exts = Collections.unmodifiableSet(exts);
		}
	}
}
