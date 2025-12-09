@echo off
REM Stop all RevTicket services

echo Stopping all services...

taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM node.exe >nul 2>&1

echo All services stopped
