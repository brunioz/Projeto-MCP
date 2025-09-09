# MCP Server - Guia de Uso dos Endpoints

## 📋 Sobre o Projeto

Este é um servidor MCP (Model Context Protocol) desenvolvido em Spring Boot que oferece APIs para gerenciar **Tools** (ferramentas) e **Resources** (recursos).

- **Tools**: Ferramentas que executam tarefas específicas (calculadora, gerador de texto, etc.)
- **Resources**: Dados e informações armazenadas (documentos, configurações, etc.)

## 🚀 Como Usar

### Iniciar o Servidor
```bash
mvn spring-boot:run
```
O servidor roda em: `http://localhost:8082`

## 🛠️ Endpoints de Tools

### Listar todas as tools
```bash
GET http://localhost:8082/api/tools
```

### Buscar tool específica
```bash
GET http://localhost:8082/api/tools/calculator
```

### Executar uma tool
```bash
POST http://localhost:8082/api/tools/calculator/execute
Content-Type: application/json

{
  "operation": "add",
  "a": 5,
  "b": 3
}
```

### Criar nova tool
```bash
POST http://localhost:8082/api/tools
Content-Type: application/json

{
  "name": "minha_tool",
  "description": "Descrição da tool",
  "inputSchema": {
    "type": "object",
    "properties": {
      "input": {"type": "string"}
    }
  }
}
```

## 📁 Endpoints de Resources

### Listar todos os resources
```bash
GET http://localhost:8082/api/resources
```

### Buscar resource por URI
```bash
GET http://localhost:8082/api/resources/by-uri?uri=mcp://resources/config/system
```

### Criar novo resource
```bash
POST http://localhost:8082/api/resources
Content-Type: application/json

{
  "uri": "mcp://meu-resource",
  "name": "Meu Resource",
  "description": "Descrição do resource",
  "mimeType": "text/plain",
  "content": "Conteúdo do resource"
}
```

## 📊 Endpoints de Informações

### Informações das Tools
```bash
GET http://localhost:8082/api/tools/info
```

### Informações dos Resources
```bash
GET http://localhost:8082/api/resources/info
```

### Estatísticas
```bash
GET http://localhost:8082/api/tools/stats
GET http://localhost:8082/api/resources/stats
```

## 🔧 Exemplos Práticos

### Exemplo 1: Usar a Calculadora
```bash
curl -X POST http://localhost:8082/api/tools/calculator/execute \
  -H "Content-Type: application/json" \
  -d '{"operation": "multiply", "a": 10, "b": 5}'
```

### Exemplo 2: Criar um Resource
```bash
curl -X POST http://localhost:8082/api/resources \
  -H "Content-Type: application/json" \
  -d '{
    "uri": "mcp://docs/manual",
    "name": "Manual do Usuário",
    "description": "Manual de instruções",
    "mimeType": "text/markdown",
    "content": "# Manual\nInstruções aqui..."
  }'
```

## ✅ Códigos de Resposta

- `200 OK` - Sucesso
- `400 Bad Request` - Dados inválidos
- `404 Not Found` - Item não encontrado
- `500 Internal Server Error` - Erro do servidor

## 📝 Formato das Respostas

Todas as respostas seguem o padrão:
```json
{
  "status": "success",
  "data": {...},
  "message": "Mensagem opcional"
}
```

---

**Pronto!** Com esses endpoints você pode gerenciar tools e resources facilmente. 🎉
