package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.request.CartRequest;
import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.service.CartService;
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
    private CartService cartService = new CartService();

    @Test
    @DisplayName("Deve ter sucesso ao adicionar item ao carrinho")
    public void shouldAddProductToCartSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        CartRequest addCartItem = DataFactory.createCartItem(product.getId());

        given()
                .spec(requestSpec(token))
                .body(addCartItem)
                .when()
                .post("/cart/items")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode201Created())
                .body("items[0].productId", equalTo(product.getId()))
                .body("items[0].productName", equalTo(product.getName()))
                .body("items[0].quantity", equalTo(addCartItem.getQuantity()))
                .body("totalCents", equalTo(addCartItem.getQuantity() * product.getPriceCents()));
    }

    @Test
    @DisplayName("Deve ter sucesso ao pegar informações do carrinho")
    public void shouldGetCartSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        ProductDTO product2 = productService.createProduct(category.getId(), token);
        CartRequest addCartItem1 = cartService.addItemToCart(product2.getId(), token);
        CartRequest addCartItem2 = cartService.addItemToCart(product1.getId(), token);

        given()
                .spec(requestSpec(token))
                .log().all()
                .when()
                .get("/cart")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("items.productId", hasItems(product1.getId(), product2.getId()))
                .body("items.quantity", hasItems(addCartItem2.getQuantity(), addCartItem1.getQuantity()))
                .body("items.size()", is(2));
    }
}
