package cfa.fishing.fishing_store_app.controller.api;

import cfa.fishing.fishing_store_app.dto.request.OrderRequest;
import cfa.fishing.fishing_store_app.dto.response.OrderResponse;
import cfa.fishing.fishing_store_app.dto.response.PaymentResponse;
import cfa.fishing.fishing_store_app.entity.order.OrderStatus;
import cfa.fishing.fishing_store_app.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getUserOrders(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(userDetails.getUsername(), request));
    }

    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<OrderResponse> confirmPayment(
            @PathVariable Long orderId,
            @RequestParam String paymentIntentId) {
        return ResponseEntity.ok(orderService.confirmPayment(orderId, paymentIntentId));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(userDetails.getUsername(), orderId));
    }

    // Admin endpoints
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}