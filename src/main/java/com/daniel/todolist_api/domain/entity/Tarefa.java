package com.daniel.todolist_api.domain.entity;

import com.daniel.todolist_api.domain.enums.StatusTarefa;
import jakarta.persistence. *;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @PrePersist
    public void PrePersist(){
        this.dataCriacao = LocalDateTime.now();
        if (this.status == null){
            this.status = StatusTarefa.PENDENTE;
        }
    }
}
