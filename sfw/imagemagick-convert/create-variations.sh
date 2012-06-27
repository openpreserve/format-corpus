#!/bin/sh

# Create an raster version of the text file:
cat ../lorem-ipsum.txt | convert -size 600x caption:@- lorem-ipsum.im.png

# Also create a lossy version
convert lorem-ipsum.im.png lorem-ipsum.im.png.im.jpg

