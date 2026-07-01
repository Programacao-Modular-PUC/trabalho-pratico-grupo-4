package br.pucminas.hospedagem.model;

import br.pucminas.hospedagem.tarifa.GerenciadorTarifas;
import br.pucminas.hospedagem.tarifa.TarifaAltaTemporada;
import br.pucminas.hospedagem.tarifa.TarifaBaixaTemporada;
import br.pucminas.hospedagem.tarifa.TarifaPadrao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AluguelTarifaTest {

    private static final double DELTA = 0.001;

    @BeforeEach
    @AfterEach
    void restaurarTarifaPadrao() {
        GerenciadorTarifas.getInstance().ativarPolitica(TarifaPadrao.NOME);
    }

    private Aluguel aluguelDeQuatroDiarias() {
        // diaria fixa de 100, capacidade 1, 4 diarias (saida antes das 12h)
        Quarto quarto = new QuartoIndividual(100.0, false, false, 1, 0.0);
        return new Aluguel(
                LocalDateTime.of(2026, 6, 1, 14, 0),
                LocalDateTime.of(2026, 6, 5, 11, 0),
                1, null, quarto, null);
    }

    @Test
    @DisplayName("Com tarifa padrao o valor final nao sofre ajuste")
    void valorFinalComTarifaPadrao() {
        assertEquals(400.0, aluguelDeQuatroDiarias().calcularValorFinal(), DELTA);
    }

    @Test
    @DisplayName("Com alta temporada o valor final sobe 30%")
    void valorFinalComAltaTemporada() {
        GerenciadorTarifas.getInstance().ativarPolitica(TarifaAltaTemporada.NOME);
        // 100 * 1.30 = 130 por diaria; 130 * 4 = 520
        assertEquals(520.0, aluguelDeQuatroDiarias().calcularValorFinal(), DELTA);
    }

    @Test
    @DisplayName("Com baixa temporada o valor final cai 20%")
    void valorFinalComBaixaTemporada() {
        GerenciadorTarifas.getInstance().ativarPolitica(TarifaBaixaTemporada.NOME);
        // 100 * 0.80 = 80 por diaria; 80 * 4 = 320
        assertEquals(320.0, aluguelDeQuatroDiarias().calcularValorFinal(), DELTA);
    }
}
