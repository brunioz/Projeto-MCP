package com.example.mcp.service;

import com.example.mcp.model.Tool;
import com.example.mcp.model.ToolExecution;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ToolService {
    
    private final Map<String, Tool> tools = new ConcurrentHashMap<>();
    private final Map<String, ToolExecution> executions = new ConcurrentHashMap<>();
    
    public ToolService() {
        initializeFixedTools();
    }
    
    private void initializeFixedTools() {

        Map<String, Object> calcSchema = new HashMap<>();
        calcSchema.put("type", "object");
        calcSchema.put("properties", Map.of(
            "operation", Map.of(
                "type", "string",
                "enum", Arrays.asList("add", "subtract", "multiply", "divide"),
                "description", "Operação matemática a ser realizada"
            ),
            "a", Map.of(
                "type", "number",
                "description", "Primeiro número"
            ),
            "b", Map.of(
                "type", "number",
                "description", "Segundo número"
            )
        ));
        calcSchema.put("required", Arrays.asList("operation", "a", "b"));
        
        Tool calculator = new Tool(
            "calculator",
            "Calculadora básica para operações matemáticas",
            calcSchema
        );
        calculator.setCapabilities(Arrays.asList("math", "arithmetic", "calculation"));
        
        Map<String, Object> calcMetadata = new HashMap<>();
        calcMetadata.put("category", "math");
        calcMetadata.put("version", "1.0");
        calculator.setMetadata(calcMetadata);
        
        tools.put(calculator.getName(), calculator);
        

        Map<String, Object> textSchema = new HashMap<>();
        textSchema.put("type", "object");
        textSchema.put("properties", Map.of(
            "template", Map.of(
                "type", "string",
                "enum", Arrays.asList("greeting", "farewell", "email", "report"),
                "description", "Tipo de template de texto"
            ),
            "name", Map.of(
                "type", "string",
                "description", "Nome para personalizar o texto"
            ),
            "language", Map.of(
                "type", "string",
                "enum", Arrays.asList("pt", "en", "es"),
                "default", "pt",
                "description", "Idioma do texto gerado"
            )
        ));
        textSchema.put("required", Arrays.asList("template", "name"));
        
        Tool textGenerator = new Tool(
            "text_generator",
            "Gerador de textos baseado em templates",
            textSchema
        );
        textGenerator.setCapabilities(Arrays.asList("text", "template", "generation"));
        
        Map<String, Object> textMetadata = new HashMap<>();
        textMetadata.put("category", "text");
        textMetadata.put("version", "1.1");
        textGenerator.setMetadata(textMetadata);
        
        tools.put(textGenerator.getName(), textGenerator);
        

        Map<String, Object> validatorSchema = new HashMap<>();
        validatorSchema.put("type", "object");
        validatorSchema.put("properties", Map.of(
            "data_type", Map.of(
                "type", "string",
                "enum", Arrays.asList("email", "cpf", "phone", "url"),
                "description", "Tipo de dado a ser validado"
            ),
            "value", Map.of(
                "type", "string",
                "description", "Valor a ser validado"
            )
        ));
        validatorSchema.put("required", Arrays.asList("data_type", "value"));
        
        Tool validator = new Tool(
            "data_validator",
            "Validador de diferentes tipos de dados",
            validatorSchema
        );
        validator.setCapabilities(Arrays.asList("validation", "data", "format"));
        
        Map<String, Object> validatorMetadata = new HashMap<>();
        validatorMetadata.put("category", "validation");
        validatorMetadata.put("version", "2.0");
        validator.setMetadata(validatorMetadata);
        
        tools.put(validator.getName(), validator);
    }
    
    public List<Tool> getAllTools() {
        return new ArrayList<>(tools.values());
    }
    
    public Optional<Tool> getToolByName(String name) {
        return Optional.ofNullable(tools.get(name));
    }
    
    public List<Tool> getToolsByCapability(String capability) {
        return tools.values().stream()
                .filter(tool -> tool.getCapabilities() != null && 
                               tool.getCapabilities().contains(capability))
                .toList();
    }
    
    public ToolExecution executeTool(String toolName, Map<String, Object> arguments) {
        ToolExecution execution = new ToolExecution(toolName, arguments);
        
        try {
            Object result = performToolExecution(toolName, arguments);
            execution.markCompleted(result);
        } catch (Exception e) {
            execution.markFailed(e.getMessage());
        }
        
        executions.put(execution.getExecutionId(), execution);
        return execution;
    }
    
    private Object performToolExecution(String toolName, Map<String, Object> arguments) {
        switch (toolName) {
            case "calculator":
                return executeCalculator(arguments);
            case "text_generator":
                return executeTextGenerator(arguments);
            case "data_validator":
                return executeDataValidator(arguments);
            default:
                throw new IllegalArgumentException("Tool não encontrado: " + toolName);
        }
    }
    
    private Object executeCalculator(Map<String, Object> args) {
        String operation = (String) args.get("operation");
        double a = ((Number) args.get("a")).doubleValue();
        double b = ((Number) args.get("b")).doubleValue();
        
        double result = switch (operation) {
            case "add" -> a + b;
            case "subtract" -> a - b;
            case "multiply" -> a * b;
            case "divide" -> {
                if (b == 0) throw new IllegalArgumentException("Divisão por zero não permitida");
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Operação inválida: " + operation);
        };
        
        return Map.of(
            "result", result,
            "operation", operation,
            "operands", Arrays.asList(a, b)
        );
    }
    
    private Object executeTextGenerator(Map<String, Object> args) {
        String template = (String) args.get("template");
        String name = (String) args.get("name");
        String language = (String) args.getOrDefault("language", "pt");
        
        String text = switch (template) {
            case "greeting" -> switch (language) {
                case "en" -> "Hello, " + name + "! Welcome to our MCP server.";
                case "es" -> "¡Hola, " + name + "! Bienvenido a nuestro servidor MCP.";
                default -> "Olá, " + name + "! Bem-vindo ao nosso servidor MCP.";
            };
            case "farewell" -> switch (language) {
                case "en" -> "Goodbye, " + name + "! Thank you for using our services.";
                case "es" -> "¡Adiós, " + name + "! Gracias por usar nuestros servicios.";
                default -> "Tchau, " + name + "! Obrigado por usar nossos serviços.";
            };
            case "email" -> switch (language) {
                case "en" -> "Dear " + name + ",\n\nThis is an automated message from our MCP server.\n\nBest regards,\nMCP Team";
                case "es" -> "Estimado/a " + name + ",\n\nEste es un mensaje automatizado de nuestro servidor MCP.\n\nSaludos cordiales,\nEquipo MCP";
                default -> "Caro(a) " + name + ",\n\nEsta é uma mensagem automatizada do nosso servidor MCP.\n\nAtenciosamente,\nEquipe MCP";
            };
            case "report" -> switch (language) {
                case "en" -> "Report generated for: " + name + "\nDate: " + new Date() + "\nStatus: Active";
                case "es" -> "Informe generado para: " + name + "\nFecha: " + new Date() + "\nEstado: Activo";
                default -> "Relatório gerado para: " + name + "\nData: " + new Date() + "\nStatus: Ativo";
            };
            default -> throw new IllegalArgumentException("Template inválido: " + template);
        };
        
        return Map.of(
            "text", text,
            "template", template,
            "language", language,
            "name", name
        );
    }
    
    private Object executeDataValidator(Map<String, Object> args) {
        String dataType = (String) args.get("data_type");
        String value = (String) args.get("value");
        
        boolean isValid = switch (dataType) {
            case "email" -> value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            case "cpf" -> value.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$") || value.matches("^\\d{11}$");
            case "phone" -> value.matches("^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$");
            case "url" -> value.matches("^https?://[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(/.*)?$");
            default -> throw new IllegalArgumentException("Tipo de dado inválido: " + dataType);
        };
        
        return Map.of(
            "valid", isValid,
            "data_type", dataType,
            "value", value,
            "message", isValid ? "Valor válido" : "Valor inválido para o tipo " + dataType
        );
    }
    
    public Optional<ToolExecution> getExecution(String executionId) {
        return Optional.ofNullable(executions.get(executionId));
    }
    
    public List<ToolExecution> getAllExecutions() {
        return new ArrayList<>(executions.values());
    }
    
    public boolean toolExists(String name) {
        return tools.containsKey(name);
    }
    
    public int getToolCount() {
        return tools.size();
    }
    
    public Tool createTool(Tool tool) {
        if (tool.getName() == null || tool.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da tool é obrigatório");
        }
        
        if (tools.containsKey(tool.getName())) {
            throw new IllegalArgumentException("Tool com nome '" + tool.getName() + "' já existe");
        }
        
        if (tool.getDescription() == null || tool.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição da tool é obrigatória");
        }
        
        if (tool.getInputSchema() == null) {
            throw new IllegalArgumentException("Schema de entrada da tool é obrigatório");
        }
        
        tools.put(tool.getName(), tool);
        return tool;
    }
    
    public Tool updateTool(String name, Tool updatedTool) {
        if (!tools.containsKey(name)) {
            throw new IllegalArgumentException("Tool com nome '" + name + "' não encontrada");
        }
        
        Tool existingTool = tools.get(name);
        
        if (updatedTool.getDescription() != null && !updatedTool.getDescription().trim().isEmpty()) {
            existingTool.setDescription(updatedTool.getDescription());
        }
        
        if (updatedTool.getInputSchema() != null) {
            existingTool.setInputSchema(updatedTool.getInputSchema());
        }
        
        if (updatedTool.getCapabilities() != null) {
            existingTool.setCapabilities(updatedTool.getCapabilities());
        }
        
        if (updatedTool.getMetadata() != null) {
            existingTool.setMetadata(updatedTool.getMetadata());
        }
        
        return existingTool;
    }
    
    public boolean deleteTool(String name) {
        return tools.remove(name) != null;
    }
}