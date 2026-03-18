package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Category;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.test.base.BaseTest;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class CategoryTest extends BaseTest {

    private UserService userService = new UserService();
    private CategoryService categoryService = new CategoryService();
    private DataFactory dataFactory = new DataFactory();

    @Test
    @DisplayName("Deve ter sucesso ao criar Categoria com informações válidas")
    public void shouldCreateCategorySuccessfullyWithValidInformation(){
        Category newCategory = DataFactory.createRandomCategory();
        String token = userService.loginUserAdmin();
        given()
                .spec(requestSpec(token))
                .body(newCategory)
                .when()
                .post("/categories/admin")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class))
                .body("name", equalTo(newCategory.getName()))
                .body("description", equalTo(newCategory.getDescription()));
    }
    @Test
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetCategorySuccessfullyWithValidInformation() {
        Category category = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .get("/categories/{id}", category.getId())
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("id", equalTo(category.getId()))
                .body("name", equalTo(category.getName()))
                .body("description", equalTo(category.getDescription()));
    }
    @Test
    @DisplayName("Deve ter sucesso ao alterar Categoria com informações válidas")
    public void shouldPutCategorySuccessfullyWithValidInformation() {
        Category category = categoryService.createCategory();
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
                .statusCode(200)
                .body("id", equalTo(category.getId()))
                .body("name", equalTo(categoryName));
    }

    @Test
    @DisplayName("Deve ter sucesso ao deletar Categoria")
    public void shouldDeleteCategorySuccessfullyWithValidInformation() {
        Category category = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/categories/admin/{id}", category.getId())
                .then()
                .log().ifValidationFails()
                .statusCode(204);
    }

    @Test
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetAllCategoriesSuccessfullyWithValidInformation() {
        Category category1 = categoryService.createCategory();
        Category category2 = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .get("/categories")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2))
                .body("content.id", hasItems(category1.getId(), category2.getId()))
                .body("content.name", hasItems(category1.getName(), category2.getName()));;
    }

}
