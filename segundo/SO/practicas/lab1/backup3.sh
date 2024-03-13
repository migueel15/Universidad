#!/bin/bash
BACKUP_PATH="./BACKUP"
CURRENT_DATE=$(date "+%y%m%d")

if [[ ! -d $BACKUP_PATH ]]; then
  mkdir $BACKUP_PATH
fi

if [[ ! -d $BACKUP_PATH/$CURRENT_DATE ]]; then
  mkdir $BACKUP_PATH/$CURRENT_DATE
fi

for current in $@
do
      cp $current $BACKUP_PATH/$CURRENT_DATE/
done
