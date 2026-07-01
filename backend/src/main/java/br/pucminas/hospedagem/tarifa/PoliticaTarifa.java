package br.pucminas.hospedagem.tarifa;

/**
 * Strategy de tarifacao: cada politica define um ajuste proprio sobre o valor
 * da diaria calculado pelo quarto. Novas regras de cobranca (feriados, eventos,
 * descontos especiais) sao adicionadas criando novas implementacoes desta
 * interface, sem alterar o codigo de calculo existente.
 */
public interface PoliticaTarifa {

    String getNome();

    String getDescricao();

    double aplicar(double valorDiaria);
}
