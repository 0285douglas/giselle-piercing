# API Contract - Giselle Piercing Backend

Este documento descreve o contrato de integração entre o Frontend e o Backend da aplicação Giselle Piercing.

## 1. 🌐 URL Base / Prefixos

- Base URL local padrão: `http://localhost:8080`
- Porta do servidor: `8080`
- Prefixo global da API: `/api`
- Prefixos por módulo:
  - Autenticação: `/api/auth`
  - Produtos: `/api/products`
  - Carrinho: `/api/cart`
  - Checkout: `/api/checkout`
  - Webhooks: `/api/webhook`
- Versão da API: não há versão explícita no código atual (`/api/...` sem `/v1`)
- Documentação Swagger:
  - `/swagger-ui/index.html`
  - `/v3/api-docs`

## 2. 🔑 Autenticação e Headers

### Autenticação
- O backend utiliza JWT.
- O token deve ser enviado no header `Authorization` no formato:
  - `Authorization: Bearer <token>`

### Headers obrigatórios
- Para requisições com body JSON:
  - `Content-Type: application/json`
- Para requisições que esperam JSON como resposta:
  - `Accept: application/json`

### Regras de acesso
- Endpoints públicos:
  - `/api/auth/**`
  - `/api/products/**`
  - `/api/webhook/**`
- Endpoints protegidos:
  - `/api/cart/**`
  - `/api/checkout/**`
  - endpoints administrativos de produtos (`POST/PUT/DELETE /api/products/**`)

## 3. 📡 Detalhamento de Endpoints

### 3.1 Autenticação

#### POST /api/auth/register
Cria um novo usuário.

Parâmetros:
- Body (JSON):
  - `firstName` (`string`, obrigatório)
  - `lastName` (`string`, obrigatório)
  - `email` (`string`, obrigatório, formato e-mail)
  - `password` (`string`, obrigatório, mínimo 6 caracteres)
  - `cpf` (`string`, obrigatório, formato CPF)

Exemplo de request:
```json
{
  "firstName": "Maria",
  "lastName": "Silva",
  "email": "maria@email.com",
  "password": "123456",
  "cpf": "12345678909"
}
```

Respostas:
- `201 Created` - sem body
- `400 Bad Request` - validação falhou
- `409 Conflict` - e-mail ou CPF já cadastrados

#### POST /api/auth/login
Autentica um usuário e retorna um JWT.

Parâmetros:
- Body (JSON):
  - `email` (`string`, obrigatório)
  - `password` (`string`, obrigatório)

Exemplo de request:
```json
{
  "email": "maria@email.com",
  "password": "123456"
}
```

Respostas:
- `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
- `401 Unauthorized` - credenciais inválidas
- `400 Bad Request` - dados inválidos

### 3.2 Produtos

#### GET /api/products
Lista produtos com filtros opcionais.

Parâmetros:
- Query params:
  - `category` (`string`, opcional)
  - `material` (`string`, opcional)

Respostas:
- `200 OK`
```json
[
  {
    "id": 1,
    "name": "Brinco Triângulo",
    "description": "Brinco de aço",
    "price": 45.90,
    "imageUrl": "https://...",
    "category": "Brinco",
    "material": "Aço",
    "stockQuantity": 10,
    "minimumStock": 3
  }
]
```

#### GET /api/products/{id}
Busca um produto pelo ID.

Parâmetros:
- Path params:
  - `id` (`number`, obrigatório)

Respostas:
- `200 OK`
```json
{
  "id": 1,
  "name": "Brinco Triângulo",
  "description": "Brinco de aço",
  "price": 45.90,
  "imageUrl": "https://...",
  "category": "Brinco",
  "material": "Aço",
  "stockQuantity": 10,
  "minimumStock": 3
}
```
- `404 Not Found` - produto não encontrado

#### POST /api/products
Cria um produto. Requer role `ADMIN`.

Parâmetros:
- Body (JSON):
  - `name` (`string`, obrigatório)
  - `description` (`string`, obrigatório)
  - `price` (`number`, obrigatório, maior que zero)
  - `imageUrl` (`string`, obrigatório)
  - `category` (`string`, obrigatório)
  - `material` (`string`, obrigatório)
  - `stockQuantity` (`number`, obrigatório, mínimo 0)
  - `minimumStock` (`number`, obrigatório, mínimo 0)

Exemplo de request:
```json
{
  "name": "Brinco Triângulo",
  "description": "Brinco de aço",
  "price": 45.90,
  "imageUrl": "https://img.com/brinco.jpg",
  "category": "Brinco",
  "material": "Aço",
  "stockQuantity": 10,
  "minimumStock": 3
}
```

Respostas:
- `201 Created`
```json
{
  "id": 1,
  "name": "Brinco Triângulo",
  "description": "Brinco de aço",
  "price": 45.90,
  "imageUrl": "https://img.com/brinco.jpg",
  "category": "Brinco",
  "material": "Aço",
  "stockQuantity": 10,
  "minimumStock": 3
}
```
- `400 Bad Request` - dados inválidos
- `401 Unauthorized` - token ausente/inválido
- `403 Forbidden` - sem permissão de admin

#### PUT /api/products/{id}
Atualiza um produto. Requer role `ADMIN`.

Parâmetros:
- Path params:
  - `id` (`number`, obrigatório)
- Body (JSON): objeto `Product` com os campos:
  - `id` (`number`, opcional)
  - `name` (`string`)
  - `description` (`string`)
  - `price` (`number`)
  - `imageUrl` (`string`)
  - `category` (`string`)
  - `material` (`string`)
  - `stockQuantity` (`number`)
  - `minimumStock` (`number`)

Respostas:
- `200 OK` - objeto `Product`
- `400 Bad Request`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`

