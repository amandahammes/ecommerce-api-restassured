package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.test.base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static br.com.ecommerce.dataFactory.DataFactory.faker;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@Epic("Gestão de Ecommerce")
@Feature("Fluxo de Categorias")
public class CategoryTest extends BaseTest {

    private UserService userService = new UserService();
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private DataFactory dataFactory = new DataFactory();

    @Test
    @Story("Criar categoria")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-003: Valida a criação de categoria com informações obrigatórias e válidas")
    @DisplayName("Deve ter sucesso ao criar Categoria com informações válidas")
    public void shouldCreateCategorySuccessfullyWithValidInformation(){
        CategoryDTO newCategory = DataFactory.createRandomCategory();
        String token = userService.loginUserAdmin();
        given()
                .spec(requestSpec(token))
                .body(newCategory)
                .when()
                .post("/categories/admin")
                .then()
                .spec(responseSpecCode201CreatedContent())
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class))
                .body("name", equalTo(newCategory.getName()))
                .body("description", equalTo(newCategory.getDescription()));
    }

    @Test
    @Story("Alterar categoria")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-004: Valida alteração de informações de categoria já existente")
    @DisplayName("Deve ter sucesso ao alterar Categoria com informações válidas")
    public void shouldPutCategorySuccessfullyWithValidInformation() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        String categoryName = dataFactory.createRandomCategoryName();
        category.setName(categoryName);
        given()
                .spec(requestSpec(token))
                .body(category)
                .when()
                .put("/categories/admin/{id}", category.getId())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("id", equalTo(category.getId().intValue()))
                .body("name", equalTo(categoryName));
    }

    @Test
    @Story("Deletar categoria")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-005: Valida se categoria foi deletada")
    @DisplayName("Deve ter sucesso ao deletar Categoria")
    public void shouldDeleteCategorySuccessfullyWithValidInformation() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/categories/admin/{id}", category.getId().intValue())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode204());
    }

    @Test
    @Story("Listar categorias")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-006: Valida a listagem das categorias e suas informações")
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetAllCategoriesSuccessfullyWithValidInformation() {
        CategoryDTO category1 = categoryService.createCategory();
        CategoryDTO category2 = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .get("/categories")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("size()", greaterThanOrEqualTo(2))
                .body("content.id", hasItems(category1.getId().intValue(), category2.getId().intValue()))
                .body("content.name", hasItems(category1.getName(), category2.getName()));;
    }

    @Test
    @Story("Mostrar categoria por id")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-007: Valida listar informações de categoria por id")
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetCategorySuccessfullyWithValidInformation() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .get("/categories/{id}", category.getId())
                .then()
                .spec(responseSpecCode200())
                .body("id", equalTo(category.getId().intValue()))
                .body("name", equalTo(category.getName()))
                .body("description", equalTo(category.getDescription()));
    }

    @Test
    @Story("Criar categoria")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-103: Valida a integridade da categoria ao tentar criar categoria com nome de categoria existente")
    @DisplayName("Deve gerar mensagem de erro ao criar categoria com nome de categoria já existente")
    public void shouldFailToCreateCategoryWithExistingCategoryName(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        CategoryDTO categorySameName = CategoryDTO.builder()
                .name(category.getName())
                .description(faker.lorem().sentence(5))
                .build();
        given()
                .spec(requestSpec(token))
                .body(categorySameName)
                .when()
                .post("/categories/admin")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode400());
    }

    @Test
    @Story("Deletar Categoria")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-104: Valida a a consistencia das regras ao deletar categoria inexistente")
    @DisplayName("Deve gerar mensagem de erro ao deletar Categoria inexistente")
    public void shouldFailToDeleteNonExistingCategory(){
        String token = userService.loginUserAdmin();
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/categories/admin/{id}", 100000)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode404())
                .body("message", equalTo("Impossível excluir: Categoria não encontrada."));
    }

    @Test
    @Story("Deletar Categoria")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-105: Valida a integridade do sistema ao tentar deletar categoria com produto vinculado")
    @DisplayName("Deve gerar mensagem de erro ao deletar Categoria com produto vinculado")
    public void shouldFailToDeleteCategoryWithLinkedProduct() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        Long idCategory = category.getId();
        ProductDTO produto = productService.createProduct(category.getId(), token);

        given()
                .spec(requestSpec(token))
                .when()
                .delete("/categories/admin/{id}", idCategory)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode409());
    }

    @Test
    @Story("Alterar Categoria")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-106: Valida a integridade do sistema ao alterar nome da categoria para outro já existente")
    @DisplayName("Deve gerar mensagem de erro ao alterar nome da Categoria para uma já existente")
    public void shouldFailToPutCategoryToExistingCategoryName() {
        CategoryDTO category1 = categoryService.createCategory();
        String token = categoryService.getToken();
        Long idCategory = category1.getId();
        CategoryDTO category2 = categoryService.createCategory();
        CategoryDTO categorySameName = CategoryDTO.builder()
                .name(category2.getName())
                .description(category1.getDescription())
                .build();
        given()
                .spec(requestSpec(token))
                .body(categorySameName)
                .when()
                .put("/categories/admin/{id}", idCategory)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode409());
    }
}