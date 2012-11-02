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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.mime.MimeTypesFactory;

/**
 * Class that wraps the Apache Tika MimeTypes Repository for purposes of
 * developing and testing Tika signatures. The main feature is one of loading
 * order & signature overloading. To be clear, the order of loading matters and
 * signatures loaded later overload those loaded before.
 * 
 * The class allows the caller to construct a TikaSigTester with files and
 * streams of their choosing, while mixing and matching with the resource files
 * with the aim of making developing and testing new signatures simple.
 * 
 * The loading of any particular category of definition is optional but to help
 * prevent unexpected bugs the order of loading is strictly enforced as follows:
 * <ol>
 * <li>THE tika-mimetypes.xml from the org.apache.tika.mime resource folder,
 * comes with the MimeTypes class.</li>
 * <li>ALL other file called custom-mimetypes.xml provided in the
 * orga.apache.tika.mime resource folder(s?).</li>
 * <li>Any MIME definitions provided by the caller, in the order provided by the
 * caller.</li>
 * </ol>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 1 Nov 2012:22:09:39
 */
public final class TikaSigTester {
	private final static String TIKA_MIMETYPES = "tika-mimetypes.xml";
	private final static String CUSTOM_MIMETYPES = "custom-mimetypes.xml";
	private final static String CLASS_PREFIX = MimeTypes.class.getPackage()
			.getName().replace('.', '/')
			+ "/";
	private final static char TAB = '\t';
	private final MimeTypes mimeRepository;

	private TikaSigTester() {
		throw new AssertionError("NO THROUGH ROAD, use the static methods.");
	}

	private TikaSigTester(MimeTypes mimeRepo) {
		assert (mimeRepo != null);
		this.mimeRepository = mimeRepo;
	}

	/**
	 * Creates a TikaSigTester with only the tika-mimetypes.xml loaded from the
	 * Tika MimeTypes classloader.
	 * 
	 * @return a Tika MimeType definition only instance of the tester
	 */
	public final static TikaSigTester justTika() {
		try {
			final MimeTypes repo = MimeTypesFactory
					.create(new URL[] { getCoreUrl() });
			return new TikaSigTester(repo);
		} catch (Exception excep) {
			throw new IllegalStateException(excep);
		}
	}

	/**
	 * Creates a TikaSigTester with only the tika-mimetypes.xml loaded from the
	 * Tika MimeTypes classloader.
	 * 
	 * @return a Tika MimeType definition only instance of the tester
	 */
	public final static TikaSigTester justCustom() {
		try {
			List<URL> custUrls = getCustomUrls();
			final MimeTypes repo = MimeTypesFactory.create(custUrls
					.toArray(new URL[custUrls.size()]));
			return new TikaSigTester(repo);
		} catch (Exception excep) {
			throw new IllegalStateException(
					"Missing or corrupt mime type definitions.");
		}
	}

	/**
	 * Creates a "vanilla" set up TikaSigTester with Tika and custom MIME
	 * definitions loaded. The custom defs override the initial defs
	 * 
	 * @return the vanilla instance of the tester
	 * @throws IllegalStateException
	 *             If the internal sig files aren't available, given this is
	 *             probably catastrophic we throw a runtime.
	 */
	public final static TikaSigTester vanilla() {
		try {
			final MimeTypes repo = MimeTypesFactory.create(TIKA_MIMETYPES,
					CUSTOM_MIMETYPES);
			return new TikaSigTester(repo);
		} catch (Exception excep) {
			throw new IllegalStateException(
					"Missing or corrupt mime type definitions.");
		}
	}

