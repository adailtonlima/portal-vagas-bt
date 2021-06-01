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
 * Criteria class for the {@link br.com.vagas.domain.AreaAtuacao} entity. This class is used
 * in {@link br.com.vagas.web.rest.AreaAtuacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /area-atuacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AreaAtuacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter descricao;

    private StringFilter iconeUrl;

    private BooleanFilter ativo;

    private ZonedDateTimeFilter criado;

    public AreaAtuacaoCriteria() {}

    public AreaAtuacaoCriteria(AreaAtuacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.iconeUrl = other.iconeUrl == null ? null : other.iconeUrl.copy();
        this.ativo = other.ativo == null ? null : other.ativo.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
    }

    @Override
    public AreaAtuacaoCriteria copy() {
        return new AreaAtuacaoCriteria(this);
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

    public StringFilter getDescricao() {
        return descricao;
    }

    public StringFilter descricao() {
        if (descricao == null) {
            descricao = new StringFilter();
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public StringFilter getIconeUrl() {
        return iconeUrl;
    }

    public StringFilter iconeUrl() {
        if (iconeUrl == null) {
            iconeUrl = new StringFilter();
        }
        return iconeUrl;
    }

    public void setIconeUrl(StringFilter iconeUrl) {
        this.iconeUrl = iconeUrl;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AreaAtuacaoCriteria that = (AreaAtuacaoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(iconeUrl, that.iconeUrl) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(criado, that.criado)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, iconeUrl, ativo, criado);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaAtuacaoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (descricao != null ? "descricao=" + descricao + ", " : "") +
            (iconeUrl != null ? "iconeUrl=" + iconeUrl + ", " : "") +
            (ativo != null ? "ativo=" + ativo + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            "}";
    }
}
