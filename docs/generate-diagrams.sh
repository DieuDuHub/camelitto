#!/bin/bash

# PlantUML Diagram Generation Script
# This script generates all PlantUML diagrams in multiple formats

echo "🚀 Starting PlantUML diagram generation..."

# Check if PlantUML is installed
if ! command -v plantuml &> /dev/null; then
    echo "❌ PlantUML is not installed. Please install it first:"
    echo "   macOS: brew install plantuml"
    echo "   Ubuntu: sudo apt-get install plantuml"
    echo "   Or use the online editor: http://www.plantuml.com/plantuml/uml/"
    exit 1
fi

# Create output directories
mkdir -p images/png
mkdir -p images/svg
mkdir -p images/pdf

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

echo "📁 Working directory: $SCRIPT_DIR"

# Count PlantUML files
PUML_COUNT=$(ls *.puml 2>/dev/null | wc -l)
if [ $PUML_COUNT -eq 0 ]; then
    echo "❌ No .puml files found in current directory"
    exit 1
fi

echo "📊 Found $PUML_COUNT PlantUML files"

# Generate PNG images (default format)
echo "🖼️  Generating PNG images..."
plantuml -o images/png *.puml
PNG_COUNT=$(ls images/png/*.png 2>/dev/null | wc -l)
echo "   ✅ Generated $PNG_COUNT PNG files"

# Generate SVG images (vector format for web)
echo "🎨 Generating SVG images..."
plantuml -tsvg -o images/svg *.puml
SVG_COUNT=$(ls images/svg/*.svg 2>/dev/null | wc -l)
echo "   ✅ Generated $SVG_COUNT SVG files"

# Generate PDF images (for documentation)
echo "📄 Generating PDF files..."
plantuml -tpdf -o images/pdf *.puml
PDF_COUNT=$(ls images/pdf/*.pdf 2>/dev/null | wc -l)
echo "   ✅ Generated $PDF_COUNT PDF files"

# Generate file list
echo "📋 Generating file index..."
cat > images/index.md << EOF
# Generated PlantUML Diagrams

Generated on: $(date)

## Architecture Diagrams

### PNG Format (Raster Images)
$(ls images/png/*.png 2>/dev/null | sed 's/^/- /')

### SVG Format (Vector Images)
$(ls images/svg/*.svg 2>/dev/null | sed 's/^/- /')

### PDF Format (Print Ready)
$(ls images/pdf/*.pdf 2>/dev/null | sed 's/^/- /')

## Source Files
$(ls *.puml 2>/dev/null | sed 's/^/- /')

## Usage

### Viewing Diagrams
- **PNG**: Best for embedding in documentation
- **SVG**: Best for web pages (scalable)
- **PDF**: Best for printing and formal documents

### Regenerating
Run the generation script:
\`\`\`bash
./generate-diagrams.sh
\`\`\`

### Online Viewing
Visit: [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)
EOF

echo "📊 Summary:"
echo "   📁 PNG files: $PNG_COUNT"
echo "   📁 SVG files: $SVG_COUNT"
echo "   📁 PDF files: $PDF_COUNT"
echo "   📁 Total diagrams: $PUML_COUNT"

# Check for errors
if [ $PNG_COUNT -ne $PUML_COUNT ] || [ $SVG_COUNT -ne $PUML_COUNT ] || [ $PDF_COUNT -ne $PUML_COUNT ]; then
    echo "⚠️  Warning: Not all diagrams were generated successfully"
    echo "   Expected: $PUML_COUNT diagrams per format"
    echo "   Generated: PNG=$PNG_COUNT, SVG=$SVG_COUNT, PDF=$PDF_COUNT"
else
    echo "🎉 All diagrams generated successfully!"
fi

# Display file sizes
echo ""
echo "📏 File sizes:"
du -h images/png/* 2>/dev/null | head -5
if [ $PNG_COUNT -gt 5 ]; then
    echo "   ... and $(($PNG_COUNT - 5)) more files"
fi

echo ""
echo "✨ Generation complete! Check the 'images' directory for outputs."
echo "🌐 For online viewing, copy the .puml content to: http://www.plantuml.com/plantuml/uml/"
