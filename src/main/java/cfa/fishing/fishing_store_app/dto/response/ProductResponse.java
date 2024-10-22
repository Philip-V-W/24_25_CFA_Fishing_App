package cfa.fishing.fishing_store_app.dto.response;

import cfa.fishing.fishing_store_app.entity.product.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private ProductCategory category;
    private String imageUrl;
    private boolean active;
}