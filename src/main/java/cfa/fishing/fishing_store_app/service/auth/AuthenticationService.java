package cfa.fishing.fishing_store_app.service.auth;

import cfa.fishing.fishing_store_app.dto.request.LoginRequest;
import cfa.fishing.fishing_store_app.dto.request.RegisterRequest;
import cfa.fishing.fishing_store_app.dto.response.AuthenticationResponse;
import cfa.fishing.fishing_store_app.entity.user.Role;
import cfa.fishing.fishing_store_app.entity.user.User;
import cfa.fishing.fishing_store_app.repository.UserRepository;
import cfa.fishing.fishing_store_app.security.JwtTokenProvider;
import cfa.fishing.fishing_store_app.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        var user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var jwt = jwtTokenProvider.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var jwt = jwtTokenProvider.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }
}