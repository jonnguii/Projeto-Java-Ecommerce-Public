@echo off
chcp 65001 >nul 2>nul
title Sistema E-commerce

echo.

rem Compila o projeto (javac segue os imports automaticamente)
javac -encoding UTF-8 -cp "lib/*;src/main/java" src/main/java/com/ecommerce/Main.java -d target/classes

if %ERRORLEVEL% NEQ 0 (
    echo.[ERRO] Falha na compilacao. Verifique o codigo.
    pause
    exit /b 1
)

echo.

rem Executa a classe Main
java -cp "target/classes;lib/*" com.ecommerce.Main

pause
