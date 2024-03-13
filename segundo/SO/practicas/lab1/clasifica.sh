#!/bin/bash

INPUT_DIR=$1
OUTPUT_DIR=$2

if [[ ! -d $OUTPUT_DIR ]] then
  mkdir $OUTPUT_DIR
fi

FILES=`ls $INPUT_DIR`
for FILE in $FILES
do
  FOLDER_NAME=`cat $INPUT_DIR/$FILE | grep autor | cut -c 7-`
  FILE_NAME=`cat $INPUT_DIR/$FILE | grep titulo | cut -c 8-`

  if [[ ! -d $OUTPUT_DIR/$FOLDER_NAME ]] then
    mkdir $OUTPUT_DIR/$FOLDER_NAME
  fi

  cp $INPUT_DIR/$FILE $OUTPUT_DIR/$FOLDER_NAME/$FILE_NAME.mp3
done



