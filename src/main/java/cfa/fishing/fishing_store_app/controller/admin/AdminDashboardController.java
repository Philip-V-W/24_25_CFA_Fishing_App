package cfa.fishing.fishing_store_app.controller.admin;

import cfa.fishing.fishing_store_app.dto.response.DashboardStatsResponse;
import cfa.fishing.fishing_store_app.dto.response.InventoryStatsResponse;
import cfa.fishing.fishing_store_app.dto.response.SalesStatsResponse;
import cfa.fishing.fishing_store_app.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getDashboardStats(startDate, endDate));
    }

    @GetMapping("/inventory")
    public ResponseEntity<InventoryStatsResponse> getInventoryStats() {
        return ResponseEntity.ok(dashboardService.getInventoryStats());
    }

    @GetMapping("/sales")
    public ResponseEntity<SalesStatsResponse> getSalesStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getSalesStats(startDate, endDate));
    }
}