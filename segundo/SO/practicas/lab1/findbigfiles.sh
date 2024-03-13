#!/bin/bash

DIR_PATH=$1
MIN_SIZE=$2

for FILE in `find $DIR_PATH`
do
  FILE_SIZE=`stat $FILE | grep Tamaño | awk '{print $2}'`

  if [[ -f $FILE ]] then
    if [[ $FILE_SIZE -ge $MIN_SIZE ]] then
      echo $FILE: $FILE_SIZE bytes
    fi
  fi
done
