# PlantUML Documentation Generation Guide

This guide explains how to generate and view the PlantUML diagrams for the Camel Spring Boot application.

## Prerequisites

### Option 1: Online PlantUML Editor (Recommended for quick viewing)
- No installation required
- Visit: [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)

### Option 2: Local PlantUML Installation

#### macOS (with Homebrew)
```bash
brew install plantuml
brew install graphviz  # Required for some diagram types
```

#### Ubuntu/Debian
```bash
sudo apt-get update
sudo apt-get install plantuml graphviz
```

#### Windows
1. Download PlantUML JAR from [official website](https://plantuml.com/download)
2. Install Java if not already installed
3. Install Graphviz from [official website](https://graphviz.org/download/)

### Option 3: VS Code Extension
1. Install the "PlantUML" extension by jebbs
2. Install Java and Graphviz as dependencies
3. Open any `.puml` file and press `Alt+D` to preview

### Option 4: IntelliJ IDEA Plugin
1. Install "PlantUML integration" plugin
2. Right-click on `.puml` files to view diagrams

## Directory Structure

```
docs/
├── plantuml-documentation.md    # Main documentation with all diagrams
├── architecture.puml             # High-level architecture
├── components.puml               # Component diagram
├── camel-routes.puml            # Camel routes flow
├── sequences.puml               # Sequence diagrams
├── transformation.puml          # JSON transformation process
├── deployment.puml              # Deployment architecture
├── states.puml                  # Route state management
├── api-overview.puml            # API endpoints overview
└── README.md                    # This guide
```

## Generating Diagrams

### Local Generation (Command Line)

Generate all diagrams in PNG format:
```bash
cd docs/
plantuml *.puml
```

Generate specific diagram:
```bash
plantuml architecture.puml
```

Generate in different formats:
```bash
# SVG format (vector graphics)
plantuml -tsvg *.puml

# PDF format
plantuml -tpdf *.puml

# ASCII art
plantuml -ttxt *.puml
```

### Batch Generation Script

Create a script to generate all diagrams:

```bash
#!/bin/bash
# generate-diagrams.sh

echo "Generating PlantUML diagrams..."

cd docs/

# Generate PNG images
plantuml *.puml

# Generate SVG for web use
plantuml -tsvg *.puml

echo "Diagrams generated successfully!"
echo "PNG files: $(ls *.png | wc -l)"
echo "SVG files: $(ls *.svg | wc -l)"
```

Make it executable:
```bash
chmod +x generate-diagrams.sh
./generate-diagrams.sh
```

## VS Code Integration

### Setup
1. Install the PlantUML extension
2. Configure settings in `settings.json`:

```json
{
    "plantuml.server": "https://www.plantuml.com/plantuml",
    "plantuml.render": "PlantUMLServer",
    "plantuml.previewAutoUpdate": true
}
```

### Usage
- Open any `.puml` file
- Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac)
- Type "PlantUML: Preview Current Diagram"
- Or use the shortcut `Alt+D`

## Online Viewing

### Method 1: Direct URL
You can view diagrams online by encoding them. Use this format:
```
http://www.plantuml.com/plantuml/uml/[ENCODED_DIAGRAM]
```

### Method 2: GitHub Integration
GitHub automatically renders PlantUML diagrams in markdown files:

```markdown
![Architecture Diagram](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/YOUR_USERNAME/YOUR_REPO/main/docs/architecture.puml)
```

## Docker Integration

Run PlantUML in Docker:

```bash
# Pull the official PlantUML Docker image
docker pull plantuml/plantuml-server:jetty

# Run PlantUML server
docker run -d -p 8080:8080 plantuml/plantuml-server:jetty

# Generate diagrams via HTTP
curl -X POST \
  --data-binary @architecture.puml \
  http://localhost:8080/png/ \
  -o architecture.png
```

## Integration with Build Process

### Maven Integration

Add to `pom.xml`:

```xml
<plugin>
    <groupId>com.github.jeluard</groupId>
    <artifactId>plantuml-maven-plugin</artifactId>
    <version>1.2</version>
    <configuration>
        <sourceFiles>
            <directory>${basedir}/docs</directory>
            <includes>
                <include>**/*.puml</include>
            </includes>
        </sourceFiles>
        <outputDirectory>${basedir}/docs/images</outputDirectory>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Gradle Integration

Add to `build.gradle`:

```gradle
plugins {
    id 'com.cosminpolifronie.gradle.plantuml' version '1.6.0'
}

plantuml {
    sourceDir = file('docs')
    outputDir = file('docs/images')
}
```

## CI/CD Integration

### GitHub Actions

Create `.github/workflows/generate-docs.yml`:

```yaml
name: Generate PlantUML Diagrams

on:
  push:
    paths:
      - 'docs/*.puml'

jobs:
  generate:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    
    - name: Setup PlantUML
      run: |
        sudo apt-get update
        sudo apt-get install -y plantuml
    
    - name: Generate diagrams
      run: |
        cd docs
        plantuml *.puml
        
    - name: Commit generated files
      run: |
        git config --local user.email "action@github.com"
        git config --local user.name "GitHub Action"
        git add docs/*.png
        git commit -m "Auto-generated PlantUML diagrams" || exit 0
        git push
```

## Troubleshooting

### Common Issues

1. **GraphViz not found**
   ```
   Error: Graphviz not found
   ```
   Solution: Install Graphviz (`brew install graphviz` or `apt-get install graphviz`)

2. **Java not found**
   ```
   Error: Java not found
   ```
   Solution: Install Java JDK 8 or higher

3. **Memory issues with large diagrams**
   ```bash
   plantuml -Xmx2048m large-diagram.puml
   ```

4. **Permission denied**
   ```bash
   chmod +x /usr/local/bin/plantuml
   ```

### Performance Tips

- Use `-Djava.awt.headless=true` for server environments
- Increase memory with `-Xmx` flag for large diagrams
- Use PlantUML server for batch processing
- Cache generated diagrams to avoid regeneration

## Best Practices

1. **File Organization**: Keep related diagrams in subdirectories
2. **Naming Convention**: Use descriptive names (e.g., `user-journey.puml`)
3. **Version Control**: Commit both `.puml` source and generated images
4. **Documentation**: Include diagram descriptions in markdown files
5. **Themes**: Use consistent themes across diagrams
6. **Comments**: Add comments to complex diagrams

## Example Workflow

1. Edit diagram source file (`.puml`)
2. Generate locally to verify
3. Commit both source and generated files
4. CI/CD automatically regenerates on push
5. Documentation site displays latest diagrams

This comprehensive setup ensures your PlantUML documentation stays current and accessible to all team members.
