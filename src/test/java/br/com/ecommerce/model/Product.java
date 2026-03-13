package br.com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer id;
    private String sku;
    private String name;
    private Integer categoryId;
    private Integer priceCents;
    private String currency;
    private boolean active;
    private Integer stockQuantity;
}
