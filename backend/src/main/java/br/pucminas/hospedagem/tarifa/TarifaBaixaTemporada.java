package br.pucminas.hospedagem.tarifa;

public class TarifaBaixaTemporada implements PoliticaTarifa {

    public static final String NOME = "BAIXA_TEMPORADA";
    public static final double PERCENTUAL_REDUCAO = 0.20;

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getDescricao() {
        return "Reducao de 20% no valor da diaria";
    }

    @Override
    public double aplicar(double valorDiaria) {
        return valorDiaria * (1 - PERCENTUAL_REDUCAO);
    }
}
