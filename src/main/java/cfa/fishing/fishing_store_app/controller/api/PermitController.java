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

    @GetMapping("/{permitId:\\d+}")
    public ResponseEntity<PermitResponse> getPermit(@PathVariable Long permitId) {
        return ResponseEntity.ok(permitService.getPermit(permitId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<PermitResponse>> getUserPermits(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(permitService.getUserPermits(userDetails.getUsername()));
    }
}