package br.pucminas.hospedagem.tarifa;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Singleton que centraliza a politica de tarifacao vigente no sistema.
 *
 * A politica ativa e um recurso global: o valor cobrado em um aluguel e o
 * valor previsto em uma consulta de diaria precisam refletir sempre a MESMA
 * regra de tarifacao. Se existissem varias instancias deste gerenciador,
 * partes diferentes do sistema poderiam aplicar tarifas diferentes para a
 * mesma hospedagem no mesmo instante.
 */
public final class GerenciadorTarifas {

    private static GerenciadorTarifas instancia;

    private final Map<String, PoliticaTarifa> politicas = new LinkedHashMap<>();

    private volatile PoliticaTarifa politicaAtiva;

    private GerenciadorTarifas() {
        TarifaPadrao padrao = new TarifaPadrao();
        registrarPolitica(padrao);
        registrarPolitica(new TarifaAltaTemporada());
        registrarPolitica(new TarifaBaixaTemporada());
        registrarPolitica(new TarifaPromocional());
        this.politicaAtiva = padrao;
    }

    public static synchronized GerenciadorTarifas getInstance() {
        if (instancia == null) {
            instancia = new GerenciadorTarifas();
        }
        return instancia;
    }

    /**
     * Permite adicionar novas regras de tarifacao em tempo de execucao,
     * sem alterar o codigo existente (principio aberto/fechado).
     */
    public void registrarPolitica(PoliticaTarifa politica) {
        politicas.put(politica.getNome(), politica);
    }

    public void ativarPolitica(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da politica de tarifa deve ser informado");
        }
        PoliticaTarifa politica = politicas.get(nome.toUpperCase());
        if (politica == null) {
            throw new IllegalArgumentException("Politica de tarifa nao encontrada: " + nome);
        }
        this.politicaAtiva = politica;
    }

    public PoliticaTarifa getPoliticaAtiva() {
        return politicaAtiva;
    }

    public Collection<PoliticaTarifa> listarPoliticas() {
        return Collections.unmodifiableCollection(politicas.values());
    }

    public double aplicarTarifa(double valorDiaria) {
        return politicaAtiva.aplicar(valorDiaria);
    }
}
