#!/bin/bash

# RevTicket Quick Start - Parallel Service Startup
# Starts all services in parallel for fastest startup

set -e

BACKEND_DIR="Microservices-Backend"
LOGS_DIR="$BACKEND_DIR/logs"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}  RevTicket Quick Start - Parallel Mode${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}\n"

# Create logs directory
mkdir -p "$LOGS_DIR"

# Kill existing Java processes
echo -e "${YELLOW}Cleaning up existing processes...${NC}"
pkill -9 java 2>/dev/null || true
sleep 2

# Start services in parallel
echo -e "${GREEN}Starting all services in parallel...${NC}\n"

services=(
    "eureka-server:8761"
    "api-gateway:8080"
    "user-service:8082"
    "settings-service:8089"
    "movie-service:8081"
    "theater-service:8083"
    "showtime-service:8086"
    "booking-service:8085"
    "payment-service:8084"
    "review-service:8087"
    "search-service:8088"
    "notification-service:8090"
    "dashboard-service:8091"
)

for service_port in "${services[@]}"; do
    IFS=':' read -r service port <<< "$service_port"
    jar="$BACKEND_DIR/$service/target/$service-1.0.0.jar"
    
    if [ -f "$jar" ]; then
        echo -e "${GREEN}✓${NC} Starting $service on port $port"
        nohup java -jar "$jar" > "$LOGS_DIR/${service%%-*}.log" 2>&1 &
    else
        echo -e "${RED}✗${NC} JAR not found: $service"
    fi
done

# Start Frontend
echo -e "${GREEN}✓${NC} Starting Frontend on port 4200"
cd Frontend
nohup npm start > ../frontend.log 2>&1 &
cd ..

echo -e "\n${YELLOW}Waiting for services to start (20 seconds)...${NC}"
sleep 20

echo -e "\n${BLUE}═══════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}  All services started!${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════${NC}\n"

echo -e "${BLUE}Access URLs:${NC}"
echo -e "  Frontend:    http://localhost:4200"
echo -e "  API Gateway: http://localhost:8080"
echo -e "  Eureka:      http://localhost:8761\n"

echo -e "${YELLOW}Logs: $LOGS_DIR/*.log${NC}"
echo -e "${YELLOW}Stop: ./stop-all.sh${NC}\n"
