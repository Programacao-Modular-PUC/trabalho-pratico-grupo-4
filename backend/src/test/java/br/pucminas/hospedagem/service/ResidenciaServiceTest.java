package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.model.Quarto;
import br.pucminas.hospedagem.model.Residencia;
import br.pucminas.hospedagem.repository.ResidenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidenciaServiceTest {

    @Mock
    private ResidenciaRepository residenciaRepository;

    @Mock
    private QuartoService quartoService;

    @InjectMocks
    private ResidenciaService residenciaService;

    @Test
    void deveAdicionarQuartoEmResidenciaExistente() {
        Long residenciaId = 1L;

        Residencia residencia = new Residencia();
        residencia.setId(residenciaId);

        Quarto quarto = new Quarto();
        quarto.setId(10L);

        when(residenciaRepository.findById(residenciaId)).thenReturn(Optional.of(residencia));
        when(quartoService.salvar(any(Quarto.class))).thenReturn(quarto);

        Quarto resultado = residenciaService.adicionarQuarto(residenciaId, quarto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());

        verify(residenciaRepository).findById(residenciaId);
        verify(quartoService).salvar(quarto);
    }

    @Test
    void deveListarQuartosDisponiveisFiltrandoCorretamente() {
        Long residenciaId = 1L;

        Residencia residencia = new Residencia();
        residencia.setId(residenciaId);

        Quarto quartoDisponivel = new Quarto();
        quartoDisponivel.setId(1L);
        quartoDisponivel.setDisponivel(true);

        Quarto quartoIndisponivel = new Quarto();
        quartoIndisponivel.setId(2L);
        quartoIndisponivel.setDisponivel(false);

        when(residenciaRepository.findById(residenciaId)).thenReturn(Optional.of(residencia));
        when(quartoService.listarPorResidencia(residenciaId))
                .thenReturn(List.of(quartoDisponivel, quartoIndisponivel));

        List<Quarto> resultado = residenciaService.listarQuartosDisponiveis(residenciaId);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isDisponivel());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void deveLancarExcecaoAoListarHistoricoDeResidenciaInexistente() {
        Long residenciaId = 99L;

        when(residenciaRepository.findById(residenciaId)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> residenciaService.listarHistorico(residenciaId)
        );
    }
}