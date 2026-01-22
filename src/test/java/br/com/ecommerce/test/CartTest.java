package br.com.ecommerce.test;

import br.com.ecommerce.util.ConfigLoader;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class CartTest {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = ConfigLoader.getProperty("base_url");
    }


}
