#!/bin/bash

# Stop all RevTicket services

echo "Stopping all services..."

# Kill Java processes
pkill -9 java 2>/dev/null || true

# Kill Node processes (Frontend)
pkill -9 node 2>/dev/null || true

echo "✓ All services stopped"
