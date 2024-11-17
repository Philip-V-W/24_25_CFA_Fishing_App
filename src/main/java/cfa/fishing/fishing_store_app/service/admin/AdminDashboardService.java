package cfa.fishing.fishing_store_app.service.admin;

import cfa.fishing.fishing_store_app.dto.response.*;
import cfa.fishing.fishing_store_app.entity.order.Order;
import cfa.fishing.fishing_store_app.entity.order.OrderStatus;
import cfa.fishing.fishing_store_app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PermitRepository permitRepository;
    private final ContestRepository contestRepository;

    public DashboardStatsResponse getDashboardStats(LocalDate startDate, LocalDate endDate) {
        return DashboardStatsResponse.builder()
                .totalOrders(orderRepository.countByOrderDateBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .newOrders(orderRepository.countByStatusAndOrderDateBetween(OrderStatus.PENDING,
                        startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .pendingPermits(permitRepository.countPendingPermits())
                .upcomingContests(contestRepository.countUpcomingContests(LocalDate.now().atStartOfDay()))
                .totalRevenue(orderRepository.calculateTotalRevenueBetween(
                        startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .averageOrderValue(orderRepository.calculateAverageOrderValueBetween(
                        startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .totalCustomers(userRepository.countCustomers())
                .newCustomers(userRepository.countCustomersJoinedBetween(
                        startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .topCategories(getTopCategories(startDate, endDate))
                .build();
    }

    public InventoryStatsResponse getInventoryStats() {
        return InventoryStatsResponse.builder()
                .totalProducts(productRepository.count())
                .lowStockProducts(productRepository.countLowStockProducts())
                .outOfStockProducts(productRepository.countOutOfStockProducts())
                .stockAlerts(getStockAlerts())
                .topSellingProducts(getTopSellingProducts())
                .build();
    }

    public SalesStatsResponse getSalesStats(LocalDate startDate, LocalDate endDate) {
        return SalesStatsResponse.builder()
                .totalRevenue(orderRepository.calculateTotalRevenueBetween(
                        startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .totalOrders(orderRepository.countByOrderDateBetween(
                        startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .averageOrderValue(orderRepository.calculateAverageOrderValueBetween(
                        startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .dailyStats(getDailyStats(startDate, endDate))
                .revenueByCategory(getRevenueByCategory(startDate, endDate))
                .topCustomers(getTopCustomers(startDate, endDate))
                .build();
    }

    public List<OrderResponse> getAllOrders(LocalDate startDate, LocalDate endDate) {
        List<Order> orders;
        if (startDate != null && endDate != null) {
            orders = orderRepository.findByOrderDateBetween(
                    startDate.atStartOfDay(),
                    endDate.atTime(23, 59, 59));
        } else {
            orders = orderRepository.findAllByOrderByOrderDateDesc();
        }

        return orders.stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .customerEmail(order.getUser().getEmail())
                        .orderDate(order.getOrderDate())
                        .status(order.getStatus())
                        .totalAmount(order.getTotalAmount())
                        .shippingAddress(order.getShippingAddress())
                        .trackingNumber(order.getTrackingNumber())
                        .items(order.getItems().stream()
                                .map(item -> OrderItemResponse.builder()
                                        .productId(item.getProduct().getId())
                                        .productName(item.getProduct().getName())
                                        .quantity(item.getQuantity())
                                        .price(item.getPrice())
                                        .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private ProductCategoryStatsResponse getTopCategories(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> statsData = productRepository.findCategoryStats(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59));

        List<ProductCategoryStatsResponse.CategoryStats> categories = statsData.stream()
                .map(stat -> ProductCategoryStatsResponse.CategoryStats.builder()
                        .category(String.valueOf(stat.get("category")))
                        .totalProducts(((Number) stat.get("totalProducts")).longValue())
                        .totalSold(((Number) stat.get("totalSold")).longValue())
                        .revenue(stat.get("revenue") != null ?
                                new BigDecimal(String.valueOf(stat.get("revenue"))) :
                                BigDecimal.ZERO)
                        .build())
                .collect(Collectors.toList());

        return ProductCategoryStatsResponse.builder()
                .categories(categories)
                .build();
    }

    private List<InventoryStatsResponse.ProductStockAlert> getStockAlerts() {
        List<Map<String, Object>> alerts = productRepository.findLowStockProducts();

        return alerts.stream()
                .map(alert -> InventoryStatsResponse.ProductStockAlert.builder()
                        .productId(((Number) alert.get("productId")).longValue())
                        .productName((String) alert.get("productName"))
                        .currentStock(((Number) alert.get("currentStock")).intValue())
                        .minimumStock(((Number) alert.get("minimumStock")).intValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<InventoryStatsResponse.TopSellingProduct> getTopSellingProducts() {
        List<Map<String, Object>> productsData = productRepository.findTopSellingProducts();

        return productsData.stream()
                .map(data -> InventoryStatsResponse.TopSellingProduct.builder()
                        .productId(((Number) data.get("productId")).longValue())
                        .productName((String) data.get("productName"))
                        .totalSold(((Number) data.get("totalSold")).longValue())
                        .currentStock(((Number) data.get("currentStock")).intValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<SalesStatsResponse.DailyStats> getDailyStats(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> statsData = orderRepository.findDailyStats(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );

        return statsData.stream()
                .map(data -> SalesStatsResponse.DailyStats.builder()
                        .date(data.get("date").toString())
                        .orders(((Number) data.get("orders")).longValue())
                        .revenue(new BigDecimal(data.get("revenue").toString()))
                        .build())
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> getRevenueByCategory(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> revenueData = orderRepository.findRevenueByCategoryBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );

        return revenueData.stream()
                .collect(Collectors.toMap(
                        data -> String.valueOf(data.get("category")),
                        data -> data.get("revenue") != null ?
                                new BigDecimal(String.valueOf(data.get("revenue"))) :
                                BigDecimal.ZERO
                ));
    }

    private List<SalesStatsResponse.TopCustomer> getTopCustomers(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> customersData = orderRepository.findTopCustomersBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );

        return customersData.stream()
                .map(data -> SalesStatsResponse.TopCustomer.builder()
                        .userId(((Number) data.get("userId")).longValue())
                        .email((String) data.get("email"))
                        .orderCount(((Number) data.get("orderCount")).longValue())
                        .totalSpent(new BigDecimal(data.get("totalSpent").toString()))
                        .build())
                .collect(Collectors.toList());
    }
}