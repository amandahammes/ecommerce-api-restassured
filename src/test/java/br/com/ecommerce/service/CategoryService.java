package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Category;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CategoryService {
    private static String token;

    public static Category createCategory(){
        Category newCategory = DataFactory.createRandomCategory();
        UserService.createUser();
        UserService.loginUserAdmin();
        token = UserService.loginUserAdmin();
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

    public static String getToken() {
        return token;
    }
}
