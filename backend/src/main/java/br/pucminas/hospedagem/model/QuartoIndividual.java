package br.pucminas.hospedagem.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INDIVIDUAL")
public class QuartoIndividual extends Quarto {

    private int quantidadeCamasSolteiro;

    private double adicionalPorCamaExtra;

    public QuartoIndividual() {
    }

    public QuartoIndividual(double valorBase, boolean possuiAR, boolean possuiHidro,
                            int quantidadeCamasSolteiro, double adicionalPorCamaExtra) {
        super(valorBase, possuiAR, possuiHidro);
        this.quantidadeCamasSolteiro = quantidadeCamasSolteiro;
        this.adicionalPorCamaExtra = adicionalPorCamaExtra;
    }

    @Override
    public double calcularDiaria() {
        int camasExtras = Math.max(0, quantidadeCamasSolteiro - 1);
        double valorCamas = camasExtras * adicionalPorCamaExtra;
        return getValorBase() + valorCamas + adicionaisDeConforto();
    }

    @Override
    public int capacidadeMaxima() {
        return quantidadeCamasSolteiro;
    }

    public int getQuantidadeCamasSolteiro() {
        return quantidadeCamasSolteiro;
    }

    public void setQuantidadeCamasSolteiro(int quantidadeCamasSolteiro) {
        this.quantidadeCamasSolteiro = quantidadeCamasSolteiro;
    }

    public double getAdicionalPorCamaExtra() {
        return adicionalPorCamaExtra;
    }

    public void setAdicionalPorCamaExtra(double adicionalPorCamaExtra) {
        this.adicionalPorCamaExtra = adicionalPorCamaExtra;
    }
}
