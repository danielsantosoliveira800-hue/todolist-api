package com.daniel.todolist_api.application.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarefaRequest {

    @NotBlank(message = "Título é obrigatório.")
    private String titulo;

    private String descricao;
}
