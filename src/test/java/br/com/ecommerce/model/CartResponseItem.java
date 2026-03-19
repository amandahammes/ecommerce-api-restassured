package br.com.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartResponseItem {
    private Integer itemId;
    private Integer produtcId;
    private String productName;
    private Integer quantity;
    private Integer unitPrice;
    private Integer subTotal;
}