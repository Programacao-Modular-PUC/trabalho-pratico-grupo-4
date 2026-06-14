package br.pucminas.hospedagem.controller;

import br.pucminas.hospedagem.dto.AluguelRequest;
import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.service.AluguelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @GetMapping
    public List<Aluguel> listar() {
        return aluguelService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluguel> buscar(@PathVariable Long id) {
        return aluguelService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aluguel> criar(@RequestBody AluguelRequest request) {
        Aluguel aluguel = aluguelService.criar(
                request.getClienteId(),
                request.getQuartoId(),
                request.getResidenciaId(),
                request.getDataEntrada(),
                request.getDataSaida(),
                request.getNumeroHospedes());
        return ResponseEntity.ok(aluguel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        if (aluguelService.cancelar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/recibo")
    public ResponseEntity<String> imprimirFormulario(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(aluguelService.imprimirFormulario(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
