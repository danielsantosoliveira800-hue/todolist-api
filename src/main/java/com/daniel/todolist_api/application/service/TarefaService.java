package com.daniel.todolist_api.application.service;

import com.daniel.todolist_api.application.DTO.TarefaRequest;
import com.daniel.todolist_api.application.DTO.TarefaResponse;
import com.daniel.todolist_api.domain.entity.Tarefa;
import com.daniel.todolist_api.domain.entity.Usuario;
import com.daniel.todolist_api.domain.enums.StatusTarefa;
import com.daniel.todolist_api.domain.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    // cada tarefa na lista tem os mesmas caracteristicas de TarefaResponde,
    // mas cada uma tem dados tiferentes e podem ser acessadas pelo id do usuario.
    public List<TarefaResponse> listaPorUsuario(long usuarioId){
        return tarefaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TarefaResponse buscarPorId(Long id, Long usuarioId){
        Tarefa tarefa = buscarTarefaValidada(id, usuarioId);
        return toResponse(tarefa);
    }

    public TarefaResponse criar(TarefaRequest request, Usuario usuario){
        Tarefa tarefa = Tarefa.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .usuario(usuario)
                .build();

        return toResponse(tarefaRepository.save(tarefa));
    }

    public TarefaResponse editar(long id, TarefaRequest request, Long usuarioId){
        Tarefa tarefa = buscarTarefaValidada(id, usuarioId);
        tarefa.setTitulo(request.getTitulo());
        tarefa.setDescricao(request.getDescricao());
        return toResponse(tarefaRepository.save(tarefa));
    }

    public TarefaResponse alternarStatus(Long id, Long usuarioId){
        Tarefa tarefa = buscarTarefaValidada(id, usuarioId);
        tarefa.setStatus(
                tarefa.getStatus() == StatusTarefa.PENDENTE
                        ? StatusTarefa.CONCLUIDA
                        : StatusTarefa.PENDENTE

        );
        return toResponse(tarefaRepository.save(tarefa));
    }

    public void remover(Long id, Long usuarioId){
        Tarefa tarefa = buscarTarefaValidada(id, usuarioId);
        tarefaRepository.delete(tarefa);
    }

    private Tarefa buscarTarefaValidada(Long id, Long usuarioId) {
        Tarefa tarefa =
                tarefaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                        "Tarefa não encontrada."
                ));
        if (!tarefa.getUsuario().getId().equals(usuarioId)){
            throw new SecurityException("Voçê não tem acesso a essa tarefa.");
        }
        return tarefa;
    }

    private TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao()
        );
    }
}
