@echo off
setlocal

set "MAVEN_VERSION=3.9.11"
set "MAVEN_BASE=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%"
set "MAVEN_HOME=%MAVEN_BASE%\apache-maven-%MAVEN_VERSION%"
set "MAVEN_ZIP=%TEMP%\apache-maven-%MAVEN_VERSION%-bin.zip"
set "MAVEN_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip"

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Maven %MAVEN_VERSION% not found.
    echo Downloading Maven...

    powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%'"

    if errorlevel 1 (
        echo Failed to download Maven.
        exit /b 1
    )

    if exist "%MAVEN_BASE%" rmdir /s /q "%MAVEN_BASE%"
    mkdir "%MAVEN_BASE%"

    powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_BASE%' -Force"

    if errorlevel 1 (
        echo Failed to extract Maven.
        exit /b 1
    )

    del "%MAVEN_ZIP%"
)

call "%MAVEN_HOME%\bin\mvn.cmd" %*

exit /b %ERRORLEVEL%