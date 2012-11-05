/**
 * 
 */
package org.opf_labs.fmts.fidget.views;

import com.yammer.dropwizard.views.View;


/**
 * TODO JavaDoc for ApplicationView.</p>
 * TODO Tests for ApplicationView.</p>
 * TODO Implementation for ApplicationView.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Oct 2012:02:10:14
 */

public final class ApplicationView extends View {

	/**
	 * @param templateName the name of the Freemarker template to load
	 */
	private ApplicationView(final String templateName) {
		super(templateName);
	}
	
	/**
	 * @param templateName
	 * @return a new ApplicationView instance processing the passed template
	 */
	public static final ApplicationView getNewInstance(final String templateName) {
		return new ApplicationView(templateName);
	}

}
