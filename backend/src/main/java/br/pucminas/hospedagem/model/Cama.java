package br.pucminas.hospedagem.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Cama {

    @Enumerated(EnumType.STRING)
    private TipoCama tipo;

    private int quantidade;

    public Cama() {
    }

    public Cama(TipoCama tipo, int quantidade) {
        this.tipo = tipo;
        this.quantidade = quantidade;
    }

    public TipoCama getTipo() {
        return tipo;
    }

    public void setTipo(TipoCama tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int capacidadeDeHospedes() {
        return switch (tipo) {
            case SOLTEIRO -> quantidade;
            case CASAL, QUEEN, KING -> quantidade * 2;
        };
    }
}
