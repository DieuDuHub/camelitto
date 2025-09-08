# PlantUML Documentation for Spring Boot + Apache Camel Application

This directory contains comprehensive PlantUML diagrams documenting the architecture and design of the Spring Boot + Apache Camel integration application.

## Diagrams Overview

### 01. System Architecture (`01-system-architecture.puml`)
- **Purpose**: High-level system architecture overview
- **Content**: Shows the main components, packages, and their relationships
- **Key Elements**: REST Controllers, Camel Routes, Spring Boot Infrastructure, External APIs

### 02. Person Data Sequence (`02-sequence-person-data.puml`)
- **Purpose**: Detailed sequence diagram for person data API integration
- **Content**: Step-by-step flow of data retrieval and transformation
- **Key Elements**: Client interaction, route processing, data filtering

### 03. Camel Routes Flow (`03-camel-routes-flow.puml`)
- **Purpose**: Apache Camel routes and their data flow patterns
- **Content**: Timer routes, manual routes, processing pipelines
- **Key Elements**: Route triggers, processors, external API calls

### 04. Component Diagram (`04-component-diagram.puml`)
- **Purpose**: Detailed component relationships and dependencies
- **Content**: Spring Boot components, Camel integration layer, interfaces
- **Key Elements**: Component responsibilities, relationships, interfaces

### 05. Data Transformation (`05-data-transformation.puml`)
- **Purpose**: Data transformation flows and processors
- **Content**: Input/output data structures, transformation logic
- **Key Elements**: JSON parsing, field filtering, data enrichment

### 06. API Endpoints (`06-api-endpoints.puml`)
- **Purpose**: REST API endpoints documentation
- **Content**: All available endpoints with methods and responses
- **Key Elements**: Health APIs, Camel management APIs, Actuator endpoints

## How to Generate Diagrams

### Prerequisites
Install PlantUML:
```bash
# Using Homebrew (macOS)
brew install plantuml

# Using npm
npm install -g plantuml

# Using Docker
docker pull plantuml/plantuml-server
```

### Generate Individual Diagrams
```bash
# Generate PNG images
plantuml -tpng docs/plantuml/01-system-architecture.puml
plantuml -tpng docs/plantuml/02-sequence-person-data.puml
plantuml -tpng docs/plantuml/03-camel-routes-flow.puml
plantuml -tpng docs/plantuml/04-component-diagram.puml
plantuml -tpng docs/plantuml/05-data-transformation.puml
plantuml -tpng docs/plantuml/06-api-endpoints.puml

# Generate SVG images (scalable)
plantuml -tsvg docs/plantuml/*.puml

# Generate PDF
plantuml -tpdf docs/plantuml/*.puml
```

### Generate All Diagrams at Once
```bash
# Navigate to project root
cd /Users/matthieudebray/dev/java/camel

# Generate all diagrams as PNG
plantuml -tpng docs/plantuml/*.puml

# Generate all diagrams as SVG
plantuml -tsvg docs/plantuml/*.puml
```

### Using the Generation Script
We provide a convenient script to generate all diagrams:
```bash
chmod +x docs/generate-diagrams.sh
./docs/generate-diagrams.sh
```

## Integration with Documentation

### In Markdown Documentation
```markdown
## Architecture Overview
![System Architecture](docs/plantuml/01-system-architecture.png)

## API Flow
![Person Data Sequence](docs/plantuml/02-sequence-person-data.png)
```

### In Project Wiki
Upload the generated images to your project wiki or documentation system.

### In README.md
Include key diagrams in your main README file for quick reference.

## Diagram Maintenance

### When to Update Diagrams
- **Architecture changes**: New components, removed components, relationship changes
- **New APIs**: Additional endpoints, modified request/response structures
- **Route modifications**: New Camel routes, processor changes, flow updates
- **External integrations**: New external APIs, changed data structures

### Best Practices
1. **Keep diagrams in sync** with code changes
2. **Use consistent naming** across all diagrams
3. **Add explanatory notes** for complex logic
4. **Version control** all .puml files
5. **Generate images** as part of CI/CD pipeline

## File Structure
```
docs/
├── plantuml/
│   ├── 01-system-architecture.puml
│   ├── 02-sequence-person-data.puml
│   ├── 03-camel-routes-flow.puml
│   ├── 04-component-diagram.puml
│   ├── 05-data-transformation.puml
│   ├── 06-api-endpoints.puml
│   ├── README.md (this file)
│   └── generated/
│       ├── *.png
│       ├── *.svg
│       └── *.pdf
├── generate-diagrams.sh
└── README.md
```

## PlantUML Themes and Styling

The diagrams use the `!theme plain` directive for consistent styling. You can customize themes by:

1. **Changing theme**: Replace `!theme plain` with other available themes
2. **Custom colors**: Add color definitions in diagrams
3. **Custom styling**: Use PlantUML skinparam directives

## Online Editing
You can edit and preview PlantUML diagrams online at:
- [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)
- [PlantText](https://www.planttext.com/)

## Resources
- [PlantUML Official Documentation](https://plantuml.com/)
- [PlantUML Language Reference](https://plantuml.com/guide)
- [Sequence Diagrams](https://plantuml.com/sequence-diagram)
- [Component Diagrams](https://plantuml.com/component-diagram)
