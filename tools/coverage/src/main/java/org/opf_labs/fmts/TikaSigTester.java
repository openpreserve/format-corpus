package org.opf_labs.fmts;

import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.mime.MimeTypesFactory;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TikaSigTester{

	private MimeTypes mimeTypes;

    public TikaSigTester() throws MimeTypeException, IOException {
    	mimeTypes = TikaSigTester.create(
                "tika-mimetypes.xml", "custom-mimetypes.xml");
    }
    
    public TikaSigTester(File sigs, boolean useAll ) throws MimeTypeException, IOException {
    	List<URL> urls = new ArrayList<URL>();
    	// Load up core and extension mime type definitions, if desired.
    	if( useAll == true ) {
    		urls = TikaSigTester.lookupSigResources( "tika-mimetypes.xml", "custom-mimetypes.xml");
    	}
    	// Now load up the additional definition:
    	urls.add( sigs.toURI().toURL() );
    	mimeTypes = TikaSigTester.create(urls);
    }
    
    public MimeTypes getMimeTypes() {
    	return this.mimeTypes;
    }
    
	public MediaType identify(InputStream input) {
        Metadata metadata = new Metadata();
        MediaType mediaType;
		try {
	        TikaInputStream stream = TikaInputStream.get(input);
			mediaType = mimeTypes.detect(stream, metadata);
	        stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        return mediaType;
	}
	
	public static List<URL> lookupSigResources(String coreFilePath, String extensionFilePath) throws IOException {
        // This allows us to replicate class.getResource() when using
        //  the classloader directly
        String classPrefix = MimeTypes.class.getPackage().getName().replace('.', '/') + "/";
        ClassLoader cl = MimeTypes.class.getClassLoader();
       
        // Get the core URL, and all the extensions URLs
        URL coreURL = cl.getResource(classPrefix+coreFilePath);
        List<URL> extensionURLs = Collections.list(
              cl.getResources(classPrefix+extensionFilePath));

        // Swap that into an Array, and process
        List<URL> urls = new ArrayList<URL>();
        urls.add(coreURL);
        urls.addAll(extensionURLs);
        return urls;
	}

    public static MimeTypes create(String coreFilePath, String extensionFilePath)
            throws IOException, MimeTypeException {
    	List<URL> urls = lookupSigResources( coreFilePath, extensionFilePath );
        return MimeTypesFactory.create( urls.toArray(new URL[urls.size()]) );
    }

    public static MimeTypes create( List<URL> urls)
            throws IOException, MimeTypeException {
        return MimeTypesFactory.create( urls.toArray(new URL[urls.size()]) );
    }

    public Identity detect(File input) throws IOException {
        long before = System.currentTimeMillis();
        MediaType mime = this.identify( TikaInputStream.get(input) );
        long duration = System.currentTimeMillis() - before;
        return new Identity(input,mime,duration);

    }


    public  List<Identity> identify(String govDocsData) throws Exception {

        List<File> datafiles = getFiles(new File(govDocsData));
        return identify(datafiles);

    }


    public List<Identity> identify(List<File> datafiles) throws Exception {

        List<Identity> identities = new ArrayList<Identity>();

        TikaSigTester sw = new TikaSigTester();

        for (File file : datafiles) {
            if (!file.isFile()){
                continue;
            }
            Identity detection = sw.detect(file);
            identities.add(detection);
            System.out.println(file.getAbsolutePath()+":"+detection.getMime());
        }
        return identities;


    }


    private static List<File> getFiles(File dir){
        List<File> result = new ArrayList<File>();
        if (!dir.isDirectory()){
            result.add(dir);
            return result;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            result.addAll(getFiles(file));
        }
        Collections.sort(result);
        return result;
    }

    public class Identity {

        private MediaType mime;

        private long time;
        private File file;

        public Identity(File file, MediaType mime, long time) {
            this.file = file;
            this.mime = mime;
            this.time = time;
        }

        public MediaType getMime() {
            return mime;
        }

        public long getTime() {
            return time;
        }

        public File getFile() {
            return file;
        }
    }

    public static void main(String... args) throws Exception {
        String govDocsData = null;
        if (args.length == 1) {
            govDocsData = args[0];
        }
        TikaSigTester sw = new TikaSigTester();
        sw.identify(govDocsData);

    }

    /**
     * Print out a summary of all known types:
     * @throws MimeTypeException 
     */
	public void printTypes() throws MimeTypeException {
		for( MediaType md : this.getMimeTypes().getMediaTypeRegistry().getTypes() ) {
			MimeType mt = this.getMimeTypes().forName(md.toString());
			System.out.println(""+mt.getType()+"\t"+mt.getExtensions()+"\t"+mt.getName());
		}		
	}


}