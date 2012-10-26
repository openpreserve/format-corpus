/**
 * 
 */
package org.opf_labs.fmts.droid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;

import eu.planets_project.ifr.core.techreg.formats.SigFileUtils;
import uk.gov.nationalarchives.pronom.FileFormatType;
import uk.gov.nationalarchives.pronom.SigFile;

/**
 * @author Andrew.Jackson@bl.uk
 *
 */
public class ForegCmd {

	/**
	 * Apache CLI
	 * http://commons.apache.org/cli/usage.html
	 * 
	 * @param args
	 * @throws Exception 
	 * @throws JAXBException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception  {
		//downloadSigFile();
		downloadAllPronomFormatRecords(new File("data/pronom"));
		//pythonInvoker();
	}
	
	
	static void downloadSigFile() {
		// To make java.net.URL cope with an authenticating proxy.
		// Apache HTTPClient does this automatically, but we're not using that here at the moment.
		String proxyUser = System.getProperty("http.proxyUser");
		if (proxyUser != null) {
            Authenticator.setDefault(
            		new ProxyAuth( proxyUser, System.getProperty("http.proxyPassword") ) );
		}
		
		SigFile sigFile = SigFileUtils.getLatestSigFile();
		try {
			SigFileUtils.writeSigFileToOutputStream(sigFile, new FileOutputStream("signaturefile.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	static class ProxyAuth extends Authenticator {
	    private PasswordAuthentication auth;

	    private ProxyAuth(String user, String password) {
	        auth = new PasswordAuthentication(user, password == null ? new char[]{} : password.toCharArray());
	    }

	    protected PasswordAuthentication getPasswordAuthentication() {
	        return auth;
	    }
	}

	public static void downloadAllPronomFormatRecords(File outputFolder) throws Exception {
		SigFile sigFile = SigFileUtils.getLatestSigFile();
		BigInteger version = sigFile.getFFSignatureFile().getVersion();
		System.out.println("Got version "+version);
		String filename = "droid-signature-file.xml";
		try {
			SigFileUtils.writeSigFileTypeToOutputStream( sigFile.getFFSignatureFile(),
					new FileOutputStream( new File( outputFolder, filename) ) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(e);
		}
		//
		try {
			for( FileFormatType fft : sigFile.getFFSignatureFile().getFileFormatCollection().getFileFormat() ) {
				String puid = fft.getPUID();
				String puidFilename = puid+".xml";
				FileOutputStream fos = new FileOutputStream(new File(outputFolder, puidFilename) );
				URL repurl = SigFileUtils.getPronomUrlForPUID(puid);
				System.out.println("Downloading "+repurl+" to "+outputFolder+"/"+puidFilename );
				IOUtils.copy( repurl.openStream(), fos );
				fos.flush();
				fos.getChannel().force(true);
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(e);
		}

	}

}
