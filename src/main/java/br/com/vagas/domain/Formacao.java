package br.com.vagas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Formacao.
 */
@Entity
@Table(name = "formacao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Formacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "instituicao")
    private String instituicao;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "conclusao")
    private LocalDate conclusao;

    @NotNull
    @Column(name = "criado", nullable = false)
    private ZonedDateTime criado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "endereco", "user" }, allowSetters = true)
    private Pessoa pessoa;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Formacao id(Long id) {
        this.id = id;
        return this;
    }

    public String getInstituicao() {
        return this.instituicao;
    }

    public Formacao instituicao(String instituicao) {
        this.instituicao = instituicao;
        return this;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Formacao tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getInicio() {
        return this.inicio;
    }

    public Formacao inicio(LocalDate inicio) {
        this.inicio = inicio;
        return this;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getConclusao() {
        return this.conclusao;
    }

    public Formacao conclusao(LocalDate conclusao) {
        this.conclusao = conclusao;
        return this;
    }

    public void setConclusao(LocalDate conclusao) {
        this.conclusao = conclusao;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Formacao criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public Formacao pessoa(Pessoa pessoa) {
        this.setPessoa(pessoa);
        return this;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formacao)) {
            return false;
        }
        return id != null && id.equals(((Formacao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formacao{" +
            "id=" + getId() +
            ", instituicao='" + getInstituicao() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", conclusao='" + getConclusao() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
