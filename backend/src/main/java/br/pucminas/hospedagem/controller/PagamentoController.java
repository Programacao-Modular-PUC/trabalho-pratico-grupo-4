package br.pucminas.hospedagem.controller;

import br.pucminas.hospedagem.dto.PagamentoRequest;
import br.pucminas.hospedagem.model.Pagamento;
import br.pucminas.hospedagem.service.PagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    public ResponseEntity<Pagamento> criar(@RequestBody PagamentoRequest request) {
        Pagamento pagamento = pagamentoService.criar(
                request.getAluguelId(),
                request.getFormaPagamento());
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> listarTodos() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{id}/recibo", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> emitirRecibo(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(pagamento -> ResponseEntity.ok(pagamentoService.emitirRecibo(id)))
                .orElse(ResponseEntity.notFound().build());
    }
}
