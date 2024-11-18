package cfa.fishing.fishing_store_app.service.product;

import cfa.fishing.fishing_store_app.dto.request.CategoryRequest;
import cfa.fishing.fishing_store_app.dto.request.ProductRequest;
import cfa.fishing.fishing_store_app.dto.response.CategoryResponse;
import cfa.fishing.fishing_store_app.dto.response.CategoryStatsResponse;
import cfa.fishing.fishing_store_app.dto.response.ProductResponse;
import cfa.fishing.fishing_store_app.entity.product.ProductCategory;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    ProductResponse getProduct(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategory(ProductCategory category);
    void deleteProduct(Long id);
    List<ProductResponse> searchProducts(String keyword);
    void updateStock(Long id, Integer quantity);
    void toggleProductStatus(Long id);
    List<CategoryStatsResponse> getAllCategoriesWithStats();
    CategoryResponse createCategory(CategoryRequest request);
    void deleteCategory(String categoryId);
    CategoryResponse updateCategory(String categoryId, CategoryRequest request);
    List<ProductResponse> getAllProductsForAdmin();
}
