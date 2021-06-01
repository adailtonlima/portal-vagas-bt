package br.com.vagas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A PerfilProfissional.
 */
@Entity
@Table(name = "perfil_profissional")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PerfilProfissional implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "estagio")
    private Boolean estagio;

    @Column(name = "procurando_emprego")
    private Boolean procurandoEmprego;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "objetivos_pessoais")
    private String objetivosPessoais;

    @Column(name = "pretensao_salarial")
    private Double pretensaoSalarial;

    @NotNull
    @Column(name = "criado", nullable = false)
    private ZonedDateTime criado;

    @ManyToOne
    private AreaAtuacao area;

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

    public PerfilProfissional id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getEstagio() {
        return this.estagio;
    }

    public PerfilProfissional estagio(Boolean estagio) {
        this.estagio = estagio;
        return this;
    }

    public void setEstagio(Boolean estagio) {
        this.estagio = estagio;
    }

    public Boolean getProcurandoEmprego() {
        return this.procurandoEmprego;
    }

    public PerfilProfissional procurandoEmprego(Boolean procurandoEmprego) {
        this.procurandoEmprego = procurandoEmprego;
        return this;
    }

    public void setProcurandoEmprego(Boolean procurandoEmprego) {
        this.procurandoEmprego = procurandoEmprego;
    }

    public String getObjetivosPessoais() {
        return this.objetivosPessoais;
    }

    public PerfilProfissional objetivosPessoais(String objetivosPessoais) {
        this.objetivosPessoais = objetivosPessoais;
        return this;
    }

    public void setObjetivosPessoais(String objetivosPessoais) {
        this.objetivosPessoais = objetivosPessoais;
    }

    public Double getPretensaoSalarial() {
        return this.pretensaoSalarial;
    }

    public PerfilProfissional pretensaoSalarial(Double pretensaoSalarial) {
        this.pretensaoSalarial = pretensaoSalarial;
        return this;
    }

    public void setPretensaoSalarial(Double pretensaoSalarial) {
        this.pretensaoSalarial = pretensaoSalarial;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public PerfilProfissional criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public AreaAtuacao getArea() {
        return this.area;
    }

    public PerfilProfissional area(AreaAtuacao areaAtuacao) {
        this.setArea(areaAtuacao);
        return this;
    }

    public void setArea(AreaAtuacao areaAtuacao) {
        this.area = areaAtuacao;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public PerfilProfissional pessoa(Pessoa pessoa) {
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
        if (!(o instanceof PerfilProfissional)) {
            return false;
        }
        return id != null && id.equals(((PerfilProfissional) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilProfissional{" +
            "id=" + getId() +
            ", estagio='" + getEstagio() + "'" +
            ", procurandoEmprego='" + getProcurandoEmprego() + "'" +
            ", objetivosPessoais='" + getObjetivosPessoais() + "'" +
            ", pretensaoSalarial=" + getPretensaoSalarial() +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
