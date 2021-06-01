package br.com.vagas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.vagas.domain.Vaga} entity. This class is used
 * in {@link br.com.vagas.web.rest.VagaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vagas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VagaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titulo;

    private BooleanFilter estagio;

    private DoubleFilter salario;

    private DoubleFilter beneficios;

    private DoubleFilter jornadaSemanal;

    private StringFilter bannerUrl;

    private StringFilter fonte;

    private StringFilter linkRecrutamento;

    private BooleanFilter ativo;

    private BooleanFilter preenchida;

    private ZonedDateTimeFilter criado;

    private ZonedDateTimeFilter prazoAnuncio;

    private LongFilter cadastrouId;

    private LongFilter empresaId;

    public VagaCriteria() {}

    public VagaCriteria(VagaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.estagio = other.estagio == null ? null : other.estagio.copy();
        this.salario = other.salario == null ? null : other.salario.copy();
        this.beneficios = other.beneficios == null ? null : other.beneficios.copy();
        this.jornadaSemanal = other.jornadaSemanal == null ? null : other.jornadaSemanal.copy();
        this.bannerUrl = other.bannerUrl == null ? null : other.bannerUrl.copy();
        this.fonte = other.fonte == null ? null : other.fonte.copy();
        this.linkRecrutamento = other.linkRecrutamento == null ? null : other.linkRecrutamento.copy();
        this.ativo = other.ativo == null ? null : other.ativo.copy();
        this.preenchida = other.preenchida == null ? null : other.preenchida.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.prazoAnuncio = other.prazoAnuncio == null ? null : other.prazoAnuncio.copy();
        this.cadastrouId = other.cadastrouId == null ? null : other.cadastrouId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
    }

    @Override
    public VagaCriteria copy() {
        return new VagaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitulo() {
        return titulo;
    }

    public StringFilter titulo() {
        if (titulo == null) {
            titulo = new StringFilter();
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public BooleanFilter getEstagio() {
        return estagio;
    }

    public BooleanFilter estagio() {
        if (estagio == null) {
            estagio = new BooleanFilter();
        }
        return estagio;
    }

    public void setEstagio(BooleanFilter estagio) {
        this.estagio = estagio;
    }

    public DoubleFilter getSalario() {
        return salario;
    }

    public DoubleFilter salario() {
        if (salario == null) {
            salario = new DoubleFilter();
        }
        return salario;
    }

    public void setSalario(DoubleFilter salario) {
        this.salario = salario;
    }

    public DoubleFilter getBeneficios() {
        return beneficios;
    }

    public DoubleFilter beneficios() {
        if (beneficios == null) {
            beneficios = new DoubleFilter();
        }
        return beneficios;
    }

    public void setBeneficios(DoubleFilter beneficios) {
        this.beneficios = beneficios;
    }

    public DoubleFilter getJornadaSemanal() {
        return jornadaSemanal;
    }

    public DoubleFilter jornadaSemanal() {
        if (jornadaSemanal == null) {
            jornadaSemanal = new DoubleFilter();
        }
        return jornadaSemanal;
    }

    public void setJornadaSemanal(DoubleFilter jornadaSemanal) {
        this.jornadaSemanal = jornadaSemanal;
    }

    public StringFilter getBannerUrl() {
        return bannerUrl;
    }

    public StringFilter bannerUrl() {
        if (bannerUrl == null) {
            bannerUrl = new StringFilter();
        }
        return bannerUrl;
    }

    public void setBannerUrl(StringFilter bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public StringFilter getFonte() {
        return fonte;
    }

    public StringFilter fonte() {
        if (fonte == null) {
            fonte = new StringFilter();
        }
        return fonte;
    }

    public void setFonte(StringFilter fonte) {
        this.fonte = fonte;
    }

    public StringFilter getLinkRecrutamento() {
        return linkRecrutamento;
    }

    public StringFilter linkRecrutamento() {
        if (linkRecrutamento == null) {
            linkRecrutamento = new StringFilter();
        }
        return linkRecrutamento;
    }

    public void setLinkRecrutamento(StringFilter linkRecrutamento) {
        this.linkRecrutamento = linkRecrutamento;
    }

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public BooleanFilter ativo() {
        if (ativo == null) {
            ativo = new BooleanFilter();
        }
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
    }

    public BooleanFilter getPreenchida() {
        return preenchida;
    }

    public BooleanFilter preenchida() {
        if (preenchida == null) {
            preenchida = new BooleanFilter();
        }
        return preenchida;
    }

    public void setPreenchida(BooleanFilter preenchida) {
        this.preenchida = preenchida;
    }

    public ZonedDateTimeFilter getCriado() {
        return criado;
    }

    public ZonedDateTimeFilter criado() {
        if (criado == null) {
            criado = new ZonedDateTimeFilter();
        }
        return criado;
    }

    public void setCriado(ZonedDateTimeFilter criado) {
        this.criado = criado;
    }

    public ZonedDateTimeFilter getPrazoAnuncio() {
        return prazoAnuncio;
    }

    public ZonedDateTimeFilter prazoAnuncio() {
        if (prazoAnuncio == null) {
            prazoAnuncio = new ZonedDateTimeFilter();
        }
        return prazoAnuncio;
    }

    public void setPrazoAnuncio(ZonedDateTimeFilter prazoAnuncio) {
        this.prazoAnuncio = prazoAnuncio;
    }

    public LongFilter getCadastrouId() {
        return cadastrouId;
    }

    public LongFilter cadastrouId() {
        if (cadastrouId == null) {
            cadastrouId = new LongFilter();
        }
        return cadastrouId;
    }

    public void setCadastrouId(LongFilter cadastrouId) {
        this.cadastrouId = cadastrouId;
    }

    public LongFilter getEmpresaId() {
        return empresaId;
    }

    public LongFilter empresaId() {
        if (empresaId == null) {
            empresaId = new LongFilter();
        }
        return empresaId;
    }

    public void setEmpresaId(LongFilter empresaId) {
        this.empresaId = empresaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VagaCriteria that = (VagaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(estagio, that.estagio) &&
            Objects.equals(salario, that.salario) &&
            Objects.equals(beneficios, that.beneficios) &&
            Objects.equals(jornadaSemanal, that.jornadaSemanal) &&
            Objects.equals(bannerUrl, that.bannerUrl) &&
            Objects.equals(fonte, that.fonte) &&
            Objects.equals(linkRecrutamento, that.linkRecrutamento) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(preenchida, that.preenchida) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(prazoAnuncio, that.prazoAnuncio) &&
            Objects.equals(cadastrouId, that.cadastrouId) &&
            Objects.equals(empresaId, that.empresaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            titulo,
            estagio,
            salario,
            beneficios,
            jornadaSemanal,
            bannerUrl,
            fonte,
            linkRecrutamento,
            ativo,
            preenchida,
            criado,
            prazoAnuncio,
            cadastrouId,
            empresaId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VagaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (estagio != null ? "estagio=" + estagio + ", " : "") +
            (salario != null ? "salario=" + salario + ", " : "") +
            (beneficios != null ? "beneficios=" + beneficios + ", " : "") +
            (jornadaSemanal != null ? "jornadaSemanal=" + jornadaSemanal + ", " : "") +
            (bannerUrl != null ? "bannerUrl=" + bannerUrl + ", " : "") +
            (fonte != null ? "fonte=" + fonte + ", " : "") +
            (linkRecrutamento != null ? "linkRecrutamento=" + linkRecrutamento + ", " : "") +
            (ativo != null ? "ativo=" + ativo + ", " : "") +
            (preenchida != null ? "preenchida=" + preenchida + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (prazoAnuncio != null ? "prazoAnuncio=" + prazoAnuncio + ", " : "") +
            (cadastrouId != null ? "cadastrouId=" + cadastrouId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            "}";
    }
}
