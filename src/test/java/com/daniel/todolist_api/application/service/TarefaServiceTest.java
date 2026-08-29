package com.daniel.todolist_api.application.service;

import com.daniel.todolist_api.domain.entity.Tarefa;
import com.daniel.todolist_api.domain.entity.Usuario;
import com.daniel.todolist_api.domain.enums.StatusTarefa;
import com.daniel.todolist_api.domain.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Usuario dono;
    private Usuario outroUsuario;
    private Tarefa tarefa;

    @BeforeEach
    void setUp(){
        dono = Usuario.builder().id(1L).nome("Daniel").email("daniel@teste.com").build();
        outroUsuario = Usuario.builder().id(2L).nome("Outro").email("outro@teste.com").build();

        tarefa = Tarefa.builder()
                .id(10L)
                .titulo("Estudar Spring")
                .status(StatusTarefa.PENDENTE)
                .usuario(dono)
                .build();
    }
    @Test
    void deveBuscarTarefaComSucessoQuandoUsuarioEODono() {
        when(tarefaRepository.findById(10L)).thenReturn(Optional.of(tarefa));

        var response = tarefaService.buscarPorId(10L, dono.getId());

        assertEquals("Estudar Spring", response.getTitulo());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEODonoDaTarefa(){
        when(tarefaRepository.findById(10L)).thenReturn(Optional.of(tarefa));

        assertThrows(SecurityException.class, () ->
                tarefaService.buscarPorId(10L, outroUsuario.getId())
        );
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoExiste(){
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> tarefaService.buscarPorId(99L, dono.getId()));
    }

    @Test
    void deveAlternarStatusDePendenteParaConcluida(){
        when(tarefaRepository.findById(10L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        var response = tarefaService.alternarStatus(10L, dono.getId());

        assertEquals(StatusTarefa.CONCLUIDA, response.getStatusTarefa());
    }
}
