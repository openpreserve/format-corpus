# The Archivist's PDF Cabinet of Horrors 


## About these files
Test *PDF* files, created for detecting *PDF* features that are undesired in archival settings. Most of these files were originally created in Microsoft Word 2003, and then converted to *PDF* with Adobe Acrobat Professional 9.5.2. The source Word files are included here as well, but in many cases the *PDFs* required further processing in Acrobat (e.g. embedding videos, attaching files, encryption) so they're probably not that useful.

Exceptions are:

*javascript.pdf*, which was created with Didier Stevens' *make-pdf* tool, which is available here: [http://blog.didierstevens.com/programs/pdf-tools/](http://blog.didierstevens.com/programs/pdf-tools/)

*digitally_signed_3D_Portfolio.pdf*, which was kindly provided by Adobe.

## Description
Here's a more detailed description of the files (arranged by feature class(es)):

### Encryption

+ [**encryption_openpassword.pdf**](./encryption_openpassword.pdf?raw=true)) - requires password to open the file
+ **encryption_nocopy.pdf** - requires password to copy document contents
+ **encryption_noprinting.pdf** - requires password for printing
+ **encryption_notextaccess.pdf** - requires password to enable text access for screen reader devices for the visually impaired

### Multimedia

+ **embedded\_video\_avi.pdf** - contains embedded *AVI* movie 
+ **embedded\_video\_quicktime.pdf** - contains embedded *Quicktime* movie

### Scripts

+ **javascript.pdf** - contains embedded *Javascript*

### Fonts

+ **text\_only\_fontsNotEmbedded.pdf** - used fonts are not embedded
+ **text\_only\_fontsEmbeddedAll.pdf** - used fonts are embedded
+ **text\_only\_fontsEmbeddedSubset.pdf** - used fonts are embedded as subset
+ **text\_only\_pdfa1b.pdf** - *PDF/A-1a* (with embedded fonts)
+ **test\_fontArialNotEmbedded.pdf** - <del>font *Arial*</del> fonts *Arial* and *Times New Roman*  <del>is</del> are not embedded

### Images

+ **veraPDFHiRes.pdf** - Intact and valid PDF/A-1b file with bitmap image
+ **veraPDFHiResChangedHeight.pdf** - As above, but wrong value of *Height*  entry in Image XObject 
+ **veraPDFHiResWrongObjectID.pdf** - As *veraPDFHiRes.pdf*, but with reference to wrong (non-existing) XObject

### File attachments

+ **fileAttachment.pdf** - contains a document-level file attachment (an oldskool Quattro Pro spreadsheet, no less!) that is defined using an *EmbeddedFiles* entry in the document’s name dictionary
+ **fileAttachment_fileAttachmentAnnotation.pdf** - contains a page-level file attachment that is defined using a *File Attachment Annotation*

### External references

+ **externalLink.pdf** - contains link to another document
+ **webCapture.pdf** - uses  Web Capture feature for importing text from a website

### Byte corruption

+ **corruptionOneByteMissing.pdf** - one byte missing from comment line following file header
 
### Digitally Signed 3D Portfolio

+ **digitally_signed_3D_Portfolio.pdf** - a PDF 1.7 portfolio with multiple sheets, forms and 3D images; one of the sheets is digitally signed

## License
All files in this folder: Creative Commons CC0: Public Domain Dedication. See [http://creativecommons.org/publicdomain/zero/1.0/](http://creativecommons.org/publicdomain/zero/1.0/)

