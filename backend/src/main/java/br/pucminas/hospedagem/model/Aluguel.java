package br.pucminas.hospedagem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "aluguel")
public class Aluguel {

    private static final LocalTime LIMITE_CHECKOUT = LocalTime.of(12, 0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataEntrada;

    private LocalDateTime dataSaida;

    private int numeroHospedes;

    private int quantidadeDiarias;

    private double valorFinal;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "quarto_id")
    private Quarto quarto;

    @ManyToOne
    @JoinColumn(name = "residencia_id")
    private Residencia residencia;

    public Aluguel() {
    }

    public Aluguel(LocalDateTime dataEntrada, LocalDateTime dataSaida, int numeroHospedes,
                   Cliente cliente, Quarto quarto, Residencia residencia) {
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.numeroHospedes = numeroHospedes;
        this.cliente = cliente;
        this.quarto = quarto;
        this.residencia = residencia;
    }

    public int calcularQuantidadeDiarias() {
        if (dataEntrada == null || dataSaida == null) {
            throw new IllegalStateException("Datas de entrada e saida devem estar definidas");
        }
        if (!dataSaida.isAfter(dataEntrada)) {
            throw new IllegalStateException("Data de saida deve ser posterior a data de entrada");
        }

        long diasInteiros = ChronoUnit.DAYS.between(dataEntrada.toLocalDate(), dataSaida.toLocalDate());
        int diarias = (int) Math.max(diasInteiros, 1);

        if (dataSaida.toLocalTime().isAfter(LIMITE_CHECKOUT)) {
            diarias += 1;
        }

        return diarias;
    }

    public double calcularValorFinal() {
        if (quarto == null) {
            throw new IllegalStateException("Quarto deve estar definido");
        }
        this.quantidadeDiarias = calcularQuantidadeDiarias();
        double valorDiaria = quarto.calcularDiariaParaHospedes(numeroHospedes > 0 ? numeroHospedes : quarto.capacidadeMaxima());
        this.valorFinal = valorDiaria * this.quantidadeDiarias;
        return this.valorFinal;
    }

    public String imprimirFormulario() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FORMULARIO DE ALUGUEL ===\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Entrada: ").append(dataEntrada).append("\n");
        sb.append("Saida: ").append(dataSaida).append("\n");
        sb.append("Diarias: ").append(quantidadeDiarias).append("\n");
        sb.append("Hospedes: ").append(numeroHospedes).append("\n");
        if (cliente != null) {
            sb.append("Cliente: ").append(cliente.getNome()).append(" (CPF ").append(cliente.getCpf()).append(")\n");
        }
        if (quarto != null) {
            sb.append("Quarto ID: ").append(quarto.getId()).append("\n");
        }
        if (residencia != null) {
            sb.append("Residencia: ").append(residencia.getEndereco()).append(", ").append(residencia.getNumero()).append("\n");
        }
        sb.append("Valor final: R$ ").append(String.format("%.2f", valorFinal)).append("\n");
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public int getNumeroHospedes() {
        return numeroHospedes;
    }

    public void setNumeroHospedes(int numeroHospedes) {
        this.numeroHospedes = numeroHospedes;
    }

    public int getQuantidadeDiarias() {
        return quantidadeDiarias;
    }

    public void setQuantidadeDiarias(int quantidadeDiarias) {
        this.quantidadeDiarias = quantidadeDiarias;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public Residencia getResidencia() {
        return residencia;
    }

    public void setResidencia(Residencia residencia) {
        this.residencia = residencia;
    }
}
