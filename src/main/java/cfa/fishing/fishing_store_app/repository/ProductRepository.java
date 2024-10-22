package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.entity.product.Product;
import cfa.fishing.fishing_store_app.entity.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByActiveTrue();

    List<Product> findByStockQuantityLessThan(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.name LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.category = :category AND p.stockQuantity > 0")
    List<Product> findAvailableProductsByCategory(@Param("category") ProductCategory category);
}