/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SubClassOf {

	@XmlAttribute
	private String type;

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
	
}
