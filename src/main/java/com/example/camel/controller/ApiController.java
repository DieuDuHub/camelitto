package com.example.camel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bienvenue dans l'application Camel Spring Boot !");
        response.put("description", "API REST avec Spring Boot 3");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "camel-springboot-app");
        response.put("version", "1.0.0");
        response.put("framework", "Spring Boot 3.2.0");
        response.put("java", "17");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("received", payload);
        response.put("message", "Echo successful");
        
        return ResponseEntity.ok(response);
    }
}
