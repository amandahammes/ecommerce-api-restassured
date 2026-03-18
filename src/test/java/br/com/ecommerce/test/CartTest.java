package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Cart;
import br.com.ecommerce.model.Category;
import br.com.ecommerce.model.Product;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.test.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CartTest extends BaseTest {

    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();

    @Test
    @DisplayName("Deve ter sucesso ao adicionar item ao carrinho")
    public void shouldAddProductToCartSuccessfully(){
        Category category = categoryService.createCategory();
        String token = categoryService.getToken();
        Product product = productService.createProduct(category.getId(), token);
        Cart addCartItem = DataFactory.createCartItem(product.getId());

        given()
                .spec(requestSpec(token))
                .body(addCartItem)
                .when()
                .post("/cart/items")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("items[0].productId", equalTo(product.getId()))
                .body("items[0].productName", equalTo(product.getName()))
                .body("items[0].quantity", equalTo(addCartItem.getQuantity()))
                .body("totalCents", equalTo(addCartItem.getQuantity() * product.getPriceCents()));
    }
}
