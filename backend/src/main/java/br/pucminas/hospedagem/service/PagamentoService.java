package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.FormaPagamento;
import br.pucminas.hospedagem.model.Pagamento;
import br.pucminas.hospedagem.model.Recibo;
import br.pucminas.hospedagem.model.StatusPagamento;
import br.pucminas.hospedagem.pagamento.ProcessadorPagamento;
import br.pucminas.hospedagem.pagamento.ProcessadorPagamentoFactory;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AluguelRepository aluguelRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository,
                            AluguelRepository aluguelRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.aluguelRepository = aluguelRepository;
    }

    public Pagamento criar(Long aluguelId, FormaPagamento formaPagamento) {
        if (aluguelId == null) {
            throw new IllegalArgumentException("Aluguel deve ser informado");
        }

        Aluguel aluguel = aluguelRepository.findById(aluguelId)
                .orElseThrow(() -> new IllegalArgumentException("Aluguel nao encontrado: " + aluguelId));

        double valor = aluguel.getValorFinal();
        ProcessadorPagamento processador = ProcessadorPagamentoFactory.paraForma(formaPagamento);
        processador.validar(valor);
        processador.processar(valor);

        Pagamento pagamento = new Pagamento(
                valor,
                LocalDateTime.now(),
                formaPagamento,
                StatusPagamento.CONFIRMADO,
                aluguel);
        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Optional<Pagamento> buscarPorId(Long id) {
        return pagamentoRepository.findById(id);
    }

    public String emitirRecibo(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento nao encontrado: " + id));
        return new Recibo(pagamento).gerar();
    }
}
