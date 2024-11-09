package cfa.fishing.fishing_store_app.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class SalesStatsResponse {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private BigDecimal averageOrderValue;
    private List<DailyStats> dailyStats;
    private Map<String, BigDecimal> revenueByCategory;
    private List<TopCustomer> topCustomers;

    @Data
    @Builder
    public static class DailyStats {
        private String date;
        private long orders;
        private BigDecimal revenue;
    }

    @Data
    @Builder
    public static class TopCustomer {
        private Long userId;
        private String email;
        private long orderCount;
        private BigDecimal totalSpent;
    }
}