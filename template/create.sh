#!/bin/bash

dayNum=$(date +'%-d')
dayNumPadded=$(date +'%0d')

packageName="day$dayNumPadded"
filePathName="src/$packageName/Day$dayNumPadded"
mainFile="$filePathName.kt"

# Make package
mkdir "src/$packageName"

# Copy template file and create input files
cp "template/Day.tmp" $mainFile
touch "$filePathName.txt"
touch "${filePathName}_test.txt"

# Replace placeholders in template
sed -i '' "s/%PACKAGE/$packageName/g" $mainFile
sed -i '' "s/%DAY/$dayNum/g" $mainFile