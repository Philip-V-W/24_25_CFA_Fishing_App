package cfa.fishing.fishing_store_app.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalOrders;
    private long newOrders;
    private long pendingPermits;
    private long upcomingContests;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
    private long totalCustomers;
    private long newCustomers;
    private ProductCategoryStatsResponse topCategories;
}