package com.daniel.todolist_api.infrastructure.security;

import com.daniel.todolist_api.domain.entity.Usuario;
import com.daniel.todolist_api.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario =  usuarioRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Usúario não encontrado."));

        return User.builder()
                .username(usuario.getNome())
                .password(usuario.getSenha())
                .authorities("USER")
                .build();
    }
}
