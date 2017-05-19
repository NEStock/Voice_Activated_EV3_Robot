#!/bin/bash

DIR=`dirname $0`
JAR=$(find $DIR/target/ -name 'calculator*.jar')
java -jar $JAR

