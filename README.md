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
    API do Ecommerce* rodando via Docker conforme instruções do projeto [E-commerce API](https://github.com/renansalves/E-Commerce-API).

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
    - DELETE /categiories/admin/{id} (Exclusão de categoria)
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

| ID    | Cenário                         | Método | Endpoint                  | Resultado Esperado                          |
|-------|---------------------------------|--------|---------------------------|---------------------------------------------|
| CT-01 | Criar novo usuário válido.      | POST   | /users/register           | Status 200.                                 |
| CT-02 | Realizar login com sucesso.     | POST   | /users/login              | Status 200. Token de sessão gerado.         |
| CT-03 | Criar categoria válida.         | POST   | /categories/admin         | Status 201. Eco dos dados enviados + ID.    |
| CT-04 | Alterar descrição de categoria. | PUT    | /categiories/admin/{id}   | Status 201. Eco da categoria atualizada.    |
| CT-05 | Excluir categoria existente.    | DELETE | /categiories/admin/{id}   | Status 204.                                 |
| CT-06 | Listar todas as categorias.     | GET    | /categories               | Status 200. Json com categorias existentes. |
| CT-07 | Listar categoria por {id}       | GET    | /categories/{id}          | Status 200. Eco dos dados referente ao id.  |
| CT-08 | Criar produto válido.           | POST   | /admin/products           | Status 201. Eco dos dados enviados + ID.    |
| CT-09 | Atualizar produto.              | PUT    | /admin/products/{id}      | Status 201. Eco do produto atualizado.      |
| CT-10 | Excluir produto existente.      | DELETE | /admin/products/{id}      | Status 204.                                 |
| CT-11 | Listar todos os produtos.       | GET    | /products                 | Status 200. Json com produtos existentes.   |
| CT-12 | Listar produto por {id}         | GET    | /products/{id}            | Status 200. Eco dos dados referente ao id.  |
| CT-13 | Finalizar checkout.             | POST   | /orders/checkout          |                                             |
| CT-14 | Atualizar status do pedido.     | PATCH  | /orders/admin/{id}/status |                                             |
| CT-15 | Listar pedidos do usuario.      | GET    | /orders                   |                                             |
| CT-16 | Consultar pedido por {id}.      | GET    | /orders/{id}              |                                             |
| CT-17 | Adiciona produto ao carrinho.   | POST   | /cart/items               |                                             |
| CT-18 | Busca carrinho do usuário.      | GET    | /cart                     |                                             |
| CT-19 | Deleta carrinho.                | DELETE | /cart                     |                                             |
| CT-20 | Deleta item do carrinho.        | DELETE | /cart/items/{id}          |                                             |


## Cenários de testes Negativos

| ID    | Cenário                                 | Método | Endpoint          | Resultado Esperado                                          |
|-------|-----------------------------------------|--------|-------------------|-------------------------------------------------------------|
| CT-00 | Realizar cadastro com e-mail existente. | POST   | /users/login      | Status 403 (???). Message: Este e-mail já está cadastrado.  |
| CT-00 | Criar categoria já existente.           | POST   | /categories/admin | Status 403. Message: Já existe uma categoria com este nome. |
| CT-00 | Excluir categoria inexistente.          | DELETE | xxx               | xxxx                                                        |
| CT-00 |                                         |        |                   |                                                             |


