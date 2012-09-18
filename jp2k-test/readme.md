## Contents
This folder contains a small test corpus of *JPEG 2000* (Part 1 and Part 2) images. 

All images were compressed lossily at a ratio of about 45:1. Encoding options (tile size, progression order, etc.) are not uniform across all samples, and in most cases the application defaults were used (except for the *Aware*-created images). The images are subdivided into 3 groups:

+ *Resolution* - shows handling of resolution fields by different encoders
+ *icc* -  shows handling of ICC profiles by different encoders (as well as some *Photoshop*-specific weirdness)
+ *byteCorruption* - shows effects of removal/modification of bytes after image creation (these files were deliberately butchered - no encoder faults to blame!)  

[*Jpylyzer*][jpylyzer] output files are included for each image (note that these are best viewed in an *XML* editor as they are not pretty-printed). 
 
The table below gives a description of the dataset:


<table border=1 cellspacing=0 cellpadding=10 width=841
>

<td width=841  colspan=4 valign=bottom >
<br/><b>Part 1: Resolution</b>
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/><i>File</i>
</td>
<td width=73  valign=bottom >
<br/><i>Format</i>
</td>
<td width=270  valign=bottom >
<br/><i>Creator
application</i>
</td>
<td width=255  valign=bottom >
<br/><i>Remarks</i>
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon.tif
</td>
<td width=73  valign=bottom >
<br/>TIFF
</td>
<td width=270  valign=bottom >
<br/>IrfanView, resolution fields added using ExifTool 
</td>
<td width=255  valign=bottom >
<br/>Source
image
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_aware.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>Aware
JPEG 2000 SDK 3.19.2.2
</td>
<td width=255  valign=bottom >
<br/>Resolution
in "Capture Resolution" box
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_oj.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>OpenJPEG 1.5, image_to_j2k tool
</td>
<td width=255  valign=bottom >
<br/>No
resolution box
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_gm.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>GraphicsMagick 1.3.16, convert tool (uses JasPer 1.9.0)
</td>
<td width=255  valign=bottom >
<br/>No
resolution box
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_kdu61.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>Kakadu 6.1, kdu_compress tool
</td>
<td width=255  valign=bottom >
<br/>Resolution
in "Display Resolution" box
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_kdu71.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>Kakadu 7.1, kdu_compress tool
</td>
<td width=255  valign=bottom >
<br/>Resolution
in "Capture Resolution" box
</td>
</tr>
<tr >
<td width=841  colspan=4 valign=bottom >
<br/><b>Part 2: ICC profiles + Photoshop weirdness</b>
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/><i>File</i>
</td>
<td width=73  valign=bottom >
<br/><i>Format</i>
</td>
<td width=270  valign=bottom >
<br/><i>Creator
application</i>
</td>
<td width=255  valign=bottom >
<br/><i>Remarks</i>
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_eciRGBv2.tif
</td>
<td width=73  valign=bottom >
<br/>TIFF
</td>
<td width=270  valign=bottom >
<br/>Adobe
Photoshop CS4
</td>
<td width=255  valign=bottom >
<br/>Source
image, contains embedded ICC profile
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_eciRGBv2_aware.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>Aware
JPEG 2000 SDK 3.19.2.2
</td>
<td width=255  valign=bottom >
<br/>Contains
ICC profile, embedded using "Restricted ICC" method
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_eciRGBv2_ps_
<br/>adobeplugin.jpf
</td>
<td width=73  valign=bottom >
<br/>JPX
</td>
<td width=270  valign=bottom >
<br/>Adobe
Photoshop CS4 + Adobe JPEG2000 plugin 2.0
</td>
<td width=255  valign=bottom >
<br/>Wrong
'brand' field; ICC profile embedded using "Any ICC" method
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_eciRGBv2_ps_
<br/>adobeplugin_jp2compatible.jpf
</td>
<td width=73  valign=bottom >
<br/>JPX
</td>
<td width=270  valign=bottom >
<br/>Adobe
Photoshop CS4 + Adobe JPEG2000 plugin 2.0
</td>
<td width=255  valign=bottom >
<br/>Wrong 'brand'
field; contains two versions of ICC profile: one embedded using "Any
ICC" method; other embedded using "Restricted ICC" method,
with description ("Modified eciRGB v2")
and profileClass ("Input Device") changed
relative to original profile.
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_eciRGBv2_ps_
<br/>kduplugin.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>Adobe
Photoshop CS4 + j2k plugin 2.01 (based on Kakadu)
</td>
<td width=255  valign=bottom >
<br/>Contains
ICC profile, embedded using "Restricted ICC" method; profileClass changed relative to original profile
("Input Device")
</td>
</tr>
<tr >
<td width=841  colspan=4 valign=bottom >
<br/><b>Part 3: Byte corruption</b>
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/><i>File</i>
</td>
<td width=73  valign=bottom >
<br/><i>Format</i>
</td>
<td width=270  valign=bottom >
<br/><i>Creator
application</i>
</td>
<td width=255  valign=bottom >
<br/><i>Remarks</i>
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon.jpg
</td>
<td width=73  valign=bottom >
<br/>JPEG
</td>
<td width=270  valign=bottom >
<br/>
</td>
<td width=255  valign=bottom >
<br/>Source image
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_intact.jp2
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>
</td>
<td width=255  valign=bottom >
<br/>Intact image (reference)
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_trunc1.jp2*
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>
</td>
<td width=255  valign=bottom >
<br/>Last byte
missing*
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_trunc2.jp2*
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>
</td>
<td width=255  valign=bottom >
<br/>Truncated
at byte 5000*
</td>
</tr>
<tr >
<td width=243  valign=bottom >
<br/>balloon_trunc3.jp2*
</td>
<td width=73  valign=bottom >
<br/>JP2
</td>
<td width=270  valign=bottom >
<br/>
</td>
<td width=255  valign=bottom >
<br/>Missing
data in most of last tile-part*
</td>
</tr>

