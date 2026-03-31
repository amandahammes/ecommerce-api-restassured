package br.com.ecommerce.test;

import br.com.ecommerce.dataFactory.DataFactory;
import br.com.ecommerce.dto.CategoryDTO;
import br.com.ecommerce.dto.ProductDTO;
import br.com.ecommerce.service.CategoryService;
import br.com.ecommerce.service.ProductService;
import br.com.ecommerce.service.UserService;
import br.com.ecommerce.test.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;

import static br.com.ecommerce.dataFactory.DataFactory.faker;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@Epic("Gestão de Ecommerce")
@Feature("Fluxo de Categorias")
public class CategoryTest extends BaseTest {

    private UserService userService = new UserService();
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private DataFactory dataFactory = new DataFactory();

    @Test
    @DisplayName("Deve ter sucesso ao criar Categoria com informações válidas")
    public void shouldCreateCategorySuccessfullyWithValidInformation(){
        CategoryDTO newCategory = DataFactory.createRandomCategory();
        String token = userService.loginUserAdmin();
        given()
                .spec(requestSpec(token))
                .body(newCategory)
                .when()
                .post("/categories/admin")
                .then()
                .spec(responseSpecCode201CreatedContent())
                .body("id", notNullValue())
                .body("id", instanceOf(Integer.class))
                .body("name", equalTo(newCategory.getName()))
                .body("description", equalTo(newCategory.getDescription()));
    }
    @Test
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetCategorySuccessfullyWithValidInformation() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .get("/categories/{id}", category.getId())
                .then()
                .spec(responseSpecCode200())
                .body("id", equalTo(category.getId().intValue()))
                .body("name", equalTo(category.getName()))
                .body("description", equalTo(category.getDescription()));
    }
    @Test
    @DisplayName("Deve ter sucesso ao alterar Categoria com informações válidas")
    public void shouldPutCategorySuccessfullyWithValidInformation() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        String categoryName = dataFactory.createRandomCategoryName();
        category.setName(categoryName);
        given()
                .spec(requestSpec(token))
                .body(category)
                .when()
                .put("/categories/admin/{id}", category.getId())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("id", equalTo(category.getId().intValue()))
                .body("name", equalTo(categoryName));
    }

    @Test
    @DisplayName("Deve ter sucesso ao deletar Categoria")
    public void shouldDeleteCategorySuccessfullyWithValidInformation() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/categories/admin/{id}", category.getId().intValue())
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode204());
    }

    @Test
    @DisplayName("Deve ter sucesso ao pegar Categoria com informações válidas")
    public void shouldGetAllCategoriesSuccessfullyWithValidInformation() {
        CategoryDTO category1 = categoryService.createCategory();
        CategoryDTO category2 = categoryService.createCategory();
        String token = categoryService.getToken();
        given()
                .spec(requestSpec(token))
                .when()
                .get("/categories")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode200())
                .body("size()", greaterThanOrEqualTo(2))
                .body("content.id", hasItems(category1.getId().intValue(), category2.getId().intValue()))
                .body("content.name", hasItems(category1.getName(), category2.getName()));;
    }

    @Test
    @DisplayName("Deve gerar mensagem de erro ao criar Categoria nome de categoria já existente")
    public void shouldFailToCreateCategoryWithExistingCategoryName(){
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        CategoryDTO categorySameName = CategoryDTO.builder()
                .name(category.getName())
                .description(faker.lorem().sentence(5))
                .build();
        given()
                .spec(requestSpec(token))
                .body(categorySameName)
                .when()
                .post("/categories/admin")
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode400());
    }

    @Test
    @DisplayName("Deve gerar mensagem de erro ao deletar Categoria inexistente")
    public void shouldFailToDeleteNonExistingCategory(){
        String token = userService.loginUserAdmin();
        given()
                .spec(requestSpec(token))
                .when()
                .delete("/categories/admin/{id}", 100000)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode404())
                .body("message", equalTo("Impossível excluir: Categoria não encontrada."));
    }

    @Test
    @DisplayName("Deve gerar mensagem de erro ao deletar Categoria com produto vinculado")
    public void shouldFailToDeleteCategoryWithLinkedProduct() {
        CategoryDTO category = categoryService.createCategory();
        String token = categoryService.getToken();
        Long idCategory = category.getId();
        ProductDTO produto = productService.createProduct(category.getId(), token);

        given()
                .spec(requestSpec(token))
                .when()
                .delete("/categories/admin/{id}", idCategory)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode409());
    }

    @Test
    @DisplayName("Deve gerar mensagem de erro ao alterar nome da Categoria para uma já existente")
    public void shouldFailToPutCategoryToExistingCategoryName() {
        CategoryDTO category1 = categoryService.createCategory();
        String token = categoryService.getToken();
        Long idCategory = category1.getId();
        CategoryDTO category2 = categoryService.createCategory();
        CategoryDTO categorySameName = CategoryDTO.builder()
                .name(category2.getName())
                .description(category1.getDescription())
                .build();
        given()
                .spec(requestSpec(token))
                .body(categorySameName)
                .when()
                .put("/categories/admin/{id}", idCategory)
                .then()
                .log().ifValidationFails()
                .spec(responseSpecCode409());
    }
}
