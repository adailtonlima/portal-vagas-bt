package br.com.vagas.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.Formacao} entity.
 */
public class FormacaoDTO implements Serializable {

    private Long id;

    private String instituicao;

    private String tipo;

    private LocalDate inicio;

    private LocalDate conclusao;

    @NotNull
    private ZonedDateTime criado;

    private PessoaDTO pessoa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getConclusao() {
        return conclusao;
    }

    public void setConclusao(LocalDate conclusao) {
        this.conclusao = conclusao;
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
        if (!(o instanceof FormacaoDTO)) {
            return false;
        }

        FormacaoDTO formacaoDTO = (FormacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormacaoDTO{" +
            "id=" + getId() +
            ", instituicao='" + getInstituicao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", conclusao='" + getConclusao() + "'" +
            ", criado='" + getCriado() + "'" +
            ", pessoa=" + getPessoa() +
            "}";
    }
}
