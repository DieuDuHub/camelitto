# 📝 Request Logging System Documentation

## Overview

The Camel Spring Boot application includes a comprehensive request logging system that tracks every API request with detailed metrics and performance data. This system provides full observability into the application's request processing lifecycle.

## Architecture

### Components

1. **RequestLoggingInterceptor** - Captures request start information
2. **ResponseLoggingInterceptor** - Captures response and execution metrics
3. **Logback Configuration** - Manages log files and rotation
4. **REQUEST_LOGGER** - Dedicated logger for request tracking

### Request Lifecycle Tracking

```
Client Request → RequestLoggingInterceptor → Camel Route → ResponseLoggingInterceptor → Client Response
       ↓                    ↓                               ↓                           ↓
   Start Time          Request Details              Response Details             End Time
```

## Log Format

### Request Start Entry
```
REQUEST_START | [timestamp] | [method] | [url] | Parameters: [params] | RequestId: [id]
```

### Request End Entry
```
REQUEST_END | [timestamp] | [status] | [url] | HTTP [code] | [duration]ms | [size]B | [params] | RequestId: [id]
```

### Request Summary Entry
```
REQUEST_SUMMARY | StartTime: [start] | EndTime: [end] | URL: [url] | Params: [params] | HTTPCode: [code] | Duration: [duration]ms | Size: [size]B | Status: [status] | RequestId: [id]
```

## Example Log Entries

```log
2025-09-13 15:29:13.850 | REQUEST_START | 2025-09-13 15:29:13.850 | INTERNAL |   | Parameters: personId=1, type=json | RequestId: 10C3ECB31CC3231-0000000000000000
2025-09-13 15:29:14.001 | REQUEST_END | 2025-09-13 15:29:14.001 | SUCCESS |  | HTTP 200 | 151ms | 89B | personId=1, type=json | RequestId: 10C3ECB31CC3231-0000000000000000
2025-09-13 15:29:14.002 | REQUEST_SUMMARY | StartTime: 2025-09-13 15:29:13.850 | EndTime: 2025-09-13 15:29:14.001 | URL:  | Params: personId=1, type=json | HTTPCode: 200 | Duration: 151ms | Size: 89B | Status: SUCCESS | RequestId: 10C3ECB31CC3231-0000000000000000
```

## Configuration

### Logback Configuration (`logback-spring.xml`)

```xml
<appender name="REQUEST_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>logs/camel-requests.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>logs/camel-requests.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxFileSize>100MB</maxFileSize>
        <maxHistory>30</maxHistory>
        <totalSizeCap>3GB</totalSizeCap>
    </rollingPolicy>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} | %msg%n</pattern>
    </encoder>
</appender>

<logger name="REQUEST_LOGGER" level="INFO" additivity="false">
    <appender-ref ref="REQUEST_FILE"/>
    <appender-ref ref="CONSOLE"/>
</logger>
```

### Key Configuration Options

- **File Size Limit**: 100MB per log file
- **Retention Period**: 30 days
- **Total Size Cap**: 3GB maximum storage
- **Log Level**: INFO (can be changed to DEBUG for more details)

## Tracked Metrics

### Request Information
- **Timestamp**: Request start and end times
- **URL**: Complete request URL with parameters
- **Method**: HTTP method (GET, POST, etc.)
- **Parameters**: Extracted request parameters (personId, type, etc.)
- **Request ID**: Unique identifier for request correlation

### Performance Metrics
- **Execution Time**: Total processing duration in milliseconds
- **Response Size**: Size of response body in bytes
- **HTTP Status Code**: Response status (200, 404, 500, etc.)
- **Success/Failure Status**: Overall request outcome

### Error Tracking
- **Exception Handling**: Automatic logging of exceptions
- **Error Status**: Distinguished error vs. success responses
- **Failure Details**: Detailed error information when available

## Monitoring and Analysis

### Real-time Monitoring
```bash
# Monitor all requests in real-time
tail -f logs/camel-requests.log

# Monitor only request summaries
tail -f logs/camel-requests.log | grep "REQUEST_SUMMARY"

# Monitor errors and exceptions
tail -f logs/camel-requests.log | grep -E "(ERROR|EXCEPTION)"
```

### Log Analysis Commands

