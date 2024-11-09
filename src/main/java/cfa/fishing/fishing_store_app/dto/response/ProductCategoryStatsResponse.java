package cfa.fishing.fishing_store_app.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductCategoryStatsResponse {
    private List<CategoryStats> categories;

    @Data
    @Builder
    public static class CategoryStats {
        private String category;
        private long totalProducts;
        private long totalSold;
        private BigDecimal revenue;
    }
}