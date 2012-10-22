/**
 * 
 */
package org.opf_labs.fmts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

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
		sigFile = SigFileUtils.getLatestSigFile();
    	
	}
	
    public List<MimeType> getTikaTypeList() {
    	List<MimeType> mts = new ArrayList<MimeType>();
        for( MediaType type : tikaTypes.getMediaTypeRegistry().getTypes() ) {
        	try {
        		MimeType mt = tikaTypes.forName(type.toString());
				System.out.println("Type: "+type.toString()+ " "+mt.getExtensions() );
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
    		MediaType mdt = MediaType.parse(mts);
    		System.out.println("Media Type Source: "+mts);
    		System.out.println("Media Type: "+mdt);
    		if( iSigID != null ) {
    			System.out.println("Has Magic.");
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
		ca.getTikaTypeList();
		ca.getDroidTypeList();
	}

}
