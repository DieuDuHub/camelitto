package com.example.camel.controller;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/camel")
public class CamelController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @GetMapping("/person/{id}")
    public ResponseEntity<Map<String, Object>> getPersonData(
            @PathVariable String id,
            @RequestParam(value = "type", defaultValue = "json") String type) {
        try {
            String result;
            String routeName;
            String dataType;
            
            // Create a map to store request context for logging
            Map<String, Object> requestContext = new HashMap<>();
            requestContext.put("personId", id);
            requestContext.put("type", type);
            requestContext.put("endpoint", "/api/camel/person/" + id);
            requestContext.put("method", "GET");
            
            // Select route based on type parameter
            if ("xml".equalsIgnoreCase(type) || "soap".equalsIgnoreCase(type)) {
                // Trigger SOAP/XML route
                Map<String, Object> headers = new HashMap<>();
                headers.put("personId", id);
                headers.put("type", type);
                result = producerTemplate.requestBodyAndHeaders("direct:soapPersonData", id, headers, String.class);
                routeName = "soapPersonData";
                dataType = "XML/SOAP";
            } else {
                // Default to JSON route
                Map<String, Object> headers = new HashMap<>();
                headers.put("personId", id);
                headers.put("type", type);
                result = producerTemplate.requestBodyAndHeaders("direct:personData", id, headers, String.class);
                routeName = "personData";
                dataType = "JSON/REST";
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Person data retrieved successfully for ID: " + id + " using " + dataType + " API");
            response.put("data", result);
            response.put("route", routeName);
            response.put("dataType", dataType);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error retrieving data for ID " + id + ": " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/routes")
    public ResponseEntity<Map<String, Object>> getRoutes() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalRoutes", camelContext.getRoutes().size());
        response.put("routes", camelContext.getRoutes().stream()
            .map(route -> {
                Map<String, Object> routeInfo = new HashMap<>();
                routeInfo.put("id", route.getId());
                routeInfo.put("endpoint", route.getEndpoint().getEndpointUri());
                try {
                    routeInfo.put("status", camelContext.getRouteController().getRouteStatus(route.getId()).name());
                } catch (Exception e) {
                    routeInfo.put("status", "UNKNOWN");
                }
                return routeInfo;
            })
            .toList());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/routes/{routeId}/start")
    public ResponseEntity<Map<String, String>> startRoute(@PathVariable String routeId) {
        try {
            camelContext.getRouteController().startRoute(routeId);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Route " + routeId + " started successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error starting route: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/routes/{routeId}/stop")
    public ResponseEntity<Map<String, String>> stopRoute(@PathVariable String routeId) {
        try {
            camelContext.getRouteController().stopRoute(routeId);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Route " + routeId + " stopped successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error stopping route: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
