package br.com.ecommerce.model;

public class Cart {
    private Integer productId;
    private Integer quantity;

    public Cart(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Cart() {
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
