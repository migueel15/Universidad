#!/bin/bash

for i in $@
do
  if [[ ! -f $i ]]; then
    echo "$i no existe"
  else
    A=$(ls $i* | wc -w)
    if [[ $A -ge 9 ]]; then
      echo "Se ha superado el número máximo de versiones"
    else
      date_str=$(date "+%y%m%d")
      Version="${date_str}_$i"
      cp $i $Version
    fi
  fi
done


