package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.dto.response.SalesStatsResponse;
import cfa.fishing.fishing_store_app.entity.order.Order;
import cfa.fishing.fishing_store_app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import cfa.fishing.fishing_store_app.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findByUserOrderByOrderDateDesc(User user);

    long countByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    long countByStatusAndOrderDateBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2")
    BigDecimal calculateTotalRevenueBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2")
    BigDecimal calculateAverageOrderValueBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new map(" +
            "CAST(o.orderDate as date) as date, " +
            "COUNT(o) as orders, " +
            "SUM(o.totalAmount) as revenue) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN ?1 AND ?2 " +
            "GROUP BY CAST(o.orderDate as date)")
    List<Map<String, Object>> findDailyStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new map(" +
            "o.user.id as userId, " +
            "o.user.email as email, " +
            "COUNT(o) as orderCount, " +
            "SUM(o.totalAmount) as totalSpent) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN ?1 AND ?2 " +
            "GROUP BY o.user.id, o.user.email " +
            "ORDER BY SUM(o.totalAmount) DESC")
    List<Map<String, Object>> findTopCustomersBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new map(" +
            "p.category as category, " +
            "SUM(oi.price * oi.quantity) as revenue) " +
            "FROM Order o " +
            "JOIN o.items oi " +
            "JOIN oi.product p " +
            "WHERE o.orderDate BETWEEN ?1 AND ?2 " +
            "GROUP BY p.category")
    List<Map<String, Object>> findRevenueByCategoryBetween(LocalDateTime start, LocalDateTime end);
}