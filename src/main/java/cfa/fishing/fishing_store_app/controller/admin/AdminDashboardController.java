//package cfa.fishing.fishing_store_app.controller.admin;
//
//import cfa.fishing.fishing_store_app.dto.response.DashboardStatsResponse;
//import cfa.fishing.fishing_store_app.dto.response.InventoryStatsResponse;
//import cfa.fishing.fishing_store_app.dto.response.OrderResponse;
//import cfa.fishing.fishing_store_app.dto.response.SalesStatsResponse;
//import cfa.fishing.fishing_store_app.service.admin.AdminDashboardService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/dashboard")
//@PreAuthorize("hasRole('ADMIN')")
//@RequiredArgsConstructor
//public class AdminDashboardController {
//
//    private final AdminDashboardService dashboardService;
//
//    @GetMapping(value = "/orders")
//    public ResponseEntity<List<OrderResponse>> getAllOrders(
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        return ResponseEntity.ok(dashboardService.getAllOrders(startDate, endDate));
//    }
//
//    @GetMapping("/stats")
//    public ResponseEntity<DashboardStatsResponse> getDashboardStats(
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        LocalDate end = endDate != null ? endDate : LocalDate.now();
//        LocalDate start = startDate != null ? startDate : end.minusDays(30);
//
//        return ResponseEntity.ok(dashboardService.getDashboardStats(start, end));
//    }
//
//    @GetMapping("/inventory")
//    public ResponseEntity<InventoryStatsResponse> getInventoryStats() {
//        return ResponseEntity.ok(dashboardService.getInventoryStats());
//    }
//
////    TODO: fix this
//    @GetMapping("/sales")
//    public ResponseEntity<SalesStatsResponse> getSalesStats(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        return ResponseEntity.ok(dashboardService.getSalesStats(startDate, endDate));
//    }
//}