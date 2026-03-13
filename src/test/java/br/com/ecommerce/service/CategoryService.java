package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Category;
import io.restassured.http.ContentType;
import lombok.Getter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CategoryService {
    private UserService userService = new UserService();
    private DataFactory dataFactory = new DataFactory();
    @Getter
    private String token;

    public Category createCategory(){
        token = userService.loginUserAdmin();
        Category newCategory = dataFactory.createRandomCategory();
        return given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newCategory)
                .when()
                .post("/categories/admin")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class))
                .extract()
                .as(Category.class);
    }
}
