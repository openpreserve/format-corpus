### old-style-jpeg-compression.tif

This TIFF uses 'old-style' JPEG compression, which was overridden by 'new-style' JPEG compression in 1995. Some contemporary software cannot open these files. 

The original implementation of JPEG-compressed data in TIFF had some serious problems. These problems are discussed in detail in [TIFF Technical Note #2 (1995)](http://libtiff.maptools.org/TIFFTechNote2.htm). This Technical Note also defines a replacement method ('new-style').  

TIFFs that use 'old-style' JPEG compression can be identified from the value '6' (0x0006) of the 'Compression' tag (259/0x0103).

ExifTool XML-report included.

See also:

* [DRAFT TIFF Technical Note #2, 17-Mar-95](http://libtiff.maptools.org/TIFFTechNote2.html)
* [TIFF Compression types (Wikipedia, scroll down to table)](https://en.wikipedia.org/wiki/TIFF#Compression)
* [Tags for TIFF, DNG, and Related Specifications](http://www.digitalpreservation.gov/formats/content/tiff_tags.shtml)


