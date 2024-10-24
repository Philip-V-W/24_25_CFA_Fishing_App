package cfa.fishing.fishing_store_app.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}