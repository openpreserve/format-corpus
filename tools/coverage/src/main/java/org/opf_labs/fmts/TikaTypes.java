/**
 * 
 */
package org.opf_labs.fmts;

import java.util.ArrayList;
import java.util.List;

import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

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
    	return mi;
    }
    
}
