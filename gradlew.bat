@REM
@REM Copyright 2017 the original author or authors.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables here so they don't leak into the environment
setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Use Jdk 17 by default for gradle daemon in case no explicit configuration is provided
set GRADLE_VERSION_FOR_JVM_SELECTION=8.3

@rem Use project-specific version of Java if not configured
if "%JAVA_HOME%" == "" (
  if exist "%APPDATA%\..\Local\Programs\Eclipse Adoptium\jdk-%GRADLE_VERSION_FOR_JVM_SELECTION%.0.%GRADLE_VERSION_FOR_JVM_SELECTION%" (
    set JAVA_HOME=%APPDATA%\..\Local\Programs\Eclipse Adoptium\jdk-%GRADLE_VERSION_FOR_JVM_SELECTION%.0.%GRADLE_VERSION_FOR_JVM_SELECTION%
  )
)

@rem Check if the Java Runtime Environment is available on the path
if not defined JAVA_HOME goto find_java
if not exist "%JAVA_HOME%\bin\java.exe" goto find_java
goto start_gradle

:find_java
    @rem If no JAVA_HOME found, use the first java.exe found on the path
    for %%i in (java.exe) do (
        set JAVA_EXE_FOUND=%%~$PATH:i
        if not "%JAVA_EXE_FOUND%" == "" goto use_java_found_on_path
    )
    echo.
    echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
    echo.
    echo Please set the JAVA_HOME variable in your environment to match the location of your Java installation.
    echo.
    goto end

:use_java_found_on_path
    set JAVA_HOME=
    goto start_gradle

:start_gradle
    @rem Get command line arguments
    set CMD_LINE_ARGS=%*

    @rem Find the location of the gradle-wrapper.jar
    set DIR=%~dp0
    set WRAPPER_JAR=%DIR%gradle\wrapper\gradle-wrapper.jar

    @rem Check if gradle-wrapper.jar exists
    if not exist "%WRAPPER_JAR%" goto download_wrapper

    @rem Execute Gradle Wrapper
    "%JAVA_HOME%\bin\java" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

    goto end

:download_wrapper
    echo.
    echo ERROR: Cannot find gradle-wrapper.jar.
    echo You may need to run 'gradle wrapper' manually to create the wrapper files.
    echo.
    goto end

:end
    endlocal