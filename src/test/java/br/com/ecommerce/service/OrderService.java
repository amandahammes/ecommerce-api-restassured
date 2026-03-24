package br.com.ecommerce.service;

import br.com.ecommerce.dto.OrderDTO;
import br.com.ecommerce.dto.response.CartResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderService {
    public OrderDTO makingCheckout(String token){
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/orders/checkout")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderDTO.class);
    }
}
