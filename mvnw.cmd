@echo off
:: -----------------------------------------------------------------------------
:: Maven Wrapper
:: -----------------------------------------------------------------------------
:: Based on https://github.com/takari/maven-wrapper
:: Licensed under the Apache License, Version 2.0
:: -----------------------------------------------------------------------------

@setlocal

set MVNW_VERBOSE=false
if "%MVNW_VERBOSE%"=="true" (
  set MAVEN_CMD_ECHO=echo
  set MAVEN_CMD_OUTPUT=
) else (
  set MAVEN_CMD_ECHO=rem
  set MAVEN_CMD_OUTPUT=>nul
)

set WRAPPER_JAR=".mvn\wrapper\maven-wrapper.jar"

for %%i in (%WRAPPER_JAR%) do (
  set WRAPPER_JAR=%%~fsi
)

if not exist "%WRAPPER_JAR%" (
  echo Error: Could not find Maven wrapper's 'maven-wrapper.jar' in '.mvn\wrapper' directory
  exit /b 1
)

set JAVA_EXE=java
if defined JAVA_HOME (
  set JAVA_EXE="%JAVA_HOME%\bin\java"
)

set WRAPPER_LAUNCHER="%JAVA_EXE% %MAVEN_OPTS% -Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR% -Dmaven.home=%MAVEN_HOME% -Dmaven.wrapper.script=%0 -jar %WRAPPER_JAR%"
if "%MVNW_VERBOSE%"=="true" (
  echo %WRAPPER_LAUNCHER%
)
%WRAPPER_LAUNCHER% %* %MAVEN_CMD_OUTPUT%
@endlocal
