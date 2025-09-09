package com.example.mcp.service;

import com.example.mcp.model.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResourceService {
    
    private final Map<String, Resource> resources = new ConcurrentHashMap<>();
    
    public ResourceService() {
        initializeFixedResources();
    }
    
    private void initializeFixedResources() {

        Resource textDoc = new Resource(
            "mcp://resources/documents/readme",
            "README Document",
            "Documento principal do projeto MCP",
            "text/plain",
            "Este é um servidor MCP (Model Context Protocol) implementado em Java com Spring Boot.\n" +
            "Ele fornece Resources e Tools com valores fixos para demonstração.\n" +
            "\nFuncionalidades:\n" +
            "- Gerenciamento de Resources\n" +
            "- Execução de Tools\n" +
            "- API REST completa"
        );
        
        Map<String, Object> textMetadata = new HashMap<>();
        textMetadata.put("author", "MCP Server");
        textMetadata.put("version", "1.0");
        textMetadata.put("language", "pt-BR");
        textDoc.setMetadata(textMetadata);
        
        resources.put(textDoc.getUri(), textDoc);
        

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("users", Arrays.asList(
            Map.of("id", 1, "name", "João Silva", "email", "joao@example.com", "active", true),
            Map.of("id", 2, "name", "Maria Santos", "email", "maria@example.com", "active", true),
            Map.of("id", 3, "name", "Pedro Costa", "email", "pedro@example.com", "active", false)
        ));
        jsonData.put("total", 3);
        jsonData.put("active_count", 2);
        
        Resource jsonResource = new Resource(
            "mcp://resources/data/users",
            "Users Data",
            "Lista de usuários do sistema",
            "application/json",
            jsonData
        );
        
        Map<String, Object> jsonMetadata = new HashMap<>();
        jsonMetadata.put("source", "database");
        jsonMetadata.put("last_updated", "2024-01-15");
        jsonResource.setMetadata(jsonMetadata);
        
        resources.put(jsonResource.getUri(), jsonResource);
        

        Map<String, Object> configData = new HashMap<>();
        configData.put("server", Map.of(
            "port", 8080,
            "host", "localhost",
            "ssl_enabled", false
        ));
        configData.put("database", Map.of(
            "type", "h2",
            "url", "jdbc:h2:mem:testdb",
            "driver", "org.h2.Driver"
        ));
        configData.put("features", Map.of(
            "logging_enabled", true,
            "metrics_enabled", true,
            "debug_mode", false
        ));
        
        Resource configResource = new Resource(
            "mcp://resources/config/system",
            "System Configuration",
            "Configurações do sistema MCP",
            "application/json",
            configData
        );
        
        Map<String, Object> configMetadata = new HashMap<>();
        configMetadata.put("environment", "development");
        configMetadata.put("config_version", "1.2.0");
        configResource.setMetadata(configMetadata);
        
        resources.put(configResource.getUri(), configResource);
    }
    
    public List<Resource> getAllResources() {
        return new ArrayList<>(resources.values());
    }
    
    public Optional<Resource> getResourceByUri(String uri) {
        return Optional.ofNullable(resources.get(uri));
    }
    
    public List<Resource> getResourcesByMimeType(String mimeType) {
        return resources.values().stream()
                .filter(resource -> resource.getMimeType().equals(mimeType))
                .toList();
    }
    
    public List<Resource> searchResourcesByName(String namePattern) {
        return resources.values().stream()
                .filter(resource -> resource.getName().toLowerCase()
                        .contains(namePattern.toLowerCase()))
                .toList();
    }
    
    public boolean resourceExists(String uri) {
        return resources.containsKey(uri);
    }
    
    public int getResourceCount() {
        return resources.size();
    }
    
    public Resource createResource(Resource resource) {
        if (resource.getUri() == null || resource.getUri().trim().isEmpty()) {
            throw new IllegalArgumentException("URI do resource é obrigatória");
        }
        
        if (resources.containsKey(resource.getUri())) {
            throw new IllegalArgumentException("Resource com URI '" + resource.getUri() + "' já existe");
        }
        
        if (resource.getName() == null || resource.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do resource é obrigatório");
        }
        
        if (resource.getMimeType() == null || resource.getMimeType().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo MIME do resource é obrigatório");
        }
        
        if (resource.getContent() == null) {
            throw new IllegalArgumentException("Conteúdo do resource é obrigatório");
        }
        
        resources.put(resource.getUri(), resource);
        return resource;
    }
    
    public Resource updateResource(String uri, Resource updatedResource) {
        if (!resources.containsKey(uri)) {
            throw new IllegalArgumentException("Resource com URI '" + uri + "' não encontrado");
        }
        
        Resource existingResource = resources.get(uri);
        
        if (updatedResource.getName() != null && !updatedResource.getName().trim().isEmpty()) {
            existingResource.setName(updatedResource.getName());
        }
        
        if (updatedResource.getDescription() != null) {
            existingResource.setDescription(updatedResource.getDescription());
        }
        
        if (updatedResource.getMimeType() != null && !updatedResource.getMimeType().trim().isEmpty()) {
            existingResource.setMimeType(updatedResource.getMimeType());
        }
        
        if (updatedResource.getContent() != null) {
            existingResource.setContent(updatedResource.getContent());
        }
        
        if (updatedResource.getMetadata() != null) {
            existingResource.setMetadata(updatedResource.getMetadata());
        }
        
        return existingResource;
    }
    
    public boolean deleteResource(String uri) {
        return resources.remove(uri) != null;
    }
}