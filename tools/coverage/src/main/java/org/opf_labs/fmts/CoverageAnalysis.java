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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.opf_labs.fmts.fidget.TikaSigTester;
import org.opf_labs.fmts.mimeinfo.Glob;
import org.opf_labs.fmts.mimeinfo.MimeType;
import org.opf_labs.fmts.mimeinfo.droid.DroidMimeType;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class CoverageAnalysis {

	private TikaTypes tikaTypes = new TikaTypes();
	private DroidTypes droidTypes = new DroidTypes();
	
	public CoverageAnalysis() {
		analyse2();
	}
	
	public void analyse2() {
		List<MimeType> tika = tikaTypes.getTypes();
		List<MimeType> droid = droidTypes.getTypes();
		
		// set up the comparison
		Map<String, ArrayList<MimeCompare>> extComp = new TreeMap<String, ArrayList<MimeCompare>>();
		
		// parse the tika files
		int t_count = 0;
		for(MimeType tika_mt: tika) {
			t_count++;
			MimeCompare mc = new MimeCompare();
			mc.tika = tika_mt;
			
			for( Glob g : tika_mt.getGlobs() ) {
				String g_type = g.getPattern();
				ArrayList<MimeCompare> mcs = null;
				if(extComp.containsKey(g_type)){
//					System.out.println("Already got "+g_type);
					mcs = extComp.get(g_type);
				} else {
					mcs = new ArrayList<MimeCompare>();
				}
				mcs.add(mc);
				extComp.put(g_type, mcs);
			}
		}
		
		System.out.println ("\nNow processing DROID signature file");
		
		// now process droid signatures, looking for matches
		int d_puid_count = 0;
		int d_mt_count = 0;
		for(MimeType droid_mt: droid){
			d_puid_count++;
			
			// count only true mimetypes (no application/x-ext- or application/x-puid-)
			System.out.println(droid_mt.getType());
			if(!droid_mt.getType().startsWith("application/x-ext-") &&
			   !droid_mt.getType().startsWith("application/x-puid-")){
				d_mt_count++;
			}
			
			
			for( Glob g: droid_mt.getGlobs() ){
				String g_type = g.getPattern();
				ArrayList<MimeCompare> mcs = null;
				if(extComp.containsKey(g_type)){
//					System.out.println("Found MimeCompare list for: "+g_type);
					mcs = extComp.get(g_type);
					
					// search through the list looking for a matching tika mimetype
					boolean foundMatch = false;
					for(MimeCompare mc: mcs){
						if( mc.tika!=null && mc.tika.getType().startsWith(droid_mt.getType())){
							// match
							System.out.println("Match");
							mc.droid = droid_mt;
							foundMatch = true;
						}
					}
					if(!foundMatch){
//						System.out.println("No equivalent Tika sig for "+droid_mt.getType());
						MimeCompare mc = new MimeCompare();
						mc.droid = droid_mt;
						mcs.add(mc);
						extComp.put(g_type, mcs);
					}
					
					
				} else {
					// not found a mimeCompare list, so create a new one
					mcs = new ArrayList<MimeCompare>();
					MimeCompare mc = new MimeCompare();
					mc.droid = droid_mt;
					mcs.add(mc);
					extComp.put(g_type, mcs);
				}
			}
		}
		
		
		// Show and write results
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("coverage-output.csv")));
		
			for(String glob: extComp.keySet()){
				
				StringBuffer t_list = new StringBuffer();
				StringBuffer d_list = new StringBuffer();
				for (MimeCompare mc: extComp.get(glob)){
					if(mc.tika!=null){
						t_list.append(mc.tika.getType()).append(" ");
					}
					if(mc.droid!=null){
						d_list.append(mc.droid.getType());
						
						DroidMimeType dmt = (DroidMimeType) mc.droid;
						List<String> sigIds = dmt.getSigIds();
						if(sigIds!=null && sigIds.size()>0){
							d_list.append("(");
							for(String id: sigIds){
								d_list.append(id).append(" ");
							}
							d_list.append(")");
						}
						
						d_list.append(" ");
					}
				}
//				System.out.print(glob+",");
//				System.out.print(t_list.toString()+","+d_list.toString());
//				System.out.println();
				
				// write out a CSV file
				writer.write(glob+",");
				writer.write(t_list.toString()+","+d_list.toString());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Tika Count: "+t_count);
		System.out.println("Droid PUID Count: "+d_puid_count);
		System.out.println("Droid True mimetype Count: "+d_mt_count);
	}
	
	/**
	 * AJ's initial version (previously in the constructor)
	 */
	public void analyse() {
		List<MimeType> tika = tikaTypes.getTypes();
		List<MimeType> droid = droidTypes.getTypes();
		
		// Set up the comparison:
		Map<String, MimeCompare> mcm = new HashMap<String,MimeCompare>();
		// By Extension:
		Map<String, MimeCompare> ecm = new HashMap<String,MimeCompare>();
		
		// Go through Tika
		int t_types = 0, tnd_types = 0;
		for( MimeType m : tika ) {
			MimeCompare mc = new MimeCompare();
			mc.tika = m;
			t_types++;
			boolean both = false;
			for( MimeType d : droid ) {
//				if( m.getType().equals(d.getType())) {
				if( m.getType().startsWith(d.getType())) {
					mc.droid = d;
					tnd_types++;
					System.out.println("Both "+m.getType());
					both = true;
				}
			}
			if( both == false ) {
				System.out.println("Only in Tika "+m.getType()+" "+m.getGlobsAsString());
			}
			mcm.put(m.getType(), mc);
			for( Glob g : m.getGlobs() ) {
				ecm.put(g.getPattern(), mc);
			}
		}
 		// Now pick up others, i.e. the DROID only ones:
		int d_types = 0;
		for( MimeType d : droid ) {
			if( ! mcm.containsKey(d.getType())) {
				MimeCompare mc = new MimeCompare();
				mc.droid = d;
				mcm.put(d.getType(), mc);			
				d_types++;
				System.out.println("DROID only "+d.getType()+" "+d.getGlobsAsString());
			}
			for( Glob g : d.getGlobs() ) {
				if( ecm.containsKey(g.getPattern())) {
					System.out.println("MATCH "+g.getPattern());
				} else {
					System.out.println("NO MATCH "+g.getPattern());
				}
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
		if( r == null ) {
			return null;
		}
		Properties p = new Properties();
		try {
			p.load(r);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return p.getProperty("version");
	}
	 
}