	/**
	 * @see org.apache.tika.mime.MimeTypesFactory#create(InputStream...)
	 * 
	 * @param streams
	 *            the mime type definition streams to parse
	 * @return a new TikaSigTester initialised from the streams
	 * @throws IOException
	 *             if the stream can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester streamsOnly(InputStream... streams)
			throws MimeTypeException, IOException {
		final MimeTypes repo = MimeTypesFactory.create(streams);
		return new TikaSigTester(repo);
	}

	/**
	 * Loads the Tika MIME defs first followed by the streams so they override
	 * the Tika defs
	 * 
	 * @param streams
	 *            the mime type definition streams to parse
	 * @return a new TikaSigTester initialised from the streams
	 * @throws IOException
	 *             if the stream can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester tikaAndStreams(InputStream... streams)
			throws MimeTypeException, IOException {
		InputStream[] coreStr = new InputStream[] { getCoreUrl().openStream() };
		return new TikaSigTester(fromStreamArrays(coreStr, streams));
	}

	/**
	 * Loads the "Vanilla" MIME defs first followed by the streams so they
	 * override the other defs, i.e. Tika <- Custom <- streams
	 * 
	 * @param streams
	 *            the mime type definition streams to parse
	 * @return a new TikaSigTester initialised from the streams
	 * @throws IOException
	 *             if the stream can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester vanillaAndStreams(InputStream... streams)
			throws MimeTypeException, IOException {
		InputStream[] vanStrs = streamsFromUrls(getVanillaUrls());
		return new TikaSigTester(fromStreamArrays(vanStrs, streams));
	}

	/**
	 * Loads the Custom MIME defs first followed by the streams so they override
	 * the Custom defs
	 * 
	 * @param streams
	 *            the mime type definition streams to parse
	 * @return a new TikaSigTester initialised from the custom defs & streams
	 * @throws IOException
	 *             if the stream can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester customAndStreams(InputStream... streams)
			throws MimeTypeException, IOException {
		InputStream[] custStrs = streamsFromUrls(getCustomUrls());
		return new TikaSigTester(fromStreamArrays(custStrs, streams));
	}

	/**
	 * @see TikaSigTester#streamsOnly(InputStream...)
	 * @param files
	 *            the mime type definition files to parse
	 * @return a new TikaSigTester initialised from the files
	 * @throws IOException
	 *             if the files can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester filesOnly(File... files)
			throws MimeTypeException, IOException {
		return streamsOnly(streamsFromFiles(files));
	}

	/**
	 * @see TikaSigTester#tikaAndStreams(InputStream...)
	 * @param files
	 *            the mime type definition files to parse
	 * @return a new TikaSigTester initialised from the files
	 * @throws IOException
	 *             if the files can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester tikaAndFiles(File... files)
			throws MimeTypeException, IOException {
		return tikaAndStreams(streamsFromFiles(files));
	}

	/**
	 * @see TikaSigTester#vanillaAndStreams(InputStream...)
	 * @param files
	 *            the mime type definition files to parse
	 * @return a new TikaSigTester initialised from the files
	 * @throws IOException
	 *             if the files can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester vanillaAndFiles(File... files)
			throws MimeTypeException, IOException {
		return tikaAndStreams(streamsFromFiles(files));
	}

	/**
	 * @see TikaSigTester#customAndStreams(InputStream...)
	 * @param files
	 *            the mime type definition files to parse
	 * @return a new TikaSigTester initialised from the files
	 * @throws IOException
	 *             if the files can not be read
	 * @throws MimeTypeException
	 *             if the type configuration is invalid
	 */
	public static final TikaSigTester customAndFiles(File... files)
			throws MimeTypeException, IOException {
		return customAndStreams(streamsFromFiles(files));
	}

	private static final InputStream[] streamsFromUrls(List<URL> urls)
			throws IOException {
		InputStream[] streams = new InputStream[urls.size()];
		int strInd = 0;
		for (URL url : urls) {
			streams[strInd++] = url.openStream();
		}
		return streams;
	}

	private static final InputStream[] streamsFromFiles(File... files)
			throws FileNotFoundException {
		InputStream[] streams = new InputStream[files.length];
		int strInd = 0;
		for (File file : files) {
			streams[strInd++] = new FileInputStream(file);
		}
		return streams;
	}

	private static MimeTypes fromStreamArrays(final InputStream[] internal,
			final InputStream[] supplied) throws IOException, MimeTypeException {
		final MimeTypes repo = MimeTypesFactory.create(concat(internal,
				supplied));
		// Close our streams
		for (InputStream str : internal) {
			str.close();
		}
		return repo;
	}

