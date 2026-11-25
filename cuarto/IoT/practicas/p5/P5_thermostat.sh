#!/bin/sh
#
# P5 - A thermostat for one room
# P5_thermostat.sh - opens and closes the valve of room 07
#
# Reads every temperature published by the sensor and decides whether the
# heating valve should be open or closed. LOW and HIGH are the two limits.
# The distance between them stops the valve opening and closing on every
# reading, which is what would happen if they were equal.

HOST=localhost
TEMP=building/floor2/temp/07
VALVE=building/floor2/valve/03
LOW=205                       # 20.5 degrees, below this the valve opens
HIGH=215                      # 21.5 degrees, above this the valve closes

state=closed
echo "thermostat running, the valve is $state"

mosquitto_sub -h "$HOST" -t "$TEMP" | while read t
do
    echo "read $t, the valve is $state"

    if [ "$state" = closed ] && [ "$t" -lt "$LOW" ]
    then
        mosquitto_pub -h "$HOST" -t "$VALVE" -m open
        state=open
        echo "   the valve has been opened"
    fi

    if [ "$state" = open ] && [ "$t" -gt "$HIGH" ]
    then
        mosquitto_pub -h "$HOST" -t "$VALVE" -m close
        state=closed
        echo "   the valve has been closed"
    fi
done
