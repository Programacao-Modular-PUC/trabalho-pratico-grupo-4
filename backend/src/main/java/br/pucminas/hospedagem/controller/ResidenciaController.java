package br.pucminas.hospedagem.controller;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.Quarto;
import br.pucminas.hospedagem.model.Residencia;
import br.pucminas.hospedagem.service.ResidenciaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/residencias")
public class ResidenciaController {

    private final ResidenciaService residenciaService;

    public ResidenciaController(ResidenciaService residenciaService) {
        this.residenciaService = residenciaService;
    }

    @GetMapping
    public ResponseEntity<List<Residencia>> listarTodas() {
        return ResponseEntity.ok(residenciaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Residencia> buscarPorId(@PathVariable Long id) {
        return residenciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Residencia> criar(@RequestBody Residencia residencia) {
        Residencia novaResidencia = residenciaService.salvar(residencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaResidencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Residencia> atualizar(@PathVariable Long id, @RequestBody Residencia residencia) {
        return residenciaService.atualizar(id, residencia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (residenciaService.remover(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/quartos/{quartoId}")
    public ResponseEntity<Residencia> adicionarQuarto(@PathVariable Long id, @PathVariable Long quartoId) {
        return residenciaService.adicionarQuarto(id, quartoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/quartos-disponiveis")
    public ResponseEntity<?> listarQuartosDisponiveis(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime saida) {
        try {
            List<Quarto> disponiveis = residenciaService.listarQuartosDisponiveis(id, entrada, saida);
            return ResponseEntity.ok(disponiveis);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<?> listarHistorico(@PathVariable Long id) {
        try {
            List<Aluguel> historico = residenciaService.listarHistorico(id);
            return ResponseEntity.ok(historico);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}