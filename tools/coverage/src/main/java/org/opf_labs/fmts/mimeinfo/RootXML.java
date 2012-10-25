/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class RootXML {
	
	@XmlAttribute
	String namespaceURI;

	@XmlAttribute
	String localName;
	
}
