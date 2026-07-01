package br.pucminas.hospedagem.tarifa;

public class TarifaPromocional implements PoliticaTarifa {

    public static final String NOME = "PROMOCIONAL";
    public static final double PERCENTUAL_DESCONTO = 0.10;

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getDescricao() {
        return "Promocao temporaria com 10% de desconto na diaria";
    }

    @Override
    public double aplicar(double valorDiaria) {
        return valorDiaria * (1 - PERCENTUAL_DESCONTO);
    }
}
