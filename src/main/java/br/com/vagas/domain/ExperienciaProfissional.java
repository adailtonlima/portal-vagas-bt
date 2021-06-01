package br.com.vagas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ExperienciaProfissional.
 */
@Entity
@Table(name = "experiencia_profissional")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExperienciaProfissional implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "empresa")
    private String empresa;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "segmento")
    private String segmento;

    @Column(name = "porte")
    private String porte;

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "fim")
    private LocalDate fim;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao_atividade")
    private String descricaoAtividade;

    @Column(name = "salario")
    private Double salario;

    @Column(name = "beneficios")
    private Double beneficios;

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

    public ExperienciaProfissional id(Long id) {
        this.id = id;
        return this;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public ExperienciaProfissional empresa(String empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return this.cargo;
    }

    public ExperienciaProfissional cargo(String cargo) {
        this.cargo = cargo;
        return this;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSegmento() {
        return this.segmento;
    }

    public ExperienciaProfissional segmento(String segmento) {
        this.segmento = segmento;
        return this;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getPorte() {
        return this.porte;
    }

    public ExperienciaProfissional porte(String porte) {
        this.porte = porte;
        return this;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public LocalDate getInicio() {
        return this.inicio;
    }

    public ExperienciaProfissional inicio(LocalDate inicio) {
        this.inicio = inicio;
        return this;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFim() {
        return this.fim;
    }

    public ExperienciaProfissional fim(LocalDate fim) {
        this.fim = fim;
        return this;
    }

    public void setFim(LocalDate fim) {
        this.fim = fim;
    }

    public String getDescricaoAtividade() {
        return this.descricaoAtividade;
    }

    public ExperienciaProfissional descricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
        return this;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public Double getSalario() {
        return this.salario;
    }

    public ExperienciaProfissional salario(Double salario) {
        this.salario = salario;
        return this;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getBeneficios() {
        return this.beneficios;
    }

    public ExperienciaProfissional beneficios(Double beneficios) {
        this.beneficios = beneficios;
        return this;
    }

    public void setBeneficios(Double beneficios) {
        this.beneficios = beneficios;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public ExperienciaProfissional criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public ExperienciaProfissional pessoa(Pessoa pessoa) {
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
        if (!(o instanceof ExperienciaProfissional)) {
            return false;
        }
        return id != null && id.equals(((ExperienciaProfissional) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExperienciaProfissional{" +
            "id=" + getId() +
            ", empresa='" + getEmpresa() + "'" +
            ", cargo='" + getCargo() + "'" +
            ", segmento='" + getSegmento() + "'" +
            ", porte='" + getPorte() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", fim='" + getFim() + "'" +
            ", descricaoAtividade='" + getDescricaoAtividade() + "'" +
            ", salario=" + getSalario() +
            ", beneficios=" + getBeneficios() +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
