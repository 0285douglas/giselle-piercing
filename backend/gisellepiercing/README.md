# Giselle Piercing API

Backend REST API para o e-commerce de piercings de luxo da Giselle Piercing.

Projeto desenvolvido com foco em:

* arquitetura limpa
* boas práticas backend
* autenticação JWT
* segurança com Spring Security
* organização em camadas
* SQL manual com JDBC Template

---

# Tecnologias Utilizadas

## Backend

* Java 21
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Validation
* Swagger/OpenAPI
* PostgreSQL
* JDBC Template (NamedParameterJdbcTemplate)
* Maven
* Lombok

---

# Arquitetura do Projeto

```txt
src/main/java/com/gisellepiercing

application
 ├── controller
 └── exception

config

dto
 ├── request
 └── response

model

repository
 ├── query

security

service
```

---

# Funcionalidades

## Produtos

* Criar produto
* Buscar produtos
* Buscar produto por ID
* Atualizar produto
* Deletar produto
* Filtros por categoria e material

---

## Autenticação

* Registro de usuário
* Login com JWT
* Proteção de rotas
* Roles:

  * ROLE_ADMIN
  * ROLE_CUSTOMER

---

# Segurança

A aplicação utiliza:

* Spring Security
* JWT Bearer Token
* BCrypt para criptografia de senha
* Controle de acesso por roles

---

# Validações

A API utiliza Bean Validation:

* @NotBlank
* @Positive
* @NotNull
* @Email

---

# Tratamento Global de Exceções

A aplicação possui:

* @RestControllerAdvice
* respostas padronizadas
* tratamento centralizado de erros

Exemplo:

```json
{
  "timestamp": "2026-05-20T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found"
}
```

---

# Documentação Swagger

Após iniciar o projeto:

```txt
http://localhost:8080/swagger-ui/index.html
```

---

# Endpoints

## Auth

### Registrar usuário

```http
POST /api/auth/register
```

Body:

```json
{
  "name": "User",
  "email": "user@email.com",
  "password": "123456"
}
```

---

### Login

```http
POST /api/auth/login
```

Body:

```json
{
  "email": "user@email.com",
  "password": "123456"
}
```

Resposta:

```json
{
  "token": "eyJhbGc..."
}
```

---

## Products

### Listar produtos

```http
GET /api/products
```

---

### Buscar por filtros

```http
GET /api/products?category=ear&material=gold
```

---

### Buscar produto por ID

```http
GET /api/products/{id}
```

---

### Criar produto

```http
POST /api/products
```

Necessário:

```txt
ROLE_ADMIN
```

---

### Atualizar produto

```http
PUT /api/products/{id}
```

Necessário:

```txt
ROLE_ADMIN
```

---

### Deletar produto

```http
DELETE /api/products/{id}
```

Necessário:

```txt
ROLE_ADMIN
```

---

# Autenticação JWT

Para acessar endpoints protegidos:

```txt
Authorization: Bearer TOKEN
```

---

# Banco de Dados

Banco utilizado:

```txt
PostgreSQL
```

Exemplo da tabela users:

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);
```

Exemplo da tabela products:

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    image_url VARCHAR(1024),
    category VARCHAR(100),
    material VARCHAR(100)
);
```

---

# Como Executar o Projeto

## Clonar repositório

```bash
git clone <URL_DO_REPOSITORIO>
```

---

## Configurar banco PostgreSQL

Criar database:

```sql
CREATE DATABASE giselle_piercing;
```

---

## Configurar `application.yml` (exemplo)

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/giselle_piercing}
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
    password: ${SPRING_DATASOURCE_PASSWORD:senha}
server:
  port: ${SERVER_PORT:8080}
```

Você também pode definir variáveis de ambiente em vez de editar o arquivo:

```bash
set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/giselle_piercing
set SPRING_DATASOURCE_USERNAME=postgres
set SPRING_DATASOURCE_PASSWORD=senha
set JWT_SECRET=troque_por_um_valor_secreto
```

---

## Executar aplicação

```bash
mvn spring-boot:run
```

ou

```bash
mvn clean package
java -jar target/*.jar
```

---

# Diferenciais Técnicos

* SQL manual com NamedParameterJdbcTemplate
* JWT implementado manualmente
* arquitetura organizada
* separação entre DTO e model
* tratamento global de erros
* logs estruturados
* autenticação stateless

---

# Melhorias Futuras

* Docker
* Flyway
* Deploy cloud
* Upload de imagens
* Testes automatizados
* Paginação

---

# Autor

Douglas Farias

Desenvolvedor Backend Java/Spring Boot.

