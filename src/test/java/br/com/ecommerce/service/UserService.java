package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.User;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class UserService {

public User createUser(){
        User randomUser = DataFactory.createRandomUser();
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

    public String loginUserAdmin() {
        User userLogin = createUser();
//        User loginCredentials = User.builder()
//                .email(userLogin.getEmail())
//                .password(userLogin.getPassword())
//                .build();
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
