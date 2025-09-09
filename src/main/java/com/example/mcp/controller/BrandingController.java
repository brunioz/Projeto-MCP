package com.example.mcp.controller;

import com.example.mcp.config.WhiteLabelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/branding")
@CrossOrigin(origins = "*")
public class BrandingController {
    
    @Autowired
    private WhiteLabelConfig whiteLabelConfig;
    
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getBrandingInfo() {
        Map<String, Object> response = Map.of(
            "application", Map.of(
                "name", whiteLabelConfig.getName(),
                "description", whiteLabelConfig.getDescription(),
                "version", whiteLabelConfig.getVersion(),
                "author", whiteLabelConfig.getAuthor(),
                "company", whiteLabelConfig.getCompany(),
                "website", whiteLabelConfig.getWebsite(),
                "copyright", whiteLabelConfig.getCopyright()
            ),
            "support", Map.of(
                "email", whiteLabelConfig.getSupport().getEmail(),
                "phone", whiteLabelConfig.getSupport().getPhone()
            ),
            "branding", Map.of(
                "logo_url", whiteLabelConfig.getLogo().getUrl(),
                "primary_color", whiteLabelConfig.getTheme().getPrimary().getColor(),
                "secondary_color", whiteLabelConfig.getTheme().getSecondary().getColor()
            ),
            "legal", Map.of(
                "terms_url", whiteLabelConfig.getTermsUrl(),
                "privacy_url", whiteLabelConfig.getPrivacyUrl()
            ),
            "timestamp", LocalDateTime.now(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/theme")
    public ResponseEntity<Map<String, Object>> getThemeInfo() {
        Map<String, Object> response = Map.of(
            "theme", Map.of(
                "primary_color", whiteLabelConfig.getTheme().getPrimary().getColor(),
                "secondary_color", whiteLabelConfig.getTheme().getSecondary().getColor(),
                "logo_url", whiteLabelConfig.getLogo().getUrl()
            ),
            "company", Map.of(
                "name", whiteLabelConfig.getCompany(),
                "website", whiteLabelConfig.getWebsite()
            ),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/contact")
    public ResponseEntity<Map<String, Object>> getContactInfo() {
        Map<String, Object> response = Map.of(
            "company", whiteLabelConfig.getCompany(),
            "website", whiteLabelConfig.getWebsite(),
            "support", Map.of(
                "email", whiteLabelConfig.getSupport().getEmail(),
                "phone", whiteLabelConfig.getSupport().getPhone()
            ),
            "legal", Map.of(
                "terms_url", whiteLabelConfig.getTermsUrl(),
                "privacy_url", whiteLabelConfig.getPrivacyUrl(),
                "copyright", whiteLabelConfig.getCopyright()
            ),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
}