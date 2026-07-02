package br.pucminas.hospedagem.pagamento;

import br.pucminas.hospedagem.model.FormaPagamento;

public interface ProcessadorPagamento {

    FormaPagamento getFormaPagamento();

    void validar(double valor);

    String processar(double valor);
}
