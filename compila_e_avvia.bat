@echo off
echo [X] Pulizia cartella bin...
rmdir /s /q ..\bin 2>nul
mkdir ..\bin

echo [X] Generazione lista file sorgenti...
cd /d "%~dp0"
dir /s /b *.java > sources.txt

echo [X] Compilazione in corso...
javac -encoding UTF-8 -d ..\bin @sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo [E] Errore nella compilazione. Controlla i messaggi sopra.
    pause
    exit /b
)

echo [X] Compilazione completata.
echo [X] Copia delle risorse statiche...

xcopy /E /I /Y img ..\bin\dashboard\img

echo [X] Avvio dell'applicazione...

cd ..\bin
java "-Dfile.encoding=UTF-8" dashboard.Calendario
pause
