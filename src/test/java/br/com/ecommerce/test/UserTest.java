package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.User;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.util.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;

public class UserTest {

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = ConfigLoader.getProperty("base_url");
    }

    @Test
    @DisplayName("Deve ter sucesso ao criar Usuário com credenciais válidas")
    public void shouldCreateUserSuccessfullyWithValidCredentials(){
        User randomUser = DataFactory.createRandomUser();
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(randomUser)
                .when()
                .post("/users/register")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("Deve ter sucesso ao realizar login do Usuário com credenciais válidas")
    public void shouldLoginUserSuccessfullyWithValidCredentials(){
        User userLogin = UserService.createUser();
        User loginCredentials = new User(userLogin.getEmail(), userLogin.getPassword());
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(loginCredentials)
                .when()
                .post("/users/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }
}
