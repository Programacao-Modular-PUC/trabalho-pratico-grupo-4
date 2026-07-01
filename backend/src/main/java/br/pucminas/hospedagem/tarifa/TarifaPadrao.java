package br.pucminas.hospedagem.tarifa;

public class TarifaPadrao implements PoliticaTarifa {

    public static final String NOME = "PADRAO";

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getDescricao() {
        return "Tarifa padrao, sem ajuste sobre o valor da diaria";
    }

    @Override
    public double aplicar(double valorDiaria) {
        return valorDiaria;
    }
}
