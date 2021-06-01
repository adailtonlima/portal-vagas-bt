package br.com.vagas.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "porte")
    private String porte;

    @NotNull
    @Column(name = "criado", nullable = false)
    private ZonedDateTime criado;

    @OneToOne
    @JoinColumn(unique = true)
    private Endereco endereco;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private AreaAtuacao area;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Empresa nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Empresa cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPorte() {
        return this.porte;
    }

    public Empresa porte(String porte) {
        this.porte = porte;
        return this;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Empresa criado(ZonedDateTime criado) {
        this.criado = criado;
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public Empresa endereco(Endereco endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public User getUser() {
        return this.user;
    }

    public Empresa user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AreaAtuacao getArea() {
        return this.area;
    }

    public Empresa area(AreaAtuacao areaAtuacao) {
        this.setArea(areaAtuacao);
        return this;
    }

    public void setArea(AreaAtuacao areaAtuacao) {
        this.area = areaAtuacao;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empresa)) {
            return false;
        }
        return id != null && id.equals(((Empresa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", porte='" + getPorte() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
