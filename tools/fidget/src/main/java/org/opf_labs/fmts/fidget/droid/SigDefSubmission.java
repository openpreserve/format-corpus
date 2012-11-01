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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * This is the class used to create to the PRONOM sig generation service.
 * 
 * It has been declared with example entries, but these should of course be
 * overwritten.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 * 
 */
public final class SigDefSubmission {

	private final String name;
	private final String version;
	private final String puid;
	private final String extension;
	private final String mimetype;
	private final List<InternalSig> signatures;

	@SuppressWarnings("synthetic-access")
	private SigDefSubmission(Builder builder) {
		assert (builder.name != null);
		assert (builder.version != null);
		assert (builder.puid != null);
		assert (builder.extension != null);
		assert (builder.mimetype != null);
		assert (builder.signatures != null);
		this.name = builder.name;
		this.version = builder.version;
		this.puid = builder.puid;
		this.extension = builder.extension;
		this.mimetype = builder.mimetype;
		this.signatures = builder.signatures;
	}

	/**
	 * convenience method to create a builder using an existing Submission as
	 * the template
	 * 
	 * @param sigDef
	 *            the Submission to copy
	 * @return a new builder initialised from sigDef
	 */
	public static Builder copy(final SigDefSubmission sigDef) {
		Preconditions.checkNotNull(sigDef, "sigDef==null");
		return new Builder(sigDef.puid, sigDef.mimetype, sigDef.signatures)
				.name(sigDef.name).version(sigDef.version)
				.extension(sigDef.extension);
	}

	/**
	 * Creates a builder with the passed values and an empty list for inserting
	 * into
	 * 
	 * @param puid
	 *            the PRONOM unique id to use
	 * @param mime
	 *            the mime type string
	 * @return a builder from the values passed
	 */
	public static Builder fromValues(final String puid, final String mime) {
		Preconditions.checkNotNull(puid, "mime==null");
		Preconditions.checkNotNull(puid, "mime==null");
		return new Builder(puid, mime, new ArrayList<InternalSig>());
	}

	/**
	 * @return an empty version of the default with a list that sigs can be added to
	 */
	public static Builder empty() {
		return fromValues(Builder.DEFAULT_PUID, Builder.DEFAULT_MIMETYPE);
	}

	/**
	 * The default submission, for testing, etc. Note the default has an empty,
	 * unmodifiable list of sigs so you can't add sigs to it, or copies of it,
	 * use empty for that
	 * 
	 * Returns a static instance so all DEFAULTs are ==
	 * 
	 * @return the empty (all default) sig submission
	 */
	@SuppressWarnings("synthetic-access")
	public static SigDefSubmission canonicalInst() {
		return Builder.CANONICAL;
	}

	/**
	 * Builder for the signature submission
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 1 Nov 2012:17:15:27
	 */
	public final static class Builder {
		private static final String DEFAULT_NAME = "Development Signature";
		private static final String DEFAULT_VERSION = "1.0";
		private static final String DEFAULT_PUID = "dev/1";
		private static final String DEFAULT_EXTENSION = "ext";
		private static final String DEFAULT_MIMETYPE = "text/x-test-signature";
		private static SigDefSubmission CANONICAL = new Builder().build();
		// Mandatory
		private final String puid;
		private final String mimetype;
		private final List<InternalSig> signatures;
		// Optional
		private String name = DEFAULT_NAME;
		private String version = DEFAULT_VERSION;
		private String extension = DEFAULT_EXTENSION;

		private Builder() {
			this(DEFAULT_PUID, DEFAULT_MIMETYPE, Collections
					.unmodifiableList(new ArrayList<InternalSig>()));
		}

		/**
		 * @param puid
		 *            the PUID for the sig
		 * @param mime
		 *            the sigs mime type
		 * @param sigs
		 *            the list of signatures
		 */
		public Builder(final String puid, final String mime,
				final List<InternalSig> sigs) {
			Preconditions.checkNotNull(puid, "puid==null");
			Preconditions.checkNotNull(mime, "mime==null");
			Preconditions.checkNotNull(sigs, "sigs==null");
			this.puid = puid;
			this.mimetype = mime;
			this.signatures = sigs;
		}

		/**
		 * @param name
		 *            the name for the sig
		 * @return this Builder
		 */
		public Builder name(final String name) {
			assert (name != null);
			this.name = name;
			return this;
		}

		/**
		 * @param ver
		 *            the version of the sig
		 * @return this Builder
		 */
		public Builder version(final String ver) {
			assert (ver != null);
			this.version = ver;
			return this;
		}

		/**
		 * @param ext
		 *            the extension for the sig
		 * @return this Builder
		 */
		public Builder extension(final String ext) {
			assert (ext != null);
			this.extension = ext;
			return this;
		}

		/**
		 * @param sig
		 *            the signature to add to the internal list of signatures
		 * @return this Builder
		 */
		public Builder addSig(final InternalSig sig) {
			this.signatures.add(sig);
			return this;
		}

		/**
		 * Convenience to remove all existing sigs, handy for templating from an
		 * existing sig.
		 * 
		 * @return this Builder
		 */
		public Builder clearSigs() {
			this.signatures.clear();
			return this;
		}

		/**
		 * Convenience to swap a set of sigs
		 * 
		 * @param sigs
		 * @return this Builder
		 */
		public Builder swapSigs(final List<InternalSig> sigs) {
			this.signatures.clear();
			for (InternalSig sig : sigs) {
				this.signatures.add(sig);
			}
			return this;
		}

		/**
		 * @return a new SigDefSubmission from the passed values
		 */
		@SuppressWarnings("synthetic-access")
		public SigDefSubmission build() {
			return new SigDefSubmission(this);
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * @return the puid
	 */
	public String getPuid() {
		return this.puid;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return this.extension;
	}

	/**
	 * @return the mimetype
	 */
	public String getMimetype() {
		return this.mimetype;
	}

	/**
	 * @return the signatures
	 */
	public List<InternalSig> getSignatures() {
		return this.signatures;
	}
}
