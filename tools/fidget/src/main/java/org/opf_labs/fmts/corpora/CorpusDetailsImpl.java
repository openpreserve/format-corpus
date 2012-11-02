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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.opf_labs.fmts.corpora.Corpora.Details;

/**
 * Immutable CoprusDetails implementation, used as component in corpora.
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:18:47:14
 */

class CorpusDetailsImpl implements CorpusDetails {
	final static String DEFAULT_NAME = "Default Corpora";
	final static CorpusDetailsImpl CANONICAL = new CorpusDetailsImpl();
	
	final String name;
	final CorporaType type;
	final int count;
	final long size;
	final Set<String> exts;
	
	private CorpusDetailsImpl() {
		this(DEFAULT_NAME, CorporaType.Unknown, 0, 0L, new HashSet<String>());
	}

	private CorpusDetailsImpl(final String name, final CorporaType type, final int count, final long size, final Set<String> exts) {
		assert(name!=null);
		assert(type!=null);
		assert(count>=0);
		assert(size>=0L);
		assert(exts!=null);
		this.name = name;
		this.type = type;
		this.count = count;
		this.size = size;
		this.exts = Collections.unmodifiableSet(exts);
	}
	
	CorpusDetailsImpl(Details builder) {
		this(builder.name, builder.type, builder.count, builder.size, builder.exts);
	}
	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getType()
	 */
	@Override
	public CorporaType getType() {
		return this.type;
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getCount()
	 */
	@Override
	public int getCount() {
		return this.count;
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getSize()
	 */
	@Override
	public long getSize() {
		return this.size;
	}

	/**
	 * @see org.opf_labs.fmts.corpora.CorpusDetails#getExtensions()
	 */
	@Override
	public Set<String> getExtensions() {
		return this.exts;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.count;
		result = prime * result
				+ ((this.exts == null) ? 0 : this.exts.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + (int) (this.size ^ (this.size >>> 32));
		result = prime * result
				+ ((this.type == null) ? 0 : this.type.hashCode());
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
		if (!(obj instanceof CorpusDetailsImpl)) {
			return false;
		}
		CorpusDetailsImpl other = (CorpusDetailsImpl) obj;
		if (this.type != other.type) {
			return false;
		}
		if (this.count != other.count) {
			return false;
		}
		if (this.size != other.size) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.exts == null) {
			if (other.exts != null) {
				return false;
			}
		} else if (!this.exts.equals(other.exts)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CorpusDetailsImpl [name=" + this.name + ", type=" + this.type
				+ ", count=" + this.count + ", size=" + this.size + ", exts="
				+ this.exts + "]";
	}
}
