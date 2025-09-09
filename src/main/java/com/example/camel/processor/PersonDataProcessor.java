package com.example.camel.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Processor to filter person data - extracting only specific fields
 */
@Component("personDataProcessor")
public class PersonDataProcessor implements Processor {
    
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
