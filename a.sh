#!/bin/bash
keywordsfile="/Users/jblau/projects/grid/keywords"
webkeywordsfile="/Users/jblau/projects/grid/webkeywords"
keywords=`cat $keywordsfile`
webkeywords=`cat $webkeywordsfile`

echo "Repo,Keyword,Count"
for dir in */; do
	for keyword in $keywords; do
		output=$(eval "grep -inrl $keyword $dir/src/* | wc -l | xargs") 
		if [ $output -ne 0 ]
		then
			echo "$dir,$keyword,$output"
		fi
	done
	for webkeyword in $webkeywords; do
		weboutput=$(eval "grep -inrl $webkeyword $dir/src/* | wc -l | xargs") 
		if [ $weboutput -ne 0 ]
		then
			echo "$dir,$webkeyword,$weboutput"
		fi
	done
done

