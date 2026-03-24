package br.com.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseItemDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Long unitPriceSnapshot;
}
