package br.pucminas.hospedagem.pagamento;

import br.pucminas.hospedagem.model.FormaPagamento;

public class ProcessadorCartaoCredito implements ProcessadorPagamento {

    private static final double VALOR_MINIMO = 10.0;

    @Override
    public FormaPagamento getFormaPagamento() {
        return FormaPagamento.CARTAO_CREDITO;
    }

    @Override
    public void validar(double valor) {
        if (valor < VALOR_MINIMO) {
            throw new IllegalArgumentException(
                    String.format("O valor minimo para cartao de credito e R$ %.2f", VALOR_MINIMO));
        }
    }

    @Override
    public String processar(double valor) {
        validar(valor);
        return String.format("Pagamento de R$ %.2f no cartao de credito confirmado", valor);
    }
}
