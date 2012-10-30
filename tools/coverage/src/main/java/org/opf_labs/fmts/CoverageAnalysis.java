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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		//
		Map<String, MimeCompare> mcm = new HashMap<String,MimeCompare>();
		// Go through Tika
		int t_types=0, tnd_types=0;
		for( MimeType m : tika ) {
			MimeCompare mc = new MimeCompare();
			mc.tika = m;
			t_types++;
			for( MimeType d : droid ) {
				if( m.getType().equals(d.getType())) {
					System.out.println("Got "+m.getType());
					mc.droid = d;
					tnd_types++;
				}
			}
			mcm.put(m.getType().toString(), mc);			
		}
		// 
		System.out.println("Types: "+tnd_types+"/"+t_types);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Look through signature sources...
		CoverageAnalysis ca = new CoverageAnalysis();

		
	}
	 
}
