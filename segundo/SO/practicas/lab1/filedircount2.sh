#!/bin/bash

if [[ -d $1 ]]; then
  for i in $1/*
  do
    if [[ -d $i ]]; then
      FILES=`ls -l "$i" | wc -l`
      FILES2=`expr $FILES - 1`

      if [[ $FILES -eq 1 ]]; then
        FILES2=0
      fi
      echo "$i:$FILES2"

    fi
  done
else
  echo "No es un directorio"
fi
