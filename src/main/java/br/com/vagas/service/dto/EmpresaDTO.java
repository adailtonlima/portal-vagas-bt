package br.com.vagas.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.Empresa} entity.
 */
public class EmpresaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String cnpj;

    private String porte;

    @NotNull
    private ZonedDateTime criado;

    private EnderecoDTO endereco;

    private UserDTO user;

    private AreaAtuacaoDTO area;

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AreaAtuacaoDTO getArea() {
        return area;
    }

    public void setArea(AreaAtuacaoDTO area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpresaDTO)) {
            return false;
        }

        EmpresaDTO empresaDTO = (EmpresaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empresaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpresaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", porte='" + getPorte() + "'" +
            ", criado='" + getCriado() + "'" +
            ", endereco=" + getEndereco() +
            ", user=" + getUser() +
            ", area=" + getArea() +
            "}";
    }
}
