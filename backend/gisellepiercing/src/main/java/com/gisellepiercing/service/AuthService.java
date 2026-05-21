package com.gisellepiercing.service;

import com.gisellepiercing.dto.request.LoginRequest;
import com.gisellepiercing.dto.request.RegisterRequest;
import com.gisellepiercing.dto.response.LoginResponse;
import com.gisellepiercing.model.User;
import com.gisellepiercing.repository.UserRepository;
import com.gisellepiercing.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {
        log.info("Registering user email={}", request.getEmail());
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_CUSTOMER");
        repository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Authenticating user email={}", request.getEmail());
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());

        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
