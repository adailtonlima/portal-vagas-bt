package br.com.vagas.service.criteria;

import br.com.vagas.domain.enumeration.Funcao;
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
 * Criteria class for the {@link br.com.vagas.domain.FuncaoPessoa} entity. This class is used
 * in {@link br.com.vagas.web.rest.FuncaoPessoaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /funcao-pessoas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FuncaoPessoaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Funcao
     */
    public static class FuncaoFilter extends Filter<Funcao> {

        public FuncaoFilter() {}

        public FuncaoFilter(FuncaoFilter filter) {
            super(filter);
        }

        @Override
        public FuncaoFilter copy() {
            return new FuncaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FuncaoFilter funcao;

    private ZonedDateTimeFilter criado;

    private BooleanFilter ativo;

    private LongFilter pessoaId;

    public FuncaoPessoaCriteria() {}

    public FuncaoPessoaCriteria(FuncaoPessoaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.funcao = other.funcao == null ? null : other.funcao.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.ativo = other.ativo == null ? null : other.ativo.copy();
        this.pessoaId = other.pessoaId == null ? null : other.pessoaId.copy();
    }

    @Override
    public FuncaoPessoaCriteria copy() {
        return new FuncaoPessoaCriteria(this);
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

    public FuncaoFilter getFuncao() {
        return funcao;
    }

    public FuncaoFilter funcao() {
        if (funcao == null) {
            funcao = new FuncaoFilter();
        }
        return funcao;
    }

    public void setFuncao(FuncaoFilter funcao) {
        this.funcao = funcao;
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
        final FuncaoPessoaCriteria that = (FuncaoPessoaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(funcao, that.funcao) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(pessoaId, that.pessoaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, funcao, criado, ativo, pessoaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncaoPessoaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (funcao != null ? "funcao=" + funcao + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (ativo != null ? "ativo=" + ativo + ", " : "") +
            (pessoaId != null ? "pessoaId=" + pessoaId + ", " : "") +
            "}";
    }
}
