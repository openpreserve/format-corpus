/**
 * Copyright (C) 2012 Andrew Jackson <Andrew.Jackson@bl.uk>
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
package org.opf_labs.fmts.fidget.droid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * This class can be used to generate a DROID signature file using the TNA's web
 * service.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 * 
 */
public class PRONOMSigGenerator {
	/** String form of the URL of the PRONOM Sig file generation service. */
	public static final String SERVICE_URL = "http://test.linkeddatapronom.nationalarchives.gov.uk/sigdev/php/process_signature_form.php";

	/**
	 * @param sigdef
	 */
	public static void generatePRONOMSigFile(SigDefSubmission sigdef) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {

			HttpPost httpost = new HttpPost(SERVICE_URL);

			httpost.setEntity(new UrlEncodedFormEntity(
					createNameValuePairs(sigdef), Consts.UTF_8));

			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();

			// Print out:
			IOUtils.copy(entity.getContent(), System.out);
			// Finish up:
			EntityUtils.consume(entity);

		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}

	private static List<NameValuePair> createNameValuePairs(
			SigDefSubmission sigDef) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("name1", sigDef.getName()));
		nvps.add(new BasicNameValuePair("version1", sigDef.getVersion()));
		nvps.add(new BasicNameValuePair("puid1", sigDef.getPuid()));
		nvps.add(new BasicNameValuePair("extension1", sigDef.getExtension()));
		nvps.add(new BasicNameValuePair("mimetype1", sigDef.getMimetype()));
		// Counter of sigs
		nvps.add(new BasicNameValuePair("counter", ""
				+ sigDef.getSignatures().size()));
		System.out.println("Got: " + sigDef.getSignatures().size());
		// For each sig:
		int sigNo = 1;
		for (InternalSig intSig : sigDef.getSignatures()) {
			nvps.add(new BasicNameValuePair("signature" + sigNo,
					intSig.getSignature()));
			System.out.println("Sig: " + intSig.getSignature());
			nvps.add(new BasicNameValuePair("anchor" + sigNo, ""
					+ intSig.getAnchor())); // BOFoffset, EOFoffset, Variable
			nvps.add(new BasicNameValuePair("offset" + sigNo, ""
					+ intSig.getOffset()));
			nvps.add(new BasicNameValuePair("offset" + sigNo, ""
					+ intSig.getMaxOffset()));
			sigNo++;
		}
		return nvps;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		SigDefSubmission sigDef = SigDefSubmission.empty()
				.addSig(InternalSig.canonicalInst()).build();
		PRONOMSigGenerator.generatePRONOMSigFile(sigDef);
	}

}
