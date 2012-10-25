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
 * mime-type
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MimeType {
	
	@XmlAttribute
	String type;
	
    @XmlElement(name = "glob")
	List<Glob> glob;
	
    @XmlElement(name = "magic")
	List<Magic> magic;
	
    @XmlElement(name = "alias")
	List<String> alias;
	
	// sub-class-of
    @XmlElement(name = "sub-class-of")
	SubClassOf subclassof;
	
	// xml:lang attributes
    @XmlElement(name = "comment")
	List<String> comment;
	
	// xml:lang attributes
    @XmlElement(name = "acronym")
	List<String> acronym;
	
	// xml:lang attributes
	//expanded-acronym
    @XmlElement(name = "expanded-acronym")
	List<String> expandedacronym;
	
	// name attribute
    @XmlElement(name = "icon")
	List<String> icon;

	// name attribute
	// generic-icon
    @XmlElement(name = "generic-icon")
	List<String> genericicon;
	
	// root-XML elements have namespaceURI and localName attributes. 
    @XmlElement(name = "root-XML")
    RootXML rootXML;
	
	// treemagic elements contain a list of treematch elements, any of which may match, and an optional priority attribute for all of the contained rules.
	// Infers type from file system data?

}
