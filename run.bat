@echo off
chcp 65001 >nul
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out src\*.java
if errorlevel 1 (
    echo Loi bien dich. Kiem tra da cai JDK 17+ chua.
    pause
    exit /b 1
)
java -Dsun.stdout.encoding=UTF-8 -Dfile.encoding=UTF-8 -cp out ChuongTrinh
pause
