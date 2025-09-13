# 🚀 Spring Boot + Apache Camel Integration Project

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Camel](https://img.shields.io/badge/Apache%20Camel-4.2.0-red.svg)](https://camel.apache.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)

Un projet complet d'intégration Spring Boot et Apache Camel démontrant les patterns d'intégration d'entreprise modernes avec transformation de données JSON et intégration d'APIs externes.

## ✨ Fonctionnalités Principales

- **🏗️ Application Spring Boot 3** - JAR exécutable standalone
- **🔄 Routes Apache Camel** - Transformation de données et intégration
- **🌐 APIs REST** - Endpoints de santé et de gestion
- **📊 Transformation JSON** - Traitement et filtrage de données
- **📚 Documentation PlantUML** - Diagrammes d'architecture complets
- **🧪 Tests Complets** - Tests unitaires et d'intégration
- **⚡ Démarrage Rapide** - Script de démarrage intelligent

## 🚀 Démarrage Rapide

### Prérequis
- Java 17+
- Maven 3.6+
- curl (pour les tests)

### Installation et Lancement

```bash
# Cloner et se déplacer dans le projet
cd /Users/matthieudebray/dev/java/camel

# Démarrage simple
./run.sh start

# Ou démarrage en mode développement
./run.sh dev

# Ou étape par étape
./run.sh build    # Construire le projet
./run.sh start    # Démarrer l'application
```

### Test de l'Application

```bash
# Vérifier que l'application fonctionne
curl http://localhost:8080/api/alive

# Tester l'intégration des données personnelles (JSON/REST par défaut)
curl http://localhost:8080/api/camel/person/1

# Tester avec l'API SOAP/XML pour données employés
curl "http://localhost:8080/api/camel/person/1?type=soap"

# Tester avec différents IDs et types
curl "http://localhost:8080/api/camel/person/42?type=json"

# Voir les routes Camel actives
curl http://localhost:8080/api/camel/routes
```

## 📁 Structure du Projet

```
camel/
├── src/main/java/com/example/camel/
│   ├── CamelSpringBootApplication.java    # Point d'entrée principal
│   ├── controller/
│   │   ├── HealthController.java          # Endpoints de santé
│   │   ├── ApiController.java             # APIs générales
│   │   └── CamelController.java           # Gestion des routes Camel
│   ├── config/
│   │   └── CamelRouteConfig.java          # Configuration routes Camel
│   └── processor/
│       ├── PersonDataProcessor.java       # Filtrage données personnelles JSON
│       ├── SoapRequestProcessor.java      # Génération requêtes SOAP
│       └── SoapResponseProcessor.java     # Traitement réponses SOAP
├── src/test/java/                         # Tests unitaires
├── docs/                                  # Documentation PlantUML
├── target/                               # JAR compilé
├── run.sh                                # Script de démarrage
└── generate-diagrams.sh                 # Génération documentation
```

## 🔗 API Endpoints

### Health and Status
- `GET /api/alive` - Application liveness check
- `GET /api/health` - Detailed health status
- `GET /actuator/health` - Spring Boot Actuator endpoint

### Camel Integration
- `GET /api/camel/person/{id}` - Data retrieval with source selection
  - `?type=json` (default) - Personal data via REST/JSON
  - `?type=soap` or `?type=xml` - Employee data via SOAP/XML
- `GET /api/camel/routes` - List active Camel routes
- `POST /api/camel/routes/{routeId}/start` - Start a route
- `POST /api/camel/routes/{routeId}/stop` - Stop a route

### Request Logging and Monitoring
- All API requests are automatically logged with detailed metrics
- Log format includes: timestamp, URL, parameters, HTTP status, execution time
- Logs are available in both console output and `logs/camel-requests.log`
- Real-time monitoring: `tail -f logs/camel-requests.log`

## 🔄 External API Integrations

### 📊 REST/JSON API - Personal Data
- **URL**: `http://localhost:8001/person_data/{id}`
- **Method**: GET
- **Usage**: Personal data retrieval with dynamic ID
- **Filtering**: Extracts `first_name`, `last_name`, and `creation_date`

**Example calls:**
- `/api/camel/person/1?type=json` → `GET http://localhost:8001/person_data/1`
- `/api/camel/person/42` → `GET http://localhost:8001/person_data/42` (default)

**Filtered response example:**
```json
{
  "first_name": "Person1",
  "last_name": "Doe1", 
  "creation_date": "2025-08-19T09:25:30.135028Z"
}
```

### 🧼 SOAP/XML API - Employee Data
- **URL**: `http://localhost:8001/soap/PersonService`
- **Method**: POST (SOAP 1.2)
- **Usage**: Employee data retrieval with professional information
- **Processing**: SOAP request generation, XML response parsing

**Example calls:**
- `/api/camel/person/1?type=soap` → `POST http://localhost:8001/soap/PersonService`
- `/api/camel/person/42?type=xml` → `POST http://localhost:8001/soap/PersonService`

**Filtered response example:**
```json
{
  "full_name": "Alexandre Sophie",
  "department": "IT",
  "position": "Developer",
  "salary": "47500",
  "hire_date": "2020-02-15",
  "office": "Building 2, Floor 2"
}
```

### 📋 API Type Selection

The `/api/camel/person/{id}` endpoint supports a `type` parameter to select the data source:

- **`type=json`** (default): REST JSON API with personal data
- **`type=soap`** or **`type=xml`**: SOAP XML API with employee data

Both APIs use distinct Camel routes with specialized processors for each protocol.
  "last_name": "Doe1", 
  "creation_date": "2025-08-19T09:25:30.135028Z"
}
```

## 🛠️ Utilisation du Script de Démarrage

Le script `run.sh` offre plusieurs options :

```bash
./run.sh start      # Démarrage standard
./run.sh dev        # Mode développement
./run.sh test       # Mode test
./run.sh build      # Construction du projet
./run.sh clean      # Construction propre
./run.sh docs       # Génération documentation
./run.sh check      # Vérification santé
./run.sh stop       # Arrêt de l'application
./run.sh help       # Aide complète
```

### Variables d'Environnement

```bash
# Changer le port
SERVER_PORT=8081 ./run.sh start

