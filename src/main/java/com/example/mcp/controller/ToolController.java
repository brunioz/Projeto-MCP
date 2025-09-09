package com.example.mcp.controller;

import com.example.mcp.config.WhiteLabelConfig;
import com.example.mcp.model.Tool;
import com.example.mcp.model.ToolExecution;
import com.example.mcp.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "*")
public class ToolController {
    
    @Autowired
    private ToolService toolService;
    
    @Autowired
    private WhiteLabelConfig whiteLabelConfig;
    

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTools() {
        List<Tool> tools = toolService.getAllTools();
        
        Map<String, Object> response = Map.of(
            "tools", tools,
            "total", tools.size(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getToolByName(@PathVariable String name) {
        return toolService.getToolByName(name)
                .map(tool -> ResponseEntity.ok(Map.of(
                    "tool", tool,
                    "status", "success"
                )))
                .orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping("/by-capability")
    public ResponseEntity<Map<String, Object>> getToolsByCapability(@RequestParam String capability) {
        List<Tool> tools = toolService.getToolsByCapability(capability);
        
        Map<String, Object> response = Map.of(
            "tools", tools,
            "capability", capability,
            "total", tools.size(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @PostMapping("/{name}/execute")
    public ResponseEntity<Map<String, Object>> executeTool(
            @PathVariable String name,
            @RequestBody Map<String, Object> arguments) {
        
        if (!toolService.toolExists(name)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            ToolExecution execution = toolService.executeTool(name, arguments);
            
            Map<String, Object> response = Map.of(
                "execution", execution,
                "status", "success"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "error", e.getMessage(),
                "tool_name", name,
                "arguments", arguments,
                "status", "error"
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    

    @GetMapping("/executions/{executionId}")
    public ResponseEntity<Map<String, Object>> getExecution(@PathVariable String executionId) {
        return toolService.getExecution(executionId)
                .map(execution -> ResponseEntity.ok(Map.of(
                    "execution", execution,
                    "status", "success"
                )))
                .orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping("/executions")
    public ResponseEntity<Map<String, Object>> getAllExecutions() {
        List<ToolExecution> executions = toolService.getAllExecutions();
        
        Map<String, Object> response = Map.of(
            "executions", executions,
            "total", executions.size(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/exists/{name}")
    public ResponseEntity<Map<String, Object>> checkToolExists(@PathVariable String name) {
        boolean exists = toolService.toolExists(name);
        
        Map<String, Object> response = Map.of(
            "tool_name", name,
            "exists", exists,
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getToolStats() {
        List<Tool> allTools = toolService.getAllTools();
        List<ToolExecution> allExecutions = toolService.getAllExecutions();
        

        Map<String, Long> executionsByStatus = allExecutions.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    ToolExecution::getStatus,
                    java.util.stream.Collectors.counting()
                ));
        

        Map<String, Long> executionsByTool = allExecutions.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    ToolExecution::getToolName,
                    java.util.stream.Collectors.counting()
                ));
        
        Map<String, Object> response = Map.of(
            "total_tools", allTools.size(),
            "total_executions", allExecutions.size(),
            "executions_by_status", executionsByStatus,
            "executions_by_tool", executionsByTool,
            "available_tools", allTools.stream()
                    .map(Tool::getName)
                    .toList(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
    

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTool(@RequestBody Tool tool) {
        try {
            Tool createdTool = toolService.createTool(tool);
            
            Map<String, Object> response = Map.of(
                "tool", createdTool,
                "message", "Tool criada com sucesso",
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
    
    @PutMapping("/{name}")
    public ResponseEntity<Map<String, Object>> updateTool(
            @PathVariable String name, 
            @RequestBody Tool tool) {
        try {
            Tool updatedTool = toolService.updateTool(name, tool);
            
            Map<String, Object> response = Map.of(
                "tool", updatedTool,
                "message", "Tool atualizada com sucesso",
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
    
    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, Object>> deleteTool(@PathVariable String name) {
        try {
            boolean deleted = toolService.deleteTool(name);
            
            if (deleted) {
                Map<String, Object> response = Map.of(
                    "message", "Tool deletada com sucesso",
                    "name", name,
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
    public ResponseEntity<Map<String, Object>> getToolInfo() {
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /api/tools", "Lista todas as tools");
        endpoints.put("POST /api/tools", "Cria uma nova tool");
        endpoints.put("PUT /api/tools/{name}", "Atualiza uma tool existente");
        endpoints.put("DELETE /api/tools/{name}", "Deleta uma tool");
        endpoints.put("GET /api/tools/{name}", "Busca tool por nome");
        endpoints.put("GET /api/tools/by-capability", "Busca tools por capacidade");
        endpoints.put("POST /api/tools/{name}/execute", "Executa uma tool");
        endpoints.put("GET /api/tools/executions/{id}", "Busca execução por ID");
        endpoints.put("GET /api/tools/executions", "Lista todas as execuções");
        endpoints.put("GET /api/tools/exists/{name}", "Verifica se tool existe");
        endpoints.put("GET /api/tools/stats", "Estatísticas das tools");
        endpoints.put("GET /api/tools/info", "Informações da API");
        
        Map<String, Object> response = Map.of(
            "api_version", "1.0",
            "description", "API para gerenciamento e execução de Tools do " + whiteLabelConfig.getName(),
            "application", Map.of(
                "name", whiteLabelConfig.getName(),
                "version", whiteLabelConfig.getVersion(),
                "company", whiteLabelConfig.getCompany()
            ),
            "endpoints", endpoints,
            "available_tools", toolService.getAllTools().stream()
                    .map(tool -> Map.of(
                        "name", tool.getName(),
                        "description", tool.getDescription(),
                        "capabilities", tool.getCapabilities()
                    ))
                    .toList(),
            "status", "success"
        );
        
        return ResponseEntity.ok(response);
    }
}