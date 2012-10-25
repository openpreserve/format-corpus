/**
 * 
 */
package org.opf_labs.fmts;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.Test;
import org.opf_labs.fmts.mimeinfo.MimeInfoUtils;
import org.opf_labs.fmts.mimeinfo.MimeInfoUtilsTest;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class TikaSigTesterTest {

	/**
	 * Test method for {@link org.opf_labs.fmts.TikaSigTester#TikaSigTester(java.io.File, boolean)}.
	 * @throws IOException 
	 * @throws MimeTypeException 
	 */
	@Test
	public void testTikaSigTesterFileBoolean() throws MimeTypeException, IOException {
		// 
		System.out.println("Just One...");
		TikaSigTester tst = new TikaSigTester(new File(MimeInfoUtilsTest.PERCIPIO_XML), false);
		System.out.println("Try: "+tst.identify( new FileInputStream( "/Users/andy/Documents/workspace/nanite/nanite-ext/src/test/resources/simple.pdf" ) ));
		this.printMimeTypes(tst.getMimeTypes());
		//
		System.out.println("All...");
		tst = new TikaSigTester( new File(MimeInfoUtilsTest.PERCIPIO_XML), true);
		this.printMimeTypes(tst.getMimeTypes());		
		
		fail("Not yet implemented");
	}
	
	public void printMimeTypes(MimeTypes mimeTypes) {
		System.out.println("Total: "+mimeTypes.getMediaTypeRegistry().getTypes().size());
		for( MediaType mt : mimeTypes.getMediaTypeRegistry().getTypes()) {
			System.out.println("MediaType: "+mt.toString());
		}
	}

}
