package br.com.vagas.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.ExperienciaProfissional} entity.
 */
public class ExperienciaProfissionalDTO implements Serializable {

    private Long id;

    private String empresa;

    private String cargo;

    private String segmento;

    private String porte;

    private LocalDate inicio;

    private LocalDate fim;

    @Lob
    private String descricaoAtividade;

    private Double salario;

    private Double beneficios;

    @NotNull
    private ZonedDateTime criado;

    private PessoaDTO pessoa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public void setFim(LocalDate fim) {
        this.fim = fim;
    }

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(Double beneficios) {
        this.beneficios = beneficios;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public PessoaDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaDTO pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExperienciaProfissionalDTO)) {
            return false;
        }

        ExperienciaProfissionalDTO experienciaProfissionalDTO = (ExperienciaProfissionalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, experienciaProfissionalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExperienciaProfissionalDTO{" +
            "id=" + getId() +
            ", empresa='" + getEmpresa() + "'" +
            ", cargo='" + getCargo() + "'" +
            ", segmento='" + getSegmento() + "'" +
            ", porte='" + getPorte() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", descricaoAtividade='" + getDescricaoAtividade() + "'" +
            ", salario=" + getSalario() +
            ", beneficios=" + getBeneficios() +
            ", criado='" + getCriado() + "'" +
            ", pessoa=" + getPessoa() +
            "}";
    }
}
