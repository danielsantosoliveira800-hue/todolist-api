package com.daniel.todolist_api.application.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroRequest {

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;


}
