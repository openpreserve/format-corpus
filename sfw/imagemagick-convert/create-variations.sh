#!/bin/sh

# Create an raster version of the text file:
cat ../../fmt/text/plain/lorem-ipsum.txt | convert -size 600x caption:@- ../../fmt/image/png/lorem-ipsum.im.png

# Also create a lossy version, two ways
cat ../../fmt/text/plain/lorem-ipsum.txt | convert -size 600x caption:@- ../../fmt/image/jpeg/lorem-ipsum.im.jpg
convert ../../fmt/image/png/lorem-ipsum.im.png ../../fmt/image/jpeg/lorem-ipsum.im.png.im.jpg

convert -version

