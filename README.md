# RevTicket Microservices Backend

Complete microservices architecture for RevTicket movie booking platform.

## 🚀 Quick Start

```bash
# 1. Configure environment
cp .env.example .env
# Edit .env with your credentials

# 2. Deploy everything
./build-and-deploy.sh

# 3. Access application
# Frontend: http://localhost:4200
# API Gateway: http://localhost:8080
# Eureka: http://localhost:8761
```

## 📁 Project Structure

```
Microservices-Backend/
├── eureka-server/          # Service discovery
├── api-gateway/            # API Gateway (port 8080)
├── user-service/           # Authentication & users (8081)
├── movie-service/          # Movie catalog (8082)
├── theater-service/        # Theaters & screens (8083)
├── showtime-service/       # Showtimes (8084)
├── booking-service/        # Bookings (8085)
├── payment-service/        # Payments (8086)
├── review-service/         # Reviews (8087)
├── search-service/         # Search (8088)
├── notification-service/   # Notifications (8089)
├── settings-service/       # Settings (8090)
├── dashboard-service/      # Admin dashboard (8091)
├── docker-compose.yml      # Docker orchestration
├── .env.example            # Environment template
└── build-and-deploy.sh     # Automated deployment
```

## 📚 Documentation

| Document | Description |
|----------|-------------|
| [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) | Complete deployment instructions |
| [QUICK_START.md](QUICK_START.md) | Quick reference guide |
| [API_GATEWAY_ROUTING_GUIDE.md](API_GATEWAY_ROUTING_GUIDE.md) | API routes & security |
| [DOCKER_DEPLOYMENT_GUIDE.md](DOCKER_DEPLOYMENT_GUIDE.md) | Docker details |
| [GATEWAY_AND_DOCKER_SUMMARY.md](GATEWAY_AND_DOCKER_SUMMARY.md) | Configuration summary |
| [ADMIN_STATS_ENDPOINTS_SUMMARY.md](ADMIN_STATS_ENDPOINTS_SUMMARY.md) | Admin stats API |

## 🛠️ Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- Java 17+
- Maven 3.8+
- Node.js 18+ (for frontend)

## 🔧 Configuration

### 1. Environment Variables

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

**Required variables:**
- `MYSQL_ROOT_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing key (256+ bits)
- `MAIL_USERNAME` - SMTP email
- `MAIL_PASSWORD` - SMTP password
- `RAZORPAY_KEY_ID` - Payment gateway key
- `RAZORPAY_KEY_SECRET` - Payment gateway secret

### 2. Build Services

```bash
# Build all services
for service in eureka-server api-gateway user-service movie-service \
               theater-service showtime-service booking-service \
               payment-service review-service search-service \
               notification-service settings-service dashboard-service; do
  cd $service && mvn clean package -DskipTests && cd ..
done
```

### 3. Start Services

```bash
# Using Docker Compose
docker-compose up -d

# Or use automated script
./build-and-deploy.sh
```

## 🌐 Access Points

| Service | URL | Purpose |
|---------|-----|---------|
| Frontend | http://localhost:4200 | User interface |
| API Gateway | http://localhost:8080 | API entry point |
| Eureka | http://localhost:8761 | Service registry |
| MySQL | localhost:3307 | Database |
| MongoDB | localhost:27018 | Document store |

## 🔍 Health Checks

```bash
# Check all services
for port in {8080..8091}; do
  echo "Port $port: $(curl -s http://localhost:$port/actuator/health | jq -r .status)"
done

# Check Eureka dashboard
open http://localhost:8761
```

## 🧪 Testing

### Public Endpoints
```bash
# Browse movies
curl http://localhost:8080/api/movies

# Search
curl http://localhost:8080/api/search?q=avengers
```

### Authenticated Endpoints
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'

# Use token
curl -H "Authorization: Bearer <TOKEN>" \
  http://localhost:8080/api/bookings/my-bookings
```

### Admin Endpoints
```bash
curl -H "Authorization: Bearer <ADMIN_TOKEN>" \
  http://localhost:8080/api/admin/users/stats
```

## 🐳 Docker Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Restart specific service
docker-compose restart user-service

# Clean restart (removes data)
docker-compose down -v && docker-compose up -d --build
```

## 🏗️ Architecture

### Microservices (11)
- **user-service** - Authentication, user management
- **movie-service** - Movie catalog
- **theater-service** - Theater & screen management
- **showtime-service** - Showtime scheduling
- **booking-service** - Ticket booking
- **payment-service** - Payment processing (Razorpay)
- **review-service** - Movie reviews
- **search-service** - Search functionality
- **notification-service** - Email/SMS notifications
- **settings-service** - System settings
- **dashboard-service** - Admin dashboard & analytics

### Infrastructure
- **Eureka Server** - Service discovery
- **API Gateway** - Single entry point, routing, security
- **MySQL** - Relational database (7 schemas)
- **MongoDB** - Document database (2 databases)

### Communication
- **Service Discovery** - Eureka-based
- **Load Balancing** - Client-side (Ribbon)
- **API Gateway** - Spring Cloud Gateway
- **Security** - JWT-based authentication

## 🔐 Security

- **JWT Authentication** - Token-based auth
- **Role-Based Access** - USER, ADMIN roles
- **API Gateway Security** - Centralized auth
- **Service-Level Security** - @PreAuthorize annotations
- **CORS** - Configured for frontend

## 📊 Monitoring

- **Eureka Dashboard** - Service health & registration
- **Actuator Endpoints** - Health, metrics, info
- **Docker Stats** - Container resource usage
- **Logs** - Centralized via docker-compose

## 🚨 Troubleshooting

### Services not starting?
```bash
docker-compose logs service-name
```

### Port conflicts?
```bash
lsof -i :8080
docker-compose down
```

### Database issues?
```bash
docker-compose logs mysql
docker-compose logs mongodb
```

### Clean restart?
```bash
docker-compose down -v
./build-and-deploy.sh
```

## 📝 Development

### Run Individual Service
```bash
cd user-service
mvn spring-boot:run
```

### Run with Profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Build Without Tests
```bash
mvn clean package -DskipTests
```

## 🤝 Contributing

1. Create feature branch
2. Make changes
3. Build and test locally
4. Submit pull request

## 📄 License

[Your License Here]

## 🆘 Support

For detailed instructions, see [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)

For quick reference, see [QUICK_START.md](QUICK_START.md)

---

**Built with Spring Boot, Spring Cloud, Docker, and ❤️**
