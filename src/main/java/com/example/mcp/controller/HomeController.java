package com.example.mcp.controller;

import com.example.mcp.config.WhiteLabelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {
    
    @Autowired
    private WhiteLabelConfig whiteLabelConfig;
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = Map.of(
            "welcome", "Welcome to " + whiteLabelConfig.getName(),
            "application", Map.of(
                "name", whiteLabelConfig.getName(),
                "description", whiteLabelConfig.getDescription(),
                "version", whiteLabelConfig.getVersion(),
                "company", whiteLabelConfig.getCompany(),
                "website", whiteLabelConfig.getWebsite()
            ),
            "api_endpoints", Map.of(
                "resources", Map.of(
                    "base_url", "/api/resources",
                    "description", "Manage and access resources",
                    "methods", "GET"
                ),
                "tools", Map.of(
                    "base_url", "/api/tools",
                    "description", "Execute and manage tools",
                    "methods", "GET, POST"
                ),
                "branding", Map.of(
                    "base_url", "/api/branding",
                    "description", "Get branding and theme information",
                    "methods", "GET"
                )
            ),
            "documentation", Map.of(
                "health_check", "/actuator/health",
                "application_info", "/actuator/info",
                "api_metrics", "/actuator/metrics"
            ),
            "support", Map.of(
                "email", whiteLabelConfig.getSupport().getEmail(),
                "phone", whiteLabelConfig.getSupport().getPhone(),
                "website", whiteLabelConfig.getWebsite()
            ),
            "timestamp", LocalDateTime.now(),
            "status", "online"
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = Map.of(
            "status", "UP",
            "application", whiteLabelConfig.getName(),
            "version", whiteLabelConfig.getVersion(),
            "timestamp", LocalDateTime.now()
        );
        
        return ResponseEntity.ok(response);
    }
}