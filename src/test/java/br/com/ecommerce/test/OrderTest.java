package br.com.ecommerce.test;

import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.response.OrderResponseDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.dto.response.CartResponseDTO;
import br.com.ecommerce.service.CartService;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.OrderService;
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
    private OrderService orderService = new OrderService();

    @Test
    @DisplayName("Deve finalizar checkout com sucesso.")
    public void shouldFinishCheckoutSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        ProductDTO product2 = productService.createProduct(category.getId(), token);
        CartResponseDTO addCartItem1 = cartService.addItemToCart(product2.getId(), token);
        CartResponseDTO addCartItem2 = cartService.addItemToCart(product1.getId(), token);

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

    @Test
    @DisplayName("Deve alterar o status do pedido para um valor válido")
    public void shouldPatchOrderStatusSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        CartResponseDTO addCartItem = cartService.addItemToCart(product.getId(), token);
        OrderResponseDTO checkout = orderService.makingCheckout(token);
        Integer idCheckout = checkout.getId().intValue();

        given()
                .spec(requestSpec(token))
                .queryParam("status", "SHIPPED")
                .when()
                .patch("/orders/admin/{id}/status", idCheckout)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200());
    }

    @Test
    @DisplayName("Deve listar todos os pedidos do usuário com sucesso.")
    public void shouldGetAllOrdersSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        ProductDTO product2 = productService.createProduct(category.getId(), token);
        CartResponseDTO addCartItem1 = cartService.addItemToCart(product2.getId(), token);
        CartResponseDTO addCartItem2 = cartService.addItemToCart(product1.getId(), token);
        OrderResponseDTO checkout = orderService.makingCheckout(token);

        given()
                .spec(requestSpec(token))
                .when()
                .get("/orders")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("status", hasItem("PENDING"))
                .body("[0].items.size()", greaterThan(0));
    }

    @Test
    @DisplayName("Deve listar pedido do usuário por ID com sucesso.")
    public void shouldGetOrderByIdSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        CartResponseDTO addCartItem = cartService.addItemToCart(product.getId(), token);
        OrderResponseDTO checkout = orderService.makingCheckout(token);
        Integer idCheckout = checkout.getId().intValue();
        given()
                .spec(requestSpec(token))
                .when()
                .get("/orders/{id}", idCheckout)
                .then()
                .log().all()
                .spec(responseSpecCode200())
                .body("status", equalTo("PENDING"))
                .body("items.size()", greaterThan(0));
    }
}
