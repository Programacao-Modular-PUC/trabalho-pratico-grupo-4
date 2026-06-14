package br.pucminas.hospedagem.service;

import br.pucminas.hospedagem.exception.CapacidadeExcedidaException;
import br.pucminas.hospedagem.exception.DataInvalidaException;
import br.pucminas.hospedagem.exception.QuartoIndisponivelException;
import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.Cliente;
import br.pucminas.hospedagem.model.Quarto;
import br.pucminas.hospedagem.model.Residencia;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.ClienteRepository;
import br.pucminas.hospedagem.repository.QuartoRepository;
import br.pucminas.hospedagem.repository.ResidenciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final QuartoRepository quartoRepository;
    private final ClienteRepository clienteRepository;
    private final ResidenciaRepository residenciaRepository;

    public AluguelService(AluguelRepository aluguelRepository,
                          QuartoRepository quartoRepository,
                          ClienteRepository clienteRepository,
                          ResidenciaRepository residenciaRepository) {
        this.aluguelRepository = aluguelRepository;
        this.quartoRepository = quartoRepository;
        this.clienteRepository = clienteRepository;
        this.residenciaRepository = residenciaRepository;
    }

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public Optional<Aluguel> buscarPorId(Long id) {
        return aluguelRepository.findById(id);
    }

    public Aluguel criar(Long clienteId, Long quartoId, Long residenciaId,
                         LocalDateTime dataEntrada, LocalDateTime dataSaida, int numeroHospedes) {

        if (dataEntrada == null || dataSaida == null) {
            throw new DataInvalidaException("Datas de entrada e saida devem estar definidas");
        }
        if (!dataSaida.isAfter(dataEntrada)) {
            throw new DataInvalidaException("Data de saida deve ser posterior a data de entrada");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado: " + clienteId));
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new IllegalArgumentException("Quarto nao encontrado: " + quartoId));
        Residencia residencia = residenciaRepository.findById(residenciaId)
                .orElseThrow(() -> new IllegalArgumentException("Residencia nao encontrada: " + residenciaId));

        if (numeroHospedes > quarto.capacidadeMaxima()) {
            throw new CapacidadeExcedidaException(
                    "Numero de hospedes (" + numeroHospedes + ") excede capacidade do quarto (" + quarto.capacidadeMaxima() + ")");
        }

        List<Aluguel> conflitos = aluguelRepository.findConflitos(quartoId, dataEntrada, dataSaida);
        if (!conflitos.isEmpty()) {
            throw new QuartoIndisponivelException("Quarto indisponivel no periodo solicitado");
        }

        Aluguel aluguel = new Aluguel(dataEntrada, dataSaida, numeroHospedes, cliente, quarto, residencia);
        aluguel.calcularValorFinal();
        return aluguelRepository.save(aluguel);
    }

    public boolean cancelar(Long id) {
        if (aluguelRepository.existsById(id)) {
            aluguelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public String imprimirFormulario(Long id) {
        return aluguelRepository.findById(id)
                .map(Aluguel::imprimirFormulario)
                .orElseThrow(() -> new IllegalArgumentException("Aluguel nao encontrado: " + id));
    }
}
