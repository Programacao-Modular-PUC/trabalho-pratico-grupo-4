package br.pucminas.hospedagem.model;

import br.pucminas.hospedagem.exception.DataInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AluguelTest {

    private static final double DELTA = 0.001;

    private Quarto quartoSimples() {
        // diaria fixa de 100, capacidade 1
        return new QuartoIndividual(100.0, false, false, 1, 0.0);
    }

    private Aluguel aluguel(LocalDateTime entrada, LocalDateTime saida) {
        return new Aluguel(entrada, saida, 1, null, quartoSimples(), null);
    }

    @Test
    @DisplayName("Saida antes das 12h nao cobra diaria adicional")
    void saidaAntesDasDozeNaoCobraDiariaExtra() {
        Aluguel aluguel = aluguel(
                LocalDateTime.of(2026, 6, 1, 14, 0),
                LocalDateTime.of(2026, 6, 5, 11, 0));
        assertEquals(4, aluguel.calcularQuantidadeDiarias());
    }

    @Test
    @DisplayName("Saida apos as 12h cobra uma diaria adicional")
    void saidaAposDasDozeCobraDiariaExtra() {
        Aluguel aluguel = aluguel(
                LocalDateTime.of(2026, 6, 1, 14, 0),
                LocalDateTime.of(2026, 6, 5, 13, 0));
        assertEquals(5, aluguel.calcularQuantidadeDiarias());
    }

    @Test
    @DisplayName("Estadia de uma noite com saida cedo conta uma diaria")
    void umaNoiteContaUmaDiaria() {
        Aluguel aluguel = aluguel(
                LocalDateTime.of(2026, 6, 1, 14, 0),
                LocalDateTime.of(2026, 6, 2, 10, 0));
        assertEquals(1, aluguel.calcularQuantidadeDiarias());
    }

    @Test
    @DisplayName("Valor final e a diaria multiplicada pela quantidade de diarias")
    void valorFinalMultiplicaDiariaPelasDiarias() {
        Aluguel aluguel = aluguel(
                LocalDateTime.of(2026, 6, 1, 14, 0),
                LocalDateTime.of(2026, 6, 5, 11, 0));
        // 100 * 4 diarias
        assertEquals(400.0, aluguel.calcularValorFinal(), DELTA);
    }

    @Test
    @DisplayName("Data de saida anterior a entrada lanca DataInvalidaException")
    void saidaAntesDaEntradaLancaExcecao() {
        Aluguel aluguel = aluguel(
                LocalDateTime.of(2026, 6, 5, 14, 0),
                LocalDateTime.of(2026, 6, 1, 11, 0));
        assertThrows(DataInvalidaException.class, aluguel::calcularQuantidadeDiarias);
    }

    @Test
    @DisplayName("Datas nulas lancam DataInvalidaException")
    void datasNulasLancamExcecao() {
        Aluguel aluguel = aluguel(null, null);
        assertThrows(DataInvalidaException.class, aluguel::calcularQuantidadeDiarias);
    }
}
