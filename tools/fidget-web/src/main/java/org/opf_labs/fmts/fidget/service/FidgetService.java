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
package org.opf_labs.fmts.fidget.service;

import org.opf_labs.fmts.fidget.resources.TikaTestResource;

import com.google.common.cache.CacheBuilderSpec;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

/**
 * TODO JavaDoc for FidgetService.</p>
 * TODO Tests for FidgetService.</p>
 * TODO Implementation for FidgetService.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 5 Nov 2012:07:14:57
 */

public class FidgetService extends Service<FidgetConfiguration> {
	/**
	 * @param args the passed arg array
	 * @throws Exception thrown by the service
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			new FidgetService().run(new String[]{"server", "src/main/resources/config.yaml"});
		} else {
			new FidgetService().run(args);
		}

	}

	private FidgetService() {
		super("fidget-service");
	    // By default a restart will be required to pick up any changes to assets.
	    // Use the following spec to disable that behaviour, useful when developing.
		// TODO: Turn off caching
	    CacheBuilderSpec cacheSpec = CacheBuilderSpec.disableCaching();
	    // CacheBuilderSpec cacheSpec = AssetsBundle.DEFAULT_CACHE_SPEC;
	    addBundle(new AssetsBundle("/assets/", cacheSpec, "/"));
	    addBundle(new ViewBundle());
	    // TODO: for H2 BD for authorisation
	    //addBundle(new DBIExceptionsBundle());
	}

    @Override
	protected void initialize(FidgetConfiguration config, Environment environment)
			throws Exception {
		environment.addResource(new TikaTestResource());
	}
}
