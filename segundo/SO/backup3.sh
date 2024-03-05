#!/bin/bash
BACKUP_PATH="./BACKUP"
CURRENT_DATE=$(date "+%y%m%d")

if [[ ! -d $BACKUP_PATH ]]; then
  mkdir $BACKUP_PATH
fi

for current in $@
do
  if [[ ! -d $BACKUP_PATH/$CURRENT_DATE ]]; then
    mkdir $BACKUP_PATH/$CURRENT_DATE
  # else
  #   A=$(ls $current* | wc -w)
  #   if [[ $A -ge 9 ]]; then
  #     echo "Se ha superado el número máximo de versiones"
  #   else
  #     date_str=$(date "+%y%m%d")
  #     Version="${date_str}_$current"
  #     cp $current $Version
  #   fi
  fi
done


