package br.com.ecommerce.dto;

import br.com.ecommerce.dto.response.CartResponseItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String status;
    private Long totalCents;
    private OffsetDateTime createdAt;
    private List<OrderItemDTO> items;
}
