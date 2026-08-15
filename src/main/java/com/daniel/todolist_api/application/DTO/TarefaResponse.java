package com.daniel.todolist_api.application.DTO;

import com.daniel.todolist_api.domain.enums.StatusTarefa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TarefaResponse {
    private long id;
    private String titulo;
    private String descricao;
    private StatusTarefa statusTarefa;
    private LocalDateTime dataCriacao;

}
