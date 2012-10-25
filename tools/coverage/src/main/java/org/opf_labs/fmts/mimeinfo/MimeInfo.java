/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Based on the MIME Info spec. source XML files: 
 *   http://standards.freedesktop.org/shared-mime-info-spec/shared-mime-info-spec-0.18.html#id2604543
 * 
 * TODO Decide whether to use the http://www.freedesktop.org/standards/shared-mime-info namespace.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
@XmlRootElement(name="mime-info")
@XmlAccessorType(XmlAccessType.FIELD)
public class MimeInfo {

    @XmlElement(name = "mime-type")
	List<MimeType> mimetype;
	
    
    public static void factory( MimeInfo mimeInfo ) throws JAXBException {
    	JAXBContext context = JAXBContext.newInstance(MimeInfo.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //
        //MimeInfo signature = (MimeInfo)unmarshaller.unmarshal(System.in);
        //
        StringWriter writer = new StringWriter();
        marshaller.marshal(mimeInfo,writer);
        System.out.println(writer.toString());

    }
}
