package br.com.vagas.service.dto;

import br.com.vagas.domain.enumeration.NivelIdioma;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.Idioma} entity.
 */
public class IdiomaDTO implements Serializable {

    private Long id;

    private String nome;

    private NivelIdioma nivel;

    @NotNull
    private ZonedDateTime criado;

    private PessoaDTO pessoa;

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

    public NivelIdioma getNivel() {
        return nivel;
    }

    public void setNivel(NivelIdioma nivel) {
        this.nivel = nivel;
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
        if (!(o instanceof IdiomaDTO)) {
            return false;
        }

        IdiomaDTO idiomaDTO = (IdiomaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, idiomaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdiomaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", nivel='" + getNivel() + "'" +
            ", criado='" + getCriado() + "'" +
            ", pessoa=" + getPessoa() +
            "}";
    }
}
