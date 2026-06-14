package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.QuartoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuartoServiceDisponibilidadeTest {

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private QuartoRepository quartoRepository;

    @InjectMocks
    private QuartoService quartoService;

    private final LocalDateTime entrada = LocalDateTime.of(2026, 6, 1, 14, 0);
    private final LocalDateTime saida = LocalDateTime.of(2026, 6, 5, 11, 0);

    @Test
    @DisplayName("Quarto esta disponivel quando nao ha conflitos no periodo")
    void disponivelQuandoNaoHaConflitos() {
        when(aluguelRepository.findConflitos(eq(1L), any(), any()))
                .thenReturn(Collections.emptyList());

        assertTrue(quartoService.estaDisponivel(1L, entrada, saida));
    }

    @Test
    @DisplayName("Quarto esta indisponivel quando ha aluguel conflitante")
    void indisponivelQuandoHaConflito() {
        when(aluguelRepository.findConflitos(eq(1L), any(), any()))
                .thenReturn(List.of(new Aluguel()));

        assertFalse(quartoService.estaDisponivel(1L, entrada, saida));
    }
}
