# Análise de alinhamento entre backend e frontend

## Contexto
O backend foi atualizado com novos endpoints e regras de autenticação, mas o frontend ainda segue uma abordagem antiga baseada em fetch direto, URL fixa e estado local de carrinho. Isso gera inconsistência entre o que a interface espera e o que o backend realmente expõe.

## Pontos do backend que precisam ser considerados

### 1. Produtos
Arquivo: [backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/ProductController.java](backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/ProductController.java)

Endpoints disponíveis:
- GET /api/products
- GET /api/products/{id}
- POST /api/products (admin)
- PUT /api/products/{id} (admin)
- DELETE /api/products/{id} (admin)

Parâmetros de filtro:
- category
- material

Observação importante:
- O backend retorna a entidade Product, que possui campos como price, imageUrl, category, material, stockQuantity e minimumStock.
- O frontend precisa tratar o preço como valor numérico/decimal compatível com JSON.

### 2. Autenticação
Arquivo: [backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/AuthController.java](backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/AuthController.java)

Endpoints disponíveis:
- POST /api/auth/register
- POST /api/auth/login

Esses endpoints retornam ou recebem payloads específicos e devem ser usados para gerar o token JWT que será enviado nas próximas requisições autenticadas.

### 3. Carrinho
Arquivo: [backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/CartController.java](backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/CartController.java)

Endpoints disponíveis:
- POST /api/cart/items
- GET /api/cart
- DELETE /api/cart/items/{id}

Ponto crítico:
- O carrinho não é mais um recurso puramente local.
- As chamadas precisam incluir o usuário autenticado e o token JWT.

### 4. Checkout
Arquivo: [backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/CheckoutController.java](backend/gisellepiercing/src/main/java/com/gisellepiercing/application/controller/CheckoutController.java)

Endpoint disponível:
- POST /api/checkout

Esse endpoint espera um payload de checkout e usa o usuário autenticado do token.

---

## Como o frontend está consumindo o backend hoje

### 1. Listagem de produtos
Arquivos:
- [frontend/components/ProductGrid.tsx](frontend/components/ProductGrid.tsx)
- [frontend/components/AnatomyView.tsx](frontend/components/AnatomyView.tsx)

Hoje o frontend faz fetch direto para:
- http://localhost:8080/api/products

Problemas:
- URL hardcoded.
- Não há camada centralizada de API.
- Não existe tratamento padronizado de erros e autenticação.

### 2. Carrinho local
Arquivo: [frontend/context/AppContext.tsx](frontend/context/AppContext.tsx)

O carrinho está sendo mantido apenas no estado do React, sem sincronização com o backend. Isso está descompassado com os novos endpoints de cart.

### 3. Drawer do carrinho
Arquivo: [frontend/components/CartDrawer.tsx](frontend/components/CartDrawer.tsx)

A interface exibe o carrinho, mas não chama os endpoints do backend para adicionar, listar ou remover itens.

---

## Desalinhamentos principais

### 1. URL base fixa
O frontend usa http://localhost:8080 diretamente. Isso é frágil e dificulta rodar em ambientes diferentes.

Recomendação:
- Criar uma variável de ambiente como NEXT_PUBLIC_API_URL.
- Exemplo: NEXT_PUBLIC_API_URL=http://localhost:8080

### 2. Falta de autenticação nas rotas protegidas
Os endpoints de cart e checkout exigem um usuário autenticado. O frontend ainda não faz login nem armazena token.

Recomendação:
- Implementar fluxo de login e registro.
- Armazenar o JWT em localStorage ou sessionStorage.
- Enviar Authorization: Bearer <token> nas requisições protegidas.

### 3. Estrutura de tipos do frontend desatualizada
O frontend usa uma interface de Product bem simples, mas o backend agora trabalha com campos extras e valores decimais.

Recomendação:
- Ajustar os tipos para incluir:
  - stockQuantity
  - minimumStock
  - price como string ou number compatível com JSON

### 4. Carrinho local em vez de carrinho real
O backend agora tem um carrinho persistido por usuário. O frontend precisa alinhar a UX com isso.

Recomendação:
- Sincronizar o carrinho após login.
- Adicionar item no backend ao clicar em adicionar ao carrinho.
- Remover item no backend quando o usuário excluir do drawer.

### 5. Checkout não implementado
O backend expõe POST /api/checkout, mas o frontend ainda não consome essa rota.

Recomendação:
- Criar um fluxo de finalização que envie o payload correto para o endpoint.

---

## Mapeamento recomendado de endpoints

| Função | Endpoint backend | Status no frontend | Ação recomendada |
|---|---|---|---|
| Listar produtos | GET /api/products | Parcialmente implementado | Manter e centralizar em uma camada de API |
| Buscar produto por id | GET /api/products/{id} | Não usado | Implementar para tela de detalhe |
| Registrar usuário | POST /api/auth/register | Não implementado | Criar fluxo de cadastro |
| Fazer login | POST /api/auth/login | Não implementado | Criar fluxo de login |
| Adicionar item ao carrinho | POST /api/cart/items | Não implementado | Integrar na ação de adicionar produto |
| Buscar carrinho | GET /api/cart | Não implementado | Carregar carrinho após login |
| Remover item do carrinho | DELETE /api/cart/items/{id} | Não implementado | Integrar no drawer |
| Finalizar pedido | POST /api/checkout | Não implementado | Criar fluxo de checkout |

---

## Plano de implementação sugerido

### Fase 1 - Base da API
- Criar uma camada única de comunicação com o backend, por exemplo em uma pasta como frontend/lib ou frontend/services.
- Centralizar URL base e headers de autenticação.

### Fase 2 - Autenticação
- Implementar login e registro.
- Persistir o JWT no browser.
- Injetar o token automaticamente nas requisições autenticadas.

### Fase 3 - Produtos
- Ajustar os tipos do frontend para refletir os dados do backend.
- Garantir que o componente de produtos consuma o endpoint com os filtros corretos.

### Fase 4 - Carrinho
- Sincronizar o estado do carrinho com os endpoints do backend.
- Substituir o estado local por uma estratégia híbrida: UI local + persistência no backend.

### Fase 5 - Checkout
- Criar o fluxo de compra usando POST /api/checkout.
- Exibir status de sucesso/erro para o usuário.

---

## Recomendação de prioridade

### Alta prioridade
1. Criar camada de API centralizada.
2. Implementar login e armazenamento de token.
3. Integrar carrinho com os endpoints do backend.

### Média prioridade
4. Integrar checkout.
5. Ajustar tipos e tratamento de erro.

### Baixa prioridade
6. Melhorar UX de carregamento, feedback e fallback para erro de rede.

---

## Conclusão
O principal problema não é apenas a rota em si, mas o modelo de integração do frontend. O backend agora está orientado a autenticação, carrinho real e checkout, enquanto o frontend ainda opera com produtos estáticos e carrinho local. A correção mais importante é mover o frontend para uma arquitetura de integração mais próxima do backend, com autenticação explícita e endpoints reais.
