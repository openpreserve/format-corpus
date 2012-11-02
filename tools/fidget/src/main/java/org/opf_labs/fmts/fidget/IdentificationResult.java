package org.opf_labs.fmts.fidget;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;

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
	private static final int HASH_LENGTH = 1024 * 64;
	// java.security.digest name for the SHA256 algorithm
	private static final String SHA256_NAME = "SHA-256";
	private static final MessageDigest SHA256;
	static {
		try {
			// Then for a SHA 256 one
			SHA256 = MessageDigest.getInstance(SHA256_NAME);
		} catch (NoSuchAlgorithmException excep) {
			// If this happens the Java Digest algorithms aren't present, a
			// faulty Java install??
			throw new IllegalStateException(
					"No digest algorithm implementation for " + SHA256_NAME
							+ ", check you Java installation.");
		}
	}

	private final String hash64K;
	private final URI location;
	private final MediaType mime;
	private final long duration;

	private IdentificationResult(final String hash64K, final URI location,
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
	 * Static factory method that creates an identity from the passed values.
	 * Converts the file to a URI by calling File.toURI().
	 * 
	 * @param file
	 *            the file identified
	 * @param mime
	 *            the MIME type returned by Tika
	 * @param duration
	 *            the time taken to identify the file in milliseconds
	 * @return the new IdentificationResult object
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	static final IdentificationResult fromFile(final MimeTypes mimeRepo,
			final File file) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		IdentificationResult result = fromStream(mimeRepo, fis, file.toURI());
		try {
			fis.close();
		} catch (IOException excep) {
			excep.printStackTrace();
		}
		return result;
	}

	static final IdentificationResult fromStream(final MimeTypes mimeRepo,
			final InputStream stream) {
		return fromStream(mimeRepo, stream, STREAM_LOC);
	}

	private static final IdentificationResult fromStream(
			final MimeTypes mimeRepo, final InputStream stream, URI loc) {
		// Get a buffered input stream that supports marks
		BufferedInputStream mrkStr = new BufferedInputStream(stream);
		assert (mrkStr.markSupported());
		// put the mark at the begining, and should be comfortable for Tika id
		// length
		mrkStr.mark(mimeRepo.getMinLength() * 2);
		// identify and time
		long start = new Date().getTime();
		MediaType mime = identify(mimeRepo, stream);
		long duration = new Date().getTime() - start;
		// Now reset the stream and hash
		IdentificationResult result;
		try {
			mrkStr.reset();
			result = new IdentificationResult(hash64K(stream), loc, mime,
					duration);
		} catch (IOException excep) {
			// OK couldn't read or hash stream, record error and what we have
			result = new IdentificationResult("", ERROR_LOC, mime, duration);
		}
		return result;
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

	private static MediaType identify(final MimeTypes mimeRepo,
			final InputStream input) {
		Metadata metadata = new Metadata();
		MediaType mediaType;
		try {
			mediaType = mimeRepo.detect(TikaInputStream.get(input), metadata);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return mediaType;
	}

	private static final String hash64K(InputStream stream) throws IOException {
		SHA256.reset();
		// Create input streams for digest calculation
		DigestInputStream SHA256Stream = new DigestInputStream(stream, SHA256);
		byte[] buff = new byte[HASH_LENGTH];
		// Read up to the 64K in on read
		SHA256Stream.read(buff, 0, HASH_LENGTH);
		// Return the new instance from the calulated details
		return Hex.encodeHexString(SHA256.digest());
	}
}