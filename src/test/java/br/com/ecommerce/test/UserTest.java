package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.UserDTO;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.test.base.BaseTest;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;

public class UserTest extends BaseTest {

    private UserService userService = new UserService();
    private DataFactory dataFactory = new DataFactory();

    @Test
    @DisplayName("Deve ter sucesso ao criar Usuário com credenciais válidas")
    public void shouldCreateUserSuccessfullyWithValidCredentials(){
        UserDTO randomUser = DataFactory.createRandomUser();
        given()
                .spec(publicSpec())
                .body(randomUser)
                .when()
                .post("/users/register")
                .then()
                .spec(responseSpecCode201Created());
    }

    @Test
    @DisplayName("Deve ter sucesso ao realizar login do Usuário com credenciais válidas")
    public void shouldLoginUserSuccessfullyWithValidCredentials(){
        UserDTO userLogin = userService.createUser();
        UserDTO loginCredentials = UserDTO.builder()
                .email(userLogin.getEmail())
                .password(userLogin.getPassword())
                .build();
        given()
                .spec(publicSpec())
                .body(loginCredentials)
                .when()
                .post("/users/login")
                .then()
                .spec(responseSpecCode200())
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Deve gerar mensagem de erro ao realizar cadastro com e-mail já existente")
    public void shouldFailToRegisterUserWithExistingEmail(){
        UserDTO userLogin = userService.createUser();
        UserDTO registerCredentials = dataFactory.createRandomUser();
        registerCredentials.setEmail(userLogin.getEmail());
        given()
                .spec(publicSpec())
                .body(registerCredentials)
                .when()
                .post("/users/register")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode409());
    }
}
