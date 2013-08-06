---
* formatName: TIFF (Exchangeable Image File Format (Uncompressed), EXIF Uncompressed Image 2.2 (little-endian, EXIF near BOF))
* formatVersion: TIFF 6.0
* extensions: tif, tiff
* mimeType: image/tiff
* mimeTypeAliases: 
* pronomId: x-fmt/387
* xmlNameSpace: 
* creatorTool: Adobe Photoshop CS3 Macintosh
* creatorToolUrl: http://www.adobe.com/support/downloads/detail.jsp?ftpID=3779
* formatSpecUrl: http://partners.adobe.com/public/developer/tiff/

---

###grayscale_8bpp_wrong_bpptag.tif

Invalid 8 bits per sample grayscale baseline TIFF, defined as 16 bits per sample, 
showing malformation when rendered.
Should not validate since it contains several invalid tags, such as "PlanarConfiguration".

ExifTool XML-report included.

Issue:
http://wiki.opf-labs.org/display/SPR/Valid+and+well-formed+TIFF's+with+scanline+corruption
Solution:
http://wiki.opf-labs.org/display/SPR/Solving+TIFF+malformation+using+exiftool

Image courtesy of Dutch digitisation project "Images of the Future":
http://www.beeldenvoordetoekomst.nl/en/
License: CC BY-NC-SA 3.0
http://creativecommons.org/licenses/by-nc-sa/3.0/

