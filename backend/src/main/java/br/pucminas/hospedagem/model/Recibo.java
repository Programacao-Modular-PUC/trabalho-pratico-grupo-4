package br.pucminas.hospedagem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "recibo")
public class Recibo {

    private static final DateTimeFormatter FORMATADOR_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataEmissao;

    @OneToOne
    @JoinColumn(name = "pagamento_id", nullable = false, unique = true)
    private Pagamento pagamento;

    public Recibo() {
    }

    public Recibo(Pagamento pagamento) {
        this.pagamento = pagamento;
        this.dataEmissao = LocalDateTime.now();
    }

    public String gerar() {
        if (pagamento == null || pagamento.getAluguel() == null) {
            throw new IllegalStateException("Pagamento e aluguel devem estar definidos para gerar o recibo");
        }

        Aluguel aluguel = pagamento.getAluguel();
        String cliente = aluguel.getCliente() != null ? aluguel.getCliente().getNome() : "Nao informado";
        String quarto = aluguel.getQuarto() != null
                ? String.valueOf(aluguel.getQuarto().getId())
                : "Nao informado";
        LocalDateTime emissao = dataEmissao != null ? dataEmissao : LocalDateTime.now();

        return String.format(Locale.forLanguageTag("pt-BR"),
                "=== RECIBO DE PAGAMENTO ===%n"
                        + "Pagamento: %s%n"
                        + "Data de emissao: %s%n"
                        + "Cliente: %s%n"
                        + "Quarto: %s%n"
                        + "Diarias: %d%n"
                        + "Valor: R$ %.2f%n"
                        + "Forma de pagamento: %s%n"
                        + "Status: %s%n",
                pagamento.getId() != null ? pagamento.getId() : "Nao informado",
                emissao.format(FORMATADOR_DATA),
                cliente,
                quarto,
                aluguel.getQuantidadeDiarias(),
                pagamento.getValor(),
                pagamento.getFormaPagamento(),
                pagamento.getStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
