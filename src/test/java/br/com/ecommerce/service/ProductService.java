package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.ProductDTO;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;

public class ProductService {
    @Step("Criando um produto de teste na API")
    public ProductDTO createProduct(Long categoryId, String token){
        ProductDTO newProduct = DataFactory.createRandomProduct(categoryId);

        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newProduct)
                .when()
                .post("/admin/products")
                .then()
                .statusCode(201)
                .extract()
                .as(ProductDTO.class);
    }
}
