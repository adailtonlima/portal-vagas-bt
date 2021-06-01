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
 * Criteria class for the {@link br.com.vagas.domain.Empresa} entity. This class is used
 * in {@link br.com.vagas.web.rest.EmpresaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /empresas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmpresaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cnpj;

    private StringFilter porte;

    private ZonedDateTimeFilter criado;

    private LongFilter enderecoId;

    private LongFilter userId;

    private LongFilter areaId;

    public EmpresaCriteria() {}

    public EmpresaCriteria(EmpresaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.porte = other.porte == null ? null : other.porte.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.enderecoId = other.enderecoId == null ? null : other.enderecoId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.areaId = other.areaId == null ? null : other.areaId.copy();
    }

    @Override
    public EmpresaCriteria copy() {
        return new EmpresaCriteria(this);
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

    public StringFilter getCnpj() {
        return cnpj;
    }

    public StringFilter cnpj() {
        if (cnpj == null) {
            cnpj = new StringFilter();
        }
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getPorte() {
        return porte;
    }

    public StringFilter porte() {
        if (porte == null) {
            porte = new StringFilter();
        }
        return porte;
    }

    public void setPorte(StringFilter porte) {
        this.porte = porte;
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

    public LongFilter getEnderecoId() {
        return enderecoId;
    }

    public LongFilter enderecoId() {
        if (enderecoId == null) {
            enderecoId = new LongFilter();
        }
        return enderecoId;
    }

    public void setEnderecoId(LongFilter enderecoId) {
        this.enderecoId = enderecoId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getAreaId() {
        return areaId;
    }

    public LongFilter areaId() {
        if (areaId == null) {
            areaId = new LongFilter();
        }
        return areaId;
    }

    public void setAreaId(LongFilter areaId) {
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmpresaCriteria that = (EmpresaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(porte, that.porte) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(enderecoId, that.enderecoId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(areaId, that.areaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cnpj, porte, criado, enderecoId, userId, areaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpresaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
            (porte != null ? "porte=" + porte + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (enderecoId != null ? "enderecoId=" + enderecoId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (areaId != null ? "areaId=" + areaId + ", " : "") +
            "}";
    }
}
