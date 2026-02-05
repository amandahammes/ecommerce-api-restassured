package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Cart;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class CartService {
    public static Cart addItemToCart(String token, Integer productId) {
        Cart addCartItem = DataFactory.createCartItem(productId);
        return given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(addCartItem)
                .when()
                .post("/cart/items")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(Cart.class);
    }
}
