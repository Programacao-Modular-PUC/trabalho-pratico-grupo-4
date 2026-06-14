package br.pucminas.hospedagem.model;

import br.pucminas.hospedagem.exception.RecursoNaoPermitidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuartoIndividualTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Com apenas uma cama a diaria e o valor base, sem adicional")
    void diariaComUmaCamaUsaApenasValorBase() {
        QuartoIndividual quarto = new QuartoIndividual(100.0, false, false, 1, 30.0);
        assertEquals(100.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Cada cama alem da primeira soma o adicional por cama")
    void diariaSomaAdicionalPorCamaExtra() {
        QuartoIndividual quarto = new QuartoIndividual(100.0, false, false, 3, 30.0);
        // 100 base + 2 camas extras * 30 = 160
        assertEquals(160.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Ar condicionado e hidromassagem somam taxas fixas")
    void diariaSomaAdicionaisDeConforto() {
        QuartoIndividual quarto = new QuartoIndividual(100.0, true, true, 1, 30.0);
        // 100 base + 50 ar + 80 hidro = 230
        assertEquals(230.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("A capacidade e igual ao numero de camas de solteiro")
    void capacidadeIgualAoNumeroDeCamas() {
        QuartoIndividual quarto = new QuartoIndividual(100.0, false, false, 4, 30.0);
        assertEquals(4, quarto.capacidadeMaxima());
    }

    @Test
    @DisplayName("Quarto individual nao permite berco")
    void solicitarBercoLancaExcecao() {
        QuartoIndividual quarto = new QuartoIndividual(100.0, false, false, 2, 30.0);
        assertThrows(RecursoNaoPermitidoException.class, quarto::solicitarBerco);
    }
}
