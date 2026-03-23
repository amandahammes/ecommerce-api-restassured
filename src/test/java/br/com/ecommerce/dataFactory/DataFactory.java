package br.com.ecommerce.dataFactory;

import br.com.ecommerce.dto.request.CartRequest;
import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.dto.UserDTO;
import com.github.javafaker.Faker;


public class DataFactory {

    private static final Faker faker = new Faker();

    public static UserDTO createRandomUser() {
        return UserDTO.builder()
                .name(faker.name().firstName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(8,15,true,true))
                .role("ADMIN")
                .build();
    }

    public static CategoryDTO createRandomCategory() {
        return CategoryDTO.builder()
                .name(faker.commerce().department()+ " " + faker.number().digits(4))
                .description(faker.lorem().sentence(5))
                .build();
    }

    public static String createRandomProductName() {
        return faker.commerce().productName();
    }

    public static ProductDTO createRandomProduct(Long categoryId) {
        return ProductDTO.builder()
                .sku(faker.number().digits(5))
                .name(faker.commerce().productName())
                .categoryId(categoryId)
                .priceCents(faker.number().numberBetween(100L, 10000L))
                .currency("R$")
                .active(true)
                .stockQuantity(200)
                .build();
    }

    public static String createRandomCategoryName() {
        return faker.commerce().department();
    }

    public static CartRequest createCartItem(Long productId) {
        return CartRequest.builder()
            .productId(productId)
            .quantity(faker.number().numberBetween(1, 2))
            .build();
    }
}