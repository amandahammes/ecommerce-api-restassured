package br.com.ecommerce.test;

import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.dto.response.CartResponse;
import br.com.ecommerce.service.CartService;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.test.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderTest extends BaseTest {
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private CartService cartService = new CartService();

    @Test
    @DisplayName("Deve finalizar checkout com sucesso.")
    public void shouldFinishCheckoutSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        ProductDTO product2 = productService.createProduct(category.getId(), token);
        CartResponse addCartItem1 = cartService.addItemToCart(product2.getId(), token);
        CartResponse addCartItem2 = cartService.addItemToCart(product1.getId(), token);

        given()
                .spec(requestSpec(token))
                .when()
                .post("/orders/checkout")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode201CreatedContent())
                .body("createdAt", notNullValue())
                .body("status", equalTo("PENDING"))
                .body("items.size()", greaterThan(0));
    }
}
