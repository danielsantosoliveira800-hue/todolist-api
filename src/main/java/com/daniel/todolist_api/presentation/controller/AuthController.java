


package com.daniel.todolist_api.presentation.controller;

import com.daniel.todolist_api.application.DTO.LoginRequest;
import com.daniel.todolist_api.application.DTO.LoginResponse;
import com.daniel.todolist_api.application.DTO.RegistroRequest;
import com.daniel.todolist_api.application.service.UsuarioService;
import com.daniel.todolist_api.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/registro")
    public ResponseEntity<Void> registrar(@Valid @RequestBody RegistroRequest request) {
        usuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        String token = jwtService.gerarToken(request.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}