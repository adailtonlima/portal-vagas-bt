package br.com.vagas.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AreaAtuacao.
 */
@Entity
@Table(name = "area_atuacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AreaAtuacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "icone_url")
    private String iconeUrl;

    @Column(name = "ativo")
    private Boolean ativo;

    @NotNull
    @Column(name = "criado", nullable = false)
    private ZonedDateTime criado;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AreaAtuacao id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public AreaAtuacao nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public AreaAtuacao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIconeUrl() {
        return this.iconeUrl;
    }

    public AreaAtuacao iconeUrl(String iconeUrl) {
        this.iconeUrl = iconeUrl;
        return this;
    }

    public void setIconeUrl(String iconeUrl) {
        this.iconeUrl = iconeUrl;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public AreaAtuacao ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public AreaAtuacao criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaAtuacao)) {
            return false;
        }
        return id != null && id.equals(((AreaAtuacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaAtuacao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", iconeUrl='" + getIconeUrl() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
