/**
 * 
 */
package org.opf_labs.fmts.droid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class SigSubmissionDef {

	String name = "Development Signature";
	String version = "1.0";
	String puid = "dev/1";
	String extension = "ext";
	String mimetype = "text/x-test-signature";
	List<InternalSig> signatures = new ArrayList<InternalSig>();
		
	public SigSubmissionDef() {
		signatures.add( new InternalSig());
	}
	
	enum Anchor { BOFoffset, EOFoffset, Variable };
	
	public class InternalSig { 
		String signature = "255044462D312E34";
		Anchor anchor = Anchor.BOFoffset;
		int offset = 0;
		int maxoffset = 0;				
	}
}
