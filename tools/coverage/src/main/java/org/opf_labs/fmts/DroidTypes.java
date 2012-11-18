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
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.opf_labs.fmts.mimeinfo.MimeType;

import eu.planets_project.ifr.core.techreg.formats.SigFileUtils;

import uk.gov.nationalarchives.pronom.FileFormatType;
import uk.gov.nationalarchives.pronom.SigFile;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class DroidTypes {
	// DROID
	private SigFile sigFile;
	
    private static QName extensionQName = new QName("http://www.nationalarchives.gov.uk/pronom/SignatureFile", "Extension");
    private static QName iSigIDQName = new QName("http://www.nationalarchives.gov.uk/pronom/SignatureFile", "InternalSignatureID");
    private static QName hasPriorityOverFFIDQName = new QName("http://www.nationalarchives.gov.uk/pronom/SignatureFile", "HasPriorityOverFileFormatID");
    
    public DroidTypes() {
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
    
    public List<MimeType> getTypes() {
    	List<MimeType> mtl = new ArrayList<MimeType>();
    	System.out.println("DROID "+sigFile);
    	System.out.println("DROID SigFile "+sigFile.getFFSignatureFile());
    	System.out.println("DROID SigFile Version "+sigFile.getFFSignatureFile().getVersion());
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
    		MimeType mt = new MimeType();
    		mt.setType(mts);
    		if( iSigID != null ) {
    			//System.out.println("Has Magic.");
    		}
    		// Add to the list:
    		mtl.add(mt);
    	}
    	return mtl;
    }

}
