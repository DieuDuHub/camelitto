# Spring Boot + Apache Camel Integration Project Documentation

## Project Overview

This project demonstrates a comprehensive integration solution using **Spring Boot 3** and **Apache Camel 4.2.0**. The application provides REST APIs for data transformation and integration with external services, specifically designed to showcase modern enterprise integration patterns.

## Key Features

### 🚀 Spring Boot 3 Foundation
- **Executable JAR**: Standalone application (32 MB)
- **REST API**: Complete health check and management endpoints
- **Multi-profile Configuration**: Development, test, and production profiles
- **Spring Boot Actuator**: Built-in monitoring and management
- **Comprehensive Testing**: Unit and integration tests

### 🔄 Apache Camel Integration Routes
- **Automatic Data Processing**: Timer-based routes for scheduled data transformation
- **Manual Processing**: On-demand data transformation via REST endpoints
- **Person Data Integration**: Specialized route for person data API integration
- **Data Filtering**: Advanced JSON processing and field extraction

### 📊 Data Transformation Capabilities
- **JSON Processing**: Parse, transform, and enrich JSON data structures
- **Field Filtering**: Extract specific fields from complex API responses
- **Data Enrichment**: Add metadata, timestamps, and processing information
- **Format Conversion**: Transform data formats and structures

## Architecture Components

### REST Controllers
- **HealthController**: Application health and status endpoints
- **ApiController**: General-purpose API endpoints
- **CamelController**: Camel route management and data processing endpoints

### Camel Routes
- **JsonTransformRoute**: Main route configuration with multiple processors
- **PersonDataRoute**: Specialized route for person data API integration
- **Automatic Timer Route**: Scheduled processing every 30 seconds
- **Manual Trigger Routes**: On-demand processing via REST calls

### Data Processors
- **JsonTransformProcessor**: Transforms JSONPlaceholder API data
- **PersonDataProcessor**: Filters person data API responses

## API Endpoints

### Health & Status
- `GET /api/alive` - Application liveness check
- `GET /api/health` - Detailed health status
- `GET /actuator/health` - Spring Boot health endpoint

### Camel Integration
- `GET /api/camel/transform` - Trigger JSONPlaceholder data transformation
- `GET /api/camel/person/{id}` - Retrieve filtered person data by ID
- `GET /api/camel/routes` - List all Camel routes
- `POST /api/camel/routes/{routeId}/start` - Start specific route
- `POST /api/camel/routes/{routeId}/stop` - Stop specific route

## External API Integrations

### JSONPlaceholder API
- **URL**: `https://jsonplaceholder.typicode.com/posts/{id}`
- **Purpose**: Demo data for transformation examples
- **Processing**: Title transformation, summary generation, metadata enrichment

### Person Data API
- **URL**: `http://localhost:8001/person_data/1`
- **Purpose**: Real person data integration
- **Response Format**:
  ```json
  {
    "Ok": {
      "id": 1,
      "first_name": "Jean",
      "last_name": "Dupont",
      "email": "jean.dupont@test.com",
      "phone": "+33123456789",
      "birth_date": "1990-05-15",
      "gender": "M",
      "created_at": "2025-08-19T09:25:30.135028Z",
      "updated_at": "2025-08-19T09:25:30.135028Z",
      "is_active": true
    }
  }
  ```
- **Filtered Output**:
  ```json
  {
    "first_name": "Jean",
    "last_name": "Dupont",
    "creation_date": "2025-08-19T09:25:30.135028Z"
  }
  ```

## Data Flow Patterns

### Automatic Processing Flow
1. **Timer Trigger** (every 30 seconds)
2. **HTTP Call** to JSONPlaceholder API
3. **Data Transformation** via JsonTransformProcessor
4. **Internal Routing** to processing pipeline
5. **Metadata Addition** and logging

