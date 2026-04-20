package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.dto.UserDTO;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.test.base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

@Epic("Gestão de Ecommerce")
@Feature("Fluxo de Produtos")
public class ProductTest extends BaseTest {

    private UserService userService = new UserService();
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private DataFactory dataFactory = new DataFactory();

    @Test
    @Story("Criar produto")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-008: Valida a criação de produto com informações obrigatórias e válidas")
    @DisplayName("Deve ter sucesso ao criar Produto com informações válidas")
    public void shouldCreateProductSuccessfullyWithValidInformation(){
        CategoryDTO newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Long categoryId = newCategory.getId();
        ProductDTO product = DataFactory.createRandomProduct(categoryId);
        given()
                .spec(requestSpec(token))
                .body(product)
                .when()
                .post("/admin/products")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode201CreatedContent())
                .body("sku", notNullValue())
                .body("categoryId", equalTo(categoryId.intValue()))
                .body("priceCents", equalTo(product.getPriceCents().intValue()))
                .body("stockQuantity", equalTo(product.getStockQuantity()));
    }

    @Test
    @Story("Alterar produto")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-009: Valida a alteração de produto com informações obrigatórias e válidas")
    @DisplayName("Deve ter sucesso ao atualizar produto com informações válidas")
    public void shouldPutProductSuccessfullyWithValidInformation(){
        CategoryDTO newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Long categoryId = newCategory.getId();
        ProductDTO newProduct = productService.createProduct(categoryId, token);
        String newNameProduct = dataFactory.createRandomProductName();
        newProduct.setName(newNameProduct);
        given()
                .spec(requestSpec(token))
                .body(newProduct)
                .when()
                .put("/admin/products/{id}", newProduct.getId())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("sku", equalTo(newProduct.getSku()))
                .body("name", equalTo(newNameProduct))
                .body("categoryId", equalTo(categoryId.intValue()))
                .body("priceCents", equalTo(newProduct.getPriceCents().intValue()))
                .body("stockQuantity", equalTo(newProduct.getStockQuantity()));
    }
    @Test
    @Story("Deletar produto")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-010: Valida a exclusão de produto existente.")
    @DisplayName("Deve ter sucesso ao deletar Produto")
    public void shouldDeleteProductSuccessfullyWithValidInformation() {
        CategoryDTO newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Long categoryId = newCategory.getId();
        ProductDTO newProduct = productService.createProduct(categoryId, token);
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/admin/products/{id}", newProduct.getId())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode204());
    }

    @Test
    @Story("Listar todos os produtos")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-011: Valida a listagem de produtos e suas informações")
    @DisplayName("Deve ter sucesso ao pegar lista de produtos com informações válidas")
    public void shouldGetAllProductsSuccessfullyWithValidInformation() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        ProductDTO product2 = productService.createProduct(category.getId(), token);
        given()
                .spec(requestSpec(token))
                .when()
                .get("/products")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("size()", greaterThanOrEqualTo(2))
                .body("content.id", hasItems(product1.getId().intValue(), product2.getId().intValue()))
                .body("content.name", hasItems(product1.getName(), product2.getName()));
    }

    @Test
    @Story("Mostrar produto por id")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-012: Valida mostrar produto por id com informações válidas")
    @DisplayName("Deve ter sucesso ao pegar/listar produto com informações válidas")
    public void shouldGetProductSuccessfullyWithValidInformation(){
        CategoryDTO newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Long categoryId = newCategory.getId();
        ProductDTO newProduct = productService.createProduct(categoryId, token);

        given()
                .spec(requestSpec(token))
                .when()
                .get("/products/{id}", newProduct.getId())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("sku", notNullValue())
                .body("sku", equalTo(newProduct.getSku()))
                .body("categoryId", equalTo(categoryId.intValue()))
                .body("priceCents", equalTo(newProduct.getPriceCents().intValue()))
                .body("stockQuantity", equalTo(newProduct.getStockQuantity()));
    }

    @Test
    @Story("Criar produto")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-107: Valida o bloqueio da criação de produto com nome já existente")
    @DisplayName("Deve falhar ao criar produto com nome de produto já existente")
    public void shouldFailToPostProductToExistingProductName() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product = productService.createProduct(category.getId(), token);
        ProductDTO productSameName = ProductDTO.builder()
                .sku(product.getSku() + 1)
                .name(product.getName())
                .priceCents(product.getPriceCents())
                .categoryId(product.getCategoryId())
                .stockQuantity(product.getStockQuantity())
                .currency(product.getCurrency())
                .active(product.isActive())
                .build();

        given()
                .spec(requestSpec(token))
                .body(productSameName)
                .when()
                .post("/admin/products")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode401());
    }

    @Test
    @Story("Deletar produto")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-108: Valida mensagem de erro ao deletar produto inexistente")
    @DisplayName("Deve falhar ao deletar produto inexistente")
    public void shouldFailDeleteNonExistingProduct() {
        String token = userService.loginUserAdmin();
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/admin/products/{id}", 100000)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode404());
    }

    @Test
    @Story("Alterar produto")
    @Severity(SeverityLevel.NORMAL)
    @Description("CT-109: Valida o bloqueio ao alterar produto com nome já existente")
    @DisplayName("Deve falhar ao alterar produto com nome de outro produto existente")
    public void shouldFailToPutProductToExistingProductName() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        ProductDTO product1 = productService.createProduct(category.getId(), token);
        ProductDTO product2 = productService.createProduct(category.getId(), token);
        Integer id = product2.getId().intValue();
        ProductDTO productSameName = ProductDTO.builder()
                .sku(product2.getSku())
                .name(product1.getName())
                .priceCents(product2.getPriceCents())
                .categoryId(product2.getCategoryId())
                .stockQuantity(product2.getStockQuantity())
                .currency(product2.getCurrency())
                .active(product2.isActive())
                .build();

        given()
                .spec(requestSpec(token))
                .body(productSameName)
                .when()
                .put("/admin/products/{id}", id)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode409());
    }
}