package br.com.ecommerce.dataFactory;

import br.com.ecommerce.model.Cart;
import br.com.ecommerce.model.Category;
import br.com.ecommerce.model.Product;
import br.com.ecommerce.model.User;
import com.github.javafaker.Faker;


public class DataFactory {

    private static final Faker faker = new Faker();

    public static User createRandomUser() {
        return User.builder()
                .name(faker.name().firstName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(8,15,true,true))
                .role("ADMIN")
                .build();
    }

    public static Category createRandomCategory() {
        return Category.builder()
                .name(faker.commerce().department())
                .description(faker.lorem().sentence(5))
                .build();
    }

    public static Product createRandomProduct(Integer categoryId) {
        return Product.builder()
                .sku(faker.number().digits(5))
                .name(faker.commerce().productName())
                .categoryId(categoryId)
                .priceCents(faker.number().numberBetween(1000, 10000))
                .currency("R$")
                .active(true)
                .stockQuantity(200)
                .build();
    }

    public static Cart createCartItem(Integer productId) {
        return Cart.builder()
            .productId(productId)
            .quantity(faker.number().numberBetween(1, 5))
            .build();
    }
}