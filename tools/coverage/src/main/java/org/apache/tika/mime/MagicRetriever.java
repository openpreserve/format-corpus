/**
 * 
 */
package org.apache.tika.mime;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that retrieves the string representations of the signatures
 * a bit primitive but I couldn't get them any other way.
 * 
 * @author carl
 *
 */
public class MagicRetriever {
	
	public static final List<String> listMagic(MimeType mimeType) {
		List<String> magics = new ArrayList<String>();
		for (Magic magic : mimeType.getMagics()) {
			magics.add(magic.toString());
		}
		return magics;
	}
}
