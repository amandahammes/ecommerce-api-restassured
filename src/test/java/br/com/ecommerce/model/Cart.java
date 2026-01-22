package br.com.ecommerce.model;

public class Cart {
    private String productId;
    private Integer quantity;

    public Cart(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Cart() {
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
