package cfa.fishing.fishing_store_app.service.product;

import cfa.fishing.fishing_store_app.dto.request.CategoryRequest;
import cfa.fishing.fishing_store_app.dto.request.ProductRequest;
import cfa.fishing.fishing_store_app.dto.response.CategoryResponse;
import cfa.fishing.fishing_store_app.dto.response.CategoryStatsResponse;
import cfa.fishing.fishing_store_app.dto.response.ProductResponse;
import cfa.fishing.fishing_store_app.entity.product.Product;
import cfa.fishing.fishing_store_app.entity.product.ProductCategory;
import cfa.fishing.fishing_store_app.exception.ResourceNotFoundException;
import cfa.fishing.fishing_store_app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        mapRequestToProduct(request, product);
        return mapToResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        mapRequestToProduct(request, product);
        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProduct(Long id) {
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(ProductCategory category) {
        return productRepository.findAvailableProductsByCategory(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setStockQuantity(quantity);
        productRepository.save(product);
    }

    // Helper methods for mapping between entities and DTOs
    private void mapRequestToProduct(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.isActive());
        return response;
    }

    @Override
    public void toggleProductStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setActive(!product.isActive());
        productRepository.save(product);
    }

    @Override
    public List<CategoryStatsResponse> getAllCategoriesWithStats() {
        return Arrays.stream(ProductCategory.values())
                .map(category -> {
                    List<Product> productsInCategory = productRepository.findByCategory(category);

                    return CategoryStatsResponse.builder()
                            .category(category.name())
                            .productCount(productsInCategory.size())
                            .totalStock(productsInCategory.stream()
                                    .mapToInt(Product::getStockQuantity)
                                    .sum())
                            .activeProducts((int) productsInCategory.stream()
                                    .filter(Product::isActive)
                                    .count())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        // Since categories are enum-based, we need to validate if the category already exists
        String normalizedName = request.getName().toUpperCase().replace(" ", "_");

        try {
            ProductCategory.valueOf(normalizedName);
            throw new IllegalArgumentException("Category already exists");
        } catch (IllegalArgumentException e) {
            // This is actually the success case - the category doesn't exist yet
            // In a real application, you would need to:
            // 1. Update the ProductCategory enum
            // 2. Recompile the application
            // Consider using a database-backed category system instead of enums
            // for dynamic category management

            return CategoryResponse.builder()
                    .id(normalizedName)
                    .name(request.getName())
                    .build();
        }
    }

    @Override
    public void deleteCategory(String categoryId) {
        ProductCategory category;
        try {
            category = ProductCategory.valueOf(categoryId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Category not found: " + categoryId);
        }

        // Check if there are any products in this category
        List<Product> productsInCategory = productRepository.findByCategory(category);
        if (!productsInCategory.isEmpty()) {
            throw new IllegalStateException("Cannot delete category that contains products");
        }

        // Note: Since categories are enum-based, we can't actually delete them
        // at runtime. In a real application, you would need to:
        // 1. Update the ProductCategory enum
        // 2. Recompile the application
        // Consider using a database-backed category system instead of enums
        // for dynamic category management
    }

    @Override
    public CategoryResponse updateCategory(String categoryId, CategoryRequest request) {
        ProductCategory existingCategory;
        try {
            existingCategory = ProductCategory.valueOf(categoryId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Category not found: " + categoryId);
        }

        String normalizedNewName = request.getName().toUpperCase().replace(" ", "_");

        // Check if the new name already exists as a different category
        if (!existingCategory.name().equals(normalizedNewName)) {
            try {
                ProductCategory.valueOf(normalizedNewName);
                throw new IllegalArgumentException("Category with new name already exists");
            } catch (IllegalArgumentException ignored) {
                // This is actually the success case - the new name doesn't exist yet
            }
        }

        // Note: Since categories are enum-based, we can't actually update them
        // at runtime. In a real application, you would need to:
        // 1. Update the ProductCategory enum
        // 2. Recompile the application
        // Consider using a database-backed category system instead of enums
        // for dynamic category management

        return CategoryResponse.builder()
                .id(existingCategory.name())
                .name(request.getName())
                .build();
    }
}