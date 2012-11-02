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
package org.opf_labs.fmts.corpora;

import java.util.Set;

/**
 * OK, not so quick and dirty, but potentially useful
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:18:26:03
 */

public interface CorpusDetails {
	/**
	 * Asserts whether a corpus is believe to be homgenous, heterogenous, or unknown
	 * 
	 * TODO A better name for CorpraType.</p>
	 * 
	 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
	 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
	 * @version 0.1
	 * 
	 * Created 2 Nov 2012:18:30:31
	 */
	public static enum CorporaType {
		/** Asserts that the content is believed to be of a single format */
		Homogenous,
		/** Asserts that the content is believed to comprise of multiple formats */
		Heterogenous,
		/** Don't know the state of the corpora */
		Unknown;
	}
	
	/**
	 * @return the name of the corpus
	 */
	public String getName();
	/**
	 * @return the type of the corpus
	 */
	public CorporaType getType();
	/**
	 * @return the number of items in the corpus
	 */
	public int getCount();
	/**
	 * @return the size of the corpus in bytes
	 */
	public long getSize();
	/**
	 * @return the string set of different file extensions found in the corpus
	 */
	public Set<String> getExtensions();
}
