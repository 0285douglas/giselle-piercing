package com.gisellepiercing.application.controller;

import com.gisellepiercing.dto.request.LoginRequest;
import com.gisellepiercing.dto.request.RegisterRequest;
import com.gisellepiercing.dto.response.LoginResponse;
import com.gisellepiercing.model.User;
import com.gisellepiercing.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Endpoints de autenticação e registro")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário com dados sendo validados. " +
                    "Retorna 201 Created se bem-sucedido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou validação falhou",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"timestamp\": \"...\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Validation error\" }"))),
            @ApiResponse(responseCode = "409", description = "Email ou CPF já cadastrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"timestamp\": \"...\", \"status\": 409, \"error\": \"Conflict\", \"message\": \"Este e-mail já está cadastrado no sistema.\" }")))
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Login de usuário",
            description = "Autentica um usuário e retorna um JWT token válido por 24 horas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"))),
            @ApiResponse(responseCode = "400", description = "Email ou senha inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"timestamp\": \"...\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"E-mail ou senha inválidos.\" }")))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}