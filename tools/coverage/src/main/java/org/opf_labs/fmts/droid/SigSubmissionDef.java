/**
 * 
 */
package org.opf_labs.fmts.droid;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class used to create to the PRONOM sig generation service.
 * 
 * It has been declared with example entries, but these should of course be overwritten.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class SigSubmissionDef {

	public String name = "Development Signature";
	public String version = "1.0";
	public String puid = "dev/1";
	public String extension = "ext";
	public String mimetype = "text/x-test-signature";
	public List<InternalSigSubmission> signatures = new ArrayList<InternalSigSubmission>();
		
}
