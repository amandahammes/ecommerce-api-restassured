package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Category;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.util.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class CategoryTest {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = ConfigLoader.getProperty("base_url");
    }

    @Test
    @DisplayName("Deve ter sucesso ao criar Categoria com informações válidas")
    public void shouldCreateCategorySuccessfullyWithValidInformation(){
        Category newCategory = DataFactory.createRandomCategory();
        UserService.createUser();
        UserService.loginUserAdmin();
        String token = UserService.loginUserAdmin();
        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newCategory)
                .when()
                .post("/categories/admin")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class))
                .body("name", equalTo(newCategory.getName()))
                .body("description", equalTo(newCategory.getDescription()));
    }
    @Test
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetCategorySuccessfullyWithValidInformation() {
        Category category = CategoryService.createCategory();
        String token = CategoryService.getToken();
        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/categories/{id}", category.getId())
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(category.getId()))
                .body("name", equalTo(category.getName()))
                .body("description", equalTo(category.getDescription()));
    }
    @Test
    @DisplayName("Deve ter sucesso ao alterar Categoria com informações válidas")
    public void shouldPutCategorySuccessfullyWithValidInformation() {
        Category category = CategoryService.createCategory();
        String token = CategoryService.getToken();
        String categoryNewName = "AAAAAAA";
        category.setName(categoryNewName);
        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(category)
                .when()
                .put("/categories/admin/{id}", category.getId())
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(category.getId()))
                .body("name", equalTo(categoryNewName));
    }

    @Test
    @DisplayName("Deve ter sucesso ao deletar Categoria")
    public void shouldDeleteCategorySuccessfullyWithValidInformation() {
        Category category = CategoryService.createCategory();
        String token = CategoryService.getToken();
        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("/categories/admin/{id}", category.getId())
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetAllCategoriesSuccessfullyWithValidInformation() {
        Category category1 = CategoryService.createCategory();
        Category category2 = CategoryService.createCategory();
        String token = CategoryService.getToken();
        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/categories")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2))
                .body("content.id", hasItems(category1.getId(), category2.getId()))
                .body("content.name", hasItems(category1.getName(), category2.getName()));;
    }

}
