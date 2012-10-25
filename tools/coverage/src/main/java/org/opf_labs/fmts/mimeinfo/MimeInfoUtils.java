/**
 * 
 */
package org.opf_labs.fmts.mimeinfo;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class MimeInfoUtils {

	private static JAXBContext context;
	private static Marshaller marshaller;
	private static Unmarshaller unmarshaller;

	static {
		try {
			context = JAXBContext.newInstance(MimeInfo.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	        unmarshaller = context.createUnmarshaller();		
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void printer( MimeInfo mimeInfo ) throws JAXBException {
        StringWriter writer = new StringWriter();
        marshaller.marshal(mimeInfo,writer);
        System.out.println(writer.toString());
    }
    
    public static MimeInfo parser( InputStream in ) throws JAXBException {
        return (MimeInfo)unmarshaller.unmarshal(in);
    }

}
