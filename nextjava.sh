#!/usr/bin/env bash

APP_DIR=/home/joshua/app
CONF_DIR=$APP_DIR/conf
LOG_DIR=$APP_DIR/logs
VERSION="0.0.1"

cd "$APP_DIR" || exit
nohup java -Dtwitter.credentials.file.path=$CONF_DIR/twitter_credentials.json -ea --enable-preview -jar app-all-${VERSION}.jar "$@" >> $LOG_DIR/app-all.log 2>&1 &
