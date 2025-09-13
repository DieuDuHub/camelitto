package com.example.camel.logging;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Camel interceptor for logging all requests with detailed information
 */
@Component
public class RequestLoggingInterceptor implements Processor {

    private static final Logger requestLogger = LoggerFactory.getLogger("REQUEST_LOGGER");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void process(Exchange exchange) throws Exception {
        String timestamp = LocalDateTime.now().format(formatter);
        String requestId = exchange.getExchangeId();
        
        // Extract request information
        String httpMethod = exchange.getIn().getHeader("CamelHttpMethod", String.class);
        String requestUri = exchange.getIn().getHeader("CamelHttpUri", String.class);
        String queryParams = exchange.getIn().getHeader("CamelHttpQuery", String.class);
        String pathInfo = exchange.getIn().getHeader("CamelHttpPath", String.class);
        
        // Store start time for execution duration calculation
        exchange.setProperty("REQUEST_START_TIME", System.currentTimeMillis());
        exchange.setProperty("REQUEST_TIMESTAMP", timestamp);
        exchange.setProperty("REQUEST_ID", requestId);
        
        // Build URL with parameters
        StringBuilder fullUrl = new StringBuilder();
        if (requestUri != null) {
            fullUrl.append(requestUri);
        }
        if (pathInfo != null) {
            fullUrl.append(pathInfo);
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            fullUrl.append("?").append(queryParams);
        }
        
        // Extract custom parameters from headers
        String personId = exchange.getIn().getHeader("personId", String.class);
        String apiType = exchange.getIn().getHeader("type", String.class);
        
        StringBuilder paramDetails = new StringBuilder();
        if (personId != null) {
            paramDetails.append("personId=").append(personId);
        }
        if (apiType != null) {
            if (paramDetails.length() > 0) paramDetails.append(", ");
            paramDetails.append("type=").append(apiType);
        }
        
        // Log request start
        requestLogger.info("REQUEST_START | {} | {} | {} {} | Parameters: {} | RequestId: {}", 
            timestamp, 
            httpMethod != null ? httpMethod : "INTERNAL",
            fullUrl.toString(),
            "",
            paramDetails.toString(),
            requestId
        );
        
        exchange.setProperty("FULL_URL", fullUrl.toString());
        exchange.setProperty("PARAM_DETAILS", paramDetails.toString());
    }
}
