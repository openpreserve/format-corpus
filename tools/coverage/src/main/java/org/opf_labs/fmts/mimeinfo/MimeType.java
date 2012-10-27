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
	private String type;
	
    @XmlElement(name = "glob")
    private List<Glob> globs;
	
    @XmlElement(name = "magic")
    private List<Magic> magics;
	
    @XmlElement(name = "alias")
    private List<String> aliases;
	
	// sub-class-of
    @XmlElement(name = "sub-class-of")
    private SubClassOf subclassof;
	
	// xml:lang attributes
    @XmlElement(name = "comment")
    private List<String> comments;
	
	// xml:lang attributes
    @XmlElement(name = "acronym")
    private List<String> acronyms;
	
	// xml:lang attributes
	//expanded-acronym
    @XmlElement(name = "expanded-acronym")
    private List<String> expandedacronyms;
	
	// name attribute
    @XmlElement(name = "icon")
    private List<String> icons;

	// name attribute
	// generic-icon
    @XmlElement(name = "generic-icon")
    private List<String> genericicons;
	
	// root-XML elements have namespaceURI and localName attributes. 
    @XmlElement(name = "root-XML")
    private RootXML rootXML;
	
	// treemagic elements contain a list of treematch elements, any of which may match, and an optional priority attribute for all of the contained rules.
	// Infers type from file system data?
    
    
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
	 * @return the globs
	 */
	public List<Glob> getGlobs() {
		return globs;
	}

	/**
	 * @param globs the globs to set
	 */
	public void setGlobs(List<Glob> globs) {
		this.globs = globs;
	}

	/**
	 * @return the magics
	 */
	public List<Magic> getMagics() {
		return magics;
	}

	/**
	 * @param magics the magics to set
	 */
	public void setMagics(List<Magic> magics) {
		this.magics = magics;
	}

	/**
	 * @return the aliases
	 */
	public List<String> getAliases() {
		return aliases;
	}

	/**
	 * @param aliases the aliases to set
	 */
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * @return the subclassof
	 */
	public SubClassOf getSubclassof() {
		return subclassof;
	}

	/**
	 * @param subclassof the subclassof to set
	 */
	public void setSubclassof(SubClassOf subclassof) {
		this.subclassof = subclassof;
	}

	/**
	 * @return the comments
	 */
	public List<String> getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	/**
	 * @return the acronyms
	 */
	public List<String> getAcronyms() {
		return acronyms;
	}

	/**
	 * @param acronyms the acronyms to set
	 */
	public void setAcronyms(List<String> acronyms) {
		this.acronyms = acronyms;
	}

	/**
	 * @return the expandedacronyms
	 */
	public List<String> getExpandedacronyms() {
		return expandedacronyms;
	}

	/**
	 * @param expandedacronyms the expandedacronyms to set
	 */
	public void setExpandedacronyms(List<String> expandedacronyms) {
		this.expandedacronyms = expandedacronyms;
	}

	/**
	 * @return the icons
	 */
	public List<String> getIcons() {
		return icons;
	}

	/**
	 * @param icons the icons to set
	 */
	public void setIcons(List<String> icons) {
		this.icons = icons;
	}

	/**
	 * @return the genericicons
	 */
	public List<String> getGenericicons() {
		return genericicons;
	}

	/**
	 * @param genericicons the genericicons to set
	 */
	public void setGenericicons(List<String> genericicons) {
		this.genericicons = genericicons;
	}

	/**
	 * @return the rootXML
	 */
	public RootXML getRootXML() {
		return rootXML;
	}

	/**
	 * @param rootXML the rootXML to set
	 */
	public void setRootXML(RootXML rootXML) {
		this.rootXML = rootXML;
	}

}
