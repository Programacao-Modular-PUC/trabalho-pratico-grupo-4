package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.model.Quarto;
import br.pucminas.hospedagem.model.QuartoIndividual;
import br.pucminas.hospedagem.model.Residencia;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.QuartoRepository;
import br.pucminas.hospedagem.repository.ResidenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidenciaServiceTest {

    @Mock
    private ResidenciaRepository residenciaRepository;

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private QuartoService quartoService;

    @InjectMocks
    private ResidenciaService residenciaService;

    @Test
    void deveAdicionarQuartoEmResidenciaExistente() {
        Residencia residencia = new Residencia();
        residencia.setId(1L);

        Quarto quarto = new QuartoIndividual(100.0, false, false, 1, 0.0);
        quarto.setId(10L);

        when(residenciaRepository.findById(1L)).thenReturn(Optional.of(residencia));
        when(quartoRepository.findById(10L)).thenReturn(Optional.of(quarto));
        when(residenciaRepository.save(residencia)).thenReturn(residencia);

        Optional<Residencia> resultado = residenciaService.adicionarQuarto(1L, 10L);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getQuartos().size());
        assertEquals(10L, resultado.get().getQuartos().get(0).getId());

        verify(residenciaRepository).save(residencia);
    }

    @Test
    void deveRetornarVazioAoAdicionarQuartoEmResidenciaInexistente() {
        when(residenciaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Residencia> resultado = residenciaService.adicionarQuarto(99L, 10L);

        assertTrue(resultado.isEmpty());
        verify(residenciaRepository, never()).save(any(Residencia.class));
    }

    @Test
    void deveListarQuartosDisponiveisFiltrandoCorretamente() {
        LocalDateTime entrada = LocalDateTime.of(2026, 7, 10, 14, 0);
        LocalDateTime saida = LocalDateTime.of(2026, 7, 15, 11, 0);

        Quarto quartoDisponivel = new QuartoIndividual(100.0, false, false, 1, 0.0);
        quartoDisponivel.setId(1L);

        Quarto quartoOcupado = new QuartoIndividual(120.0, false, false, 2, 30.0);
        quartoOcupado.setId(2L);

        Residencia residencia = new Residencia();
        residencia.setId(1L);
        residencia.adicionarQuarto(quartoDisponivel);
        residencia.adicionarQuarto(quartoOcupado);

        when(residenciaRepository.findById(1L)).thenReturn(Optional.of(residencia));
        when(quartoService.estaDisponivel(1L, entrada, saida)).thenReturn(true);
        when(quartoService.estaDisponivel(2L, entrada, saida)).thenReturn(false);

        List<Quarto> resultado = residenciaService.listarQuartosDisponiveis(1L, entrada, saida);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void deveLancarExcecaoAoListarHistoricoDeResidenciaInexistente() {
        when(residenciaRepository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> residenciaService.listarHistorico(99L));

        verify(aluguelRepository, never()).findByResidenciaId(anyLong());
    }
}
