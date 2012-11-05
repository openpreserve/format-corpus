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

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.opf_labs.fmts.corpora.CorpusDetails;

/**
 * Describes the GovDocsCorpora corpus slice and dice interface.
 * 
 * GovDocsCorpora 1 million file heterogenous corpus. Each file numbered between 0 and
 * 999,999. Files named by zero padded number (6 chars), with different
 * extensions:
 * 
 * 000001.txt -> 999999.pdf
 * 
 * The corpus is spit into 1,000 folders, name 000 -> 999, each containing 1,000
 * files. Folder 000 contains file 000000.? -> 000999.?, and folder 999 contains
 * 999000.? -> 999999.?
 * 
 * If we ignore the extension, we know the name of every entry, and the folder
 * that contained it is given by the first 3 chars of the file name
 * 
 * 
 * TODO Better documentation here Can get a file by full name with or without
 * extension (String number) Can get an item
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 3 Nov 2012:17:19:53
 */

public interface GovDocsCorpora extends CorpusDetails{
	/**	The name of the GovDocsCorpora corpora */
	public final static String NAME = "GovDocsCorpora";
	/**	The type of the GovDocsCorpora corpora, it's heterogeneous */
	public final static CorporaType TYPE= CorporaType.Heterogenous;
	/**
	 * @return the number of folders in the corpora
	 */
	public int folderCount();
	/**
	 * @param number
	 *            the number of the item to get (0 <= folderNum <= 999999)
	 * @return the InputStream to the item data
	 * @throws FileNotFoundException if the item could not be found
	 */
	public InputStream getItem(int number) throws FileNotFoundException;

	/**
	 * A convenience method to allow quick (parallel?) iteration and division of
	 * the corpus. Each file is viewed as an item numbered numbered between 0
	 * and 999 within it's folder (ignore the first 3 digits). Useful for
	 * looping through the corpus or taking smaller samples.
	 * 
	 * @param folderNum
	 *            the number of the folder the item is in (0 <= folderNum <=
	 *            999)
	 * @param fileNum
	 *            the number of the file to retrieve RELATIVE to the folder
	 *            structure. Each folder is viewed
	 * @return the InputStream to the item data
	 * @throws FileNotFoundException if the item could not be found
	 */
	public InputStream getItem(int folderNum, int fileNum) throws FileNotFoundException;

	/**
	 * @param number
	 *            the number of the item to get (0 <= folderNum <= 999999)
	 * @return the file name of the item including extension
	 * @throws FileNotFoundException if the item could not be found
	 */
	public String getItemName(int number) throws FileNotFoundException;

	/**
	 * @param folderNum
	 *            the number of the folder the item is in (0 <= folderNum <=
	 *            999)
	 * @param fileNum
	 *            the number of the file to retrieve RELATIVE to the folder
	 *            structure. Each folder is viewed
	 * @return the file name of the item including extension
	 * @throws FileNotFoundException if the item could not be found
	 */
	public String getItemName(int folderNum, int fileNum) throws FileNotFoundException;
}
