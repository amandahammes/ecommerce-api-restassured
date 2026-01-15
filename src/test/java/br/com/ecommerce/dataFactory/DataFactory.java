package br.com.ecommerce.dataFactory;

import br.com.ecommerce.model.Category;
import br.com.ecommerce.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.io.File;
import java.io.IOException;

public class DataFactory {

    private static final Faker faker = new Faker();

    public static User createRandomUser() {
        return new User(
                faker.internet().emailAddress(),
                faker.internet().password(8,15,true,true),
                "ADMIN"
        );
    }

    public static Category createRandomCategory() {
        return new Category(
                faker.commerce().department(),
                faker.lorem().sentence(5)
        );
    }

    public static Category loadCategoryData(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    new File("src/test/resources/category.json"),
                    Category.class
            );
        } catch (IOException e) {
            throw new RuntimeException("Error reading login JSON file.", e);
        }
    }

    public static User loadUserData(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    new File("src/test/resources/user.json"),
                    User.class
            );
        } catch (IOException e) {
            throw new RuntimeException("Error reading login JSON file.", e);
        }
    }
}