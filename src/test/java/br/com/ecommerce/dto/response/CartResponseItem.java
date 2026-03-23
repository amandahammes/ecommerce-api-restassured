package br.com.ecommerce.dto.response;

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
    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Long unitPrice;
    private Long subTotal;
}