# Profil personnalisé
SPRING_PROFILES=production ./run.sh start
```

## 📊 Apache Camel Routes

### Person Data Route (JSON)
- **Name**: `person-data-route`
- **Trigger**: REST call `GET /api/camel/person/{id}?type=json`
- **Source**: REST JSON API `http://localhost:8001/person_data/{id}`
- **Processing**: Personal field filtering via `PersonDataProcessor`
- **Destination**: Direct HTTP JSON response

### SOAP Person Data Route (XML)
- **Name**: `soap-person-data-route`
- **Trigger**: REST call `GET /api/camel/person/{id}?type=soap`
- **Source**: SOAP XML API `http://localhost:8001/soap/PersonService`  
- **Processing**: SOAP request generation → XML response parsing → Employee data extraction
- **Processors**: `SoapRequestProcessor` and `SoapResponseProcessor`
- **Destination**: HTTP JSON response with employee data

## 📝 Request Logging System

### Comprehensive Request Tracking
All API requests are automatically logged with detailed metrics:

**Log Format:**
```
REQUEST_START | [timestamp] | [method] | [url] | Parameters: [params] | RequestId: [id]
REQUEST_END | [timestamp] | [status] | [url] | HTTP [code] | [duration]ms | [size]B | [params] | RequestId: [id]
REQUEST_SUMMARY | StartTime: [start] | EndTime: [end] | URL: [url] | Params: [params] | HTTPCode: [code] | Duration: [duration]ms | Size: [size]B | Status: [status] | RequestId: [id]
```

**Example Log Entry:**
```
2025-09-13 15:29:13.850 | REQUEST_START | 2025-09-13 15:29:13.850 | INTERNAL |   | Parameters: personId=1, type=json | RequestId: 10C3ECB31CC3231-0000000000000000
2025-09-13 15:29:14.001 | REQUEST_END | 2025-09-13 15:29:14.001 | SUCCESS |  | HTTP 200 | 151ms | 89B | personId=1, type=json | RequestId: 10C3ECB31CC3231-0000000000000000
```

### Log Files and Monitoring
- **Console Output**: Real-time logging in application console
- **File Output**: `logs/camel-requests.log` with automatic rotation
- **Retention**: 30 days history, 100MB per file, 3GB total cap
- **Real-time Monitoring**: `tail -f logs/camel-requests.log`

### Log Analysis Commands
```bash
# View all requests
cat logs/camel-requests.log

# Filter by HTTP status
grep 'HTTP 200' logs/camel-requests.log

# Filter by execution time over 100ms
grep -E 'Duration: [1-9][0-9]{2,}ms' logs/camel-requests.log

# Count total requests
grep 'REQUEST_START' logs/camel-requests.log | wc -l

# Monitor real-time
tail -f logs/camel-requests.log
```

## 📚 Documentation

## 📚 Documentation

### PlantUML Diagrams
Complete documentation with diagrams:
- System architecture
- Interaction sequences
- Components and dependencies
- Data flows
- API specifications

```bash
# Generate all diagrams
./run.sh docs

# Or manually
./generate-diagrams.sh
```

### Technical Documentation
- **`docs/PROJECT_DOCUMENTATION.md`** - Complete project documentation
- **PlantUML Diagrams** - Architecture and flows
- **Code Comments** - Javadoc and inline comments

## 🧪 Testing

### Test Execution
```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Or via script
./run.sh test
```

