package br.com.ecommerce.service;

import br.com.ecommerce.dto.response.OrderResponseDTO;

import static io.restassured.RestAssured.given;

public class OrderService {
    public OrderResponseDTO makingCheckout(String token){
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/orders/checkout")
                .then()
                .statusCode(201)
                .extract()
                .as(OrderResponseDTO.class);
    }
}
