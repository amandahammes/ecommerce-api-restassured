package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.request.CartRequestDTO;
import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.dto.response.CartResponseDTO;
import br.com.ecommerce.service.CartService;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.test.base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Gestão de Ecommerce")
@Feature("Fluxo de Carrinho")
public class CartTest extends BaseTest {

    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private CartService cartService = new CartService();

    @Test
    @Story("Adicionar item ao carrinho")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-017: Valida a inserção de produto existente com quantidade válida")
    @DisplayName("Deve ter sucesso ao adicionar item ao carrinho")
    public void shouldAddProductToCartSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        CartRequestDTO addCartItem = DataFactory.createCartItem(product.getId());

        given()
                .spec(requestSpec(token))
                .body(addCartItem)
                .when()
                .post("/cart/items")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode201Created())
                .body("items[0].productId", equalTo(product.getId().intValue()))
                .body("items[0].productName", equalTo(product.getName()))
                .body("items[0].quantity", equalTo(addCartItem.getQuantity()))
                .body("totalCents", equalTo(addCartItem.getQuantity() * product.getPriceCents().intValue()));
    }

    @Test
    @Story("Listar carrinho usuário")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-018: Valida informações corretas ao buscar carrinho do usuário")
    @DisplayName("Deve ter sucesso ao pegar informações do carrinho")
    public void shouldGetCartSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        ProductDTO product2 = productService.createProduct(category.getId(), token);
        CartResponseDTO addCartItem1 = cartService.addItemToCart(product2.getId(), token);
        CartResponseDTO addCartItem2 = cartService.addItemToCart(product1.getId(), token);

        given()
                .spec(requestSpec(token))
                .when()
                .get("/cart")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("items.productId", hasItems(product1.getId().intValue(), product2.getId().intValue()))
                .body("items.size()", is(2));
    }

    @Test
    @Story("Deletar carrinho")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-019: Valida a exclusão de carrinho do usuário com sucesso")
    @DisplayName("Deve ter sucesso ao deletar carrinho")
    public void shouldDeleteCartSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        CartResponseDTO addCartItem = cartService.addItemToCart(product.getId(), token);

        given()
                .spec(requestSpec(token))
                .when()
                .delete("/cart")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode204());
    }

    @Test
    @Story("Deletar item do carrinho por id")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-020: Valida a exclusão de item do carrinho com sucesso")
    @DisplayName("Deve ter sucesso ao deletar item do carrinho")
    public void shouldDeleteItemCartSuccessfully(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        CartResponseDTO addCartItem1 = cartService.addItemToCart(product1.getId(), token);

        given()
                .spec(requestSpec(token))
                .when()
                .delete("/cart/items/{productId}",product1.getId().intValue())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode204());
    }

    @Test
    @Story("Inserir item no carrinho")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-112: Valida erro ao acrescentar produto inativo no carrinho")
    @DisplayName("Deve falhar ao acrescentar produto inativo no carrinho")
    public void shouldFailPostInactiveProductIntoCart(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        product.setActive(false);

        given()
                .spec(requestSpec(token))
                .body(product)
                .when()
                .put("/admin/products/{id}", product.getId())
                .then()
                .statusCode(200);

        CartRequestDTO cartRequest = CartRequestDTO.builder()
                .productId(product.getId())
                .quantity(10)
                .build();
        given()
                .spec(requestSpec(token))
                .body(cartRequest)
                .when()
                .post("/cart/items")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode404());
    }

    @Test
    @Story("Inserir produto no carrinho")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-113: Valida erro ao adicionar quantidade de produto maior que a disponível")
    @DisplayName("Deve falhar ao adicionar quantidade de produto maior que a disponível")
    public void shouldReturnErrorWhenProductQuantityExceedsAvailable(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        Integer quantidade = product.getStockQuantity();

        CartRequestDTO cartRequest = CartRequestDTO.builder()
                .productId(product.getId())
                .quantity(quantidade + 10)
                .build();

        given()
                .spec(requestSpec(token))
                .body(cartRequest)
                .when()
                .post("/cart/items")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode400())
                .body("message", equalTo("Estoque insuficiente"));
    }
}