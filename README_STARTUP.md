# RevTicket Startup Guide

## Quick Start Options

### Option 1: Docker Compose (Recommended - Fastest)
```bash
# Build and start all services
docker-compose up --build

# Or run in background
docker-compose up -d

# Stop all services
docker-compose down
```

**Pros:**
- ✅ Fastest startup (~2-3 minutes)
- ✅ All services start in parallel
- ✅ Automatic dependency management
- ✅ Consistent across all platforms
- ✅ Easy to stop/restart

**Cons:**
- ❌ Requires Docker installed

---

### Option 2: Build Script + Start Script (Current)
```bash
# First time: Build all JARs (one-time, ~5-10 minutes)
cd Microservices-Backend
./build-all.sh

# Then start services
cd ..
node start.js
```

**Pros:**
- ✅ No Docker required
- ✅ Auto-builds missing JARs
- ✅ Good progress tracking

**Cons:**
- ❌ Slower startup (~3-5 minutes)
- ❌ Sequential startup

---

### Option 3: Maven Spring Boot (Development)
```bash
# Start individual service for development
cd Microservices-Backend/movie-service
mvn spring-boot:run
```

**Pros:**
- ✅ Best for development
- ✅ Hot reload support
- ✅ Easy debugging

**Cons:**
- ❌ Very slow startup
- ❌ Must start each service manually

---

### Option 4: Parallel JAR Startup (Fastest without Docker)
```bash
# Build once
cd Microservices-Backend
./build-all.sh

# Start all in parallel
./start-services.sh
```

**Pros:**
- ✅ Fast startup (~2 minutes)
- ✅ All services start together
- ✅ No Docker needed

**Cons:**
- ❌ Less progress visibility
- ❌ Harder to debug issues

---

## Recommended Workflow

### For Production/Demo:
```bash
docker-compose up -d
```

### For Development:
```bash
# Start infrastructure
docker-compose up -d eureka-server api-gateway

# Start service you're working on
cd Microservices-Backend/your-service
mvn spring-boot:run
```

### For Testing:
```bash
node start.js
```

---

## Troubleshooting

### Services not starting?
```bash
# Check logs
tail -f Microservices-Backend/logs/*.log

# Check if ports are in use
lsof -i :8080  # API Gateway
lsof -i :8761  # Eureka

# Kill processes
pkill -9 java
```

### Build failures?
```bash
# Clean and rebuild
cd Microservices-Backend
mvn clean install -DskipTests
```

### Docker issues?
```bash
# Clean everything
docker-compose down -v
docker system prune -a

# Rebuild
docker-compose up --build
```
