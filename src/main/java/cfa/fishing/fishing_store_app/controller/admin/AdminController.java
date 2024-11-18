package cfa.fishing.fishing_store_app.controller.admin;

import cfa.fishing.fishing_store_app.dto.request.CategoryRequest;
import cfa.fishing.fishing_store_app.dto.request.ProductRequest;
import cfa.fishing.fishing_store_app.dto.response.*;
import cfa.fishing.fishing_store_app.entity.order.OrderStatus;
import cfa.fishing.fishing_store_app.service.order.OrderService;
import cfa.fishing.fishing_store_app.service.permit.PermitService;
import cfa.fishing.fishing_store_app.service.product.ProductService;
import cfa.fishing.fishing_store_app.service.admin.AdminDashboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final AdminDashboardService dashboardService;
    private final OrderService orderService;
    private final PermitService permitService;

    // View endpoints
    @GetMapping("/views/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/views/products")
    public String products() {
        return "admin/products";
    }

    @GetMapping("/views/orders")
    public String orders() {
        return "admin/orders";
    }

    @GetMapping("/views/customers")
    public String customers() {
        return "admin/customers";
    }

    @GetMapping("/views/permits")
    public String permits() {
        return "admin/permits";
    }

    @GetMapping("/views/contests")
    public String contests() {
        return "admin/contests";
    }

    // Dashboard API endpoints
    @GetMapping("/dashboard/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getAllOrders(startDate, endDate));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        LocalDate start = startDate != null ? startDate : end.minusDays(30);
        return ResponseEntity.ok(dashboardService.getDashboardStats(start, end));
    }

    @GetMapping("/dashboard/inventory")
    public ResponseEntity<InventoryStatsResponse> getInventoryStats() {
        return ResponseEntity.ok(dashboardService.getInventoryStats());
    }

    @GetMapping("/dashboard/sales")
    public ResponseEntity<SalesStatsResponse> getSalesStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getSalesStats(startDate, endDate));
    }

    // Product API endpoints
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PatchMapping("/products/{productId}/toggle-status")
    public ResponseEntity<Void> toggleProductStatus(@PathVariable Long productId) {
        productService.toggleProductStatus(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAdminProducts() {
        return ResponseEntity.ok(productService.getAllProductsForAdmin());
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(productId, request));
    }

    // Order API endpoints
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // Permit API endpoints
    @GetMapping("/all")
    public ResponseEntity<List<PermitResponse>> getAllPermits() {
        return ResponseEntity.ok(permitService.getAllPermits());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PermitResponse>> getPermitsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(permitService.getPermitsByUserId(userId));
    }

    @PatchMapping("/{permitId}/approve")
    public ResponseEntity<PermitResponse> approvePermit(@PathVariable Long permitId) {
        return ResponseEntity.ok(permitService.approvePermit(permitId));
    }

    @PatchMapping("/{permitId}/reject")
    public ResponseEntity<PermitResponse> rejectPermit(
            @PathVariable Long permitId,
            @RequestParam String reason) {
        return ResponseEntity.ok(permitService.rejectPermit(permitId, reason));
    }

    // Category API endpoints
    @GetMapping
    public ResponseEntity<List<CategoryStatsResponse>> getAllCategoriesWithStats() {
        return ResponseEntity.ok(productService.getAllCategoriesWithStats());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(productService.createCategory(request));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId) {
        productService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(productService.updateCategory(categoryId, request));
    }
}