/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

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
@XmlAccessorType(XmlAccessType.FIELD)
public class Glob {

	// Of the form *.ext'
	@XmlAttribute
	private String pattern;
	
	// Integer 0-100, default 50
	@XmlAttribute
	private String weight;

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
}
