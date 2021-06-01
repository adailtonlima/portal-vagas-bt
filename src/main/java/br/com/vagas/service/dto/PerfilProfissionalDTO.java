package br.com.vagas.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.PerfilProfissional} entity.
 */
public class PerfilProfissionalDTO implements Serializable {

    private Long id;

    private Boolean estagio;

    private Boolean procurandoEmprego;

    @Lob
    private String objetivosPessoais;

    private Double pretensaoSalarial;

    @NotNull
    private ZonedDateTime criado;

    private AreaAtuacaoDTO area;

    private PessoaDTO pessoa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstagio() {
        return estagio;
    }

    public void setEstagio(Boolean estagio) {
        this.estagio = estagio;
    }

    public Boolean getProcurandoEmprego() {
        return procurandoEmprego;
    }

    public void setProcurandoEmprego(Boolean procurandoEmprego) {
        this.procurandoEmprego = procurandoEmprego;
    }

    public String getObjetivosPessoais() {
        return objetivosPessoais;
    }

    public void setObjetivosPessoais(String objetivosPessoais) {
        this.objetivosPessoais = objetivosPessoais;
    }

    public Double getPretensaoSalarial() {
        return pretensaoSalarial;
    }

    public void setPretensaoSalarial(Double pretensaoSalarial) {
        this.pretensaoSalarial = pretensaoSalarial;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public AreaAtuacaoDTO getArea() {
        return area;
    }

    public void setArea(AreaAtuacaoDTO area) {
        this.area = area;
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
        if (!(o instanceof PerfilProfissionalDTO)) {
            return false;
        }

        PerfilProfissionalDTO perfilProfissionalDTO = (PerfilProfissionalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, perfilProfissionalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilProfissionalDTO{" +
            "id=" + getId() +
            ", estagio='" + getEstagio() + "'" +
            ", procurandoEmprego='" + getProcurandoEmprego() + "'" +
            ", objetivosPessoais='" + getObjetivosPessoais() + "'" +
            ", pretensaoSalarial=" + getPretensaoSalarial() +
            ", criado='" + getCriado() + "'" +
            ", area=" + getArea() +
            ", pessoa=" + getPessoa() +
            "}";
    }
}
