package com.daniel.todolist_api.presentation.controller;

import com.daniel.todolist_api.application.DTO.TarefaRequest;
import com.daniel.todolist_api.application.DTO.TarefaResponse;
import com.daniel.todolist_api.application.service.TarefaService;
import com.daniel.todolist_api.domain.entity.Usuario;
import com.daniel.todolist_api.domain.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<TarefaResponse>> listar(Authentication authentication) {
        Long usuarioId = extrairUsuarioId(authentication);
        return ResponseEntity.ok(tarefaService.listaPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(@PathVariable Long id, Authentication authentication) {
        Long usuarioId = extrairUsuarioId(authentication);
        return ResponseEntity.ok(tarefaService.buscarPorId(id, usuarioId));
    }

    @PostMapping
    public ResponseEntity<TarefaResponse> criar(@Valid @RequestBody TarefaRequest request, Authentication authentication) {
        Usuario usuario = buscarUsuario(authentication);
        TarefaResponse response = tarefaService.criar(request, usuario);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> editar(
            @PathVariable Long id,
            @Valid @RequestBody TarefaRequest request,
            Authentication authentication
    ) {
        Long usuarioId = extrairUsuarioId(authentication);
        return ResponseEntity.ok(tarefaService.editar(id, request, usuarioId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaResponse> alterarStatus(@PathVariable Long id, Authentication authentication) {
        Long usuarioId = extrairUsuarioId(authentication);
        return ResponseEntity.ok(tarefaService.alternarStatus(id, usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id, Authentication authentication) {
        Long usuarioId = extrairUsuarioId(authentication);
        tarefaService.remover(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    private Long extrairUsuarioId(Authentication authentication) {
        return buscarUsuario(authentication).getId();
    }

    private Usuario buscarUsuario(Authentication authentication) {
        String email;

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = authentication.getName();
        }

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("usuário não encontrado para o e-mail: " + email));
    }
}