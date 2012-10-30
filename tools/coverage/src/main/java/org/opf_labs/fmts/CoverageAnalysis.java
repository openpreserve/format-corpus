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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import uk.gov.nationalarchives.pronom.FileFormatType;
import uk.gov.nationalarchives.pronom.SigFile;
import eu.planets_project.ifr.core.techreg.formats.SigFileUtils;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class CoverageAnalysis {

	// Apache Tika
	private MimeTypes tikaTypes;
	// DROID
	private SigFile sigFile;
	
	public CoverageAnalysis() {
		// Set up Apache Tika
		// This default should load:
        //   "tika-mimetypes.xml", "custom-mimetypes.xml"
    	tikaTypes = MimeTypes.getDefaultMimeTypes();

    	// Set up DROID coverage analysis.
    	// FIXME Failover if no network?
		//sigFile = SigFileUtils.getLatestSigFile();
    	try {
			sigFile = SigFileUtils.readSigFile( 
					new FileInputStream("/Users/andy/Documents/workspace/nanite/DROID_SignatureFile_V63.xml") );
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public List<MimeType> getTikaTypeList() {
    	List<MimeType> mts = new ArrayList<MimeType>();
        for( MediaType type : tikaTypes.getMediaTypeRegistry().getTypes() ) {
        	try {
        		MimeType mt = tikaTypes.forName(type.toString());
				//System.out.println("Type: "+type.toString()+ " "+mt.getExtensions() );
				mts.add(mt);
			} catch (MimeTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return mts;
    }
    
    private static QName extensionQName = new QName("http://www.nationalarchives.gov.uk/pronom/SignatureFile", "Extension");
    private static QName iSigIDQName = new QName("http://www.nationalarchives.gov.uk/pronom/SignatureFile", "InternalSignatureID");
    private static QName hasPriorityOverFFIDQName = new QName("http://www.nationalarchives.gov.uk/pronom/SignatureFile", "HasPriorityOverFileFormatID");
    
    public List<MimeType> getDroidTypeList() {
    	System.out.println("DROID "+sigFile);
    	System.out.println("DROID SigFile "+sigFile.getFFSignatureFile());
    	System.out.println("DROID SigFile Version "+sigFile.getFFSignatureFile().getVersion());
    	System.out.println("DROID SigFile Version "+sigFile.getFFSignatureFile().getFileFormatCollection());
    	System.out.println("DROID SigFile Version "+sigFile.getFFSignatureFile().getFileFormatCollection().getFileFormat());
    	for( FileFormatType ff :
    			sigFile.getFFSignatureFile().getFileFormatCollection().getFileFormat() ) {
    		ff.getID();
    		ff.getPUID();
    		ff.getName();
    		ff.getMIMEType();
    		ff.getVersion();
       		String iSigID = null;
       		List<String> exts = new ArrayList<String>();
    		List<String> hasPOver = new ArrayList<String>();
    		for( JAXBElement<? extends Serializable> e : ff.getInternalSignatureIDOrExtensionOrHasPriorityOverFileFormatID() ) {
    			if( extensionQName.equals(e.getName()) ) {
    				String v = e.getValue().toString();
    				if( !"".equals(v) && !"unknown".equalsIgnoreCase(v))
    					exts.add(v);
       			} else if( iSigIDQName.equals(e.getName()) ) {
       				iSigID = e.getValue().toString();
       			} else if( hasPriorityOverFFIDQName.equals(e.getName()) ) {
    				String v = e.getValue().toString();
    				if( !"".equals(v) )
    					hasPOver.add(v);
       			} else {
        			System.out.println("Did not parse: " + e.getName()+" "+e.getValue());
    			}
    		}
    	    // Build Media Type:
    		String mts = "";
    		boolean useVersion = true;
    		// If there is a Mime Type, use that:
    		if( ff.getMIMEType() != null && ! "".equals(ff.getMIMEType().trim()) ) {
    			mts = ff.getMIMEType().trim();
    		}
    		// Fall back to extension if no Mime Type:
    		else if( exts.size() > 0 ) {
    			mts = "application/x-ext-"+exts.get(0);
    		}
    		// Fall back to PUID if all else has failed:
    		else {
    			mts = "application/x-puid-"+ff.getPUID().replace("/", "-");
    			useVersion = false;
    		}
    		// Add the version parameter, if any:
    		if( useVersion && ff.getVersion() != null && ! "".equals(ff.getVersion()) && !"unknown".equalsIgnoreCase(ff.getVersion()) ) {
    			String version = ff.getVersion().replace("/", "-");
    		    mts += ";version=\""+version+"\"";
    		}
    		// Parse as MediaType:
    		MediaType mdt = MediaType.parse(mts);
    		//System.out.println("Media Type Source: "+mts);
    		//System.out.println("Media Type: "+mdt);
    		if( iSigID != null ) {
    			//System.out.println("Has Magic.");
    		}
    	}
    	return null;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Look through signature sources...
		CoverageAnalysis ca = new CoverageAnalysis();
		List<MimeType> tika = ca.getTikaTypeList();
		List<MimeType> droid = ca.getDroidTypeList();
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
					mc.droid = d;
				}
				tnd_types++;
			}
			mcm.put(m.getType().toString(), mc);			
		}
		// 
		System.out.println("Types: "+tnd_types+"/"+t_types);
		
	}
	 
}
