/**
 * 
 */
package org.opf_labs.fmts.droid;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class InternalSigSubmission {

	public enum Anchor { BOFoffset, EOFoffset, Variable };
	
	public String signature = "255044462D312E34";
	public Anchor anchor = Anchor.BOFoffset;
	public int offset = 0;
	public int maxoffset = 0;				
	
}
