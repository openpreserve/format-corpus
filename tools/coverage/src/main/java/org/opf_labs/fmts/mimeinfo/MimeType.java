/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * mime-type
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MimeType {
	
    @XmlElement(name = "glob")
	List<Glob> glob;
	
    @XmlElement(name = "")
	List<Magic> magic;
	
    @XmlElement(name = "")
	List<String> alias;
	
	// sub-class-of
    @XmlElement(name = "sub-class-of")
	List<String> subclassof;
	
	// xml:lang attributes
    @XmlElement(name = "")
	List<String> comment;
	
	// xml:lang attributes
    @XmlElement(name = "")
	List<String> acronym;
	
	// xml:lang attributes
	//expanded-acronym
    @XmlElement(name = "")
	List<String> expandedacronym;
	
	// name attribute
    @XmlElement(name = "")
	List<String> icon;

	// name attribute
	// generic-icon
    @XmlElement(name = "")
	List<String> genericicon;
	
	// root-XML elements have namespaceURI and localName attributes. 
	
	// treemagic elements contain a list of treematch elements, any of which may match, and an optional priority attribute for all of the contained rules.
	// Infers type from file system data?

}
