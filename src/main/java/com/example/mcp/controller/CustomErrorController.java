package com.example.mcp.controller;

import com.example.mcp.config.WhiteLabelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {
    
    @Autowired
    private WhiteLabelConfig whiteLabelConfig;
    
    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal Server Error";
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            httpStatus = HttpStatus.valueOf(statusCode);
            
            switch (statusCode) {
                case 404:
                    message = "The requested resource was not found";
                    break;
                case 403:
                    message = "Access to this resource is forbidden";
                    break;
                case 500:
                    message = "Internal server error occurred";
                    break;
                default:
                    message = httpStatus.getReasonPhrase();
            }
        }
        
        Map<String, Object> errorResponse = Map.of(
            "error", Map.of(
                "status", httpStatus.value(),
                "error", httpStatus.getReasonPhrase(),
                "message", message,
                "path", requestUri != null ? requestUri.toString() : "unknown",
                "timestamp", LocalDateTime.now()
            ),
            "application", Map.of(
                "name", whiteLabelConfig.getName(),
                "version", whiteLabelConfig.getVersion(),
                "company", whiteLabelConfig.getCompany()
            ),
            "support", Map.of(
                "email", whiteLabelConfig.getSupport().getEmail(),
                "website", whiteLabelConfig.getWebsite()
            ),
            "available_endpoints", Map.of(
                "resources", "/api/resources",
                "tools", "/api/tools",
                "branding", "/api/branding/info",
                "health", "/actuator/health"
            )
        );
        
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}