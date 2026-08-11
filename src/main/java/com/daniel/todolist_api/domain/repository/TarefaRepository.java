package com.daniel.todolist_api.domain.repository;

import com.daniel.todolist_api.domain.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByUsuarioId(Long usuarioId);
}