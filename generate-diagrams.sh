#!/bin/bash

# Generate PlantUML Diagrams Script
# This script generates all PlantUML diagrams in multiple formats

echo "🚀 Generating PlantUML diagrams for Spring Boot + Apache Camel project..."

# Create output directory
mkdir -p docs/plantuml/generated

# Check if PlantUML is installed
if ! command -v plantuml &> /dev/null; then
    echo "❌ PlantUML is not installed. Please install it first:"
    echo "   brew install plantuml (macOS)"
    echo "   npm install -g plantuml"
    exit 1
fi

# Generate PNG images
echo "📊 Generating PNG images..."
plantuml -tpng -o generated docs/plantuml/*.puml

# Generate SVG images
echo "🎨 Generating SVG images..."
plantuml -tsvg -o generated docs/plantuml/*.puml

# Generate PDF files
echo "📄 Generating PDF files..."
plantuml -tpdf -o generated docs/plantuml/*.puml

echo "✅ All diagrams generated successfully!"
echo "📁 Check the 'docs/plantuml/generated' directory for output files"

# List generated files
echo ""
echo "Generated files:"
ls -la docs/plantuml/generated/
