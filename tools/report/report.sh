#!/bin/bash

# git clone git://github.com/openplanets/format-corpus.git
# pushd format-corpus
git reset --hard FETCH_HEAD

while read commit
do
	git checkout $commit 1> /dev/null 2> /dev/null
	echo $commit
	git show $commit | sed -n '3p'
	while read dir
	do
		NUM_FILES=0
		while read file
		do
			NUM_FILES=$((NUM_FILES+1))
			echo "$(basename "$file")" | sed -r 's@^.+\.([^.]+)$@\1@'
		done < <(find $dir -type f)
	done < <(ls -d */ 2> /dev/null | grep -v tools) | sort | uniq -c | sort -rn
done < <(git log | grep -E "^commit" | tac | awk '{ print $2 }')
