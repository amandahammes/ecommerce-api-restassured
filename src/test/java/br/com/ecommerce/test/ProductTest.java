package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Category;
import br.com.ecommerce.model.Product;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.test.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class ProductTest extends BaseTest {

    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private DataFactory dataFactory = new DataFactory();

    @Test
    @DisplayName("Deve ter sucesso ao criar Produto com informações válidas")
    public void shouldCreateProductSuccessfullyWithValidInformation(){
        Category newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Integer categoryId = newCategory.getId();
        Product product = DataFactory.createRandomProduct(categoryId);
        given()
                .spec(requestSpec(token))
                .body(product)
                .when()
                .post("/admin/products")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode201CreatedContent())
                .body("sku", notNullValue())
                .body("categoryId", equalTo(categoryId))
                .body("priceCents", equalTo(product.getPriceCents()))
                .body("stockQuantity", equalTo(product.getStockQuantity()));
    }

    @Test
    @DisplayName("Deve ter sucesso ao pegar/listar Produto com informações válidas")
    public void shouldGetProductSuccessfullyWithValidInformation(){
        Category newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Integer categoryId = newCategory.getId();
        Product newProduct = productService.createProduct(categoryId, token);

        given()
                .spec(requestSpec(token))
                .when()
                .get("/products/{id}", newProduct.getId())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("sku", notNullValue())
                .body("sku", equalTo(newProduct.getSku()))
                .body("categoryId", equalTo(categoryId))
                .body("priceCents", equalTo(newProduct.getPriceCents()))
                .body("stockQuantity", equalTo(newProduct.getStockQuantity()));
    }
    @Test
    @DisplayName("Deve ter sucesso ao alterar Produto com informações válidas")
    public void shouldPutProductSuccessfullyWithValidInformation(){
        Category newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Integer categoryId = newCategory.getId();
        Product newProduct = productService.createProduct(categoryId, token);
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
                .body("categoryId", equalTo(categoryId))
                .body("priceCents", equalTo(newProduct.getPriceCents()))
                .body("stockQuantity", equalTo(newProduct.getStockQuantity()));
    }

    @Test
    @DisplayName("Deve ter sucesso ao deletar Produto")
    public void shouldDeleteProductSuccessfullyWithValidInformation() {
        Category newCategory = categoryService.createCategory();
        String token = categoryService.getToken();
        Integer categoryId = newCategory.getId();
        Product newProduct = productService.createProduct(categoryId, token);
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/admin/products/{id}", newProduct.getId())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode204());
    }

    @Test
    @DisplayName("Deve ter sucesso ao pegar lista de Produtos com informações válidas")
    public void shouldGetAllProductsSuccessfullyWithValidInformation() {
        Category category = categoryService.createCategory();
        String token = categoryService.getToken();
        Product product1 = productService.createProduct(category.getId(), token);
        Product product2 = productService.createProduct(category.getId(), token);
        given()
                .spec(requestSpec(token))
                .when()
                .get("/products")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("size()", greaterThanOrEqualTo(2))
                .body("content.id", hasItems(product1.getId(), product2.getId()))
                .body("content.name", hasItems(product1.getName(), product2.getName()));
    }
}
