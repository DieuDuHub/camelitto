package com.example.camel.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.camel.logging.RequestLoggingInterceptor;
import com.example.camel.logging.ResponseLoggingInterceptor;

/**
 * Configuration class for Camel routes
 */
@Component
public class CamelRouteConfig extends RouteBuilder {

    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;
    
    @Autowired
    private ResponseLoggingInterceptor responseLoggingInterceptor;

    @Override
    public void configure() throws Exception {
        
        // Global interceptors for all routes
        interceptFrom()
            .process(requestLoggingInterceptor);
            
        interceptSendToEndpoint("http*")
            .process(responseLoggingInterceptor);
        
        // Route to retrieve and filter person data (JSON/REST)
        from("direct:personData")
            .routeId("person-data-route")
            .log("Retrieving JSON person data with ID: ${body}")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD("{{person.api.base-url}}/person_data/${body}")
            .process("personDataProcessor")
            .process(responseLoggingInterceptor)
            .log("Filtered JSON person data: ${body}");
        
        // Route to retrieve employee data via SOAP/XML
        from("direct:soapPersonData")
            .routeId("soap-person-data-route")
            .log("Retrieving SOAP employee data with ID: ${body}")
            .setHeader("CamelHttpMethod", constant("POST"))
            .setHeader("Content-Type", constant("application/soap+xml; charset=utf-8"))
            .setHeader("SOAPAction", constant("getEmployee"))
            .process("soapRequestProcessor")
            .toD("{{person.api.base-url}}/soap/PersonService")
            .process("soapResponseProcessor")
            .process(responseLoggingInterceptor)
            .log("Processed SOAP employee data: ${body}");
    }
}
