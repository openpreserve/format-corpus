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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.opf_labs.fmts.fidget.TikaSigTester;
import org.opf_labs.fmts.mimeinfo.MimeType;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class CoverageAnalysis {

	private TikaTypes tikaTypes = new TikaTypes();
	private DroidTypes droidTypes = new DroidTypes();
	
	public CoverageAnalysis() {
		List<MimeType> tika = tikaTypes.getTypes();
		List<MimeType> droid = droidTypes.getTypes();
		
		// Set up the comparison:
		Map<String, MimeCompare> mcm = new HashMap<String,MimeCompare>();
		
		// Go through Tika
		int t_types = 0, tnd_types = 0;
		for( MimeType m : tika ) {
			MimeCompare mc = new MimeCompare();
			mc.tika = m;
			t_types++;
			for( MimeType d : droid ) {
				if( m.getType().equals(d.getType())) {
					mc.droid = d;
					tnd_types++;
					System.out.println("Both "+m.getType());
				}
			}
			mcm.put(m.getType(), mc);			
		}
		// Now pick up others, i.e. the DROID only ones:
		int d_types = 0;
		for( MimeType d : droid ) {
			if( ! mcm.containsKey(d.getType())) {
				MimeCompare mc = new MimeCompare();
				mc.droid = d;
				mcm.put(d.getType(), mc);			
				d_types++;
				System.out.println("DROID only "+d.getType());
			}
		}
		// Currently not lineing up as treated as strings, and some are string plus version
		// import javax.activation.MimeType; instead?
		// 
		System.out.println("Types: "+tnd_types+"/"+t_types+" "+d_types);
	}
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		
		// Look through signature sources...
		CoverageAnalysis ca = new CoverageAnalysis();

		
	}

	/**
	 * 
	 * @param groupId
	 * @param artifactId
	 * @return
	 */
	public static String getComponentVersion(String groupId, String artifactId) {
		InputStream r = CoverageAnalysis.class.getResourceAsStream( "/META-INF/maven/"+groupId+"/"+artifactId+"/pom.properties");
		Properties p = new Properties();
		try {
			p.load(r);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return p.getProperty("version");
	}
	 
}
