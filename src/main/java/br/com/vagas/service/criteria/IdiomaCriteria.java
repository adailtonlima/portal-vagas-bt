package br.com.vagas.service.criteria;

import br.com.vagas.domain.enumeration.NivelIdioma;
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
 * Criteria class for the {@link br.com.vagas.domain.Idioma} entity. This class is used
 * in {@link br.com.vagas.web.rest.IdiomaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /idiomas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IdiomaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NivelIdioma
     */
    public static class NivelIdiomaFilter extends Filter<NivelIdioma> {

        public NivelIdiomaFilter() {}

        public NivelIdiomaFilter(NivelIdiomaFilter filter) {
            super(filter);
        }

        @Override
        public NivelIdiomaFilter copy() {
            return new NivelIdiomaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private NivelIdiomaFilter nivel;

    private ZonedDateTimeFilter criado;

    private LongFilter pessoaId;

    public IdiomaCriteria() {}

    public IdiomaCriteria(IdiomaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.nivel = other.nivel == null ? null : other.nivel.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.pessoaId = other.pessoaId == null ? null : other.pessoaId.copy();
    }

    @Override
    public IdiomaCriteria copy() {
        return new IdiomaCriteria(this);
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

    public NivelIdiomaFilter getNivel() {
        return nivel;
    }

    public NivelIdiomaFilter nivel() {
        if (nivel == null) {
            nivel = new NivelIdiomaFilter();
        }
        return nivel;
    }

    public void setNivel(NivelIdiomaFilter nivel) {
        this.nivel = nivel;
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
        final IdiomaCriteria that = (IdiomaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(nivel, that.nivel) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(pessoaId, that.pessoaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, nivel, criado, pessoaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdiomaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (nivel != null ? "nivel=" + nivel + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (pessoaId != null ? "pessoaId=" + pessoaId + ", " : "") +
            "}";
    }
}
