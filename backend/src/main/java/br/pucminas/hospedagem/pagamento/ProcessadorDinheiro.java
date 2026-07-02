package br.pucminas.hospedagem.pagamento;

import br.pucminas.hospedagem.model.FormaPagamento;

public class ProcessadorDinheiro implements ProcessadorPagamento {

    @Override
    public FormaPagamento getFormaPagamento() {
        return FormaPagamento.DINHEIRO;
    }

    @Override
    public void validar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero");
        }
    }

    @Override
    public String processar(double valor) {
        validar(valor);
        return String.format(
                "Pagamento de R$ %.2f em dinheiro registrado; o troco sera acertado no local",
                valor);
    }
}