### Manual Processing Flow
1. **REST API Call** to `GET /api/camel/person/{id}` with path parameter
2. **Route Activation** via ProducerTemplate with dynamic ID
3. **HTTP GET Call** to Person Data API (`http://localhost:8001/person_data/{id}`)
4. **Data Filtering** via PersonDataProcessor
5. **Response Formatting** and return

### Route Management Flow
1. **Route Discovery** via CamelContext
2. **Status Monitoring** and control
3. **Dynamic Start/Stop** capabilities
4. **Health Reporting** integration

## Configuration

### Application Properties
- **Server Port**: Configurable (default: 8080)
- **Camel Settings**: Auto-startup, context configuration
- **Logging**: Structured logging with multiple levels
- **Actuator**: Comprehensive endpoint exposure

### Profile Management
- **Development Profile**: Enhanced logging, all actuator endpoints
- **Test Profile**: Minimal port assignment, test-specific settings
- **Production Profile**: Optimized for deployment

## Documentation

### PlantUML Diagrams
Complete architectural documentation with PlantUML diagrams:
- **System Architecture**: High-level component overview
- **Sequence Diagrams**: Detailed interaction flows
- **Component Diagrams**: Relationships and dependencies
- **Data Flow Diagrams**: Transformation processes
- **API Documentation**: Endpoint specifications

### Generation Scripts
- **Automated Diagram Generation**: Shell script for all formats
- **Multiple Output Formats**: PNG, SVG, PDF
- **Documentation Integration**: Ready for wiki/markdown inclusion

## Development Workflow

### Build & Test
```bash
# Compile project
mvn clean compile

# Run tests
mvn test

# Create executable JAR
mvn clean package

# Run application
java -jar target/camel-springboot-app-1.0.0.jar
```

### Documentation Generation
```bash
# Generate all PlantUML diagrams
./generate-diagrams.sh

# Install PlantUML (if needed)
brew install plantuml  # macOS
npm install -g plantuml  # Node.js
```

### Testing Endpoints
```bash
# Health check
curl http://localhost:8080/api/alive

# Person data integration
curl http://localhost:8080/api/camel/person/1

# Person data with different ID
curl http://localhost:8080/api/camel/person/42

# Route management
curl http://localhost:8080/api/camel/routes
```

## Enterprise Integration Patterns

This project demonstrates several key Enterprise Integration Patterns (EIP):

- **Message Router**: Conditional routing based on content
- **Message Translator**: Data format transformation
- **Content Filter**: Field extraction and filtering
- **Message Enricher**: Adding metadata and processing information
- **Polling Consumer**: Timer-based message consumption
- **Request-Reply**: Synchronous request processing

## Technology Stack

- **Java 17**: Modern JVM features and performance
- **Spring Boot 3.2.0**: Latest framework capabilities
- **Apache Camel 4.2.0**: Enterprise integration patterns
- **Jackson**: JSON processing and transformation
- **Maven**: Build automation and dependency management
- **JUnit 5**: Modern testing framework
- **PlantUML**: Architecture documentation

## Production Considerations

### Monitoring
- Spring Boot Actuator endpoints for health checks
- Camel route monitoring and metrics
- Structured logging for observability

### Scalability
- Stateless design for horizontal scaling
- Configurable timer intervals
- Connection pooling for external APIs

### Security
- Endpoint security configuration ready
- External API authentication support
- Input validation and error handling

### Deployment
- Executable JAR for containerization
- Profile-based configuration
- External configuration support

## Future Enhancements

### Additional Integrations
- Database connectivity with JPA
- Message queue integration (RabbitMQ, Kafka)
- File system processing capabilities
- Email and notification services

### Advanced Features
- Circuit breaker patterns
- Retry mechanisms and error handling
- Data transformation pipelines
- Event-driven architecture patterns

### Monitoring & Observability
- Prometheus metrics integration
- Distributed tracing support
- Custom health indicators
- Performance monitoring

This documentation provides a comprehensive overview of the Spring Boot + Apache Camel integration project, designed for enterprise-grade data processing and API integration scenarios.
