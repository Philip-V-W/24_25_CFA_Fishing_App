package cfa.fishing.fishing_store_app.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CategoryStatsResponse {
    private String category;
    private int productCount;
    private int totalStock;
    private int activeProducts;
}