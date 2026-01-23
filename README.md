# Automação de Testes - Ecommerce API

Primeira etapa da automação de testes com REST Assured da Aplicação [E-commerce API](https://github.com/renansalves/E-Commerce-API).
O objetivo final é garantir que as operações CRUD de `produto`, `categoria`, `carrinho`, `order` e `usuário` funcionem corretamente.

## Escopo e Cenários

### Endpoints Mapeados

    - **USERS**: Registro, Login e Consulta.
    - **CATEGORY**: CRUD administrativo e listagem pública.
    - **PRODUCTS**: CRUD administrativo e consulta de catálogo.
    - **ORDERS**: Checkout, atualização de status e histórico.
    - **CART**: Gerenciamento de itens e carrinho do usuário.


### Cenários de testes realizados:


| ID     | Funcionalidade | Classificação |                   Cenário                           | Método | Endpoint                  | Status | Resposta Esperada                 | 
|:-------|:---------------|:--------------|:----------------------------------------------------|:-------|:--------------------------|:-------|:----------------------------------|
| CT-001 |      USER      |   Positivo    | Criar novo usuário válido.                          | POST   | /users/register           | 200    | -                                 |
| CT-002 |      USER      |   Positivo    | Realizar login com sucesso.                         | POST   | /users/login              | 200    | Token de sessão gerado.           |
| CT-003 |   CATEGORIES   |   Positivo    | Criar categoria válida.                             | POST   | /categories/admin         | 201    | Eco dos dados enviados + ID.      |
| CT-004 |   CATEGORIES   |   Positivo    | Alterar descrição de categoria.                     | PUT    | /categories/admin/{id}    | 201    | Eco da categoria atualizada.      |
| CT-005 |   CATEGORIES   |   Positivo    | Excluir categoria existente.                        | DELETE | /categories/admin/{id}    | 204    | -                                 |
| CT-006 |   CATEGORIES   |   Positivo    | Listar todas as categorias.                         | GET    | /categories               | 200    | Json com categorias existentes.   |
| CT-007 |   CATEGORIES   |   Positivo    | Listar categoria por {id}                           | GET    | /categories/{id}          | 200    | Eco dos dados referente ao id.    |
| CT-008 |    PRODUCTS    |   Positivo    | Criar produto válido.                               | POST   | /admin/products           | 201    | Eco dos dados enviados + ID.      |
| CT-009 |    PRODUCTS    |   Positivo    | Atualizar produto.                                  | PUT    | /admin/products/{id}      | 201    | Eco do produto atualizado.        |
| CT-010 |    PRODUCTS    |   Positivo    | Excluir produto existente.                          | DELETE | /admin/products/{id}      | 204    | -                                 |
| CT-011 |    PRODUCTS    |   Positivo    | Listar todos os produtos.                           | GET    | /products                 | 200    | Json com produtos existentes.     |
| CT-012 |    PRODUCTS    |   Positivo    | Listar produto por {id}                             | GET    | /products/{id}            | 200    | Eco dos dados referente ao id.    |
| CT-017 |      CART      |   Positivo    | Adiciona produto ao carrinho.                       | POST   | /cart/items               | 201    | Json com produtos do carrinho.    |


### Cenários de testes a serem desenvolvidos nas próximas etapas:

| ID     | Funcionalidade | Classificação |                   Cenário                           | Método | Endpoint                  | Status | Resposta Esperada                   | 
|:-------|:---------------|:--------------|:----------------------------------------------------|:-------|:--------------------------|:-------|:------------------------------------|
| CT-013 |     ORDERS     |   Positivo    | Finalizar checkout.                                 | POST   | /orders/checkout          | 200    | Eco dos dados referente ao pedido.  |
| CT-014 |     ORDERS     |   Positivo    | Atualizar status do pedido.                         | PATCH  | /orders/admin/{id}/status | 200    | Eco dos dados referente ao pedido.  |
| CT-015 |     ORDERS     |   Positivo    | Listar pedidos do usuario.                          | GET    | /orders                   | 200    | Eco dos dados referente aos pedidos.|
| CT-016 |     ORDERS     |   Positivo    | Consultar pedido por {id}.                          | GET    | /orders/{id}              | 200    | Eco dos dados referente ao pedido.  |
| CT-018 |      CART      |   Positivo    | Busca carrinho do usuário.                          | GET    | /cart                     | 200    | Json com produtos e quantidades.    |
| CT-019 |      CART      |   Positivo    | Deleta carrinho.                                    | DELETE | /cart                     | 204    | -                                   |
| CT-020 |      CART      |   Positivo    | Deleta item do carrinho.                            | DELETE | /cart/items/{id}          | 204    | -                                   |
| CT-101 |      USER      |   Negativo    | Realizar cadastro com e-mail já existente.          | POST   | /users/login              | 403    | Msg: Este e-mail já está cadastrado.|
| CT-102 |   CATEGORIES   |   Negativo    | Realizar cadastro com senha inválida.               | POST   | /users/login              | 400    | Msg: size must be between 8 and 147483647. |
| CT-103 |   CATEGORIES   |   Negativo    | Criar categoria já existente.                       | POST   | /categories/admin         | 403    | Msg: Já existe uma categoria com este nome.      |
| CT-104 |   CATEGORIES   |   Negativo    | Deletar categoria inexistente.                      | DELETE | /categories/admin/id      | 404    | Msg: Impossível excluir: Categoria não encontrada. |
| CT-105 |   CATEGORIES   |   Negativo    | Deletar categoria com produto vinculado.            | DELETE | /categories/admin/id      | -      | A ser definido.                     |
| CT-106 |   CATEGORIES   |   Negativo    | Alterar categoria para outra já existente.          | PUT    | /categories/admin/id      | 403    | -                                   |
| CT-107 |    PRODUCTS    |   Negativo    | Criar produto já existente.                         | POST   | /admin/products           | 403    | -                                   |
| CT-108 |    PRODUCTS    |   Negativo    | Deletar produto inexistente.                        | DELETE | /admin/products/id        | 404    | Msg: Produto não encontrado.        |
| CT-109 |    PRODUCTS    |   Negativo    | Alterar produto para outro já existente.            | PUT    | /admin/products/id        | -      | A ser definido.                     |
| CT-110 |     ORDERS     |   Negativo    | Finalizar checkout carrinho inexistente.            | POST   | /orders/checkout          | 404    | Msg: Carrinho não encontrado para este usuário. |
| CT-111 |      CART      |   Negativo    | Adicionar produto inativo no carrinho.              | POST   | /cart/items               | 404    | Msg: Produto não encontrado ou inativo. |
| CT-112 |      CART      |   Negativo    | Adicionar + produtodo que o disponível no carrinho. | POST   | /cart/items               | 400    | Msg: Estoque insuficiente.              |
| CT-113 |      CART      |   Negativo    | Deletar carrinho com Checkout finalizado.           | DELETE | /cart                     | -      | A ser definido.                         |
| CT-114 |      CART      |   Negativo    | Deletar item do carrinho com Checkout finalizado.   | DELETE | /cart/items/id            | 404    | Msg: Carrinho vazio ou não encontrado.  |

## Ferramentas e Tecnologias Utilizadas

Este projeto utiliza a stack de automação Java com as seguintes tecnologias:

    - Java 21: Linguagem base do projeto.
    - JUnit 5: Framework de execução de testes.
    - Rest Assured: Biblioteca para automação de testes de APIs REST.
    - Maven: Gerenciador de dependências e automação de build.
    - DataFaker: Geração de massa de dados aleatória.
    - Hamcrest: Biblioteca de matchers que permite criar regras de verificação (assertions) mais legíveis.
    - Jackson Databind: Responsável pela serialização e desserialização de objetos Java para JSON e vice-versa. 

A ser implementado:

- Allure: cria arquivos .txt e .json com os dados sobre os testes rodados.


## Ambiente

URL Swagger: http://localhost:8080/swagger-ui/index.html <br>
Só estará disponível ao rodar a API como explicado no item: Rodar API.

## Para rodar o Projeto na sua Máquina

### Pré requisitos

[Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) <br>
[Maven](https://maven.apache.org/download.cgi) <br>
[E-commerce API](https://github.com/renansalves/E-Commerce-API) <br>


## Rodar API
    
Para rodar a API, o docker deve estar instalado/aberto e, na pasta principal do projeto, é necessário rodar o comando: `docker compose up -d --build app postgres`. Para mais informações, acessar repositório da [E-commerce API](https://github.com/renansalves/E-Commerce-API)


## Rodar testes na sua máquina

1. **Clonar o projeto:**

Para realizar o clone do projeto, clique em clone, escolha a forma que deseja fazer a clonagem (se por SSH ou HTTPS), no seu ambiente local de trabalho, abra o terminal, cole o seguinte código e execute o comando: git clone [cole-o-link-copiado-aqui]

2. **Como executar projeto**

Após o clone do projeto, acesse o diretório recém clonado e execute o seguinte comando no terminal: `mvn clean test`


### Resultados

Como a API ainda está em processo de desenvolvimento e ajustes estão sendo realizados, há a possibilidade de alguns testes quebrarem.


* Projeto desenvolvido por [Amanda Kopper Hammes](https://github.com/amandahammes) em janeiro/2026.
