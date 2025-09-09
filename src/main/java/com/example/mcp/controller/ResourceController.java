package com.example.mcp.controller;

import com.example.mcp.config.WhiteLabelConfig;
import com.example.mcp.model.Resource;
import com.example.mcp.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
public class ResourceController {
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private WhiteLabelConfig whiteLabelConfig;
    

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllResources() {
        List<Resource> resources = resourceService.getAllResources();
        
        Map<String, Object> response = Map.of(
            "resources", resources,
            "total", resources.size(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/by-uri")
    public ResponseEntity<Map<String, Object>> getResourceByUri(@RequestParam String uri) {
        return resourceService.getResourceByUri(uri)
                .map(resource -> ResponseEntity.ok(Map.of(
                    "resource", resource,
                    "status", "success"
                )))
                .orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping("/by-mime-type")
    public ResponseEntity<Map<String, Object>> getResourcesByMimeType(@RequestParam String mimeType) {
        List<Resource> resources = resourceService.getResourcesByMimeType(mimeType);
        
        Map<String, Object> response = Map.of(
            "resources", resources,
            "mime_type", mimeType,
            "total", resources.size(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchResourcesByName(@RequestParam String name) {
        List<Resource> resources = resourceService.searchResourcesByName(name);
        
        Map<String, Object> response = Map.of(
            "resources", resources,
            "search_term", name,
            "total", resources.size(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkResourceExists(@RequestParam String uri) {
        boolean exists = resourceService.resourceExists(uri);
        
        Map<String, Object> response = Map.of(
            "uri", uri,
            "exists", exists,
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getResourceStats() {
        List<Resource> allResources = resourceService.getAllResources();
        

        Map<String, Long> mimeTypeCount = allResources.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    Resource::getMimeType,
                    java.util.stream.Collectors.counting()
                ));
        
        Map<String, Object> response = Map.of(
            "total_resources", allResources.size(),
            "mime_type_distribution", mimeTypeCount,
            "available_uris", allResources.stream()
                    .map(Resource::getUri)
                    .toList(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @PostMapping
    public ResponseEntity<Map<String, Object>> createResource(@RequestBody Resource resource) {
        try {
            Resource createdResource = resourceService.createResource(resource);
            
            Map<String, Object> response = Map.of(
                "resource", createdResource,
                "message", "Resource criado com sucesso",
                "status", "success"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "error", e.getMessage(),
                "status", "error"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/{uri}")
    public ResponseEntity<Map<String, Object>> updateResource(
            @PathVariable String uri, 
            @RequestBody Resource resource) {
        try {
            Resource updatedResource = resourceService.updateResource(uri, resource);
            
            Map<String, Object> response = Map.of(
                "resource", updatedResource,
                "message", "Resource atualizado com sucesso",
                "status", "success"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "error", e.getMessage(),
                "status", "error"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{uri}")
    public ResponseEntity<Map<String, Object>> deleteResource(@PathVariable String uri) {
        try {
            boolean deleted = resourceService.deleteResource(uri);
            
            if (deleted) {
                Map<String, Object> response = Map.of(
                    "message", "Resource deletado com sucesso",
                    "uri", uri,
                    "status", "success"
                );
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "error", e.getMessage(),
                "status", "error"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getResourceInfo() {
        Map<String, Object> info = Map.of(
            "api_name", "Resources API",
            "version", "1.0.0",
            "description", "API para gerenciamento de resources do MCP Server",
            "endpoints", Map.of(
                "GET /api/resources", "Lista todos os resources",
                "POST /api/resources", "Cria um novo resource",
                "PUT /api/resources/{uri}", "Atualiza um resource existente",
                "DELETE /api/resources/{uri}", "Deleta um resource",
                "GET /api/resources/by-uri", "Busca resource por URI",
                "GET /api/resources/by-mime-type", "Busca resources por tipo MIME",
                "GET /api/resources/search", "Busca resources por nome",
                "GET /api/resources/exists", "Verifica se resource existe",
                "GET /api/resources/stats", "Estatísticas dos resources",
                "GET /api/resources/info", "Informações da API"
            ),
            "white_label", Map.of(
                "application_name", whiteLabelConfig.getName(),
                "company_name", whiteLabelConfig.getCompany(),
                "support_email", whiteLabelConfig.getSupport().getEmail()
            )
        );
        
        return ResponseEntity.ok(info);
    }
}