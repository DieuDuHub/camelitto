package com.example.camel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/alive")
    public ResponseEntity<Map<String, Object>> alive() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "alive");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("application", "camel-springboot-app");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Vous pouvez ajouter ici des vérifications de santé plus complexes
        Map<String, String> components = new HashMap<>();
        components.put("application", "UP");
        components.put("database", "UP"); // Exemple - à adapter selon vos besoins
        
        response.put("components", components);
        
        return ResponseEntity.ok(response);
    }
}
