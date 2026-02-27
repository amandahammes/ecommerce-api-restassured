package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.User;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class UserService {

public static User createUser(){
        User randomUser = DataFactory.createRandomUser();
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(randomUser)
                .when()
                .post("/users/register")
                .then()
                .statusCode(201);
        return randomUser;
    }

    public static String loginUserAdmin() {
        User userLogin = createUser();
        User loginCredentials = new User(userLogin.getEmail(), userLogin.getPassword());
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(loginCredentials)
                .when()
                .post("/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
