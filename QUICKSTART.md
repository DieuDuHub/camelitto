# 📋 Quick Start Guide

## 🎯 Essential Commands

### Application Startup
```bash
# Simple startup (recommended)
./run.sh start

# Development mode (detailed logs)
./run.sh dev

# Build then start
./run.sh build && ./run.sh start
```

### Application Testing
```bash
# Verify app is running
curl http://localhost:8080/api/alive

# Test Person Data integration (JSON)
curl http://localhost:8080/api/camel/person/1

# Test SOAP integration
curl "http://localhost:8080/api/camel/person/1?type=soap"

# Test with different IDs
curl http://localhost:8080/api/camel/person/42

# View Camel routes
curl http://localhost:8080/api/camel/routes

# Automatic health check
./run.sh check

# Test request logging system
./test-logging.sh
```

### Application Management
```bash
# Stop application
./run.sh stop

# Clean and rebuild
./run.sh clean

# Generate documentation
./run.sh docs

# Monitor request logs
tail -f logs/camel-requests.log
```

## 🔗 Main Endpoints

| Endpoint | Method | Description |
|----------|---------|-------------|
| `/api/alive` | GET | Liveness test |
| `/api/health` | GET | Detailed status |
| `/api/camel/person/{id}` | GET | Filtered personal data (JSON/SOAP) |
| `/api/camel/person/{id}?type=json` | GET | JSON personal data |
| `/api/camel/person/{id}?type=soap` | GET | SOAP employee data |
| `/api/camel/routes` | GET | Active routes list |

## 📊 Project Status

✅ **Spring Boot 3 Application** - Functional  
✅ **Apache Camel Integration** - 2 active routes  
✅ **JSON API** - Filters first_name, last_name, creation_date  
✅ **SOAP API** - Employee data with professional info  
✅ **Request Logging** - Complete request tracking system  
✅ **Comprehensive Tests** - Unit and integration tests  
✅ **PlantUML Documentation** - Architecture diagrams  
✅ **Executable JAR** - 32MB ready for deployment  
✅ **Startup Scripts** - Simple interface  

## 🚀 Technical Summary

- **Framework**: Spring Boot 3.2.0 + Apache Camel 4.2.0
- **Java**: Version 17+
- **Build**: Maven 3.6+
- **JAR Size**: ~32 MB
- **Port**: 8080 (configurable)
- **Camel Routes**: 2 routes (JSON + SOAP)
- **Logging**: File-based request tracking with rotation

## 📝 Request Logging Features

- **Complete Request Tracking**: Every API call logged with full details
- **Performance Metrics**: Execution time, response size, HTTP status
- **File Rotation**: Automatic log rotation (100MB files, 30 days retention)
- **Real-time Monitoring**: Live log monitoring capabilities
- **Structured Format**: Easy parsing and analysis

```bash
# Monitor requests in real-time
tail -f logs/camel-requests.log

# Analyze request patterns
grep "REQUEST_SUMMARY" logs/camel-requests.log

# Test logging system
./test-logging.sh
```

## 🎉 Project Completed!

The application is now fully functional with all requested features:

1. **Spring Boot 3 JAR** standard ✅
2. **HTTP REST connector** with alive API ✅  
3. **Apache Camel routes** for JSON transformation ✅
4. **Dual API support** JSON/REST + SOAP/XML ✅
5. **Request logging system** with comprehensive tracking ✅
5. **Documentation PlantUML** complète ✅

**Commande finale recommandée :**
```bash
./run.sh start
```

L'application sera accessible sur http://localhost:8080
