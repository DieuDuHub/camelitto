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

    @PostMapping("/transform")
    public ResponseEntity<Map<String, Object>> triggerTransform() {
        try {
            // Déclenche la route de transformation manuelle
            String result = producerTemplate.requestBody("direct:manualTransform", "", String.class);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Transformation déclenchée avec succès");
            response.put("transformedData", result);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erreur lors de la transformation: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Map<String, Object>> getPersonData(@PathVariable String id) {
        try {
            // Déclenche la route de récupération des données de personne avec l'ID
            String result = producerTemplate.requestBody("direct:personData", id, String.class);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Données de personne récupérées avec succès pour l'ID: " + id);
            response.put("data", result);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erreur lors de la récupération des données pour l'ID " + id + ": " + e.getMessage());
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
            response.put("message", "Route " + routeId + " démarrée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erreur lors du démarrage de la route: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/routes/{routeId}/stop")
    public ResponseEntity<Map<String, String>> stopRoute(@PathVariable String routeId) {
        try {
            camelContext.getRouteController().stopRoute(routeId);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Route " + routeId + " arrêtée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erreur lors de l'arrêt de la route: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
