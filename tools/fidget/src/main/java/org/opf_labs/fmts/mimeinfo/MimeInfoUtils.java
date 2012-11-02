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
package org.opf_labs.fmts.mimeinfo;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Hex;
import org.opf_labs.fmts.fidget.droid.InternalSig;
import org.opf_labs.fmts.fidget.droid.InternalSig.Anchor;
import org.opf_labs.fmts.fidget.droid.SigDefSubmission;
import org.opf_labs.fmts.fidget.droid.SigDefSubmission.Builder;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 * 
 */
public class MimeInfoUtils {

	private static JAXBContext context;
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;

	static {
		try {
			context = JAXBContext.newInstance(MimeInfo.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convenience method to print MimeInfo to screen
	 * 
	 * @param mimeInfo
	 * @throws JAXBException
	 */
	public static void printer(MimeInfo mimeInfo) throws JAXBException {
		StringWriter writer = new StringWriter();
		marshaller.marshal(mimeInfo, writer);
		System.out.println(writer.toString());
	}

	/**
	 * @param in
	 *            Input stream to the XML MimeInfo representation
	 * @return the MimeInfo object from XML
	 * @throws JAXBException
	 *             if the XML sucks...
	 */
	public static MimeInfo parser(InputStream in) throws JAXBException {
		return (MimeInfo) unmarshaller.unmarshal(in);
	}

	/**
	 * Generates a DROID-compatible SigDefSubmission from a MimeInfo class.
	 * 
	 * FIXME A number of unresolved issues with translation: - No clear AND/OR
	 * mapping. Are multiple DROID signatures ORs or ANDs in this context? -
	 * Only maps a very limited sub-set of MimeInfo - No priority mapping.
	 * 
	 * @param mi
	 * @return the DROID signature definitions submission
	 */
	public static SigDefSubmission toDroidSigDef(MimeInfo mi) {
		MimeType mt = mi.getMimetypes().get(0);
		Builder builder = SigDefSubmission.fromValues("tika/" + mt.getType(),
				mt.getType()).name(mt.getAcronyms().toString());
		builder.version("").extension(mt.getGlobs().get(0).getPattern());
		if (mt.getMagics() != null) {
			for (Magic mag : mt.getMagics()) {
				if (mag.getMatches() != null) {
					for (Match m : mag.getMatches()) {
						if (!"string".equalsIgnoreCase(m.getType())) {
							// FIXME Only supports simple match types, so warn
							// for now.
							System.err
									.println("Cannot currently transform sigs of type "
											+ m.getType());
							continue;
						}
						// FIXME Warn because getMask is not supported?
						// FIXME Warn because getMatches (AND matches) not
						// supported.
						// MimeInfo only really support BOF offsets.
						// FIXME Parse 0:8 style offsets and fill out maxoffset
						// accordingly.
						int offset = Integer.parseInt(m.getOffset());
						String cleanSig = cleanSigString(m.getValue());
						InternalSig iss = InternalSig.fromValues(cleanSig,
								Anchor.BOFoffset, offset, offset);
						// Add to the set:
						builder.addSig(iss);
					}
				}
			}
		}
		return builder.build();
	}

	private static String cleanSigString(final String sigString) {
		// FIXME hex-encode if leading 0x not present
		// FIXME case sensitivity
		if (sigString.startsWith("0x"))
			return sigString.substring(2);
		// TODO: test this as I'm not sure it does the right thing ATM
		Hex.encodeHexString(sigString.getBytes());
		return sigString;
	}

	// TODO Add method to create a PRONOM record from the MimeInfo?

}
