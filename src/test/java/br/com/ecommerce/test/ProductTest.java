package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Category;
import br.com.ecommerce.model.Product;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.util.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class ProductTest {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = ConfigLoader.getProperty("base_url");
    }

    @Test
    @DisplayName("Deve ter sucesso ao criar Produto com informações válidas")
    public void shouldCreateProductSuccessfullyWithValidInformation(){
        Category newCategory = CategoryService.createCategory();
        String token = CategoryService.getToken();
        Integer categoryId = newCategory.getId();
        Product product = DataFactory.createRandomProduct(categoryId);
        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(product)
                .when()
                .post("/admin/products")
                .then()
                .log().all()
                .statusCode(201)
                .body("sku", notNullValue())
                .body("categoryId", equalTo(categoryId))
                .body("priceCents", equalTo(product.getPriceCents()))
                .body("stockQuantity", equalTo(product.getStockQuantity()));
    }

    @Test
    @DisplayName("Deve ter sucesso ao pegar/listar Produto com informações válidas")
    public void shouldGetProductSuccessfullyWithValidInformation(){
        Category newCategory = CategoryService.createCategory();
        String token = CategoryService.getToken();
        Integer categoryId = newCategory.getId();
        Product newProduct = ProductService.createProduct(categoryId, token);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/products/{id}", newProduct.getId())
                .then()
                .log().all()
                .statusCode(302)
                .body("sku", notNullValue())
                .body("sku", equalTo(newProduct.getSku()))
                .body("categoryId", equalTo(categoryId))
                .body("priceCents", equalTo(newProduct.getPriceCents()))
                .body("stockQuantity", equalTo(newProduct.getStockQuantity()));
    }
}
