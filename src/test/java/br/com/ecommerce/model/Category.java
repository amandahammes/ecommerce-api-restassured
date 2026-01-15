package br.com.ecommerce.model;

public class Category {
    private String name;
    private String description;
    private Integer id;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public Category(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }
}
