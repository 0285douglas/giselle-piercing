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
        log.info("Iniciando registro do usuário email={}", request.getEmail());

        String cpfLimpo = request.getCpf().replaceAll("[^0-9]", "");


        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado no sistema.");
        }

        if (repository.existsByCpf(cpfLimpo)) {
            throw new IllegalArgumentException("Este CPF já está cadastrado no sistema.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCpf(cpfLimpo);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_CUSTOMER");

        repository.save(user);
        log.info("Usuário registrado com sucesso! ID gerado no banco.");
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Autenticando usuário email={}", request.getEmail());

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos."));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new IllegalArgumentException("E-mail ou senha inválidos.");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());
        return LoginResponse.builder().token(token).build();
    }
}