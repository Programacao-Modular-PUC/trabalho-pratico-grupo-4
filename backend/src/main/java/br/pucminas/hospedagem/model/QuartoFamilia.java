package br.pucminas.hospedagem.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("FAMILIA")
public class QuartoFamilia extends Quarto {

    public static final double PERCENTUAL_POR_HOSPEDE = 0.05;
    public static final double VALOR_POR_AMBIENTE = 70.0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "quarto_familia_camas", joinColumns = @JoinColumn(name = "quarto_id"))
    private List<Cama> camas = new ArrayList<>();

    private int quantidadeAmbientes;

    public QuartoFamilia() {
    }

    public QuartoFamilia(double valorBase, boolean possuiAR, boolean possuiHidro,
                         List<Cama> camas, int quantidadeAmbientes) {
        super(valorBase, possuiAR, possuiHidro);
        this.camas = (camas != null) ? camas : new ArrayList<>();
        this.quantidadeAmbientes = quantidadeAmbientes;
    }

    @Override
    public double calcularDiaria() {
        return calcularDiariaParaHospedes(capacidadeMaxima());
    }

    @Override
    public double calcularDiariaParaHospedes(int numeroHospedes) {
        if (numeroHospedes <= 0) {
            throw new IllegalArgumentException("Numero de hospedes deve ser maior que zero");
        }
        if (numeroHospedes > capacidadeMaxima()) {
            throw new IllegalArgumentException(
                    "Numero de hospedes (" + numeroHospedes + ") excede a capacidade do quarto (" + capacidadeMaxima() + ")");
        }

        double valorComPercentualPorHospede = getValorBase() * (1 + PERCENTUAL_POR_HOSPEDE * numeroHospedes);
        double valorAmbientes = quantidadeAmbientes * VALOR_POR_AMBIENTE;
        double valorBruto = valorComPercentualPorHospede + valorAmbientes + adicionaisDeConforto();

        double desconto = calcularDescontoProgressivo(numeroHospedes);
        return valorBruto * (1.0 - desconto);
    }

    private double calcularDescontoProgressivo(int numeroHospedes) {
        if (numeroHospedes >= 8) {
            return 0.15;
        }
        if (numeroHospedes >= 6) {
            return 0.10;
        }
        if (numeroHospedes >= 4) {
            return 0.05;
        }
        return 0.0;
    }

    @Override
    public int capacidadeMaxima() {
        return camas.stream().mapToInt(Cama::capacidadeDeHospedes).sum();
    }

    public List<Cama> getCamas() {
        return camas;
    }

    public void setCamas(List<Cama> camas) {
        this.camas = (camas != null) ? camas : new ArrayList<>();
    }

    public int getQuantidadeAmbientes() {
        return quantidadeAmbientes;
    }

    public void setQuantidadeAmbientes(int quantidadeAmbientes) {
        this.quantidadeAmbientes = quantidadeAmbientes;
    }
}
