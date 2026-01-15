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
}
