package br.com.ecommerce.model;

public class Product {
    private Integer id;
    private String sku;
    private String name;
    private Integer categoryId;
    private Integer priceCents;
    private String currency;
    private boolean active;
    private Integer stockQuantity;

    public Product(String sku, String name, Integer categoryId, Integer priceCents, String currency, boolean active, Integer stockQuantity) {
        this.sku = sku;
        this.name = name;
        this.categoryId = categoryId;
        this.priceCents = priceCents;
        this.currency = currency;
        this.active = active;
        this.stockQuantity = stockQuantity;
    }
    public Product(Integer id, String sku, String name, Integer categoryId, Integer priceCents, String currency, boolean active, Integer stockQuantity) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.categoryId = categoryId;
        this.priceCents = priceCents;
        this.currency = currency;
        this.active = active;
        this.stockQuantity = stockQuantity;
    }

    public Product(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(Integer priceCents) {
        this.priceCents = priceCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
