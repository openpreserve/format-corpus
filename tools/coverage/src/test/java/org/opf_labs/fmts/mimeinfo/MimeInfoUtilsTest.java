package org.opf_labs.fmts.mimeinfo;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

/**
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class MimeInfoUtilsTest {
	
	public static String PERCIPIO_XML = "src/test/resources/percipio.pdf.xml";
	public static String TIKA_XML = "src/main/resources/org/apache/tika/mime/custom-mimetypes.xml";

	@Test
	public void testPercipioParser() throws FileNotFoundException, JAXBException {
		MimeInfo mi = MimeInfoUtils.parser(new FileInputStream(PERCIPIO_XML));
		MimeInfoUtils.printer(mi);
		//
		mi = MimeInfoUtils.parser(new FileInputStream(TIKA_XML));
		MimeInfoUtils.printer(mi);
		//
		fail("Not yet implemented");
	}

}
