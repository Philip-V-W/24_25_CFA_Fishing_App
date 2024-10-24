package cfa.fishing.fishing_store_app.service.order;

import cfa.fishing.fishing_store_app.dto.request.OrderRequest;
import cfa.fishing.fishing_store_app.dto.response.OrderItemResponse;
import cfa.fishing.fishing_store_app.dto.response.OrderResponse;
import cfa.fishing.fishing_store_app.dto.response.PaymentResponse;
import cfa.fishing.fishing_store_app.entity.order.Order;
import cfa.fishing.fishing_store_app.entity.order.OrderItem;
import cfa.fishing.fishing_store_app.entity.order.OrderStatus;
import cfa.fishing.fishing_store_app.entity.product.Product;
import cfa.fishing.fishing_store_app.entity.user.User;
import cfa.fishing.fishing_store_app.exception.ResourceNotFoundException;
import cfa.fishing.fishing_store_app.repository.OrderRepository;
import cfa.fishing.fishing_store_app.repository.ProductRepository;
import cfa.fishing.fishing_store_app.repository.UserRepository;
import cfa.fishing.fishing_store_app.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    @Transactional
    public PaymentResponse createOrder(String userEmail, OrderRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());

        request.getItems().forEach(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem(product, itemRequest.getQuantity());
            order.addItem(orderItem);
        });

        Order savedOrder = orderRepository.save(order);
        return paymentService.createPaymentIntent(savedOrder);
    }

    @Transactional
    public OrderResponse confirmPayment(Long orderId, String paymentIntentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(OrderStatus.PAID);

        // Update product stock
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        });

        return mapToOrderResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse cancelOrder(String userEmail, Long orderId) {
        Order order = getOrderForUser(userEmail, orderId);

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Order cannot be cancelled in current status: " + order.getStatus());
        }

        // Restore product stock
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        });

        order.setStatus(OrderStatus.CANCELLED);
        return mapToOrderResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);

        return mapToOrderResponse(orderRepository.save(order));
    }

    public OrderResponse getOrder(String userEmail, Long orderId) {
        return mapToOrderResponse(getOrderForUser(userEmail, orderId));
    }

    public List<OrderResponse> getUserOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return orderRepository.findByUserOrderByOrderDateDesc(user).stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    private Order getOrderForUser(String userEmail, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("Not authorized to access this order");
        }

        return order;
    }

    private void validateOrderRequest(OrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        if (request.getShippingAddress() == null || request.getShippingAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address is required");
        }
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // TODO: add more logic to validate status PENDING -> PAID -> PROCESSING -> SHIPPED -> DELIVERED
        if (currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status of cancelled order");
        }
        if (currentStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot update status of delivered order");
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerEmail(order.getUser().getEmail());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setTrackingNumber(order.getTrackingNumber());

        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());
        response.setItems(items);

        return response;
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }
}