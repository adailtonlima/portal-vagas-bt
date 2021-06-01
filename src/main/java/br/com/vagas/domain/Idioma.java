package br.com.vagas.domain;

import br.com.vagas.domain.enumeration.NivelIdioma;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Idioma.
 */
@Entity
@Table(name = "idioma")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Idioma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel")
    private NivelIdioma nivel;

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

    public Idioma id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Idioma nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public NivelIdioma getNivel() {
        return this.nivel;
    }

    public Idioma nivel(NivelIdioma nivel) {
        this.nivel = nivel;
        return this;
    }

    public void setNivel(NivelIdioma nivel) {
        this.nivel = nivel;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Idioma criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public Idioma pessoa(Pessoa pessoa) {
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
        if (!(o instanceof Idioma)) {
            return false;
        }
        return id != null && id.equals(((Idioma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Idioma{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", nivel='" + getNivel() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
