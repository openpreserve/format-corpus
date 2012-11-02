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
package org.opf_labs.fmts.fidget;

import java.net.URI;

import org.apache.tika.mime.MediaType;

/**
 * Class to hold the result of a Tika identification, carries the following
 * info:
 * 
 * <ul>
 * <li>hash64K : sha256 hash of up to 64K of the bytestream identified, but no
 * more. Set to be close to the max bytes required by Tika.</li>
 * <li>location: URI of bytestream if known, or STREAM_LOC for streams.</li>
 * <li>mime: the Tika MediaType returned when identified by Tika</li>
 * <li>duration: the time taken by Tika in millisecs</li>
 * </ul>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 1 Nov 2012:23:11:01
 */
public final class IdentificationResult {
	/** URI location given to a stream */
	public static final URI STREAM_LOC = URI.create("null:stream");
	/** URI location given when there was an error identifying */
	public static final URI ERROR_LOC = URI.create("null:io.error");

	private final String hash64K;
	private final URI location;
	private final MediaType mime;
	private final long duration;

	IdentificationResult(final String hash64K, final URI location,
			final MediaType mime, long duration) {
		assert (hash64K != null);
		assert (location != null);
		assert (mime != null);
		assert (duration > 0);
		this.hash64K = hash64K;
		this.location = location;
		this.mime = mime;
		this.duration = duration;
	}

	/**
	 * @return the hash64K of the bytes id'd
	 */
	public String getHash64K() {
		return this.hash64K;
	}

	/**
	 * @return the MediaType returned from Tika identification
	 */
	public MediaType getMime() {
		return this.mime;
	}

	/**
	 * @return the time in milliseconds that identification took
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 * @return the URI identifying the file that was identified
	 */
	public URI getLocation() {
		return this.location;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IdentificationResult [hash64K=" + this.hash64K + ", location="
				+ this.location + ", mime=" + this.mime + ", duration="
				+ this.duration + "]";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (this.duration ^ (this.duration >>> 32));
		result = prime * result
				+ ((this.hash64K == null) ? 0 : this.hash64K.hashCode());
		result = prime * result
				+ ((this.location == null) ? 0 : this.location.hashCode());
		result = prime * result
				+ ((this.mime == null) ? 0 : this.mime.hashCode());
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
		if (!(obj instanceof IdentificationResult)) {
			return false;
		}

		IdentificationResult other = (IdentificationResult) obj;
		if (this.duration != other.duration) {
			return false;
		}
		if (this.hash64K == null) {
			if (other.hash64K != null) {
				return false;
			}
		} else if (!this.hash64K.equals(other.hash64K)) {
			return false;
		}
		if (this.location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!this.location.equals(other.location)) {
			return false;
		}
		if (this.mime == null) {
			if (other.mime != null) {
				return false;
			}
		} else if (!this.mime.equals(other.mime)) {
			return false;
		}
		return true;
	}

}