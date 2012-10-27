/**
 * 
 */
package org.opf_labs.fmts;

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
import org.apache.tika.mime.TikaMimeInfo;
import org.opf_labs.fmts.droid.PRONOMSigGenerator;
import org.opf_labs.fmts.droid.SigDefSubmission;
import org.opf_labs.fmts.mimeinfo.MimeInfo;
import org.opf_labs.fmts.mimeinfo.MimeInfoUtils;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class SigGenCommand {

	public static void main( String[] args ) throws MimeTypeException, IOException {
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options options = new Options();
		// Main option, setting the signature file
		options.addOption( OptionBuilder.withLongOpt( "sig-file" )
				.withDescription( "use this mime-info signature file" )
				.hasArg()
				.withArgName("FILE")
				.create("s") );
		// 
		options.addOption( "A", "alone", false, "use only the supplied signature file, do not load the embedded ones" );
		options.addOption( "C", "convert-to-droid", false, "convert supplied signature file into DROID form" );
		options.addOption( "?", "help", false, "print help message");

		HelpFormatter formatter = new HelpFormatter();
		
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
				// HELP mode:
				formatter.printHelp( "siggen [OPTION]... [FILE]...", options );
				
			} else if( line.hasOption("C") ) {
				// Convert mode:
				if( sigfile == null ) {
					System.err.println("No signature file argument found!");
					return;
				}
				System.out.println("Generate DROID signature...");
				MimeInfo mi = null;
				try {
					mi = MimeInfoUtils.parser( new FileInputStream(sigfile) );
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SigDefSubmission sigdef = MimeInfoUtils.toDroidSigDef(mi);
				//TikaMimeInfo.fromTikaMimeType(null);
				PRONOMSigGenerator.generatePRONOMSigFile(sigdef);
			} else {
				// We are in file identification mode:
				if( line.getArgList().size() == 0 ) {
					System.err.println("No identification test file found!");
					return;
				}
				// Otherwise, set up the right Tika:
				TikaSigTester tst = new TikaSigTester();
				if( sigfile != null ) {
					tst = new TikaSigTester( new File(sigfile), !line.hasOption("A"));					
				}
				// Return result:
				System.out.println(""+tst.identify( new FileInputStream(""+line.getArgList().get(0))));
				return;
			}
		}
		catch( ParseException exp ) {
			System.out.println( "Unexpected exception:" + exp.getMessage() );
		}
	}
}
