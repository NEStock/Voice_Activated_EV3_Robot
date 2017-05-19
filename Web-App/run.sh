#!/bin/bash

DIR=`dirname $0`
JAR=$(find $DIR/target/ -name 'web*.war')
java -jar $JAR "$@"

