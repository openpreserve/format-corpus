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

import org.opf_labs.fmts.fidget.droid.InternalSigSubmission;
import org.opf_labs.fmts.fidget.droid.SigDefSubmission;

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
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	        unmarshaller = context.createUnmarshaller();		
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void printer( MimeInfo mimeInfo ) throws JAXBException {
        StringWriter writer = new StringWriter();
        marshaller.marshal(mimeInfo,writer);
        System.out.println(writer.toString());
    }
    
    public static MimeInfo parser( InputStream in ) throws JAXBException {
        return (MimeInfo)unmarshaller.unmarshal(in);
    }

    /**
     * Generates a DROID-compatible SigDefSubmission from a MimeInfo class.
     * 
     * FIXME A lot of issues with translation:
     *  - No clear AND/OR mapping. Are multiple DROID signatures ORs or ANDs in this context?
     *  - Only maps a very limited sub-set of MimeInfo 
     *  - No priority mapping.
     * 
     * @param mi
     * @return
     */
	public static SigDefSubmission toDroidSigDef(MimeInfo mi) {
		MimeType mt = mi.getMimetypes().get(0);
		SigDefSubmission sd = new SigDefSubmission();
		sd.name = mt.getAcronyms().toString();
		sd.version = null;
		sd.puid = "tika/"+mt.getType();
		sd.extension = mt.getGlobs().get(0).getPattern();
		sd.mimetype = mt.getType();
		if( mt.getMagics() != null ) {
			for( Magic mag : mt.getMagics() ) {
				if( mag.getMatches() != null ) {
					for( Match m : mag.getMatches() ) {
						InternalSigSubmission iss = new InternalSigSubmission();
						if( ! "string".equalsIgnoreCase(m.getType()) ) {
							// FIXME Only supports simple match types, so warn for now.
							System.err.println("Cannot currently transform sigs of type "+m.getType());
							continue;
						}
						// FIXME Warn because getMask is not supported?
						// FIXME Warn because getMatches (AND matches) not supported.
						// MimeInfo only really support BOF offsets.
						iss.anchor = InternalSigSubmission.Anchor.BOFoffset;
						// FIXME Parse 0:8 style offsets and fill out maxoffset accordingly.
						iss.offset = Integer.parseInt(m.getOffset());
						iss.maxoffset = iss.offset;
						// FIXME Strip of leading 0x, or hex-encode if that is not present.
						iss.signature = m.getValue().substring(2);
						// Add to the set:
						sd.signatures.add(iss);
					}
				}
			}
		}
		return sd;
	}

}
