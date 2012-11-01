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
package org.opf_labs.fmts.fidget.droid;

import com.google.common.base.Preconditions;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 */
public final class InternalSig {
	private static final InternalSig CANONICAL = new InternalSig();

	/**
	 * Enum to indicate the anchor for the signature:
	 * 
	 * BOFOffest : Beginning of file EOFoffset : End of file Variable : Variable
	 * position anchor
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 1 Nov 2012:20:15:16
	 */
	public static enum Anchor {
		/** BOF value */
		BOFoffset,
		/** EOF value */
		EOFoffset,
		/** Variable */
		Variable
	}

	private final String signature;
	private final Anchor anchor;
	private final int offset;
	private final int maxOffset;

	private InternalSig() {
		this("255044462D312E34", Anchor.BOFoffset, 0, 0);
	}

	private InternalSig(final String sig, final Anchor anchor, final int off,
			final int maxOff) {
		assert (sig != null);
		assert (anchor != null);
		this.signature = sig;
		this.anchor = anchor;
		this.offset = off;
		this.maxOffset = maxOff;
	}

	/**
	 * @return the static canonical instance, used for testing etc
	 */
	public static final InternalSig canonicalInst() {
		return CANONICAL;
	}

	/**
	 * Static factory method to create an internal signature
	 * 
	 * @param sig
	 *            the signature
	 * @param anchor
	 *            the signatures "anchor" (BOF, EOF, Variable)
	 * @param off
	 *            the offset value for the sig
	 * @param maxOff
	 *            the offset maximum
	 * @return the new internal signature created from the values
	 */
	public static final InternalSig fromValues(final String sig,
			final Anchor anchor, final int off, final int maxOff) {
		Preconditions.checkNotNull(sig, "sig==null");
		Preconditions.checkNotNull(anchor, "anchor==null");
		// TODO any offest sanity checks here
		return new InternalSig(sig, anchor, off, maxOff);
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return this.signature;
	}

	/**
	 * @return the anchor
	 */
	public Anchor getAnchor() {
		return this.anchor;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return this.offset;
	}

	/**
	 * @return the maxOffset
	 */
	public int getMaxOffset() {
		return this.maxOffset;
	}
}