### Coverage
- **Unit Tests**: Controllers, Processors, Routes
- **Integration Tests**: Spring Boot, Camel Context
- **Transformation Tests**: JSON processing, filtering

### Request Logging Testing
```bash
# Test logging system
./test-logging.sh

# Manual testing
curl "http://localhost:8080/api/camel/person/1?type=json"
curl "http://localhost:8080/api/camel/person/2?type=soap"
```

## 🏗️ Build and Deployment

### Build Process
```bash
# Standard build
mvn clean package

# Or via script
./run.sh build
```

### Generated JAR
- **File**: `target/camel-springboot-app-1.0.0.jar`
- **Size**: ~32 MB

## 🧪 Tests

### Exécution des Tests
```bash
# Via Maven
mvn test

# Via script
./run.sh build  # Inclut les tests
```

### Couverture
- **Tests unitaires** : Controllers, Processors, Routes
- **Tests d'intégration** : Spring Boot, Camel Context
- **Tests de transformation** : JSON processing, filtrage

## 🏗️ Construction et Déploiement

### Construction
```bash
# Construction standard
mvn clean package

# Ou via script
./run.sh build
```

### JAR Produit
- **Fichier** : `target/camel-springboot-app-1.0.0.jar`
- **Taille** : ~32 MB
- **Type**: Executable Spring Boot JAR
- **Java**: Requires Java 17+

### Deployment
```bash
# Direct startup
java -jar target/camel-springboot-app-1.0.0.jar

# With configuration
java -jar target/camel-springboot-app-1.0.0.jar --server.port=8081
```

## ⚙️ Configuration

### Spring Profiles
- **Default**: Standard configuration
- **Dev**: Detailed logs, all actuator endpoints
- **Test**: Minimal configuration for testing
- **Production**: Optimized for deployment

### Main Properties
```yaml
server:
  port: 8080

camel:
  springboot:
    main-run-controller: true

person:
  api:
    base-url: http://localhost:8001

management:
  endpoints:
    web:
      exposure:
        include: health,info,camel

# Logging configuration (logback-spring.xml)
logging:
  file:
    name: logs/camel-requests.log
  level:
    REQUEST_LOGGER: INFO
```

## 🔍 Monitoring and Observability

### Health Checks
- Integrated Spring Boot Actuator
- Custom health endpoints
- Camel routes monitoring

### Logging System
- **Structured Logging**: Multi-level logging with detailed request tracking
- **Request Logging**: Complete request lifecycle with timestamps, parameters, and performance metrics
- **File Rotation**: Automatic log rotation with configurable retention
- **Real-time Monitoring**: Console and file output for live debugging

### Log Levels and Categories
```yaml
# Main application logs
logging.level.com.example.camel: INFO

# Request tracking logs  
logging.level.REQUEST_LOGGER: INFO

# Camel framework logs
logging.level.org.apache.camel: INFO
logging.level.org.apache.camel.component.http: DEBUG
```

### Monitoring Endpoints
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/camel` - Camel routes and metrics
- `GET /actuator/loggers` - Dynamic log level management

## 🚀 Enterprise Integration Patterns

This project demonstrates several EIP patterns:
- **Message Router**: Conditional routing based on `type` parameter
- **Message Translator**: JSON→JSON and XML→JSON transformation  
- **Content Filter**: API-specific field filtering
- **Message Enricher**: Processing metadata addition
- **Protocol Adapter**: REST and SOAP support in unified interface
- **Request-Reply**: Multi-protocol synchronous processing

## 🔮 Future Enhancements

### Additional Integrations
- Database connectivity with JPA
- Message queue integration (RabbitMQ, Kafka)
- File processing capabilities
- Notification services

### Advanced Features
- Circuit Breaker patterns
- Retry mechanisms with exponential backoff
- Advanced transformation pipelines
- Event-driven architecture
- Distributed tracing integration
- Performance metrics and alerting

## 📄 License

This project is a demonstration example for Spring Boot + Apache Camel integration.

## 🤝 Support

For questions or issues:
1. Check application logs in `logs/camel-requests.log`
2. Test health endpoints
3. Consult PlantUML documentation
4. Use `./run.sh check` for diagnostics
5. Monitor request logs: `tail -f logs/camel-requests.log`

## 📋 Quick Start Commands

```bash
# Build and run with logging
mvn clean package -DskipTests
java -jar target/camel-springboot-app-1.0.0.jar &

# Test APIs with logging
curl "http://localhost:8080/api/camel/person/1?type=json"
curl "http://localhost:8080/api/camel/person/2?type=soap"

# Monitor request logs
tail -f logs/camel-requests.log

# Run test suite
./test-logging.sh
```

---

**Project created with ❤️ using Spring Boot 3 and Apache Camel 4.2.0**
