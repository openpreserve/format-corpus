/**
 * 
 */
package org.apache.tika.mime;

import org.opf_labs.fmts.droid.SigDefSubmission;

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
	public static SigDefSubmission fromTikaMimeType( MimeType mt ) {
		SigDefSubmission sd = new SigDefSubmission();
		sd.name = mt.getName();
		sd.version = "";
		sd.puid = "tika/"+mt.getName();
		sd.extension = mt.getExtension();
		sd.mimetype = mt.getType().toString();
		if( mt.hasMagic() ) {
			for( Magic m : mt.getMagics() ) {
				// TODO Fix up Tika so the Clause is only package-private.
				System.err.println("Could not access Tika signature magic clauses... "+m.getPriority());
			}
		}
		return sd;
	}
}
