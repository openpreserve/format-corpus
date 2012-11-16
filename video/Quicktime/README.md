Apple Quicktime video codecs
============================

These videos are tests of the various, often proprietary, codecs
which ship with Quicktime X. Some of these are supported in open-
source software like ffmpeg, though others (like Apple Intermediate
Codec) are not.

Two formats were omitted, due to difficulties enticing Quicktime to
export the test pattern to them: Apple ProRes 4444, and Apple Pixlet
Video.

Methodology
===========

These videos were created from an ffmpeg test pattern. The command used
to create the original raw video was:

`ffmpeg -f lavfi -i testsrc -c:v rawvideo -c:a none -t 1`

The raw video was then encoded using Quicktime 7. Default settings were
typically used, and "Prepare for Internet Streaming" was disabled. If a
quality setting was available, the highest option was chosen.

All videos are 320x240 at 25fps for one second with no sound, unless the
codec imposed a particular resolution or framerate.

If the codec offered an option of interlaced vs progressive, one video
is provided for each.

License
=======

These were created using ffmpeg's builtin "testsrc" test pattern, which
is presumed to be unlicensed. ffmpeg's code is GPLv2 licensed.

Codec-specific notes
====================

apple-intermediate-codec.mov
----------------------------

A professional video editing codec, intended as an editing format rather
than a delivery format. Not supported by ffmpeg.

XDCAM
-----

The various XDCAM codecs are specific profiles of MPEG-2 used in
professional video. XDCAM HD is an anamorphic format, while
XDCAM EX is a full-resolution HD format. XDCAM HD422 is a
full-resolution format which uses 4:2:2 chroma subsampling instead of
4:2:0.
