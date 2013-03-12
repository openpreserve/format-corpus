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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tika.mime.MagicRetriever;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.opf_labs.fmts.mimeinfo.Glob;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class TikaTypes {

	// Apache Tika
	private MimeTypes tikaTypes;
	
	public TikaTypes() {
		// Set up Apache Tika
		// This default should load:
        //   "tika-mimetypes.xml", "custom-mimetypes.xml"
    	tikaTypes = MimeTypes.getDefaultMimeTypes();
	}
	
	/**
	 * 
	 * @return
	 */
    public List<org.opf_labs.fmts.mimeinfo.MimeType> getTypes() {
    	List<org.opf_labs.fmts.mimeinfo.MimeType> mts = new ArrayList<org.opf_labs.fmts.mimeinfo.MimeType>();
        for( MediaType type : tikaTypes.getMediaTypeRegistry().getTypes() ) {
        	try {
        		MimeType mt = tikaTypes.forName(type.toString());
				//System.out.println("Type: "+type.toString()+ " "+mt.getExtensions() );
				mts.add(this.toMimeInfo(mt));
			} catch (MimeTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return mts;
    }
    
    /**
     * 
     * @param mt
     * @return
     */
    private org.opf_labs.fmts.mimeinfo.MimeType toMimeInfo( MimeType mt ) {
    	org.opf_labs.fmts.mimeinfo.MimeType mi = new org.opf_labs.fmts.mimeinfo.MimeType();
    	mi.setType( mt.getType().toString() );
    	List<Glob> globs = new ArrayList<Glob>();
    	for( String ext : mt.getExtensions() ) {
    		Glob g = new Glob();
    		g.setPattern(ext.replaceAll("^\\.", ""));
    		globs.add(g);
    	}
    	// FIXME: List<Magic> magics = TikaMimeType.getMagics(mt);
    	mi.setGlobs( globs );
    	return mi;
    }
        /**
         * Main function used for testing
         * @param args
         * @throws MimeTypeException 
         */
        public static void main(String [] args) throws MimeTypeException {
        	TikaTypes types = new TikaTypes();
        	int typeCount = 0;
        	int sigCounter = 0;
        	int extCounter = 0;
        	for (MediaType mediaType : types.tikaTypes.getMediaTypeRegistry().getTypes()) {
        		System.out.print(mediaType.toString() + ",");
        		/**
        		 * Output the parameters here
        		 */
//        		Map<String, String> params = mediaType.getParameters();
//        		for (String paramKey : params.keySet()) {
//        			System.out.println(paramKey + ":" + params.get(paramKey));
//        		}
        		MimeType mimeType = types.tikaTypes.getRegisteredMimeType(mediaType.toString());
        		System.out.print(mimeType.getDescription() + ",");
        		for (String ext : mimeType.getExtensions()) {
        			System.out.print(ext + " ");
        			extCounter++;
        		}
        		if (mimeType.hasMagic()) {
        			List<String> sigs = MagicRetriever.listMagic(mimeType);
        			System.out.print(",");
        			for (String sig : sigs) {
        				System.out.print(sig + " ");
        			}
            		sigCounter+=sigs.size();
        		}
        		typeCount++;
        		System.out.println();
        	}
        	System.out.println(typeCount + " types.");
        	System.out.println(sigCounter + " sigs.");
        	System.out.println(extCounter + " exts.");
        }
}
