/**
 * 
 */
package org.opf_labs.fmts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.tika.Tika;
import org.opf_labs.fmts.fidget.IdentificationResult;
import org.opf_labs.fmts.fidget.TikaSigTester;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class CorpusCoverageAnalysis {

	private File root;
	private TikaSigTester tBase = TikaSigTester.justTika();
	private TikaSigTester tCustom = TikaSigTester.justCustom();
	private TikaSigTester tAll = TikaSigTester.vanilla();

	public CorpusCoverageAnalysis(File file) throws IOException {
		this.root = file.getCanonicalFile();
		System.out.println("Tika Version: "+CoverageAnalysis.getComponentVersion("org.apache.tika", "tika-core"));
		System.out.println("Fidget Version: "+CoverageAnalysis.getComponentVersion("org.opf-labs.fmt", "fidget"));
		System.exit(1);
		
	}

	public void ident() throws FileNotFoundException, URISyntaxException {
		this.ident(root);
	}

	private void ident( File folder ) throws FileNotFoundException, URISyntaxException {
		for( File f : folder.listFiles() ) {
			if( !f.isHidden() && !f.getName().equals("tools") ) {
				if( f.isDirectory() ) {
					ident(f);
				}
				else {
					IdentificationResult ibf = tBase.identify(f);
					IdentificationResult ibs = tBase.identify( new FileInputStream(f) );
					IdentificationResult iaf = tAll.identify(f);
					IdentificationResult ias = tAll.identify( new FileInputStream(f) );
					System.out.println("\""+ibf.getLocation().normalize().toASCIIString().replaceFirst(root.toURI().toASCIIString(), "")+"\""
											+", \""+ibf.getMime()+"\""
										    +", \""+ibs.getMime()+"\""
											+", \""+iaf.getMime()+"\""
										    +", \""+ias.getMime()+"\""
								    );
				}
			}
		}
	}
	
	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		CorpusCoverageAnalysis cca = new CorpusCoverageAnalysis(new File("../.."));
		cca.ident();
	}

}
