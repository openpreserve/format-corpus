/**
 * 
 */
package org.apache.tika.mime;

import java.util.List;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class TikaMimeType {

	public static List<Magic> getMagics(MimeType mt) {
		return mt.getMagics();
	}
	
}
