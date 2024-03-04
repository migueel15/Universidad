#!/bin/bash

if [[ -f $1 ]]; then
  if [[ -x $1 ]];
  then
    echo "es un archivo ejecutable"
  else
    echo "no es ejecutable"
  fi
else
  echo "no es ejecutable"
fi

