package br.pucminas.hospedagem.dto;

import br.pucminas.hospedagem.model.FormaPagamento;

public class PagamentoRequest {

    private Long aluguelId;

    private FormaPagamento formaPagamento;

    public Long getAluguelId() {
        return aluguelId;
    }

    public void setAluguelId(Long aluguelId) {
        this.aluguelId = aluguelId;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
