package cfa.fishing.fishing_store_app.controller.api;

import cfa.fishing.fishing_store_app.dto.request.PermitRequest;
import cfa.fishing.fishing_store_app.dto.response.PermitResponse;
import cfa.fishing.fishing_store_app.service.permit.PermitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permits")
@RequiredArgsConstructor
public class PermitController {

    private final PermitService permitService;

    @PostMapping
    public ResponseEntity<PermitResponse> createPermit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PermitRequest request) {
        return ResponseEntity.ok(permitService.createPermit(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<PermitResponse>> getUserPermits(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(permitService.getUserPermits(userDetails.getUsername()));
    }

    @GetMapping("/{permitId}")
    public ResponseEntity<PermitResponse> getPermit(@PathVariable Long permitId) {
        return ResponseEntity.ok(permitService.getPermit(permitId));
    }

    // Admin endpoints
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
}