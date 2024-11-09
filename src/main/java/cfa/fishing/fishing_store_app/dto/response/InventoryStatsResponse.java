package cfa.fishing.fishing_store_app.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class InventoryStatsResponse {
    private long totalProducts;
    private long lowStockProducts;
    private long outOfStockProducts;
    private List<ProductStockAlert> stockAlerts;
    private List<TopSellingProduct> topSellingProducts;

    @Data
    @Builder
    public static class ProductStockAlert {
        private Long productId;
        private String productName;
        private Integer currentStock;
        private Integer minimumStock;
    }

    @Data
    @Builder
    public static class TopSellingProduct {
        private Long productId;
        private String productName;
        private Long totalSold;
        private Integer currentStock;
    }
}