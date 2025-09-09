package com.example.camel.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Configuration class for Camel routes
 */
@Component
public class CamelRouteConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        // Route to retrieve and filter person data (JSON/REST)
        from("direct:personData")
            .routeId("person-data-route")
            .log("Retrieving JSON person data with ID: ${body}")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD("{{person.api.base-url}}/person_data/${body}")
            .process("personDataProcessor")
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
            .log("Processed SOAP employee data: ${body}");
    }
}
