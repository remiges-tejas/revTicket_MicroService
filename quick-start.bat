@echo off
REM RevTicket Quick Start - Windows

echo ===================================================
echo   RevTicket Quick Start - Parallel Mode
echo ===================================================
echo.

set BACKEND_DIR=Microservices-Backend
set LOGS_DIR=%BACKEND_DIR%\logs

REM Create logs directory
if not exist "%LOGS_DIR%" mkdir "%LOGS_DIR%"

REM Kill existing processes
echo Cleaning up existing processes...
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM node.exe >nul 2>&1
timeout /t 2 >nul

echo Starting all services in parallel...
echo.

REM Start services
call :start_service eureka-server 8761
call :start_service api-gateway 8080
call :start_service user-service 8082
call :start_service settings-service 8089
call :start_service movie-service 8081
call :start_service theater-service 8083
call :start_service showtime-service 8086
call :start_service booking-service 8085
call :start_service payment-service 8084
call :start_service review-service 8087
call :start_service search-service 8088
call :start_service notification-service 8090
call :start_service dashboard-service 8091

REM Start Frontend
echo [*] Starting Frontend on port 4200
cd Frontend
start /B npm start > ..\frontend.log 2>&1
cd ..

echo.
echo Waiting for services to start (20 seconds)...
timeout /t 20 >nul

echo.
echo ===================================================
echo   All services started!
echo ===================================================
echo.
echo Access URLs:
echo   Frontend:    http://localhost:4200
echo   API Gateway: http://localhost:8080
echo   Eureka:      http://localhost:8761
echo.
echo Logs: %LOGS_DIR%\*.log
echo Stop: stop-all.bat
echo.
goto :eof

:start_service
set SERVICE=%1
set PORT=%2
set JAR=%BACKEND_DIR%\%SERVICE%\target\%SERVICE%-1.0.0.jar

if exist "%JAR%" (
    echo [*] Starting %SERVICE% on port %PORT%
    start /B java -jar "%JAR%" > "%LOGS_DIR%\%SERVICE%.log" 2>&1
) else (
    echo [X] JAR not found: %SERVICE%
)
goto :eof
