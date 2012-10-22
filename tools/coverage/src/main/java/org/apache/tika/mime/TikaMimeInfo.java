/**
 * 
 */
package org.apache.tika.mime;

import org.opf_labs.fmts.droid.SigSubmissionDef;

/**
 * Utility class to access package-protected fields of Tika's MIME code.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class TikaMimeInfo {

	/**
	 * 
	 * @param mt
	 * @return
	 */
	public static SigSubmissionDef fromTikaMimeType( MimeType mt ) {
		SigSubmissionDef sd = new SigSubmissionDef();
		sd.name = mt.getName();
		sd.version = "";
		sd.puid = "tika/"+mt.getName();
		sd.extension = mt.getExtension();
		sd.mimetype = mt.getType().toString();
		if( mt.hasMagic() ) {
			for( Magic m : mt.getMagics() ) {
				// TODO Fix up Tika so the Clause is only package-private.
			}
		}
		return sd;
	}
}
