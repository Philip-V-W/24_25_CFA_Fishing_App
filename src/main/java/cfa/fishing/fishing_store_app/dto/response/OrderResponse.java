package cfa.fishing.fishing_store_app.dto.response;

import cfa.fishing.fishing_store_app.entity.order.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String customerEmail;
    private List<OrderItemResponse> items;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String trackingNumber;
}