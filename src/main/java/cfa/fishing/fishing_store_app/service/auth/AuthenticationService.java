package cfa.fishing.fishing_store_app.service.auth;

import cfa.fishing.fishing_store_app.dto.request.LoginRequest;
import cfa.fishing.fishing_store_app.dto.request.RegisterRequest;
import cfa.fishing.fishing_store_app.dto.response.AuthenticationResponse;
import cfa.fishing.fishing_store_app.entity.user.Role;
import cfa.fishing.fishing_store_app.entity.user.User;
import cfa.fishing.fishing_store_app.repository.UserRepository;
import cfa.fishing.fishing_store_app.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }


        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.CUSTOMER)
                .build();

        user = userRepository.save(user);
        log.debug("Created new user with email: {}", user.getEmail());

        String jwt = jwtService.generateToken(user);
        return buildAuthResponse(user, jwt);
    }

    public AuthenticationResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            log.debug("User authenticated successfully: {}", user.getEmail());
            String jwt = jwtService.generateToken(user);
            return buildAuthResponse(user, jwt);

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user {}: {}", request.getEmail(), e.getMessage());
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    private AuthenticationResponse buildAuthResponse(User user, String jwt) {
        return AuthenticationResponse.builder()
                .token(jwt)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}