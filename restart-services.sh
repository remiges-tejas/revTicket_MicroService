#!/bin/bash

echo "🔄 Restarting RevTicket Microservices..."

# Kill all Java processes
echo "Stopping all Java services..."
pkill -9 -f "java -jar"
sleep 2

# Kill node processes
echo "Stopping Node processes..."
pkill -9 -f "node start.js"
pkill -9 -f "ng serve"
sleep 2

# Clear ports
echo "Clearing ports..."
for port in 8761 8080 8081 8082 8083 8084 8085 8086 8087 8088 8089 8090 8091 4200; do
    lsof -ti:$port | xargs kill -9 2>/dev/null
done

sleep 3

echo "✓ All services stopped"
echo ""
echo "Starting services with node start.js..."
echo "Run: node start.js"
