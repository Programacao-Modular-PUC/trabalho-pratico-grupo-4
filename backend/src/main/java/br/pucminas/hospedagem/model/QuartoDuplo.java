package br.pucminas.hospedagem.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("DUPLO")
public class QuartoDuplo extends Quarto {

    public static final double ADICIONAL_CONFORTO_CASAL = 0.0;
    public static final double ADICIONAL_CONFORTO_QUEEN = 60.0;
    public static final double ADICIONAL_CONFORTO_KING = 100.0;
    public static final double TAXA_BERCO = 40.0;

    @Enumerated(EnumType.STRING)
    private TipoCamaCasal tipoCama;

    private boolean possuiBerco;

    public QuartoDuplo() {
    }

    public QuartoDuplo(double valorBase, boolean possuiAR, boolean possuiHidro,
                       TipoCamaCasal tipoCama, boolean possuiBerco) {
        super(valorBase, possuiAR, possuiHidro);
        this.tipoCama = tipoCama;
        this.possuiBerco = possuiBerco;
    }

    @Override
    public double calcularDiaria() {
        double adicionalConforto = switch (tipoCama) {
            case CASAL -> ADICIONAL_CONFORTO_CASAL;
            case QUEEN -> ADICIONAL_CONFORTO_QUEEN;
            case KING -> ADICIONAL_CONFORTO_KING;
        };
        double extraBerco = possuiBerco ? TAXA_BERCO : 0.0;
        return getValorBase() + adicionalConforto + extraBerco + adicionaisDeConforto();
    }

    @Override
    public int capacidadeMaxima() {
        return 2;
    }

    @Override
    public void solicitarBerco() {
        this.possuiBerco = true;
    }

    public TipoCamaCasal getTipoCama() {
        return tipoCama;
    }

    public void setTipoCama(TipoCamaCasal tipoCama) {
        this.tipoCama = tipoCama;
    }

    public boolean isPossuiBerco() {
        return possuiBerco;
    }

    public void setPossuiBerco(boolean possuiBerco) {
        this.possuiBerco = possuiBerco;
    }
}
