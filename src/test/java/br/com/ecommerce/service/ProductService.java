package br.com.ecommerce.service;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.model.Product;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;

public class ProductService {

    public Product createProduct(Integer categoryId, String token){
        Product newProduct = DataFactory.createRandomProduct(categoryId);

        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(newProduct)
                .when()
                .post("/admin/products")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(Product.class);
    }
}
