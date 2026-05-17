package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.Quarto;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.QuartoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuartoService {

    private final QuartoRepository quartoRepository;
    private final AluguelRepository aluguelRepository;

    public QuartoService(QuartoRepository quartoRepository, AluguelRepository aluguelRepository) {
        this.quartoRepository = quartoRepository;
        this.aluguelRepository = aluguelRepository;
    }

    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    }

    public Optional<Quarto> buscarPorId(Long id) {
        return quartoRepository.findById(id);
    }

    public Quarto criar(Quarto quarto) {
        return quartoRepository.save(quarto);
    }

    public Quarto atualizar(Long id, Quarto quartoAtualizado) {
        return quartoRepository.findById(id)
                .map(existente -> {
                    quartoAtualizado.setId(id);
                    return quartoRepository.save(quartoAtualizado);
                })
                .orElseThrow(() -> new IllegalArgumentException("Quarto nao encontrado: " + id));
    }

    public void deletar(Long id) {
        quartoRepository.deleteById(id);
    }

    public boolean estaDisponivel(Long quartoId, LocalDateTime entrada, LocalDateTime saida) {
        List<Aluguel> conflitos = aluguelRepository.findConflitos(quartoId, entrada, saida);
        return conflitos.isEmpty();
    }

    public double calcularDiariaPrevista(Long quartoId, Integer numeroHospedes) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto nao encontrado: " + quartoId));
        if (numeroHospedes == null || numeroHospedes <= 0) {
            return quarto.calcularDiaria();
        }
        return quarto.calcularDiariaParaHospedes(numeroHospedes);
    }
}
