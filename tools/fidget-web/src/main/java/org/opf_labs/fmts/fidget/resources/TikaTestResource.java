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
package org.opf_labs.fmts.fidget.resources;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.tika.mime.MimeTypeException;
import org.opf_labs.fmts.fidget.IdentificationResult;
import org.opf_labs.fmts.fidget.TikaSigTester;
import org.opf_labs.fmts.fidget.views.ApplicationView;

import com.sun.jersey.multipart.FormDataParam;


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

	/**
	 * @param sigStream the Stream containing the Sig File
	 * @param sigName the name of the sig file
	 * @param datStream the Stream containg 64K of test data
	 * @param datName the test data file name
	 * @return tests the supplied signature against the supplied data file
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	public ApplicationView testSignature(@FormDataParam("sigFile") InputStream sigStream, @FormDataParam("sigName") String sigName, @FormDataParam("datFile") InputStream datStream, @FormDataParam("datName") String datName) {
		IdentificationResult result = null;
		try {
			TikaSigTester tika = TikaSigTester.streamsOnly(sigStream);
			result = tika.identify(datStream);
			
		} catch (MimeTypeException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		} catch (IOException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
		return ApplicationView.getNewInstance("result.ftl", result);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("about")
	public ApplicationView aboutHtml() {
		return ApplicationView.getNewInstance("about.ftl");
	}
	
}
