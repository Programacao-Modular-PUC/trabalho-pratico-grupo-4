package br.pucminas.hospedagem.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "quarto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_quarto")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuartoIndividual.class, name = "INDIVIDUAL"),
        @JsonSubTypes.Type(value = QuartoDuplo.class, name = "DUPLO"),
        @JsonSubTypes.Type(value = QuartoFamilia.class, name = "FAMILIA")
})
public abstract class Quarto {

    public static final double TAXA_AR_CONDICIONADO = 50.0;
    public static final double TAXA_HIDROMASSAGEM = 80.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double valorBase;

    private boolean possuiAR;

    private boolean possuiHidro;

    protected Quarto() {
    }

    protected Quarto(double valorBase, boolean possuiAR, boolean possuiHidro) {
        this.valorBase = valorBase;
        this.possuiAR = possuiAR;
        this.possuiHidro = possuiHidro;
    }

    public abstract double calcularDiaria();

    public abstract int capacidadeMaxima();

    public double calcularDiariaParaHospedes(int numeroHospedes) {
        return calcularDiaria();
    }

    protected double adicionaisDeConforto() {
        double adicional = 0.0;
        if (possuiAR) {
            adicional += TAXA_AR_CONDICIONADO;
        }
        if (possuiHidro) {
            adicional += TAXA_HIDROMASSAGEM;
        }
        return adicional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public boolean isPossuiAR() {
        return possuiAR;
    }

    public void setPossuiAR(boolean possuiAR) {
        this.possuiAR = possuiAR;
    }

    public boolean isPossuiHidro() {
        return possuiHidro;
    }

    public void setPossuiHidro(boolean possuiHidro) {
        this.possuiHidro = possuiHidro;
    }
}
