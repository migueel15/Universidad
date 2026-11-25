#!/bin/bash
#
# P5 - A thermostat for one room
# P5_sensor.sh - the temperature sensor of room 07
#
# Publishes one temperature every PERIOD seconds on the topic below.
# Temperatures travel as whole numbers in tenths of a degree, so 214 is
# 21.4 degrees. The sequence falls from 23 degrees to 19 and climbs back.

HOST=localhost
TOPIC=building/floor2/temp/07
PERIOD=1                      # seconds between two readings
NOISE=8                       # the sensor is not perfect, tenths of a degree

printf "how many readings should I send? "
read COUNT

i=0
value=230
step=-2
while [ "$i" -lt "$COUNT" ]
do
    reading=$(( value - NOISE + (RANDOM % (2 * NOISE + 1)) ))
    if mosquitto_pub -h "$HOST" -t "$TOPIC" -m "$reading"
    then
        echo "sent $reading tenths of a degree"
    else
        echo "not sent $reading, no broker answered"
    fi

    value=$(( value + step ))
    if [ "$value" -le 190 ]; then step=2; fi
    if [ "$value" -ge 230 ]; then step=-2; fi

    i=$(( i + 1 ))
    sleep "$PERIOD"
done

echo "the sensor has finished after $COUNT readings"
