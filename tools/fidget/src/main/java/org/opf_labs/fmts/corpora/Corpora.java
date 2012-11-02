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

import org.opf_labs.fmts.corpora.CorpusDetails.CorporaType;

import com.google.common.base.Preconditions;

/**
 * Static utility, and factory class for corpora related objects
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:19:10:03
 */

public final class Corpora {

	/**
	 * @return the canonical details object, useful for testing
	 */
	public static final CorpusDetails canonicalDetails() {
		return CorpusDetailsImpl.CANONICAL;
	}
	
	/**
	 * @param count the number of items in the courpus
	 * @param size the size of the corpus in bytes
	 * @return the new details builder
	 */
	public static final Details details(final int count, final long size) {
		Preconditions.checkArgument(count>=0, "count < 0");
		Preconditions.checkArgument(size>=0, "size < 0");
		return new Details(count, size);
	}
	/**
	 * Builder class for Coprora Details
	 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
	 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
	 * @version 0.1
	 * 
	 * Created 2 Nov 2012:19:48:42
	 */
	public static class Details {
		final int count;
		final long size;
		String name = CorpusDetailsImpl.CANONICAL.name;
		CorporaType type = CorporaType.Unknown;
		Set<String> exts = CorpusDetailsImpl.CANONICAL.exts;
		
		Details(final int count, final long size) {
			assert(count>=0);
			assert(size>=0);
			this.count = count;
			this.size = size;
		}
		
		/**
		 * @param name the name for the corpora 
		 * @return this builder
		 */
		public Details name(final String name) {
			Preconditions.checkNotNull(name, "name==null");
			this.name = name;
			return this;
		}
		
		/**
		 * @param type the type for the corpora 
		 * @return this builder
		 */
		public Details type(final CorporaType type) {
			Preconditions.checkNotNull(type, "type==null");
			this.type = type;
			return this;
		}
		
		/**
		 * @param exts the set of file extensions for the corpora 
		 * @return this builder
		 */
		public Details extensions(Set<String> exts) {
			Preconditions.checkNotNull(exts, "exts==null");
			this.exts = exts;
			return this;
		}
		
		/**
		 * @return the constructed CorpusDetails
		 */
		public CorpusDetails build() {
			return new CorpusDetailsImpl(this);
		}
	}
}
