package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.request.CartRequestDTO;
import br.com.ecommerce.dto.response.CartResponseDTO;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class CartService {
    public CartResponseDTO addItemToCart(Long productId, String token) {
        CartRequestDTO addCartItem = DataFactory.createCartItem(productId);
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(addCartItem)
                .when()
                .post("/cart/items")
                .then()
                .statusCode(201)
                .extract()
                .as(CartResponseDTO.class);
    }
}