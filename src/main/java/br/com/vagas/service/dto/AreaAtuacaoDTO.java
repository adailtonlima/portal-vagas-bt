package br.com.vagas.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.AreaAtuacao} entity.
 */
public class AreaAtuacaoDTO implements Serializable {

    private Long id;

    private String nome;

    private String descricao;

    private String iconeUrl;

    private Boolean ativo;

    @NotNull
    private ZonedDateTime criado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIconeUrl() {
        return iconeUrl;
    }

    public void setIconeUrl(String iconeUrl) {
        this.iconeUrl = iconeUrl;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaAtuacaoDTO)) {
            return false;
        }

        AreaAtuacaoDTO areaAtuacaoDTO = (AreaAtuacaoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, areaAtuacaoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaAtuacaoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", iconeUrl='" + getIconeUrl() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
