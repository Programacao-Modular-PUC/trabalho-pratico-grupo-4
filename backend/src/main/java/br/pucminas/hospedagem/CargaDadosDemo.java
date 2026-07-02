package br.pucminas.hospedagem;

import br.pucminas.hospedagem.model.Aluguel;
import br.pucminas.hospedagem.model.Cama;
import br.pucminas.hospedagem.model.Cliente;
import br.pucminas.hospedagem.model.FormaPagamento;
import br.pucminas.hospedagem.model.Quarto;
import br.pucminas.hospedagem.model.QuartoDuplo;
import br.pucminas.hospedagem.model.QuartoFamilia;
import br.pucminas.hospedagem.model.QuartoIndividual;
import br.pucminas.hospedagem.model.Residencia;
import br.pucminas.hospedagem.model.TipoCama;
import br.pucminas.hospedagem.model.TipoCamaCasal;
import br.pucminas.hospedagem.repository.AluguelRepository;
import br.pucminas.hospedagem.repository.ClienteRepository;
import br.pucminas.hospedagem.repository.QuartoRepository;
import br.pucminas.hospedagem.repository.ResidenciaRepository;
import br.pucminas.hospedagem.service.PagamentoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Carrega dados de exemplo na inicializacao para o sistema nao subir vazio.
 * Como o H2 roda em memoria, o banco e recriado a cada execucao; a carga so
 * acontece quando nao ha quartos cadastrados, entao nao duplica dados no MySQL.
 */
@Component
public class CargaDadosDemo implements CommandLineRunner {

    private final ResidenciaRepository residenciaRepository;
    private final QuartoRepository quartoRepository;
    private final ClienteRepository clienteRepository;
    private final AluguelRepository aluguelRepository;
    private final PagamentoService pagamentoService;

    public CargaDadosDemo(ResidenciaRepository residenciaRepository,
                          QuartoRepository quartoRepository,
                          ClienteRepository clienteRepository,
                          AluguelRepository aluguelRepository,
                          PagamentoService pagamentoService) {
        this.residenciaRepository = residenciaRepository;
        this.quartoRepository = quartoRepository;
        this.clienteRepository = clienteRepository;
        this.aluguelRepository = aluguelRepository;
        this.pagamentoService = pagamentoService;
    }

    @Override
    public void run(String... args) {
        if (quartoRepository.count() > 0) {
            return;
        }

        Residencia residencia = new Residencia(
                "Rua das Piscinas Naturais", "45", "Barra Grande", "45520-000",
                "73988001122", "pousada.marau@email.com");

        Quarto individual = new QuartoIndividual(120.0, true, false, 2, 30.0);
        Quarto duplo = new QuartoDuplo(200.0, true, true, TipoCamaCasal.QUEEN, true);
        Quarto familia = new QuartoFamilia(350.0, true, false,
                List.of(new Cama(TipoCama.CASAL, 1), new Cama(TipoCama.SOLTEIRO, 3)), 2);

        residencia.adicionarQuarto(individual);
        residencia.adicionarQuarto(duplo);
        residencia.adicionarQuarto(familia);
        residenciaRepository.save(residencia);

        Cliente joao = clienteRepository.save(new Cliente(
                "Joao Silva", "12345678900", "Rua A, 10 - Belo Horizonte",
                "31999998888", "joao.silva@email.com"));
        Cliente maria = clienteRepository.save(new Cliente(
                "Maria Souza", "98765432100", "Av. B, 200 - Salvador",
                "71988887777", "maria.souza@email.com"));

        Aluguel aluguelConcluido = new Aluguel(
                LocalDateTime.of(2026, 6, 10, 14, 0),
                LocalDateTime.of(2026, 6, 14, 11, 0),
                2, joao, individual, residencia);
        aluguelConcluido.calcularValorFinal();
        aluguelRepository.save(aluguelConcluido);

        Aluguel aluguelFuturo = new Aluguel(
                LocalDateTime.of(2026, 12, 20, 14, 0),
                LocalDateTime.of(2026, 12, 23, 10, 0),
                4, maria, familia, residencia);
        aluguelFuturo.calcularValorFinal();
        aluguelRepository.save(aluguelFuturo);

        pagamentoService.criar(aluguelConcluido.getId(), FormaPagamento.PIX);

        System.out.println(">>> Dados de demonstracao carregados: 1 residencia, 3 quartos, "
                + "2 clientes, 2 alugueis e 1 pagamento PIX confirmado");
    }
}
