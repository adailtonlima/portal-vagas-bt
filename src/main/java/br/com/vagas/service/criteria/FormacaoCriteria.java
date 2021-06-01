package br.com.vagas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link br.com.vagas.domain.Formacao} entity. This class is used
 * in {@link br.com.vagas.web.rest.FormacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /formacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FormacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter instituicao;

    private StringFilter tipo;

    private LocalDateFilter inicio;

    private LocalDateFilter conclusao;

    private ZonedDateTimeFilter criado;

    private LongFilter pessoaId;

    public FormacaoCriteria() {}

    public FormacaoCriteria(FormacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.instituicao = other.instituicao == null ? null : other.instituicao.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.inicio = other.inicio == null ? null : other.inicio.copy();
        this.conclusao = other.conclusao == null ? null : other.conclusao.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.pessoaId = other.pessoaId == null ? null : other.pessoaId.copy();
    }

    @Override
    public FormacaoCriteria copy() {
        return new FormacaoCriteria(this);
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

    public StringFilter getInstituicao() {
        return instituicao;
    }

    public StringFilter instituicao() {
        if (instituicao == null) {
            instituicao = new StringFilter();
        }
        return instituicao;
    }

    public void setInstituicao(StringFilter instituicao) {
        this.instituicao = instituicao;
    }

    public StringFilter getTipo() {
        return tipo;
    }

    public StringFilter tipo() {
        if (tipo == null) {
            tipo = new StringFilter();
        }
        return tipo;
    }

    public void setTipo(StringFilter tipo) {
        this.tipo = tipo;
    }

    public LocalDateFilter getInicio() {
        return inicio;
    }

    public LocalDateFilter inicio() {
        if (inicio == null) {
            inicio = new LocalDateFilter();
        }
        return inicio;
    }

    public void setInicio(LocalDateFilter inicio) {
        this.inicio = inicio;
    }

    public LocalDateFilter getConclusao() {
        return conclusao;
    }

    public LocalDateFilter conclusao() {
        if (conclusao == null) {
            conclusao = new LocalDateFilter();
        }
        return conclusao;
    }

    public void setConclusao(LocalDateFilter conclusao) {
        this.conclusao = conclusao;
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

    public LongFilter getPessoaId() {
        return pessoaId;
    }

    public LongFilter pessoaId() {
        if (pessoaId == null) {
            pessoaId = new LongFilter();
        }
        return pessoaId;
    }

    public void setPessoaId(LongFilter pessoaId) {
        this.pessoaId = pessoaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FormacaoCriteria that = (FormacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(instituicao, that.instituicao) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(inicio, that.inicio) &&
            Objects.equals(conclusao, that.conclusao) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(pessoaId, that.pessoaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instituicao, tipo, inicio, conclusao, criado, pessoaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (instituicao != null ? "instituicao=" + instituicao + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (inicio != null ? "inicio=" + inicio + ", " : "") +
            (conclusao != null ? "conclusao=" + conclusao + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (pessoaId != null ? "pessoaId=" + pessoaId + ", " : "") +
            "}";
    }
}
