# The Archivist's PDF Cabinet of Horrors 


## About these files

Test *PDF* files, created for detecting *PDF* features that are undesired in archival settings. Most of these files were originally created in Microsoft Word 2003, and then converted to *PDF* with Adobe Acrobat Professional 9.5.2. The source Word files are included here as well, but in many cases the *PDFs* required further processing in Acrobat (e.g. embedding videos, attaching files, encryption) so they're probably not that useful.

Exceptions are:

*javascript.pdf*, which was created with Didier Stevens' *make-pdf* tool, which is available here: [http://blog.didierstevens.com/programs/pdf-tools/](http://blog.didierstevens.com/programs/pdf-tools/)

*digitally_signed_3D_Portfolio.pdf*, which was kindly provided by Adobe.

## Description

Here's a more detailed description of the files (arranged by feature class(es)):

### Encryption

+ [**encryption_openpassword.pdf**](./encryption_openpassword.pdf?raw=true) - requires password to open the file
+ [**encryption_nocopy.pdf**](./encryption_nocopy.pdf?raw=true) - requires password to copy document contents
+ [**encryption_noprinting.pdf**](./encryption_noprinting.pdf?raw=true) - requires password for printing
+ [**encryption_notextaccess.pdf**](./encryption_notextaccess.pdf?raw=true) - requires password to enable text access for screen reader devices for the visually impaired

### Multimedia

+ [**embedded\_video\_avi.pdf**](./embedded_video_avi.pdf?raw=true) - contains embedded *AVI* movie 
+ [**embedded\_video\_quicktime.pdf**](./embedded_video_quicktime.pdf?raw=true) - contains embedded *Quicktime* movie

### Scripts

+ [**javascript.pdf**](./javascript.pdf?raw=true) - contains embedded *Javascript*

### Fonts

+ [**text\_only\_fontsNotEmbedded.pdf**](./text_only_fontsNotEmbedded.pdf?raw=true) - used fonts are not embedded
+ [**text\_only\_fontsEmbeddedAll.pdf**](./text_only_fontsEmbeddedAll.pdf?raw=true) - used fonts are embedded
+ [**text\_only\_fontsEmbeddedSubset.pdf**](./text_only_fontsEmbeddedSubset.pdf?raw=true) - used fonts are embedded as subset
+ [**text\_only\_pdfa1b.pdf**](./text_only_pdfa1b.pdf?raw=true) - *PDF/A-1a* (with embedded fonts)
+ [**test\_fontArialNotEmbedded.pdf**](./test_fontArialNotEmbedded.pdf?raw=true) - <del>font *Arial*</del> fonts *Arial* and *Times New Roman*  <del>is</del> are not embedded
+ [**calistoMTNoFontsEmbedded.pdf**](./calistoMTNoFontsEmbedded.pdf?raw=true) - Font *Calisto MT* is not embedded

### Images

+ [**veraPDFHiRes.pdf**](./veraPDFHiRes.pdf?raw=true) - Intact and valid PDF/A-1b file with bitmap image
+ [**veraPDFHiResChangedHeight.pdf**](./veraPDFHiResChangedHeight.pdf?raw=true) - As above, but wrong value of *Height*  entry in Image XObject 
+ [**veraPDFHiResWrongObjectID.pdf**](./veraPDFHiResWrongObjectID.pdf?raw=true) - As *veraPDFHiRes.pdf*, but with reference to wrong (non-existing) XObject
+ [**balloon_a1b_jp2k.pdf**](./balloon_a1b_jp2k.pdf?raw=true) - file claims PDF/A-1b conformance, but contains JPEG 2000 image, which is not allowed in PDF/A-1 (file also has some other violations of PDF/A-1).
 
### File attachments

+ [**fileAttachment.pdf**](./fileAttachment.pdf?raw=true) - contains a document-level file attachment (an oldskool Quattro Pro spreadsheet, no less!) that is defined using an *EmbeddedFiles* entry in the document’s name dictionary
+ [**fileAttachment_fileAttachmentAnnotation.pdf**](./fileAttachment_fileAttachmentAnnotation.pdf?raw=true) - contains a page-level file attachment that is defined using a *File Attachment Annotation*

### External references

+ [**externalLink.pdf**](./externalLink.pdf?raw=true) - contains link to another document
+ [**webCapture.pdf**](./webCapture.pdf?raw=true) - uses  Web Capture feature for importing text from a website

### Byte corruption

+ [**corruptionOneByteMissing.pdf**](./corruptionOneByteMissing.pdf?raw=true) - one byte missing from comment line following file header
 
### Digitally Signed 3D Portfolio

+ [**digitally_signed_3D_Portfolio.pdf**](./digitally_signed_3D_Portfolio.pdf?raw=true) - a PDF 1.7 portfolio with multiple sheets, forms and 3D images; one of the sheets is digitally signed

### Miscellaneous

+ [**pdf-17-header18.pdf**](./pdf-17-header18.pdf?raw=true) - PDF 1.7, but header string is `%PDF-1.8` (which causes a false negative with some identification tools, see e.g. [here](https://github.com/digital-preservation/droid/issues/114)). 

## License
All files in this folder: Creative Commons CC0: Public Domain Dedication. See [http://creativecommons.org/publicdomain/zero/1.0/](http://creativecommons.org/publicdomain/zero/1.0/)

