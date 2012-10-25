/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * magic elements contain a list of match elements, any of which may match, and
 * an optional priority attribute for all of the contained rules. Low numbers
 * should be used for more generic types (such as 'gzip compressed data') and
 * higher values for specific subtypes (such as a word processor format that
 * happens to use gzip to compress the file). The default priority value is 50,
 * and the maximum is 100.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 * 
 */
public class Magic {

	@XmlElement(nillable = true)
	private List<Match> match;
	
	// Integer between 0 and 100, defaults to 50.
	@XmlAttribute
	String priority;
	
}
