title Crypto - KMS Encryption
:: Crypto
@echo off

for /f %%j in ("java.exe") do (
    set JAVA_HOME=%%~dp$PATH:j
)

if %JAVA_HOME%.==. (
    @echo java.exe not found
    @timeout /t 10 /nobreak
) else (
    @echo JAVA_HOME = %JAVA_HOME%
    shift
    shift

    @%JAVA_HOME%java.exe -jar encryption4s.jar %*
)
