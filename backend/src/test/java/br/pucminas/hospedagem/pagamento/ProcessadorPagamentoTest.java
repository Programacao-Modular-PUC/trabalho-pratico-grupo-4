package br.pucminas.hospedagem.pagamento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessadorPagamentoTest {

    @Test
    @DisplayName("PIX processa pagamento valido instantaneamente")
    void pixProcessaPagamentoValido() {
        String confirmacao = assertDoesNotThrow(() -> new ProcessadorPix().processar(150.0));

        assertTrue(confirmacao.contains("confirmado instantaneamente"));
    }

    @Test
    @DisplayName("Cartao de credito rejeita valor abaixo do minimo")
    void cartaoCreditoRejeitaValorAbaixoDoMinimo() {
        ProcessadorCartaoCredito processador = new ProcessadorCartaoCredito();

        assertThrows(IllegalArgumentException.class, () -> processador.processar(9.99));
    }
}
