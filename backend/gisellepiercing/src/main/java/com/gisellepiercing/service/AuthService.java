package com.gisellepiercing.service;

import com.gisellepiercing.application.exception.InvalidCredentialsException;
import com.gisellepiercing.application.exception.UserAlreadyExistsException;
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
            log.warn("Tentativa de registrar email já existente: {}", request.getEmail());
            throw new UserAlreadyExistsException("Este e-mail já está cadastrado no sistema.");
        }

        if (repository.existsByCpf(cpfLimpo)) {
            log.warn("Tentativa de registrar CPF já existente: {}", cpfLimpo);
            throw new UserAlreadyExistsException("Este CPF já está cadastrado no sistema.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCpf(cpfLimpo);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_CUSTOMER");

        repository.save(user);
        log.info("Usuário registrado com sucesso! Email: {}", request.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Autenticando usuário email={}", request.getEmail());

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Tentativa de login com email inexistente: {}", request.getEmail());
                    return new InvalidCredentialsException("E-mail ou senha inválidos.");
                });

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {
            log.warn("Tentativa de login com senha inválida para email: {}", request.getEmail());
            throw new InvalidCredentialsException("E-mail ou senha inválidos.");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());
        log.info("Usuário autenticado com sucesso! Email: {}, userId: {}", request.getEmail(), user.getId());
        return LoginResponse.builder().token(token).build();
    }
}