# API de Gestão de Usuários 🔐

API REST desenvolvida em **Spring Boot** com autenticação **JWT**, focada em boas práticas de backend.

## 🚀 Tecnologias
- Java 17
- Spring Boot
- Spring Security
- JWT
- Maven
- Swagger

## 🔑 Autenticação
- Login com email e senha
- Token JWT no padrão Bearer

## 📌 Endpoints principais

### Auth
- POST /auth/login

### Usuários
- POST /usuarios
- GET /usuarios
- GET /usuarios/{id}
- PUT /usuarios/{id}
- DELETE /usuarios/{id}

## ▶️ Como executar
```bash
mvn spring-boot:run
