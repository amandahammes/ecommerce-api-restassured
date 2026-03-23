package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.request.CartRequest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class CartService {
    public CartRequest addItemToCart(Integer productId, String token) {
        CartRequest addCartItem = DataFactory.createCartItem(productId);
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
                .as(CartRequest.class);
    }
}