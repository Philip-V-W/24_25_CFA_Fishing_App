package cfa.fishing.fishing_store_app.controller.api;

import cfa.fishing.fishing_store_app.dto.request.LoginRequest;
import cfa.fishing.fishing_store_app.dto.request.RegisterRequest;
import cfa.fishing.fishing_store_app.dto.response.AuthenticationResponse;
import cfa.fishing.fishing_store_app.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Registration failed: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthenticationResponse response = authenticationService.login(request);
            log.debug("Login response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}