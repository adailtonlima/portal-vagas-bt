package br.com.vagas.service.dto;

import br.com.vagas.domain.enumeration.Funcao;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.FuncaoPessoa} entity.
 */
public class FuncaoPessoaDTO implements Serializable {

    private Long id;

    private Funcao funcao;

    @NotNull
    private ZonedDateTime criado;

    private Boolean ativo;

    private PessoaDTO pessoa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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
        if (!(o instanceof FuncaoPessoaDTO)) {
            return false;
        }

        FuncaoPessoaDTO funcaoPessoaDTO = (FuncaoPessoaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, funcaoPessoaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncaoPessoaDTO{" +
            "id=" + getId() +
            ", funcao='" + getFuncao() + "'" +
            ", criado='" + getCriado() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", pessoa=" + getPessoa() +
            "}";
    }
}
