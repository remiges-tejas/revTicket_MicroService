#!/bin/bash

echo "Starting Payment Service..."
cd "Microservices-Backend/payment-service"
java -jar target/payment-service-1.0.0.jar
