package br.pucminas.hospedagem.pagamento;

import br.pucminas.hospedagem.model.FormaPagamento;

public class ProcessadorPix implements ProcessadorPagamento {

    @Override
    public FormaPagamento getFormaPagamento() {
        return FormaPagamento.PIX;
    }

    @Override
    public void validar(double valor) {
        validarValorPositivo(valor);
    }

    @Override
    public String processar(double valor) {
        validar(valor);
        return String.format("Pagamento PIX de R$ %.2f confirmado instantaneamente", valor);
    }

    private void validarValorPositivo(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero");
        }
    }
}