<tr >
<td width=841  colspan=4 valign=bottom >
<br/>* Image
was manually edited (bytes were removed) after creation to demonstrate
effects of truncation -image faults are purely artificial and NOT related to
the creator application!!!!!
</td>
</tr>
</table>


##Image attribution and provenance

All images are derived from the following source image: 

[http://commons.wikimedia.org/wiki/File:1783_balloonj.jpg](http://commons.wikimedia.org/wiki/File:1783_balloonj.jpg "http://upload.wikimedia.org/wikipedia/commons/a/a8/1783_balloonj.jpg")

> 1786 description of the historic Montgolfier Brothers' 1783 balloon flight. Illustration with engineering proportions and description.

Public Domain.

Resolution fields and ICC profile were added specifically to demonstrate how these features are handled by different encoders. The diagram below shows the approximate workflow:

![](https://raw.github.com/openplanets/format-corpus/master/jp2k-test/diagram.png)


[jpylyzer]: http://www.openplanetsfoundation.org/software/jpylyzer
[Aware]:http://www.aware.com/imaging/jpeg2000sdk.html
[Kakadu]:http://www.kakadusoftware.com/
[Luratech]:https://www.luratech.com/en/products/imaging-solutions/additional-imaging-solutions.html
[IrfanView]:http://www.irfanview.com/
[OpenJPEG]:http://www.openjpeg.org/
[GraphicMagick]:http://www.graphicsmagick.org/


[sampleJP2]:https://github.com/bitsgalore/jp2kMagic/tree/master/sampleImages/balloon.jp2
[sampleJPX]:https://github.com/bitsgalore/jp2kMagic/tree/master/sampleImages/balloon.jpf
[sampleJPM]:https://github.com/bitsgalore/jp2kMagic/tree/master/sampleImages/balloon.jpm
[sampleJ2C]:https://github.com/bitsgalore/jp2kMagic/tree/master/sampleImages/balloon.j2c
[sampleMJ2]:https://github.com/bitsgalore/jp2kMagic/tree/master/sampleImages/Speedway.mj2
[sampleImages]:https://github.com/bitsgalore/jp2kMagic/tree/master/sampleImages/
[magic]:https://github.com/bitsgalore/jp2kMagic/tree/master/magic/

