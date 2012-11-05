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
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.opf_labs.fmts.corpora.Corpora;
import org.opf_labs.fmts.corpora.CorpusDetails;

import com.google.common.base.Preconditions;

/**
 * Factory for GovDocsCorpora. Does the grunt work of:
 * 
 * <ul>
 * <li>Deciding whether a zip or directory instance has been passed</li>
 * <li>Counting the files, and assessing the size of the corpus</li>
 * <li>Working out the gaps in the corpus (missing numbers (0 <= numb <=
 * 999,999)</li>
 * </ul>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 5 Nov 2012:01:17:15
 */
public class GovDocs {

	private GovDocs() {
		throw new AssertionError("NO THROUGH ROAD.");
	}

	/**
	 * Get a new GovDocsCorpora instance from a root directory, can be in either zip
	 * (1000 zip files) or directory (1,000 directories) based.
	 * 
	 * @param root
	 * @return the new GovDocsCorpora instance
	 */
	public static final GovDocsCorpora newInstance(final File root) {
		Preconditions.checkNotNull(root, "root==null");
		Preconditions.checkArgument(root.isDirectory(), "root should be an existing directory.");
		boolean isZip = isZip(root);
		Pattern foldPattern = (isZip) ? GovDocsZipped.ZIP_PATTERN : AbstractGovDocs.DIR_PATTERN;
		FilenameFilter filter = new RegexFileFilter(foldPattern);
		final File[] folders = root.listFiles(filter);
		int count = 0;
		long size = 0L;
		final Set<String> exts = new HashSet<String>();
		for (File folder : folders) {
			FolderDetails foldDets;
			try {
				foldDets = (isZip) ? getZipFolderDetails(folder) : getFolderDetails(folder);
			} catch (ZipException excep) {
				System.err.println("Folder: " + folder.getAbsolutePath() + " does not appear to be a valid zip file.");
				excep.printStackTrace();
				continue;
			} catch (IOException excep) {
				System.err.println("IOException reading Folder: " + folder.getAbsolutePath());
				excep.printStackTrace();
				continue;
			}
			count+=foldDets.count;
			size+=foldDets.size;
			exts.addAll(foldDets.exts);
		}
		CorpusDetails details = Corpora.details(count, size).name(GovDocsCorpora.NAME).type(GovDocsCorpora.TYPE).extensions(exts).build();
		return (isZip) ? GovDocsZipped.newInstance(root, details, folders.length) : GovDocsDirectories.newInstance(root, details, folders.length);
	}
	
	private static final FolderDetails getFolderDetails(final File folder) {
		FilenameFilter filter = new RegexFileFilter(AbstractGovDocs.FILE_PATTERN);
		final File[] files = folder.listFiles(filter);
		int count = 0;
		long size = 0L;
		final Set<String> exts = new HashSet<String>();
		for (File file : files) {
			count++;
			size+=file.length();
			exts.add(FilenameUtils.getExtension(file.getName()));
		}
		return new FolderDetails(count, size, exts);
	}

	private static final FolderDetails getZipFolderDetails(final File zipFolder) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(zipFolder);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		ZipEntry entry = null;
		int count = 0;
		long size = 0L;
		final Set<String> exts = new HashSet<String>();
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (!entry.isDirectory()) {
				count++;
				size+=entry.getSize();
				exts.add(FilenameUtils.getExtension(entry.getName()));
			}
		}
		zipFile.close();
		FolderDetails dets = new FolderDetails(count, size, exts);
		return dets;
	}
	
	static boolean isZip(final File root) {
		/**
		 * TODO a really lazy check for zip form, could do better
		 */
		return FileUtils.listFiles(root,
				new String[] { GovDocsZipped.ZIP_EXT }, false).size() > 0;
	}


	private static class FolderDetails {
		final int count;
		final long size;
		final Set<String> exts;

		FolderDetails(final int count, final long size, final Set<String> exts) {
			assert (count >= 0);
			assert (size >= 0);
			assert (exts != null);
			this.count = count;
			this.size = size;
			this.exts = Collections.unmodifiableSet(exts);
		}
	}
}
