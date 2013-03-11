/**
 * 
 */
package org.opf_labs.fmts.mimeinfo.droid;

import java.util.List;

import org.opf_labs.fmts.mimeinfo.Magic;
import org.opf_labs.fmts.mimeinfo.MimeType;

/**
 * @author pmay
 *
 */
public class DroidMimeType extends MimeType {

	private List<String> sigIds;
	
	/**
	 * @return the sigIds
	 */
	public List<String> getSigIds() {
		return this.sigIds;
	}

	/**
	 * @param sigIds the sigIds to set
	 */
	public void setSigIds(List<String> sigIds) {
		this.sigIds = sigIds;
	}
}
