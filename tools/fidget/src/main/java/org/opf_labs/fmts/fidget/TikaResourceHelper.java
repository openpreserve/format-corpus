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
package org.opf_labs.fmts.fidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.mime.MimeTypesFactory;

/**
 * Utility class that provides laoding methods for the Tika Sig Definitions 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Nov 2012:07:08:23
 */

class TikaResourceHelper {
	/** Name of the core Tika MIME Type definition file */
	public final static String TIKA_MIMETYPES = "tika-mimetypes.xml";
	/** Name of the custom Tika MIME Type definition file */
	public final static String CUSTOM_MIMETYPES = "custom-mimetypes.xml";

	private TikaResourceHelper() {
		throw new AssertionError("NO THROUGH ROAD");
	}

	private final static String CLASS_PREFIX = MimeTypes.class.getPackage()
			.getName().replace('.', '/')
			+ "/";

	final static URL getCoreUrl() {
		// This allows us to replicate class.getResource() when using
		// the classloader directly
		final ClassLoader cl = MimeTypes.class.getClassLoader();

		// Return the core url
		return cl.getResource(CLASS_PREFIX + TIKA_MIMETYPES);
	}

	static List<URL> getCustomUrls() throws IOException {
		// This allows us to replicate class.getResource() when using
		// the classloader directly
		final ClassLoader cl = MimeTypes.class.getClassLoader();
		return Collections.list(cl
				.getResources(CLASS_PREFIX + CUSTOM_MIMETYPES));
	}

	final static List<URL> getVanillaUrls() throws IOException {
		final List<URL> tikaFirstUrls = new ArrayList<URL>();
		tikaFirstUrls.add(getCoreUrl());
		tikaFirstUrls.addAll(getCustomUrls());
		return tikaFirstUrls;
	}

	static final InputStream[] streamsFromUrls(final List<URL> urls)
			throws IOException {
		InputStream[] streams = new InputStream[urls.size()];
		int strInd = 0;
		for (URL url : urls) {
			streams[strInd++] = url.openStream();
		}
		return streams;
	}

	static final InputStream[] streamsFromFiles(final File... files)
			throws FileNotFoundException {
		InputStream[] streams = new InputStream[files.length];
		int strInd = 0;
		for (File file : files) {
			streams[strInd++] = new FileInputStream(file);
		}
		return streams;
	}

	static final MimeTypes fromStreamArrays(final InputStream[] internal,
			final InputStream[] supplied) throws IOException, MimeTypeException {
		final MimeTypes repo = MimeTypesFactory.create(concat(internal,
				supplied));
		// Close our streams
		for (InputStream str : internal) {
			str.close();
		}
		return repo;
	}

	static final <T> T[] concat(final T[] first, final T[] second) {
		T[] res = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, res, first.length, second.length);
		return res;
	}
}
