//package cfa.fishing.fishing_store_app.controller.admin;
//
//import cfa.fishing.fishing_store_app.dto.request.ProductRequest;
//import cfa.fishing.fishing_store_app.dto.response.ProductResponse;
//import cfa.fishing.fishing_store_app.service.product.ProductService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/admin/products")
//@PreAuthorize("hasRole('ADMIN')")
//@RequiredArgsConstructor
//public class AdminProductController {
//
//    private final ProductService productService;
//
//    @PostMapping
//    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
//        return ResponseEntity.ok(productService.createProduct(request));
//    }
//
//    @PatchMapping("/{productId}/toggle-status")
//    public ResponseEntity<ProductResponse> toggleProductStatus(@PathVariable Long productId) {
//        ProductResponse product = productService.getProduct(productId);
//        ProductRequest updateRequest = new ProductRequest();
//        updateRequest.setName(product.getName());
//        updateRequest.setDescription(product.getDescription());
//        updateRequest.setPrice(product.getPrice());
//        updateRequest.setStockQuantity(product.getStockQuantity());
//        updateRequest.setCategory(product.getCategory());
//        updateRequest.setImageUrl(product.getImageUrl());
//
//        return ResponseEntity.ok(productService.updateProduct(productId, updateRequest));
//    }
//
//    @DeleteMapping("/{productId}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
//        productService.deleteProduct(productId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/{productId}")
//    public ResponseEntity<ProductResponse> updateProduct(
//            @PathVariable Long productId,
//            @Valid @RequestBody ProductRequest request) {
//        return ResponseEntity.ok(productService.updateProduct(productId, request));
//    }
//}