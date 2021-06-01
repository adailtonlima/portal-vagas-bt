package br.com.vagas.domain;

import br.com.vagas.domain.enumeration.Funcao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FuncaoPessoa.
 */
@Entity
@Table(name = "funcao_pessoa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FuncaoPessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "funcao")
    private Funcao funcao;

    @NotNull
    @Column(name = "criado", nullable = false)
    private ZonedDateTime criado;

    @Column(name = "ativo")
    private Boolean ativo;

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

    public FuncaoPessoa id(Long id) {
        this.id = id;
        return this;
    }

    public Funcao getFuncao() {
        return this.funcao;
    }

    public FuncaoPessoa funcao(Funcao funcao) {
        this.funcao = funcao;
        return this;
    }

    public void setFuncao(Funcao funcao) {
        this.funcao = funcao;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public FuncaoPessoa criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public FuncaoPessoa ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public FuncaoPessoa pessoa(Pessoa pessoa) {
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
        if (!(o instanceof FuncaoPessoa)) {
            return false;
        }
        return id != null && id.equals(((FuncaoPessoa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncaoPessoa{" +
            "id=" + getId() +
            ", funcao='" + getFuncao() + "'" +
            ", criado='" + getCriado() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
