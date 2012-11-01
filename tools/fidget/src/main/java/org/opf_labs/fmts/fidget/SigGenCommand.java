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
package org.opf_labs.fmts.fidget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.tika.mime.MimeTypeException;
import org.opf_labs.fmts.fidget.droid.PRONOMSigGenerator;
import org.opf_labs.fmts.fidget.droid.SigDefSubmission;
import org.opf_labs.fmts.mimeinfo.MimeInfo;
import org.opf_labs.fmts.mimeinfo.MimeInfoUtils;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class SigGenCommand {

	/**
	 * @param args
	 * @throws MimeTypeException
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static void main( String[] args ) throws MimeTypeException, IOException {
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options options = new Options();
		// Main option, setting the signature file
		options.addOption( 
				OptionBuilder.withLongOpt( "sig-file" )
				.withDescription( "use this mime-info signature file" )
				.hasArg()
				.withArgName("FILE")
				.create("s") );
		// 
		options.addOption( "A", "alone", false, "use only the supplied signature file, do not load the embedded ones" );
		options.addOption( "C", "convert-to-droid", false, "convert supplied signature file into DROID form" );
		options.addOption( "l", "list", false, "list all known types.");
		options.addOption( "?", "help", false, "print help message");

		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );

			// validate that sig-file has been set
			String sigfile = null;
			if( line.hasOption( "sig-file" ) ) {
				// print the value of sig-file
				sigfile = line.getOptionValue( "sig-file" );
			}
			
			// Check mode:
			if( line.hasOption("?") ) {
				printHelp(options);
				
			} else if( line.hasOption("C") ) {
				// Convert mode:
				if( sigfile == null ) {
					System.err.println("No signature file argument found!");
					return;
				}
				System.out.println("Generate DROID signature...");
				MimeInfo mi = null;
				FileInputStream sigStr = new FileInputStream(sigfile);
				try {
					mi = MimeInfoUtils.parser( sigStr );
				} catch (JAXBException e) {
					e.printStackTrace();
					return;
				} finally {
					sigStr.close();
				}
				SigDefSubmission sigdef = MimeInfoUtils.toDroidSigDef(mi);
				// TODO Make is possible to print out the signature submission definition?
				// This just creates a submission template, but we could output a PRONOM record too.
				PRONOMSigGenerator.generatePRONOMSigFile(sigdef);
			} else if( line.hasOption("l") ) {
				// Set up Tika:
				TikaSigTester tst = SigGenCommand.tikaStarter(sigfile, line.hasOption("A"));
				tst.printTypes();
				
			} else {
				// We are in file identification mode:
				if( line.getArgList().size() == 0 ) {
					System.err.println("No identification test file found!");
					return;
				}
				// Set up Tika:
				TikaSigTester tst = SigGenCommand.tikaStarter(sigfile, line.hasOption("A"));
				// Return result:
				FileInputStream inStr = new FileInputStream(""+line.getArgList().get(0));
				System.out.println(""+tst.identify(inStr));
				inStr.close();
				return;
			}
		}
		catch( ParseException exp ) {
			System.out.println( "Unexpected exception:" + exp.getMessage() +"\n" );
			printHelp(options);
		}
	}
	
	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "fidget [OPTION]... [FILE]...", options );		
	}
	
	private static TikaSigTester tikaStarter( String sigfile, boolean sigfileAlone ) throws MimeTypeException, IOException {
		TikaSigTester tst = new TikaSigTester();
		if( sigfile != null ) {
			tst = new TikaSigTester( new File(sigfile), !sigfileAlone );					
		}
		return tst;
	}
}
