#!/bin/bash

# Spring Boot + Apache Camel Application Startup Script
# Description: Convenient script to start the application with various options

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Project configuration
PROJECT_NAME="Camel Spring Boot Application"
JAR_FILE="target/camel-springboot-app-1.0.0.jar"
DEFAULT_PORT=8080

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show help
show_help() {
    echo -e "${BLUE}${PROJECT_NAME} - Startup Script${NC}"
    echo ""
    echo "Usage: $0 [OPTION]"
    echo ""
    echo "Options:"
    echo "  start           Start the application with default settings"
    echo "  dev             Start in development mode"
    echo "  test            Start in test mode"
    echo "  build           Build the project"
    echo "  clean           Clean and build the project"
    echo "  docs            Generate PlantUML documentation"
    echo "  check           Health check for running application"
    echo "  stop            Stop running application (if found)"
    echo "  help            Show this help message"
    echo ""
    echo "Environment Variables:"
    echo "  SERVER_PORT     Override default port (default: ${DEFAULT_PORT})"
    echo "  SPRING_PROFILES Override active profiles"
    echo ""
    echo "Examples:"
    echo "  $0 start                    # Start with default settings"
    echo "  SERVER_PORT=8081 $0 start   # Start on port 8081"
    echo "  $0 dev                      # Start in development mode"
    echo "  $0 build && $0 start        # Build and start"
}

# Function to check if Java is available
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        print_error "Please install Java 17 or higher"
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [[ "$java_version" -lt 17 ]]; then
        print_warning "Java version $java_version detected. Java 17+ recommended."
    else
        print_status "Java version $java_version detected"
    fi
}

# Function to check if Maven is available
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        print_error "Please install Apache Maven"
        exit 1
    fi
    print_status "Maven is available"
}

# Function to build the project
build_project() {
    print_status "Building project..."
    check_maven
    
    if mvn clean package -DskipTests; then
        print_success "Project built successfully"
        print_status "JAR file created: $JAR_FILE"
    else
        print_error "Build failed"
        exit 1
    fi
}

# Function to clean and build
clean_build() {
    print_status "Cleaning and building project..."
    check_maven
    
    if mvn clean compile test package; then
        print_success "Clean build completed successfully"
        print_status "JAR file: $JAR_FILE"
    else
        print_error "Clean build failed"
        exit 1
    fi
}

# Function to generate documentation
generate_docs() {
    print_status "Generating PlantUML documentation..."
    
    if [ -f "generate-diagrams.sh" ]; then
        chmod +x generate-diagrams.sh
        if ./generate-diagrams.sh; then
            print_success "Documentation generated successfully"
            print_status "Check docs/ folder for generated diagrams"
        else
            print_warning "Documentation generation completed with warnings"
        fi
    else
        print_error "generate-diagrams.sh not found"
        exit 1
    fi
}

# Function to check if JAR exists
check_jar() {
    if [ ! -f "$JAR_FILE" ]; then
        print_warning "JAR file not found: $JAR_FILE"
        print_status "Building project first..."
        build_project
    fi
}

# Function to start the application
start_application() {
    local profile=${1:-"default"}
    local port=${SERVER_PORT:-$DEFAULT_PORT}
    
    print_status "Starting $PROJECT_NAME..."
    print_status "Profile: $profile"
    print_status "Port: $port"
    
    check_java
    check_jar
    
    # Prepare JVM arguments
    JVM_ARGS="-Xmx512m -Xms256m"
    
    # Prepare Spring arguments
    SPRING_ARGS="--server.port=$port"
    
    if [ "$profile" != "default" ]; then
        SPRING_ARGS="$SPRING_ARGS --spring.profiles.active=$profile"
    fi
    
    if [ -n "$SPRING_PROFILES" ]; then
        SPRING_ARGS="$SPRING_ARGS --spring.profiles.active=$SPRING_PROFILES"
    fi
    
    print_status "Starting application with: java $JVM_ARGS -jar $JAR_FILE $SPRING_ARGS"
    
    # Start the application
    java $JVM_ARGS -jar "$JAR_FILE" $SPRING_ARGS
}

# Function to perform health check
health_check() {
    local port=${SERVER_PORT:-$DEFAULT_PORT}
    local url="http://localhost:$port/api/alive"
    
    print_status "Performing health check on $url"
    
    if command -v curl &> /dev/null; then
        response=$(curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null)
        if [ "$response" = "200" ]; then
            print_success "Application is running and healthy"
            print_status "Testing Camel routes endpoint..."
            curl -s "http://localhost:$port/api/camel/routes" | head -3
        else
            print_error "Application health check failed (HTTP $response)"
            print_error "Is the application running on port $port?"
        fi
    else
        print_warning "curl not available for health check"
        print_status "Try: curl http://localhost:$port/api/alive"
    fi
}

# Function to stop the application
stop_application() {
    print_status "Looking for running application processes..."
    
    pids=$(ps aux | grep "camel-springboot-app" | grep -v grep | awk '{print $2}')
    
    if [ -z "$pids" ]; then
        print_warning "No running application found"
    else
        for pid in $pids; do
            print_status "Stopping process $pid"
            kill "$pid"
            sleep 2
            if kill -0 "$pid" 2>/dev/null; then
                print_warning "Force killing process $pid"
                kill -9 "$pid"
            fi
            print_success "Process $pid stopped"
        done
    fi
}

# Main script logic
case "${1:-start}" in
    start)
        start_application
        ;;
    dev)
        start_application "dev"
        ;;
    test)
        start_application "test"
        ;;
    build)
        build_project
        ;;
    clean)
        clean_build
        ;;
    docs)
        generate_docs
        ;;
    check)
        health_check
        ;;
    stop)
        stop_application
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        print_error "Unknown option: $1"
        show_help
        exit 1
        ;;
esac
