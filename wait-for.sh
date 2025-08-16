#!/bin/sh
# wait-for.sh: wait for host:port to be available

HOST=$1
PORT=$2

echo "Waiting for $HOST:$PORT ..."

while ! nc -z $HOST $PORT; do
  sleep 1
done

echo "$HOST:$PORT is available"
shift 2
exec "$@"
