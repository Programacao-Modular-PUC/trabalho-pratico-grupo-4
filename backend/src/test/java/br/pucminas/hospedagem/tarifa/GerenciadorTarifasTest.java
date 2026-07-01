package br.pucminas.hospedagem.tarifa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GerenciadorTarifasTest {

    private static final double DELTA = 0.001;

    @BeforeEach
    @AfterEach
    void restaurarTarifaPadrao() {
        GerenciadorTarifas.getInstance().ativarPolitica(TarifaPadrao.NOME);
    }

    @Test
    @DisplayName("getInstance retorna sempre a mesma instancia (Singleton)")
    void instanciaUnica() {
        GerenciadorTarifas primeira = GerenciadorTarifas.getInstance();
        GerenciadorTarifas segunda = GerenciadorTarifas.getInstance();
        assertSame(primeira, segunda);
    }

    @Test
    @DisplayName("Politica padrao e a ativa inicialmente")
    void politicaPadraoAtivaInicialmente() {
        assertEquals(TarifaPadrao.NOME, GerenciadorTarifas.getInstance().getPoliticaAtiva().getNome());
    }

    @Test
    @DisplayName("Ativar politica muda o resultado da tarifa aplicada")
    void ativarPoliticaMudaCalculo() {
        GerenciadorTarifas gerenciador = GerenciadorTarifas.getInstance();

        assertEquals(100.0, gerenciador.aplicarTarifa(100.0), DELTA);

        gerenciador.ativarPolitica(TarifaAltaTemporada.NOME);
        assertEquals(130.0, gerenciador.aplicarTarifa(100.0), DELTA);

        gerenciador.ativarPolitica(TarifaBaixaTemporada.NOME);
        assertEquals(80.0, gerenciador.aplicarTarifa(100.0), DELTA);
    }

    @Test
    @DisplayName("Nome da politica nao diferencia maiusculas de minusculas")
    void ativarPoliticaIgnoraCaixa() {
        GerenciadorTarifas gerenciador = GerenciadorTarifas.getInstance();
        gerenciador.ativarPolitica("promocional");
        assertEquals(TarifaPromocional.NOME, gerenciador.getPoliticaAtiva().getNome());
    }

    @Test
    @DisplayName("Ativar politica inexistente lanca IllegalArgumentException")
    void ativarPoliticaInexistenteLancaExcecao() {
        GerenciadorTarifas gerenciador = GerenciadorTarifas.getInstance();
        assertThrows(IllegalArgumentException.class, () -> gerenciador.ativarPolitica("NATAL"));
    }

    @Test
    @DisplayName("Nova politica pode ser registrada e ativada sem alterar codigo existente")
    void registrarNovaPolitica() {
        GerenciadorTarifas gerenciador = GerenciadorTarifas.getInstance();

        PoliticaTarifa feriado = new PoliticaTarifa() {
            @Override
            public String getNome() {
                return "FERIADO";
            }

            @Override
            public String getDescricao() {
                return "Acrescimo de 50% em feriados";
            }

            @Override
            public double aplicar(double valorDiaria) {
                return valorDiaria * 1.5;
            }
        };

        gerenciador.registrarPolitica(feriado);
        gerenciador.ativarPolitica("FERIADO");

        assertEquals(150.0, gerenciador.aplicarTarifa(100.0), DELTA);
    }
}
