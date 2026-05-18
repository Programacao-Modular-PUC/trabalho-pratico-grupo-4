package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.Quarto;
import br.pucminas.hospedagem.model.Residencia;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.QuartoRepository;
import br.pucminas.hospedagem.repository.ResidenciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResidenciaService {

    private final ResidenciaRepository residenciaRepository;
    private final QuartoRepository quartoRepository;
    private final AluguelRepository aluguelRepository;
    private final QuartoService quartoService;

    public ResidenciaService(ResidenciaRepository residenciaRepository,
                             QuartoRepository quartoRepository,
                             AluguelRepository aluguelRepository,
                             QuartoService quartoService) {
        this.residenciaRepository = residenciaRepository;
        this.quartoRepository = quartoRepository;
        this.aluguelRepository = aluguelRepository;
        this.quartoService = quartoService;
    }

    public List<Residencia> listarTodas() {
        return residenciaRepository.findAll();
    }

    public Optional<Residencia> buscarPorId(Long id) {
        return residenciaRepository.findById(id);
    }

    public Residencia salvar(Residencia residencia) {
        return residenciaRepository.save(residencia);
    }

    public Optional<Residencia> atualizar(Long id, Residencia residenciaAtualizada) {
        return residenciaRepository.findById(id).map(residenciaExistente -> {
            residenciaExistente.setEndereco(residenciaAtualizada.getEndereco());
            residenciaExistente.setNumero(residenciaAtualizada.getNumero());
            residenciaExistente.setBairro(residenciaAtualizada.getBairro());
            residenciaExistente.setCep(residenciaAtualizada.getCep());
            residenciaExistente.setTelefone(residenciaAtualizada.getTelefone());
            residenciaExistente.setEmail(residenciaAtualizada.getEmail());
            return residenciaRepository.save(residenciaExistente);
        });
    }

    public boolean remover(Long id) {
        if (residenciaRepository.existsById(id)) {
            residenciaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Residencia> adicionarQuarto(Long residenciaId, Long quartoId) {
        Optional<Residencia> residenciaOpt = residenciaRepository.findById(residenciaId);
        Optional<Quarto> quartoOpt = quartoRepository.findById(quartoId);

        if (residenciaOpt.isPresent() && quartoOpt.isPresent()) {
            Residencia residencia = residenciaOpt.get();
            Quarto quarto = quartoOpt.get();
            residencia.adicionarQuarto(quarto);
            return Optional.of(residenciaRepository.save(residencia));
        }
        return Optional.empty();
    }

    public List<Quarto> listarQuartosDisponiveis(Long residenciaId, LocalDateTime entrada, LocalDateTime saida) {
        Optional<Residencia> residenciaOpt = residenciaRepository.findById(residenciaId);
        if (residenciaOpt.isEmpty()) {
            throw new IllegalArgumentException("Residência não encontrada.");
        }

        Residencia residencia = residenciaOpt.get();
        return residencia.getQuartos().stream()
                .filter(quarto -> quartoService.estaDisponivel(quarto.getId(), entrada, saida))
                .collect(Collectors.toList());
    }

    public List<Aluguel> listarHistorico(Long residenciaId) {
        if (!residenciaRepository.existsById(residenciaId)) {
            throw new IllegalArgumentException("Residência não encontrada.");
        }
        return aluguelRepository.findByResidenciaId(residenciaId);
    }
}