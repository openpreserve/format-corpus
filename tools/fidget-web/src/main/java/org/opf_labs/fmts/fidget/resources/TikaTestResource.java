/**
 * 
 */
package org.opf_labs.fmts.fidget.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.opf_labs.fmts.fidget.views.ApplicationView;


/**
 * TODO JavaDoc for TikaTestResource.</p>
 * TODO Tests for TikaTestResource.</p>
 * TODO Implementation for TikaTestResource.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 5 Nov 2012:07:17:09
 */
@Path("/")
public class TikaTestResource {
	/**
	 * This is the BEAM application home page.
	 * 
	 * @return the home page for the BEAM application
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public ApplicationView homeHtml() {
		return ApplicationView.getNewInstance("home.ftl");
	}
}
