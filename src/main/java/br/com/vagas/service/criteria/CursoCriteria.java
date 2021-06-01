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
 * Criteria class for the {@link br.com.vagas.domain.Curso} entity. This class is used
 * in {@link br.com.vagas.web.rest.CursoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cursos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CursoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private LocalDateFilter periodo;

    private ZonedDateTimeFilter criado;

    private LongFilter pessoaId;

    public CursoCriteria() {}

    public CursoCriteria(CursoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.periodo = other.periodo == null ? null : other.periodo.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.pessoaId = other.pessoaId == null ? null : other.pessoaId.copy();
    }

    @Override
    public CursoCriteria copy() {
        return new CursoCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public LocalDateFilter getPeriodo() {
        return periodo;
    }

    public LocalDateFilter periodo() {
        if (periodo == null) {
            periodo = new LocalDateFilter();
        }
        return periodo;
    }

    public void setPeriodo(LocalDateFilter periodo) {
        this.periodo = periodo;
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
        final CursoCriteria that = (CursoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(periodo, that.periodo) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(pessoaId, that.pessoaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, periodo, criado, pessoaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (periodo != null ? "periodo=" + periodo + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (pessoaId != null ? "pessoaId=" + pessoaId + ", " : "") +
            "}";
    }
}