	private static <T> T[] concat(T[] first, T[] second) {
		T[] res = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, res, first.length, second.length);
		return res;
	}

	public Identity identify(File input) throws IOException {
		long before = System.currentTimeMillis();
		MediaType mime = this.identify(TikaInputStream.get(input));
		long duration = System.currentTimeMillis() - before;
		return Identity.fromFile(input, mime, duration);

	}

	public List<Identity> identify(String govDocsData) throws Exception {

		List<File> datafiles = getFiles(new File(govDocsData));
		return identify(datafiles);

	}

	public List<Identity> identify(List<File> datafiles) throws Exception {

		List<Identity> identities = new ArrayList<Identity>();

		TikaSigTester sw = new TikaSigTester();

		for (File file : datafiles) {
			if (!file.isFile()) {
				continue;
			}
			Identity detection = sw.identify(file);
			identities.add(detection);
			System.out.println(file.getAbsolutePath() + ":"
					+ detection.getMime());
		}
		return identities;

	}

	/**
	 * @return the sorted set of media types contained in the MIME Type Repo
	 */
	public final SortedSet<MediaType> getTypes() {
		return Collections.unmodifiableSortedSet(this.mimeRepository
				.getMediaTypeRegistry().getTypes());
	}

	private static List<File> getFiles(File dir) {
		List<File> result = new ArrayList<File>();
		if (!dir.isDirectory()) {
			result.add(dir);
			return result;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			result.addAll(getFiles(file));
		}
		Collections.sort(result);
		return result;
	}

	private MediaType identify(InputStream input) {
		Metadata metadata = new Metadata();
		MediaType mediaType;
		try {
			TikaInputStream stream = TikaInputStream.get(input);
			mediaType = this.mimeRepository.detect(stream, metadata);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return mediaType;
	}

	private static List<URL> getVanillaUrls() throws IOException {
		final List<URL> tikaFirstUrls = new ArrayList<URL>();
		tikaFirstUrls.add(getCoreUrl());
		tikaFirstUrls.addAll(getCustomUrls());
		return tikaFirstUrls;
	}

	private static List<URL> getCustomUrls() throws IOException {
		// This allows us to replicate class.getResource() when using
		// the classloader directly
		final ClassLoader cl = MimeTypes.class.getClassLoader();
		return Collections.list(cl
				.getResources(CLASS_PREFIX + CUSTOM_MIMETYPES));
	}

	private static URL getCoreUrl() {
		// This allows us to replicate class.getResource() when using
		// the classloader directly
		final ClassLoader cl = MimeTypes.class.getClassLoader();

		// Return the core url
		return cl.getResource(CLASS_PREFIX + TIKA_MIMETYPES);
	}

	/**
	 * A wrapper class that wraps the identified Tika Media Type with the
	 * duration taken to identify the file in milliseconds and the File object
	 * ide
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 1 Nov 2012:23:11:01
	 */
	public static final class Identity {
		private final MediaType mime;
		private final long duration;
		private final URI location;

		private Identity(final URI location, final MediaType mime, long duration) {
			this.location = location;
			this.mime = mime;
			this.duration = duration;
		}

		/**
		 * Static factory method that creates an identity from the passed
		 * values. Converts the file to a URI by calling File.toURI().
		 * 
		 * @param file
		 *            the file identified
		 * @param mime
		 *            the MIME type returned by Tika
		 * @param duration
		 *            the time taken to identify the file in milliseconds
		 * @return the new Identity object
		 */
		public static final Identity fromFile(File file, MediaType mime,
				long duration) {
			return new Identity(file.toURI(), mime, duration);
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
	}

	/**
	 * A little test main to identify GovDocs from passed param
	 * @param args
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
		String govDocsData = null;
		if (args.length == 1) {
			govDocsData = args[0];
		}
		TikaSigTester sw = new TikaSigTester();
		sw.identify(govDocsData);

	}

	/**
	 * Print out a summary of all known types:
	 * @param tester the TikaSigTester to print the known types from
	 * 
	 * @throws MimeTypeException
	 */
	public static void printTypes(TikaSigTester tester)
			throws MimeTypeException {
		for (MediaType md : tester.getTypes()) {
			MimeType mt = tester.mimeRepository.forName(md.toString());
			StringBuilder str = new StringBuilder(mt.getType().toString())
					.append(TAB).append(mt.getExtension()).append(TAB)
					.append(mt.getName()).append(TAB)
					.append(mt.getDescription());
			System.out.println(str);
		}
	}
}