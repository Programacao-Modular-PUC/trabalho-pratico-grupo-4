package br.pucminas.hospedagem.tarifa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PoliticaTarifaTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Tarifa padrao mantem o valor da diaria")
    void tarifaPadraoNaoAlteraValor() {
        assertEquals(100.0, new TarifaPadrao().aplicar(100.0), DELTA);
    }

    @Test
    @DisplayName("Alta temporada acresce 30% na diaria")
    void altaTemporadaAcresce30PorCento() {
        assertEquals(130.0, new TarifaAltaTemporada().aplicar(100.0), DELTA);
    }

    @Test
    @DisplayName("Baixa temporada reduz 20% da diaria")
    void baixaTemporadaReduz20PorCento() {
        assertEquals(80.0, new TarifaBaixaTemporada().aplicar(100.0), DELTA);
    }

    @Test
    @DisplayName("Tarifa promocional da 10% de desconto")
    void promocionalDesconta10PorCento() {
        assertEquals(90.0, new TarifaPromocional().aplicar(100.0), DELTA);
    }
}
