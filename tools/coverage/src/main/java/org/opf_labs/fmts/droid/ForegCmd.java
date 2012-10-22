/**
 * 
 */
package org.opf_labs.fmts.droid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.xml.bind.JAXBException;

import eu.planets_project.ifr.core.techreg.formats.SigFileUtils;
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
		SigFileUtils.downloadAllPronomFormatRecords(new File("data/pronom"));
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


}
