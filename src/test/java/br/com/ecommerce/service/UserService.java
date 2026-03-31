package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.UserDTO;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class UserService {

    @Step("Criando um usuário de teste na API")
    public UserDTO createUser(){
        UserDTO randomUser = DataFactory.createRandomUser();
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(randomUser)
                .when()
                .post("/users/register")
                .then()
                .log().ifValidationFails()
                .statusCode(201);
        return randomUser;
    }

    @Step("Realizando login com um usuário de teste na API")
    public String loginUserAdmin() {
        UserDTO userLogin = createUser();
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userLogin)
                .when()
                .post("/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}