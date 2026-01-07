# Automação de Testes - Ecommerce API

Projeto desenvolvido para automação de testes do Repositório [E-commerce API](https://github.com/renansalves/E-Commerce-API) com REST Assured.
O objetivo é garantir que as operações CRUD de `produto`, `categoria`, `carrinho`, `order` e `usuário` funcionem corretamente.


## Ferramentas e Tecnologias

Linguagem: Java 21
Framework de Teste: JUnit 5
Bibliotecas: Rest Assured, Jackson Databind e Hamcrest.
REPORTS????


## Ambiente

URL Swagger: http://localhost:8080/swagger-ui/index.html
Massa de Teste: src/test/resources


## Pré-requisitos

    Java JDK 21
    Maven
    API do Ecommerce* 

* rodando via Docker conforme instruções do projeto [E-commerce API](https://github.com/renansalves/E-Commerce-API).


## Rodar testes

Baixar projeto
Rodar testes - executar.
Reports


## Escopo

Endpoints Testados:

USERS

    - POST /users/register (Criar usuário)
    - POST /users/login (Realizar login de usuário criado)
    - GET /users/{id} (Consulta usuário)

CATEGORY

    - POST /categories/admin (Criar categoria)
    - PUT /categiories/admin/{id} (Atualização de informação de categoria)
    - DELETE /categories/admin/{id} (Exclusão de categoria)
    - GET /categories (Listar categorias existentes)
    - GET /categories/{id} (Consulta categoria por {id})

PRODUCTS

    - POST /admin/products (Criar produto)
    - PUT /admin/products/{id} (Atualização de informação de produto)
    - DELETE /admin/products/{id} (Exclusão de produto)
    - GET /products (Listar produtos existentes)
    - GET /products/{id} (Consulta produto por {id}) 

ORDERS

    - POST /orders/checkout (Finaliza o checkout)
    - PATCH /orders/admin/{id}/status (Atualiza status do pedido)
    - GET /orders (Lista pedidos do usuario)
    - GET /orders/{id} (Consulta pedido por {id})

CART

    - POST /cart/items (Adiciona produto)
    - GET /cart (Busca carrinho usuário)
    - DELETE /cart (Deleta carrinho)
    - DELETE /cart/items/{id} (Deleta item do carrinho)


## Cenários de testes Positivos

| ID     | Cenário                         | Método | Endpoint                  | Resultado Esperado                               |
|--------|---------------------------------|--------|---------------------------|--------------------------------------------------|
| CT-001 | Criar novo usuário válido.      | POST   | /users/register           | Status 200.                                      |
| CT-002 | Realizar login com sucesso.     | POST   | /users/login              | Status 200. Token de sessão gerado.              |
| CT-003 | Criar categoria válida.         | POST   | /categories/admin         | Status 201. Eco dos dados enviados + ID.         |
| CT-004 | Alterar descrição de categoria. | PUT    | /categories/admin/{id}    | Status 201. Eco da categoria atualizada.         |
| CT-005 | Excluir categoria existente.    | DELETE | /categories/admin/{id}    | Status 204.                                      |
| CT-006 | Listar todas as categorias.     | GET    | /categories               | Status 200. Json com categorias existentes.      |
| CT-007 | Listar categoria por {id}       | GET    | /categories/{id}          | Status 200. Eco dos dados referente ao id.       |
| CT-008 | Criar produto válido.           | POST   | /admin/products           | Status 201. Eco dos dados enviados + ID.         |
| CT-009 | Atualizar produto.              | PUT    | /admin/products/{id}      | Status 201. Eco do produto atualizado.           |
| CT-010 | Excluir produto existente.      | DELETE | /admin/products/{id}      | Status 204.                                      |
| CT-011 | Listar todos os produtos.       | GET    | /products                 | Status 200. Json com produtos existentes.        |
| CT-012 | Listar produto por {id}         | GET    | /products/{id}            | Status 200. Eco dos dados referente ao id.       |
| CT-013 | Finalizar checkout.             | POST   | /orders/checkout          | Status 200. Eco dos dados referente ao pedido.   |
| CT-014 | Atualizar status do pedido.     | PATCH  | /orders/admin/{id}/status | Status 200. Eco dos dados referente ao pedido.   |
| CT-015 | Listar pedidos do usuario.      | GET    | /orders                   | Status 200. Eco dos dados referente aos pedidos. |
| CT-016 | Consultar pedido por {id}.      | GET    | /orders/{id}              | Status 200. Eco dos dados referente ao pedido.   |
| CT-017 | Adiciona produto ao carrinho.   | POST   | /cart/items               | Status 201. Json com produtos do carrinho.       |
| CT-018 | Busca carrinho do usuário.      | GET    | /cart                     | Status 200. Json com produtos e quantidades.     |
| CT-019 | Deleta carrinho.                | DELETE | /cart                     | Status 204.                                      |
| CT-020 | Deleta item do carrinho.        | DELETE | /cart/items/{id}          | Status 204.                                      |



## Cenários de testes Negativos

| ID     | Cenário                                                           | Método | Endpoint             | Resultado Esperado                                                 |
|--------|-------------------------------------------------------------------|--------|----------------------|--------------------------------------------------------------------|
| CT-101 | Realizar cadastro com e-mail já existente.                        | POST   | /users/login         | Status 403. Message: Este e-mail já está cadastrado.               |
| CT-102 | Realizar cadastro com senha inválida.                             | POST   | /users/login         | Status 400. Message: size must be between 8 and 2147483647.        |
| CT-103 | Criar categoria já existente.                                     | POST   | /categories/admin    | Status 403. Message: Já existe uma categoria com este nome.        |
| CT-104 | Deletar categoria inexistente.                                    | DELETE | /categories/admin/id | Status 404. Message: Impossível excluir: Categoria não encontrada. |
| CT-105 | Deletar categoria com produto vinculado.                          | DELETE | /categories/admin/id | ?                                                                  |
| CT-106 | Alterar categoria para outra já existente.                        | PUT    | /categories/admin/id | Status 403. Message:                                               |
| CT-107 | Criar produto já existente.                                       | POST   | /admin/products      | Status 403. Message:                                               |
| CT-108 | Deletar produto inexistente.                                      | DELETE | /admin/products/id   | Status 404. Message: Produto não encontrado.                       |
| CT-109 | Alterar produto para outro já existente.                          | PUT    | /admin/products/id   | ?                                                                  |
| CT-110 | Finalizar checkout carrinho inexistente.                          | POST   | /orders/checkout     | Status 404. Message: Carrinho não encontrado para este usuário.    |
| CT-111 | Adicionar produto inexistente/inativo no carrinho.                | POST   | /cart/items          | Status 404. Message: Produto não encontrado ou inativo.            |
| CT-112 | Adicionar quantidade de produto maior que disponível no carrinho. | POST   | /cart/items          | Status 400. Message: Estoque insuficiente.                         |
| CT-113 | Deletar carrinho com Checkout finalizado.                         | DELETE | /cart                | ?                                                                  |
| CT-114 | Deletar item do carrinho com Checkout finalizado.                 | DELETE |                      | Status 404. Message: Carrinho vazio ou não encontrado.             |
