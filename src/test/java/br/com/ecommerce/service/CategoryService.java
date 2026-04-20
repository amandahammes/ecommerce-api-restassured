package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.CategoryDTO;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.Getter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CategoryService {
    private UserService userService = new UserService();
    private DataFactory dataFactory = new DataFactory();
    @Getter
    private String token;
    @Step("Criando uma categoria de teste na API")
    public CategoryDTO createCategory(){
        token = userService.loginUserAdmin();
        CategoryDTO newCategory = dataFactory.createRandomCategory();
        return given()
                .header("Authorization", "Bearer " + token)
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
                .as(CategoryDTO.class);
    }
}
