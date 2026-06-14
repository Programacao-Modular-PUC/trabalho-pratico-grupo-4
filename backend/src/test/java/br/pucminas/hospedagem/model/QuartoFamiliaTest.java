package br.pucminas.hospedagem.model;

import br.pucminas.hospedagem.exception.CapacidadeExcedidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuartoFamiliaTest {

    private static final double DELTA = 0.001;

    private QuartoFamilia quartoPequeno() {
        // capacidade 4: 1 cama casal (2) + 2 camas solteiro (2)
        return new QuartoFamilia(300.0, false, false,
                List.of(new Cama(TipoCama.CASAL, 1), new Cama(TipoCama.SOLTEIRO, 2)), 1);
    }

    private QuartoFamilia quartoGrande() {
        // capacidade 8: 2 camas casal (4) + 4 camas solteiro (4)
        return new QuartoFamilia(400.0, false, false,
                List.of(new Cama(TipoCama.CASAL, 2), new Cama(TipoCama.SOLTEIRO, 4)), 2);
    }

    @Test
    @DisplayName("Capacidade e a soma das camas (casal conta como 2)")
    void capacidadeSomaAsCamas() {
        assertEquals(4, quartoPequeno().capacidadeMaxima());
        assertEquals(8, quartoGrande().capacidadeMaxima());
    }

    @Test
    @DisplayName("Sem atingir o minimo de grupo nao ha desconto")
    void diariaSemDescontoParaPoucosHospedes() {
        // 300 * (1 + 0.05*2) = 330 ; + 1 ambiente * 70 = 400 ; sem desconto
        assertEquals(400.0, quartoPequeno().calcularDiariaParaHospedes(2), DELTA);
    }

    @Test
    @DisplayName("A partir de 4 hospedes aplica 5% de desconto")
    void diariaComDescontoDe5PorCento() {
        // 300 * 1.2 = 360 ; + 70 = 430 ; * 0.95 = 408.5
        assertEquals(408.5, quartoPequeno().calcularDiariaParaHospedes(4), DELTA);
    }

    @Test
    @DisplayName("A partir de 6 hospedes aplica 10% de desconto")
    void diariaComDescontoDe10PorCento() {
        // 400 * 1.3 = 520 ; + 2 ambientes * 70 = 660 ; * 0.90 = 594
        assertEquals(594.0, quartoGrande().calcularDiariaParaHospedes(6), DELTA);
    }

    @Test
    @DisplayName("A partir de 8 hospedes aplica 15% de desconto")
    void diariaComDescontoDe15PorCento() {
        // 400 * 1.4 = 560 ; + 140 = 700 ; * 0.85 = 595
        assertEquals(595.0, quartoGrande().calcularDiariaParaHospedes(8), DELTA);
    }

    @Test
    @DisplayName("Exceder a capacidade lanca CapacidadeExcedidaException")
    void hospedesAcimaDaCapacidadeLancaExcecao() {
        QuartoFamilia quarto = quartoPequeno();
        assertThrows(CapacidadeExcedidaException.class, () -> quarto.calcularDiariaParaHospedes(5));
    }

    @Test
    @DisplayName("Numero de hospedes invalido lanca IllegalArgumentException")
    void hospedesZeroLancaExcecao() {
        QuartoFamilia quarto = quartoPequeno();
        assertThrows(IllegalArgumentException.class, () -> quarto.calcularDiariaParaHospedes(0));
    }
}
