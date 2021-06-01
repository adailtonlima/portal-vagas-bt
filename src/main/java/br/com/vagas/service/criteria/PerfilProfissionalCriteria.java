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
 * Criteria class for the {@link br.com.vagas.domain.PerfilProfissional} entity. This class is used
 * in {@link br.com.vagas.web.rest.PerfilProfissionalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /perfil-profissionals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PerfilProfissionalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter estagio;

    private BooleanFilter procurandoEmprego;

    private DoubleFilter pretensaoSalarial;

    private ZonedDateTimeFilter criado;

    private LongFilter areaId;

    private LongFilter pessoaId;

    public PerfilProfissionalCriteria() {}

    public PerfilProfissionalCriteria(PerfilProfissionalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.estagio = other.estagio == null ? null : other.estagio.copy();
        this.procurandoEmprego = other.procurandoEmprego == null ? null : other.procurandoEmprego.copy();
        this.pretensaoSalarial = other.pretensaoSalarial == null ? null : other.pretensaoSalarial.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.areaId = other.areaId == null ? null : other.areaId.copy();
        this.pessoaId = other.pessoaId == null ? null : other.pessoaId.copy();
    }

    @Override
    public PerfilProfissionalCriteria copy() {
        return new PerfilProfissionalCriteria(this);
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

    public BooleanFilter getProcurandoEmprego() {
        return procurandoEmprego;
    }

    public BooleanFilter procurandoEmprego() {
        if (procurandoEmprego == null) {
            procurandoEmprego = new BooleanFilter();
        }
        return procurandoEmprego;
    }

    public void setProcurandoEmprego(BooleanFilter procurandoEmprego) {
        this.procurandoEmprego = procurandoEmprego;
    }

    public DoubleFilter getPretensaoSalarial() {
        return pretensaoSalarial;
    }

    public DoubleFilter pretensaoSalarial() {
        if (pretensaoSalarial == null) {
            pretensaoSalarial = new DoubleFilter();
        }
        return pretensaoSalarial;
    }

    public void setPretensaoSalarial(DoubleFilter pretensaoSalarial) {
        this.pretensaoSalarial = pretensaoSalarial;
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
        final PerfilProfissionalCriteria that = (PerfilProfissionalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(estagio, that.estagio) &&
            Objects.equals(procurandoEmprego, that.procurandoEmprego) &&
            Objects.equals(pretensaoSalarial, that.pretensaoSalarial) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(areaId, that.areaId) &&
            Objects.equals(pessoaId, that.pessoaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, estagio, procurandoEmprego, pretensaoSalarial, criado, areaId, pessoaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilProfissionalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (estagio != null ? "estagio=" + estagio + ", " : "") +
            (procurandoEmprego != null ? "procurandoEmprego=" + procurandoEmprego + ", " : "") +
            (pretensaoSalarial != null ? "pretensaoSalarial=" + pretensaoSalarial + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (areaId != null ? "areaId=" + areaId + ", " : "") +
            (pessoaId != null ? "pessoaId=" + pessoaId + ", " : "") +
            "}";
    }
}
