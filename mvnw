#!/usr/bin/env sh

# -----------------------------------------------------------------------------
# Maven Wrapper
# -----------------------------------------------------------------------------
# Based on https://github.com/takari/maven-wrapper
# Licensed under the Apache License, Version 2.0
# -----------------------------------------------------------------------------

if [ -z "$MAVEN_OPTS" ]; then
  MAVEN_OPTS=""
fi

if [ -z "$MVNW_VERBOSE" ]; then
  MVNW_VERBOSE=false
fi

MAVEN_PROJECTBASEDIR=$(pwd)

if [ -x "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" ]; then
  WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
  WRAPPER_LAUNCHER="java $MAVEN_OPTS -Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR -Dmaven.home=$MAVEN_HOME -Dmaven.wrapper.script=$0 -jar $WRAPPER_JAR"
  if [ "$MVNW_VERBOSE" = true ]; then
    echo "$WRAPPER_LAUNCHER"
  fi
  exec $WRAPPER_LAUNCHER "$@"
else
  echo "Error: Could not find Maven wrapper's 'maven-wrapper.jar' in '.mvn/wrapper' directory"
  exit 1
fi
