/**
 * Copyright (C) 2012 Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.opf_labs.fmts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.tika.Tika;
import org.opf_labs.fmts.fidget.IdentificationResult;
import org.opf_labs.fmts.fidget.TikaSigTester;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class CorpusCoverageAnalysis {

	private File root;
	// NOTE that using Tika directly picks up custom sigs.
	private Tika tika = new Tika();
	private TikaSigTester tBase = TikaSigTester.justTika();
	private TikaSigTester tCustom = TikaSigTester.justCustom();
	private TikaSigTester tAll = TikaSigTester.vanilla();

	public CorpusCoverageAnalysis(File file) throws IOException {
		this.root = file.getCanonicalFile();
	}

	public void ident() throws URISyntaxException, IOException {
		this.ident(root);
	}

	private void ident( File folder ) throws URISyntaxException, IOException {
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
					System.out.println("\""+iaf.getLocation().normalize().toASCIIString().replaceFirst(root.toURI().toASCIIString(), "")+"\""
//							+", \""+tika.detect(f)+"\""
//							+", \""+tika.detect( new FileInputStream(f) )+"\""
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
		CorpusCoverageAnalysis cca = new CorpusCoverageAnalysis(new File(args[0]));
		System.out.println("# Path, Tika, Tika (stream only), Fidget, Fidget (stream only)");
		cca.ident();
		System.out.println("# Fidget Version: "+CoverageAnalysis.getComponentVersion("org.opf-labs.fmt", "fidget"));
		System.out.println("# Tika Version: "+CoverageAnalysis.getComponentVersion("org.apache.tika", "tika-core"));
	}

}
