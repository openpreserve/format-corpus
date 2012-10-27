/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * Each match element has a number of attributes:
 * 
 * Attribute	Required?	Value
 * type	Yes	string, host16, host32, big16, big32, little16, little32 or byte.
 * offset	Yes	The byte offset(s) in the file to check. This may be a single number or a range in the form `start:end', indicating that all offsets in the range should be checked. The range is inclusive.
 * value	Yes	 The value to compare the file contents with, in the format indicated by the type attribute.
 * mask	No	 The number to AND the value in the file with before comparing it to `value'. Masks for numerical types can be any number, while masks for strings must be in base 16, and start with 0x.
 * 
 * Each element corresponds to one line of file(1)'s magic.mime file. They can be 
 * nested in the same way to provide the equivalent of continuation lines. 
 * That is, <a><b/><c/></a> means 'a and (b or c)'.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Match {
	
	@XmlElement( name = "match" )
	private List<Match> matches;
	
	@XmlAttribute
	private String type;
	
	@XmlAttribute
	private String offset;
	
	@XmlAttribute
	private String value;
	
	@XmlAttribute
	private String mask;

	/**
	 * @return the match
	 */
	public List<Match> getMatches() {
		return matches;
	}

	/**
	 * @param match the match to set
	 */
	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the offset
	 */
	public String getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}

	/**
	 * @param mask the mask to set
	 */
	public void setMask(String mask) {
		this.mask = mask;
	}

}
