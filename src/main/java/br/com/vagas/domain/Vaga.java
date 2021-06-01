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
 * A Vaga.
 */
@Entity
@Table(name = "vaga")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vaga implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "estagio")
    private Boolean estagio;

    @Column(name = "salario")
    private Double salario;

    @Column(name = "beneficios")
    private Double beneficios;

    @Column(name = "jornada_semanal")
    private Double jornadaSemanal;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "fonte")
    private String fonte;

    @Column(name = "link_recrutamento")
    private String linkRecrutamento;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "preenchida")
    private Boolean preenchida;

    @NotNull
    @Column(name = "criado", nullable = false)
    private ZonedDateTime criado;

    @Column(name = "prazo_anuncio")
    private ZonedDateTime prazoAnuncio;

    @ManyToOne
    @JsonIgnoreProperties(value = { "endereco", "user" }, allowSetters = true)
    private Pessoa cadastrou;

    @ManyToOne
    @JsonIgnoreProperties(value = { "endereco", "user", "area" }, allowSetters = true)
    private Empresa empresa;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vaga id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Vaga descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Vaga titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getEstagio() {
        return this.estagio;
    }

    public Vaga estagio(Boolean estagio) {
        this.estagio = estagio;
        return this;
    }

    public void setEstagio(Boolean estagio) {
        this.estagio = estagio;
    }

    public Double getSalario() {
        return this.salario;
    }

    public Vaga salario(Double salario) {
        this.salario = salario;
        return this;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getBeneficios() {
        return this.beneficios;
    }

    public Vaga beneficios(Double beneficios) {
        this.beneficios = beneficios;
        return this;
    }

    public void setBeneficios(Double beneficios) {
        this.beneficios = beneficios;
    }

    public Double getJornadaSemanal() {
        return this.jornadaSemanal;
    }

    public Vaga jornadaSemanal(Double jornadaSemanal) {
        this.jornadaSemanal = jornadaSemanal;
        return this;
    }

    public void setJornadaSemanal(Double jornadaSemanal) {
        this.jornadaSemanal = jornadaSemanal;
    }

    public String getBannerUrl() {
        return this.bannerUrl;
    }

    public Vaga bannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        return this;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getFonte() {
        return this.fonte;
    }

    public Vaga fonte(String fonte) {
        this.fonte = fonte;
        return this;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getLinkRecrutamento() {
        return this.linkRecrutamento;
    }

    public Vaga linkRecrutamento(String linkRecrutamento) {
        this.linkRecrutamento = linkRecrutamento;
        return this;
    }

    public void setLinkRecrutamento(String linkRecrutamento) {
        this.linkRecrutamento = linkRecrutamento;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Vaga ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getPreenchida() {
        return this.preenchida;
    }

    public Vaga preenchida(Boolean preenchida) {
        this.preenchida = preenchida;
        return this;
    }

    public void setPreenchida(Boolean preenchida) {
        this.preenchida = preenchida;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Vaga criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public ZonedDateTime getPrazoAnuncio() {
        return this.prazoAnuncio;
    }

    public Vaga prazoAnuncio(ZonedDateTime prazoAnuncio) {
        this.prazoAnuncio = prazoAnuncio;
        return this;
    }

    public void setPrazoAnuncio(ZonedDateTime prazoAnuncio) {
        this.prazoAnuncio = prazoAnuncio;
    }

    public Pessoa getCadastrou() {
        return this.cadastrou;
    }

    public Vaga cadastrou(Pessoa pessoa) {
        this.setCadastrou(pessoa);
        return this;
    }

    public void setCadastrou(Pessoa pessoa) {
        this.cadastrou = pessoa;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public Vaga empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vaga)) {
            return false;
        }
        return id != null && id.equals(((Vaga) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vaga{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", titulo='" + getTitulo() + "'" +
            ", estagio='" + getEstagio() + "'" +
            ", salario=" + getSalario() +
            ", beneficios=" + getBeneficios() +
            ", jornadaSemanal=" + getJornadaSemanal() +
            ", bannerUrl='" + getBannerUrl() + "'" +
            ", fonte='" + getFonte() + "'" +
            ", linkRecrutamento='" + getLinkRecrutamento() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", preenchida='" + getPreenchida() + "'" +
            ", criado='" + getCriado() + "'" +
            ", prazoAnuncio='" + getPrazoAnuncio() + "'" +
            "}";
    }
}
