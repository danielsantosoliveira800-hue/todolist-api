package com.daniel.todolist_api.application.service;

import com.daniel.todolist_api.application.DTO.RegistroRequest;
import com.daniel.todolist_api.domain.entity.Usuario;
import com.daniel.todolist_api.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario registrar(RegistroRequest request){
        if (usuarioRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .build();

        return usuarioRepository.save(usuario);
    }
}
