package br.com.vagas.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.vagas.domain.Vaga} entity.
 */
public class VagaDTO implements Serializable {

    private Long id;

    @Lob
    private String descricao;

    @NotNull
    private String titulo;

    private Boolean estagio;

    private Double salario;

    private Double beneficios;

    private Double jornadaSemanal;

    private String bannerUrl;

    private String fonte;

    private String linkRecrutamento;

    private Boolean ativo;

    private Boolean preenchida;

    @NotNull
    private ZonedDateTime criado;

    private ZonedDateTime prazoAnuncio;

    private PessoaDTO cadastrou;

    private EmpresaDTO empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getEstagio() {
        return estagio;
    }

    public void setEstagio(Boolean estagio) {
        this.estagio = estagio;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(Double beneficios) {
        this.beneficios = beneficios;
    }

    public Double getJornadaSemanal() {
        return jornadaSemanal;
    }

    public void setJornadaSemanal(Double jornadaSemanal) {
        this.jornadaSemanal = jornadaSemanal;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getLinkRecrutamento() {
        return linkRecrutamento;
    }

    public void setLinkRecrutamento(String linkRecrutamento) {
        this.linkRecrutamento = linkRecrutamento;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getPreenchida() {
        return preenchida;
    }

    public void setPreenchida(Boolean preenchida) {
        this.preenchida = preenchida;
    }

    public ZonedDateTime getCriado() {
        return criado;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    public ZonedDateTime getPrazoAnuncio() {
        return prazoAnuncio;
    }

    public void setPrazoAnuncio(ZonedDateTime prazoAnuncio) {
        this.prazoAnuncio = prazoAnuncio;
    }

    public PessoaDTO getCadastrou() {
        return cadastrou;
    }

    public void setCadastrou(PessoaDTO cadastrou) {
        this.cadastrou = cadastrou;
    }

    public EmpresaDTO getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VagaDTO)) {
            return false;
        }

        VagaDTO vagaDTO = (VagaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vagaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VagaDTO{" +
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
            ", cadastrou=" + getCadastrou() +
            ", empresa=" + getEmpresa() +
            "}";
    }
}
