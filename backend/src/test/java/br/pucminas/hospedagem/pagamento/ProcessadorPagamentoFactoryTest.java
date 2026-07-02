package br.pucminas.hospedagem.pagamento;

import br.pucminas.hospedagem.model.FormaPagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessadorPagamentoFactoryTest {

    @Test
    @DisplayName("Factory retorna processador de dinheiro")
    void retornaProcessadorDinheiro() {
        assertInstanceOf(
                ProcessadorDinheiro.class,
                ProcessadorPagamentoFactory.paraForma(FormaPagamento.DINHEIRO));
    }

    @Test
    @DisplayName("Factory retorna processador de cartao de credito")
    void retornaProcessadorCartaoCredito() {
        assertInstanceOf(
                ProcessadorCartaoCredito.class,
                ProcessadorPagamentoFactory.paraForma(FormaPagamento.CARTAO_CREDITO));
    }

    @Test
    @DisplayName("Factory retorna processador de cartao de debito")
    void retornaProcessadorCartaoDebito() {
        assertInstanceOf(
                ProcessadorCartaoDebito.class,
                ProcessadorPagamentoFactory.paraForma(FormaPagamento.CARTAO_DEBITO));
    }

    @Test
    @DisplayName("Factory retorna processador PIX")
    void retornaProcessadorPix() {
        assertInstanceOf(
                ProcessadorPix.class,
                ProcessadorPagamentoFactory.paraForma(FormaPagamento.PIX));
    }

    @Test
    @DisplayName("Factory rejeita forma de pagamento nula")
    void rejeitaFormaNula() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ProcessadorPagamentoFactory.paraForma(null));
    }
}
