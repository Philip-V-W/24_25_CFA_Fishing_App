package cfa.fishing.fishing_store_app.controller.api;

import cfa.fishing.fishing_store_app.dto.request.AddressRequest;
import cfa.fishing.fishing_store_app.dto.request.UserProfileUpdateRequest;
import cfa.fishing.fishing_store_app.dto.response.AddressResponse;
import cfa.fishing.fishing_store_app.dto.response.UserProfileResponse;
import cfa.fishing.fishing_store_app.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import static java.util.Objects.requireNonNull;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = requireNonNull(userDetails, "User must be authenticated").getUsername();
        return ResponseEntity.ok(accountService.getUserProfile(username));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserProfileUpdateRequest request) {
        String username = requireNonNull(userDetails, "User must be authenticated").getUsername();
        return ResponseEntity.ok(accountService.updateProfile(username, request));
    }

    @GetMapping("/addresses")
    @Operation(summary = "Get user addresses")
    public ResponseEntity<List<AddressResponse>> getAddresses(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = requireNonNull(userDetails, "User must be authenticated").getUsername();
        return ResponseEntity.ok(accountService.getAddresses(username));
    }

    @PostMapping("/addresses")
    @Operation(summary = "Add new address")
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest request) {
        String username = requireNonNull(userDetails, "User must be authenticated").getUsername();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.addAddress(username, request));
    }

    @PutMapping("/addresses/{addressId}")
    @Operation(summary = "Update existing address")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        String username = requireNonNull(userDetails, "User must be authenticated").getUsername();
        return ResponseEntity.ok(accountService.updateAddress(username, addressId, request));
    }

    @DeleteMapping("/addresses/{addressId}")
    @Operation(summary = "Delete address")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        String username = requireNonNull(userDetails, "User must be authenticated").getUsername();
        accountService.deleteAddress(username, addressId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/addresses/{addressId}/default")
    @Operation(summary = "Set address as default")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> setDefaultAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long addressId) {
        String username = requireNonNull(userDetails, "User must be authenticated").getUsername();
        accountService.setDefaultAddress(username, addressId);
        return ResponseEntity.noContent().build();
    }
}