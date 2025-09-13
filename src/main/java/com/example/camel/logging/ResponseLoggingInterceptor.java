package com.example.camel.logging;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Camel interceptor for logging response information and execution time
 */
@Component
public class ResponseLoggingInterceptor implements Processor {

    private static final Logger requestLogger = LoggerFactory.getLogger("REQUEST_LOGGER");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void process(Exchange exchange) throws Exception {
        String endTimestamp = LocalDateTime.now().format(formatter);
        
        // Retrieve stored request information
        String requestId = exchange.getProperty("REQUEST_ID", String.class);
        String startTimestamp = exchange.getProperty("REQUEST_TIMESTAMP", String.class);
        Long startTime = exchange.getProperty("REQUEST_START_TIME", Long.class);
        String fullUrl = exchange.getProperty("FULL_URL", String.class);
        String paramDetails = exchange.getProperty("PARAM_DETAILS", String.class);
        
        // Calculate execution time
        long executionTime = 0;
        if (startTime != null) {
            executionTime = System.currentTimeMillis() - startTime;
        }
        
        // Get response information
        Integer httpResponseCode = exchange.getIn().getHeader("CamelHttpResponseCode", Integer.class);
        if (httpResponseCode == null) {
            // For internal routes, assume 200 if no error
            httpResponseCode = exchange.getException() != null ? 500 : 200;
        }
        
        // Get response size (approximate)
        String responseBody = exchange.getIn().getBody(String.class);
        int responseSize = responseBody != null ? responseBody.length() : 0;
        
        // Determine success/failure status
        String status = httpResponseCode >= 200 && httpResponseCode < 300 ? "SUCCESS" : "ERROR";
        if (exchange.getException() != null) {
            status = "EXCEPTION";
        }
        
        // Log response end with all details
        requestLogger.info("REQUEST_END | {} | {} | {} | HTTP {} | {}ms | {}B | {} | RequestId: {}", 
            endTimestamp,
            status,
            fullUrl != null ? fullUrl : "UNKNOWN",
            httpResponseCode,
            executionTime,
            responseSize,
            paramDetails != null ? paramDetails : "",
            requestId != null ? requestId : "UNKNOWN"
        );
        
        // Also log in a more structured format for parsing
        requestLogger.info("REQUEST_SUMMARY | StartTime: {} | EndTime: {} | URL: {} | Params: {} | HTTPCode: {} | Duration: {}ms | Size: {}B | Status: {} | RequestId: {}",
            startTimestamp,
            endTimestamp,
            fullUrl,
            paramDetails,
            httpResponseCode,
            executionTime,
            responseSize,
            status,
            requestId
        );
    }
}
