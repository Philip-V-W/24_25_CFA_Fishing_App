package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.dto.response.InventoryStatsResponse;
import cfa.fishing.fishing_store_app.dto.response.ProductCategoryStatsResponse;
import cfa.fishing.fishing_store_app.entity.product.Product;
import cfa.fishing.fishing_store_app.entity.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByActiveTrue();

    List<Product> findByStockQuantityLessThan(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.name LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.category = :category AND p.stockQuantity > 0")
    List<Product> findAvailableProductsByCategory(@Param("category") ProductCategory category);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity <= 5 AND p.stockQuantity > 0")
    long countLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity = 0")
    long countOutOfStockProducts();

    @Query("SELECT new map(" +
            "p.id as productId, " +
            "p.name as productName, " +
            "p.stockQuantity as currentStock, " +
            "5 as minimumStock) " +
            "FROM Product p WHERE p.stockQuantity <= 5")
    List<Map<String, Object>> findLowStockProducts();

    @Query("SELECT new map(" +
            "p.id as productId, " +
            "p.name as productName, " +
            "COUNT(oi) as totalSold, " +
            "p.stockQuantity as currentStock) " +
            "FROM Product p " +
            "LEFT JOIN OrderItem oi ON p.id = oi.product.id " +
            "GROUP BY p.id, p.name, p.stockQuantity " +
            "ORDER BY COUNT(oi) DESC")
    List<Map<String, Object>> findTopSellingProducts();

    @Query("SELECT new map(" +
            "p.category as category, " +
            "COUNT(p) as totalProducts, " +
            "COUNT(oi) as totalSold, " +
            "SUM(CASE WHEN oi IS NOT NULL THEN oi.price * oi.quantity ELSE 0 END) as revenue) " +
            "FROM Product p " +
            "LEFT JOIN OrderItem oi ON p.id = oi.product.id " +
            "GROUP BY p.category")
    List<Map<String, Object>> findCategoryStats(LocalDateTime start, LocalDateTime end);
}