#### DELETE /api/products/{id}
Remove um produto. Requer role `ADMIN`.

Parâmetros:
- Path params:
  - `id` (`number`, obrigatório)

Respostas:
- `204 No Content`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`

### 3.3 Carrinho

#### POST /api/cart/items
Adiciona um produto ao carrinho do usuário autenticado.

Parâmetros:
- Query params:
  - `productId` (`number`, obrigatório)
  - `quantity` (`number`, obrigatório, maior que zero)
- Autenticação obrigatória

Respostas:
- `200 OK` - sem body
- `400 Bad Request` - quantidade inválida ou estoque insuficiente
- `401 Unauthorized`

#### GET /api/cart
Retorna o carrinho do usuário autenticado.

Parâmetros:
- Nenhum body
- Autenticação obrigatória

Respostas:
- `200 OK`
```json
{
  "cartId": 1,
  "userId": 2,
  "items": [
    {
      "itemId": 10,
      "productId": 1,
      "productName": "Brinco Triângulo",
      "imageUrl": "https://...",
      "price": 45.90,
      "quantity": 2,
      "subtotal": 91.80
    }
  ],
  "total": 91.80
}
```
- `401 Unauthorized`

#### DELETE /api/cart/items/{id}
Remove um item do carrinho pelo ID do item.

Parâmetros:
- Path params:
  - `id` (`number`, obrigatório)

Respostas:
- `204 No Content`
- `401 Unauthorized`

### 3.4 Checkout

#### POST /api/checkout
Processa o checkout do carrinho do usuário autenticado.

Parâmetros:
- Body (JSON):
  - `paymentMethod` (`string`, obrigatório; valores esperados: `PIX`, `CREDIT_CARD`, `BOLETO`)
  - `token` (`string`, opcional)
  - `installments` (`number`, opcional)
  - `paymentMethodId` (`string`, opcional)

Exemplo de request:
```json
{
  "paymentMethod": "PIX"
}
```

Respostas:
- `200 OK`
- Retorna um objeto `Payment` do SDK do Mercado Pago (estrutura dependente da resposta do gateway)
- `400 Bad Request` - carrinho vazio, estoque insuficiente ou dados inválidos
- `401 Unauthorized`
- `500 Internal Server Error` - erro no processamento do pagamento

### 3.5 Webhooks

#### POST /api/webhook/mercado-pago
Recebe webhook do Mercado Pago.

Parâmetros:
- Body (JSON) com payload do webhook, incluindo:
  - `data` (`object`, obrigatório)
    - `id` (`string|number`, obrigatório)

Exemplo de request:
```json
{
  "data": {
    "id": "123456"
  }
}
```

Respostas:
- `200 OK` - sem body
- `500 Internal Server Error` - erro ao processar pagamento

#### POST /api/webhook/mercado-pago/simulate
Simula um pagamento aprovado para um pedido.

Parâmetros:
- Query params:
  - `orderId` (`number`, obrigatório)

Respostas:
- `200 OK`
```text
Pagamento simulado com sucesso
```

## 4. 🔄 CORS & WebSocket

### CORS
- O backend está configurado com CORS aberto para `origins = "*"` em controllers principais.
- Isso permite chamadas a partir de qualquer origem para os endpoints da API.

### WebSocket
- Não há implementação de WebSocket ou rotas em tempo real identificadas no backend atual.

## 5. 🧩 Estrutura padrão de erro

Todas as respostas de erro seguem este formato:

```json
{
  "timestamp": "2026-07-26T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error",
  "validations": {
    "email": "must be a well-formed email address"
  }
}
```

### Status codes comuns
- `400 Bad Request` - validação, estoque insuficiente, carrinho vazio, produto inválido
- `401 Unauthorized` - token ausente, inválido ou credenciais incorretas
- `403 Forbidden` - acesso não autorizado para admin
- `404 Not Found` - recurso não encontrado
- `409 Conflict` - e-mail ou CPF duplicados no cadastro
- `500 Internal Server Error` - erro inesperado ou falha no processamento de pagamento
