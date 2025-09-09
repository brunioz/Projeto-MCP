package com.example.mcp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public class ToolExecution {
    
    @NotBlank
    private String toolName;
    
    @NotNull
    private Map<String, Object> arguments;
    
    private Object result;
    
    private String status;
    
    private String errorMessage;
    
    @JsonProperty("execution_id")
    private String executionId;
    
    @JsonProperty("started_at")
    private LocalDateTime startedAt;
    
    @JsonProperty("completed_at")
    private LocalDateTime completedAt;
    
    private Long executionTimeMs;
    

    public ToolExecution() {
        this.startedAt = LocalDateTime.now();
        this.status = "running";
    }
    
    public ToolExecution(String toolName, Map<String, Object> arguments) {
        this();
        this.toolName = toolName;
        this.arguments = arguments;
        this.executionId = generateExecutionId();
    }
    

    private String generateExecutionId() {
        return "exec_" + System.currentTimeMillis() + "_" + Math.random();
    }
    

    public void markCompleted(Object result) {
        this.result = result;
        this.status = "success";
        this.completedAt = LocalDateTime.now();
        if (this.startedAt != null) {
            this.executionTimeMs = java.time.Duration.between(this.startedAt, this.completedAt).toMillis();
        }
    }
    

    public void markFailed(String errorMessage) {
        this.status = "error";
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
        if (this.startedAt != null) {
            this.executionTimeMs = java.time.Duration.between(this.startedAt, this.completedAt).toMillis();
        }
    }
    

    public String getToolName() {
        return toolName;
    }
    
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
    
    public Map<String, Object> getArguments() {
        return arguments;
    }
    
    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
    
    public Object getResult() {
        return result;
    }
    
    public void setResult(Object result) {
        this.result = result;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getExecutionId() {
        return executionId;
    }
    
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }
    
    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
}