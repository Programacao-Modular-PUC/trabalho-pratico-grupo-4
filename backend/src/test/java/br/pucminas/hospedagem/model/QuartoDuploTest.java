package br.pucminas.hospedagem.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuartoDuploTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Cama casal comum nao tem adicional de conforto")
    void diariaCamaCasalSemAdicional() {
        QuartoDuplo quarto = new QuartoDuplo(200.0, false, false, TipoCamaCasal.CASAL, false);
        assertEquals(200.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Cama queen adiciona valor de conforto")
    void diariaCamaQueenAdicionaConforto() {
        QuartoDuplo quarto = new QuartoDuplo(200.0, false, false, TipoCamaCasal.QUEEN, false);
        // 200 + 60 conforto queen
        assertEquals(260.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Cama king adiciona valor de conforto maior")
    void diariaCamaKingAdicionaConforto() {
        QuartoDuplo quarto = new QuartoDuplo(200.0, false, false, TipoCamaCasal.KING, false);
        // 200 + 100 conforto king
        assertEquals(300.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Berco adiciona taxa extra a diaria")
    void diariaComBercoSomaTaxa() {
        QuartoDuplo quarto = new QuartoDuplo(200.0, false, false, TipoCamaCasal.CASAL, true);
        // 200 + 40 berco
        assertEquals(240.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Conforto, berco, ar e hidromassagem somam todos os adicionais")
    void diariaComTodosOsAdicionais() {
        QuartoDuplo quarto = new QuartoDuplo(200.0, true, true, TipoCamaCasal.QUEEN, true);
        // 200 + 60 queen + 40 berco + 50 ar + 80 hidro = 430
        assertEquals(430.0, quarto.calcularDiaria(), DELTA);
    }

    @Test
    @DisplayName("Capacidade do quarto duplo e sempre 2")
    void capacidadeSempreDois() {
        QuartoDuplo quarto = new QuartoDuplo(200.0, false, false, TipoCamaCasal.CASAL, false);
        assertEquals(2, quarto.capacidadeMaxima());
    }

    @Test
    @DisplayName("Solicitar berco passa a cobrar a taxa de berco")
    void solicitarBercoHabilitaTaxa() {
        QuartoDuplo quarto = new QuartoDuplo(200.0, false, false, TipoCamaCasal.CASAL, false);
        quarto.solicitarBerco();
        assertTrue(quarto.isPossuiBerco());
        assertEquals(240.0, quarto.calcularDiaria(), DELTA);
    }
}