```bash
# Count total requests
grep "REQUEST_START" logs/camel-requests.log | wc -l

# Filter by HTTP status code
grep "HTTP 200" logs/camel-requests.log
grep "HTTP 5[0-9][0-9]" logs/camel-requests.log  # Server errors

# Find slow requests (over 1000ms)
grep -E "Duration: [1-9][0-9]{3,}ms" logs/camel-requests.log

# Analyze by API type
grep "type=json" logs/camel-requests.log
grep "type=soap" logs/camel-requests.log

# Extract performance statistics
grep "REQUEST_SUMMARY" logs/camel-requests.log | grep -oE "Duration: [0-9]+ms" | sort -n

# Find requests by date range
grep "2025-09-13 15:29" logs/camel-requests.log
```

### Performance Analysis

```bash
# Average response time calculation
grep "Duration:" logs/camel-requests.log | \
  grep -oE "[0-9]+ms" | \
  sed 's/ms//' | \
  awk '{sum+=$1; count++} END {print "Average:", sum/count, "ms"}'

# Response size statistics
grep "Size: [0-9]*B" logs/camel-requests.log | \
  grep -oE "[0-9]+B" | \
  sed 's/B//' | \
  sort -n | \
  tail -10  # Top 10 largest responses
```

## Testing the Logging System

### Test Script
The project includes a comprehensive test script:

```bash
./test-logging.sh
```

This script:
1. Executes various API calls (JSON and SOAP)
2. Tests different parameter combinations
3. Generates error scenarios
4. Displays recent log entries
5. Provides analysis commands

### Manual Testing
```bash
# Test JSON API
curl "http://localhost:8080/api/camel/person/1?type=json"

# Test SOAP API
curl "http://localhost:8080/api/camel/person/2?type=soap"

# Test error scenarios
curl "http://localhost:8080/api/camel/person/invalid"

# Check logs immediately
tail -5 logs/camel-requests.log
```

## Integration with Camel Routes

### Route Configuration
The logging interceptors are integrated into Camel routes via:

```java
@Override
public void configure() throws Exception {
    // Global interceptors for all routes
    interceptFrom()
        .process(requestLoggingInterceptor);
        
    interceptSendToEndpoint("http*")
        .process(responseLoggingInterceptor);
    
    // Individual route logging
    from("direct:personData")
        .routeId("person-data-route")
        .process(responseLoggingInterceptor)
        // ... route processing
}
```

### Header Propagation
Important headers are captured and logged:
- `personId`: Extracted person identifier
- `type`: API type selection (json/soap)
- `CamelHttpMethod`: HTTP method used
- `CamelHttpResponseCode`: Response status code

## Troubleshooting

### Common Issues

1. **Log File Not Created**
   - Check `logs/` directory exists
   - Verify write permissions
   - Check logback configuration

2. **Missing Log Entries**
   - Verify REQUEST_LOGGER level is INFO or DEBUG
   - Check if interceptors are properly configured
   - Ensure requests are going through Camel routes

3. **Large Log Files**
   - Adjust `maxFileSize` in logback configuration
   - Reduce `maxHistory` for shorter retention
   - Monitor `totalSizeCap` setting

### Debug Mode
Enable debug logging for more detailed information:

```yaml
logging:
  level:
    REQUEST_LOGGER: DEBUG
    com.example.camel.logging: DEBUG
```

## Best Practices

### Production Deployment
- Set appropriate log levels (INFO for production)
- Monitor disk space usage
- Implement log aggregation (ELK stack, Splunk)
- Set up alerting for error rates
- Regular log file cleanup

### Development
- Use DEBUG level for detailed troubleshooting
- Correlate logs with application metrics
- Use request IDs for tracking specific requests
- Monitor performance trends over time

### Security Considerations
- Avoid logging sensitive data in parameters
- Implement log sanitization for PII
- Secure log file access
- Consider log encryption for sensitive environments

## Advanced Features

### Custom Metrics
The logging system can be extended to include:
- Business-specific metrics
- Custom correlation IDs
- Integration with monitoring systems
- Performance alerting thresholds

### Integration Examples
```java
// Custom metric logging
exchange.setProperty("BUSINESS_CONTEXT", "high-priority-customer");
exchange.setProperty("CUSTOM_METRIC", "processing-time-critical");
```

### Monitoring Integration
The logs can be integrated with:
- Prometheus/Grafana for metrics visualization
- ELK Stack for log aggregation and search
- APM tools for performance monitoring
- Custom dashboards for business metrics

---

This logging system provides comprehensive observability into the Camel application's request processing, enabling effective monitoring, troubleshooting, and performance analysis.
