# Camel Spring Boot Application - PlantUML Documentation

This document contains PlantUML diagrams documenting the architecture and flows of the Camel Spring Boot application.

## Table of Contents

1. [Application Architecture](#application-architecture)
2. [Component Diagram](#component-diagram)
3. [Camel Routes Flow](#camel-routes-flow)
4. [REST API Sequence Diagrams](#rest-api-sequence-diagrams)
5. [JSON Transformation Process](#json-transformation-process)
6. [Deployment Diagram](#deployment-diagram)

## Application Architecture

```plantuml
@startuml Application_Architecture
!theme plain
title Camel Spring Boot Application - High Level Architecture

package "External APIs" {
    [JSONPlaceholder API] as ExtAPI
}

package "Spring Boot Application" {
    package "REST Controllers" {
        [HealthController] as HC
        [ApiController] as AC
        [CamelController] as CC
    }
    
    package "Apache Camel" {
        [JsonTransformRoute] as JTR
        [Timer Component] as TC
        [HTTP Component] as HTTPComp
        [Direct Component] as DC
    }
    
    package "Core Components" {
        [ProducerTemplate] as PT
        [CamelContext] as CCtx
        [JsonTransformProcessor] as JTP
    }
    
    package "Configuration" {
        [application.yml] as Config
        [Spring Boot Auto-configuration] as AutoConfig
    }
}

package "Clients" {
    [Web Browser] as WB
    [REST Client] as RC
    [curl/Postman] as CP
}

' Relationships
WB --> HC : GET /api/alive
RC --> AC : REST calls
CP --> CC : Camel management

TC --> JTR : Timer trigger (30s)
JTR --> HTTPComp : HTTP calls
HTTPComp --> ExtAPI : GET requests
JTR --> JTP : Transform JSON
JTR --> DC : Internal routing

CC --> PT : Manual triggers
CC --> CCtx : Route management

Config --> AutoConfig : Configuration
AutoConfig --> JTR : Route configuration

@enduml
```

## Component Diagram

```plantuml
@startuml Component_Diagram
!theme plain
title Component Diagram - Internal Structure

package "com.example.camel" {
    
    package "controller" {
        class HealthController {
            +alive(): ResponseEntity
            +health(): ResponseEntity
        }
        
        class ApiController {
            +welcome(): ResponseEntity
            +info(): ResponseEntity
            +echo(payload): ResponseEntity
        }
        
        class CamelController {
            -producerTemplate: ProducerTemplate
            -camelContext: CamelContext
            +triggerTransform(): ResponseEntity
            +getRoutes(): ResponseEntity
            +startRoute(routeId): ResponseEntity
            +stopRoute(routeId): ResponseEntity
        }
    }
    
    package "route" {
        class JsonTransformRoute {
            +configure(): void
        }
        
        class JsonTransformProcessor {
            -objectMapper: ObjectMapper
            +process(exchange): void
        }
    }
    
    package "main" {
        class CamelSpringBootApplication {
            +main(args): void
        }
    }
}

' Relationships
CamelController --> "1" ProducerTemplate
CamelController --> "1" CamelContext
JsonTransformRoute --> "1" JsonTransformProcessor
CamelSpringBootApplication --> "*" HealthController
CamelSpringBootApplication --> "*" ApiController
CamelSpringBootApplication --> "*" CamelController
CamelSpringBootApplication --> "*" JsonTransformRoute

@enduml
```

## Camel Routes Flow

```plantuml
@startuml Camel_Routes_Flow
!theme plain
title Apache Camel Routes Flow Diagram

start

:Timer (30 seconds);
note right: Automatic trigger every 30 seconds

:Log: Start JSON data retrieval;

:HTTP GET Request;
note right: https://jsonplaceholder.typicode.com/posts/1

:Log: Data retrieved;

:JsonTransformProcessor;
note right
Transform JSON:
- originalId
- transformedTitle (UPPERCASE)
- summary (truncated)
- userId
- transformationTimestamp
- source
end note

:Log: Data transformed;

:Send to direct:processTransformedData;

partition "Process Transformed Data Route" {
    :Log: Processing transformed data;
    
    :Add headers;
    note right
    - ProcessedAt: timestamp
    - DataLength: data size
    end note
    
    :Log: Final data and headers;
}

stop

note bottom
**Manual Transform Route**
Can be triggered via POST /api/camel/transform
Uses the same transformation logic
but fetches from posts/2
end note

@enduml
```

## REST API Sequence Diagrams

### Health Check Flow

```plantuml
@startuml Health_Check_Sequence
!theme plain
title Health Check API Sequence

actor "Client" as C
participant "HealthController" as HC
participant "Spring Boot" as SB

C -> HC: GET /api/alive
activate HC
HC -> HC: Create response map
HC -> HC: Add status, timestamp, version
HC -> C: ResponseEntity<Map>
deactivate HC

note right of C
Response:
{
  "status": "alive",
  "timestamp": "2025-09-08T10:30:00",
  "application": "camel-springboot-app",
  "version": "1.0.0"
}
end note

@enduml
```

### Manual Transformation Flow

```plantuml
@startuml Manual_Transform_Sequence
!theme plain
title Manual Transformation Sequence

actor "Client" as C
participant "CamelController" as CC
participant "ProducerTemplate" as PT
participant "JsonTransformRoute" as JTR
participant "JsonTransformProcessor" as JTP
participant "External API" as API

C -> CC: POST /api/camel/transform
activate CC

CC -> PT: requestBody("direct:manualTransform", "")
activate PT

PT -> JTR: Trigger manual-transform-route
activate JTR

JTR -> API: GET /posts/2
activate API
API -> JTR: JSON response
deactivate API

JTR -> JTP: process(exchange)
activate JTP
JTP -> JTP: Parse JSON
JTP -> JTP: Transform data
JTP -> JTP: Create new JSON
JTP -> JTR: Transformed JSON
deactivate JTP

JTR -> PT: Return result
deactivate JTR

PT -> CC: Transformed JSON string
deactivate PT

CC -> CC: Create response map
CC -> C: ResponseEntity with transformed data
deactivate CC

@enduml
```

## JSON Transformation Process

```plantuml
@startuml JSON_Transformation_Process
!theme plain
title JSON Transformation Process Flow

start

:Receive JSON Input;
note right
Input example:
{
  "id": 1,
  "title": "sample title",
  "body": "sample body content...",
  "userId": 123
}
end note

:Parse JSON with ObjectMapper;

:Extract fields;
split
:Extract id;
split again
:Extract title;
split again
:Extract body;
split again
:Extract userId;
end split

:Create transformed object;
note right
Transformation rules:
- originalId = id
- transformedTitle = "TRANSFORMED: " + title.toUpperCase()
- summary = body.substring(0, 50) + "..."
- userId = userId
- transformationTimestamp = System.currentTimeMillis()
- source = "external-api"
end note

:Convert to JSON string;

:Set as exchange body;

:Add Content-Type header;

stop

note bottom
Output example:
{
  "originalId": 1,
  "transformedTitle": "TRANSFORMED: SAMPLE TITLE",
  "summary": "sample body content...",
  "userId": 123,
  "transformationTimestamp": 1725795600000,
  "source": "external-api"
}
end note

@enduml
```

## Deployment Diagram

```plantuml
@startuml Deployment_Diagram
!theme plain
title Deployment Architecture

node "Development Environment" {
    artifact "camel-springboot-app-1.0.0.jar" as JAR {
        component "Spring Boot 3.2.0" as SB
        component "Apache Camel 4.2.0" as AC
        component "Embedded Tomcat" as ET
    }
    
    database "Configuration Files" as CF {
        file "application.yml"
        file "application-dev.yml"
        file "application-test.yml"
    }
}

cloud "External Services" {
    interface "JSONPlaceholder API" as JPAPI
    note bottom of JPAPI
    https://jsonplaceholder.typicode.com
    - /posts/1 (automatic route)
    - /posts/2 (manual route)
    end note
}

node "Client Applications" {
    artifact "Web Browser" as WB
    artifact "REST Client" as RC
    artifact "curl/Postman" as CP
}

' Connections
JAR --> JPAPI : HTTP GET requests
WB --> JAR : HTTP REST calls
RC --> JAR : HTTP REST calls
CP --> JAR : HTTP REST calls
CF --> JAR : Configuration

' Ports and protocols
JAR : Port 8080-8083 (configurable)
JAR : HTTP/HTTPS
JPAPI : Port 443 (HTTPS)

@enduml
```

## Route States and Management

```plantuml
@startuml Route_State_Management
!theme plain
title Camel Route State Management

state "Route States" as RS {
    state "Started" as Started
    state "Stopped" as Stopped
    state "Suspended" as Suspended
}

[*] --> Started : Application startup\nAuto-start enabled

Started --> Stopped : POST /api/camel/routes/{id}/stop
Stopped --> Started : POST /api/camel/routes/{id}/start
Started --> Suspended : Suspend route
Suspended --> Started : Resume route

Started : Route is active
Started : Timer triggers every 30s
Started : Can process messages

Stopped : Route is inactive
Stopped : No message processing
Stopped : Timer is stopped

Suspended : Route is paused
Suspended : No new messages
Suspended : Existing messages continue

note bottom
Available routes:
- json-transform-route (timer)
- process-transformed-data (direct)
- manual-transform-route (direct)
end note

@enduml
```

## API Endpoints Overview

```plantuml
@startuml API_Endpoints_Overview
!theme plain
title REST API Endpoints Overview

package "Health & Info APIs" {
    [GET /api/alive] as Alive
    [GET /api/health] as Health
    [GET /api/welcome] as Welcome
    [GET /api/info] as Info
    [POST /api/echo] as Echo
}

package "Camel Management APIs" {
    [POST /api/camel/transform] as Transform
    [GET /api/camel/routes] as Routes
    [POST /api/camel/routes/{id}/start] as Start
    [POST /api/camel/routes/{id}/stop] as Stop
}

package "Spring Boot Actuator" {
    [GET /actuator/health] as ActHealth
    [GET /actuator/info] as ActInfo
    [GET /actuator/metrics] as ActMetrics
    [GET /actuator/env] as ActEnv
    [GET /actuator/camel] as ActCamel
}

note right of Alive
Returns application status
with timestamp and version
end note

note right of Transform
Triggers manual JSON
transformation from external API
end note

note right of Routes
Lists all Camel routes
with their current status
end note

note bottom of ActCamel
Camel-specific actuator endpoints
for monitoring and management
end note

@enduml
```

## Usage Instructions

To render these PlantUML diagrams:

1. **Online PlantUML Editor**: Copy and paste each diagram code into [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)

2. **VS Code Extension**: Install the "PlantUML" extension and open any `.puml` file

3. **Local PlantUML**: 
   ```bash
   # Install PlantUML
   brew install plantuml  # macOS
   sudo apt-get install plantuml  # Ubuntu
   
   # Generate diagrams
   plantuml documentation.puml
   ```

4. **IntelliJ IDEA**: Install the "PlantUML integration" plugin

## File Organization

Save each diagram in separate `.puml` files:
- `architecture.puml` - Application Architecture
- `components.puml` - Component Diagram  
- `camel-routes.puml` - Camel Routes Flow
- `sequences.puml` - Sequence Diagrams
- `transformation.puml` - JSON Transformation Process
- `deployment.puml` - Deployment Diagram
- `states.puml` - Route State Management
- `api-overview.puml` - API Endpoints Overview
