/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

/**
 * 
 * glob elements have a pattern attribute. Any file whose name matches this
 * pattern will be given this MIME type (subject to conflicting rules in other
 * files, of course). There is also an optional weight attribute which is used
 * when resolving conflicts with other glob matches. The default weight value is
 * 50, and the maximum is 100.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 * 
 */
public class Glob {

	// Of the form *.ext'
	String pattern;
	
	// Integer 0-100, default 50
	String weight;
	
}
