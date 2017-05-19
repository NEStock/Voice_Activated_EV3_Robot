@REM --------------------------------------------------------------------------
@REM Dorset Calculator Demo Batch Script
@REM 
@REM Required ENV vars:
@REM JAVA_HOME - location of a JRE home dir
@REM
@REM --------------------------------------------------------------------------

@echo off

set ERROR_CODE=0

REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto java_check

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:java_check
if exist "%JAVA_HOME%\bin\java.exe" goto run

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

REM ==== SETUP COMMAND LINE ====
:run
set DORSET_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

for %%i in (target\calculator-*.jar) do set DORSET_JAR="%%i"

%DORSET_JAVA_EXE% -jar %DORSET_JAR%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
cmd /C exit /B %ERROR_CODE%
