package com.example.camel.route;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JsonTransformRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        // Route that calls external API and transforms data
        from("timer://jsonFetcher?period=30000") // Executes every 30 seconds
            .routeId("json-transform-route")
            .log("Starting JSON data retrieval...")
            
            // Call external API (example with JSONPlaceholder)
            .to("https://jsonplaceholder.typicode.com/posts/1")
            .log("Data retrieved: ${body}")
            
            // Transform JSON data
            .process(new JsonTransformProcessor())
            .log("Data transformed: ${body}")
            
            // Send to direct endpoint for further processing
            .to("direct:processTransformedData");

        // Route to process transformed data
        from("direct:processTransformedData")
            .routeId("process-transformed-data")
            .log("Processing transformed data...")
            
            // Here you can add other transformations or storage
            .process(exchange -> {
                String transformedData = exchange.getIn().getBody(String.class);
                // Example: store in database, send to another system, etc.
                exchange.getIn().setHeader("ProcessedAt", System.currentTimeMillis());
                exchange.getIn().setHeader("DataLength", transformedData.length());
            })
            .log("Final data: ${body}")
            .log("Header ProcessedAt: ${header.ProcessedAt}");

        // REST route to manually trigger transformation
        from("direct:manualTransform")
            .routeId("manual-transform-route")
            .log("Manual transformation triggered...")
            .to("https://jsonplaceholder.typicode.com/posts/2")
            .process(new JsonTransformProcessor())
            .log("Manual transformation completed: ${body}");

        // Route to retrieve and filter person data with dynamic parameter
        from("direct:personData")
            .routeId("person-data-route")
            .log("Retrieving person data with ID: ${body}")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD("{{person.api.base-url}}/person_data/${body}")
            .process(new PersonDataProcessor())
            .log("Filtered person data: ${body}");
    }

    /**
     * Processor to transform JSON data
     */
    public static class JsonTransformProcessor implements Processor {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void process(Exchange exchange) throws Exception {
            String jsonInput = exchange.getIn().getBody(String.class);
            
            // Parse input JSON
            JsonNode inputNode = objectMapper.readTree(jsonInput);
            
            // Create new transformed object
            Map<String, Object> transformedData = new HashMap<>();
            transformedData.put("originalId", inputNode.get("id").asInt());
            transformedData.put("transformedTitle", "TRANSFORMED: " + inputNode.get("title").asText().toUpperCase());
            transformedData.put("summary", inputNode.get("body").asText().substring(0, Math.min(50, inputNode.get("body").asText().length())) + "...");
            transformedData.put("userId", inputNode.get("userId").asInt());
            transformedData.put("transformationTimestamp", System.currentTimeMillis());
            transformedData.put("source", "external-api");
            
            // Convert to JSON and update body
            String transformedJson = objectMapper.writeValueAsString(transformedData);
            exchange.getIn().setBody(transformedJson);
            exchange.getIn().setHeader("Content-Type", "application/json");
        }
    }

    /**
     * Processor to filter person data
     */
    public static class PersonDataProcessor implements Processor {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void process(Exchange exchange) throws Exception {
            String jsonInput = exchange.getIn().getBody(String.class);
            
            // Parse input JSON
            JsonNode inputNode = objectMapper.readTree(jsonInput);
            
            // Extract "Ok" object from response
            JsonNode okNode = inputNode.get("Ok");
            if (okNode == null) {
                throw new IllegalArgumentException("Invalid response format: 'Ok' field not found");
            }
            
            // Create filtered object with only requested fields
            Map<String, Object> filteredData = new HashMap<>();
            filteredData.put("first_name", okNode.get("first_name").asText());
            filteredData.put("last_name", okNode.get("last_name").asText());
            filteredData.put("creation_date", okNode.get("created_at").asText());
            
            // Convert to JSON and update body
            String filteredJson = objectMapper.writeValueAsString(filteredData);
            exchange.getIn().setBody(filteredJson);
            exchange.getIn().setHeader("Content-Type", "application/json");
        }
    }
}
