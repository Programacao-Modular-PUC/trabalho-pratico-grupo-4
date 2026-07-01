package br.pucminas.hospedagem.tarifa;

public class TarifaAltaTemporada implements PoliticaTarifa {

    public static final String NOME = "ALTA_TEMPORADA";
    public static final double PERCENTUAL_ACRESCIMO = 0.30;

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getDescricao() {
        return "Acrescimo de 30% no valor da diaria";
    }

    @Override
    public double aplicar(double valorDiaria) {
        return valorDiaria * (1 + PERCENTUAL_ACRESCIMO);
    }
}
