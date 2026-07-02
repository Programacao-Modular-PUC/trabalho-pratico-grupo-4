package br.pucminas.hospedagem.pagamento;

import br.pucminas.hospedagem.model.FormaPagamento;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class ProcessadorPagamentoFactory {

    private static final Map<FormaPagamento, ProcessadorPagamento> PROCESSADORES;

    static {
        Map<FormaPagamento, ProcessadorPagamento> processadores = new EnumMap<>(FormaPagamento.class);
        registrar(processadores, new ProcessadorDinheiro());
        registrar(processadores, new ProcessadorCartaoCredito());
        registrar(processadores, new ProcessadorCartaoDebito());
        registrar(processadores, new ProcessadorPix());
        PROCESSADORES = Collections.unmodifiableMap(processadores);
    }

    private ProcessadorPagamentoFactory() {
    }

    public static ProcessadorPagamento paraForma(FormaPagamento forma) {
        if (forma == null) {
            throw new IllegalArgumentException("Forma de pagamento deve ser informada");
        }

        ProcessadorPagamento processador = PROCESSADORES.get(forma);
        if (processador == null) {
            throw new IllegalArgumentException("Forma de pagamento nao suportada: " + forma);
        }
        return processador;
    }

    private static void registrar(Map<FormaPagamento, ProcessadorPagamento> processadores,
                                  ProcessadorPagamento processador) {
        processadores.put(processador.getFormaPagamento(), processador);
    }
}
