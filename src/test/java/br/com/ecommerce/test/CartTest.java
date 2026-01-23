package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Cart;
import br.com.ecommerce.model.Category;
import br.com.ecommerce.model.Product;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.util.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CartTest {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = ConfigLoader.getProperty("base_url");
    }

    @Test
    @DisplayName("Deve ter sucesso ao adicionar item ao carrinho")
    public void shouldAddProductToCartSuccessfully(){
        Category category = CategoryService.createCategory();
        String token = CategoryService.getToken();
        Product product = ProductService.createProduct(category.getId(), token);
        Cart addCartItem = DataFactory.createCartItem(product.getId());

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(addCartItem)
                .when()
                .post("/cart/items")
                .then()
                .log().all()
                .statusCode(201)
                .body("items[0].productId", equalTo(product.getId()))
                .body("items[0].productName", equalTo(product.getName()))
                .body("items[0].quantity", equalTo(addCartItem.getQuantity()))
                .body("totalCents", equalTo(addCartItem.getQuantity() * product.getPriceCents()));
    }
}
