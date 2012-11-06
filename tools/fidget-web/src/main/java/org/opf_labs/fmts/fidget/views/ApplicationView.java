/**
 * Copyright (C) 2012 Carl Wilson <carl@openplanetsfoundation.org>
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
package org.opf_labs.fmts.fidget.views;

import org.opf_labs.fmts.fidget.IdentificationResult;

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
	private final IdentificationResult result;
	private final boolean isNull;

	/**
	 * @param templateName the name of the Freemarker template to load
	 */
	private ApplicationView(final String templateName) {
		super(templateName);
		this.result = null;
		this.isNull = true;
	}
	
	/**
	 * @param templateName the name of the Freemarker template to load
	 */
	private ApplicationView(final String templateName, final IdentificationResult result) {
		super(templateName);
		this.result = result;
		this.isNull = (this.result == null);
	}
	
	/**
	 * @param templateName
	 * @return a new ApplicationView instance processing the passed template
	 */
	public static final ApplicationView getNewInstance(final String templateName) {
		return new ApplicationView(templateName);
	}

	/**
	 * @param templateName
	 * @param result 
	 * @return a new ApplicationView instance processing the passed template
	 */
	public static final ApplicationView getNewInstance(final String templateName, final IdentificationResult result) {
		return new ApplicationView(templateName, result);
	}
	
	/**
	 * @return the identification result
	 */
	public IdentificationResult getResult() {
		return this.result;
	}
	
	/**
	 * @return the identification result
	 */
	public boolean isNull() {
		return this.isNull;
	}
}
