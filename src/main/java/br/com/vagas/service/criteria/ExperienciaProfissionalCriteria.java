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
 * Criteria class for the {@link br.com.vagas.domain.ExperienciaProfissional} entity. This class is used
 * in {@link br.com.vagas.web.rest.ExperienciaProfissionalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /experiencia-profissionals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExperienciaProfissionalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter empresa;

    private StringFilter cargo;

    private StringFilter segmento;

    private StringFilter porte;

    private LocalDateFilter inicio;

    private LocalDateFilter fim;

    private DoubleFilter salario;

    private DoubleFilter beneficios;

    private ZonedDateTimeFilter criado;

    private LongFilter pessoaId;

    public ExperienciaProfissionalCriteria() {}

    public ExperienciaProfissionalCriteria(ExperienciaProfissionalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.empresa = other.empresa == null ? null : other.empresa.copy();
        this.cargo = other.cargo == null ? null : other.cargo.copy();
        this.segmento = other.segmento == null ? null : other.segmento.copy();
        this.porte = other.porte == null ? null : other.porte.copy();
        this.inicio = other.inicio == null ? null : other.inicio.copy();
        this.fim = other.fim == null ? null : other.fim.copy();
        this.salario = other.salario == null ? null : other.salario.copy();
        this.beneficios = other.beneficios == null ? null : other.beneficios.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.pessoaId = other.pessoaId == null ? null : other.pessoaId.copy();
    }

    @Override
    public ExperienciaProfissionalCriteria copy() {
        return new ExperienciaProfissionalCriteria(this);
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

    public StringFilter getEmpresa() {
        return empresa;
    }

    public StringFilter empresa() {
        if (empresa == null) {
            empresa = new StringFilter();
        }
        return empresa;
    }

    public void setEmpresa(StringFilter empresa) {
        this.empresa = empresa;
    }

    public StringFilter getCargo() {
        return cargo;
    }

    public StringFilter cargo() {
        if (cargo == null) {
            cargo = new StringFilter();
        }
        return cargo;
    }

    public void setCargo(StringFilter cargo) {
        this.cargo = cargo;
    }

    public StringFilter getSegmento() {
        return segmento;
    }

    public StringFilter segmento() {
        if (segmento == null) {
            segmento = new StringFilter();
        }
        return segmento;
    }

    public void setSegmento(StringFilter segmento) {
        this.segmento = segmento;
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

    public LocalDateFilter getFim() {
        return fim;
    }

    public LocalDateFilter fim() {
        if (fim == null) {
            fim = new LocalDateFilter();
        }
        return fim;
    }

    public void setFim(LocalDateFilter fim) {
        this.fim = fim;
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
        final ExperienciaProfissionalCriteria that = (ExperienciaProfissionalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(empresa, that.empresa) &&
            Objects.equals(cargo, that.cargo) &&
            Objects.equals(segmento, that.segmento) &&
            Objects.equals(porte, that.porte) &&
            Objects.equals(inicio, that.inicio) &&
            Objects.equals(fim, that.fim) &&
            Objects.equals(salario, that.salario) &&
            Objects.equals(beneficios, that.beneficios) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(pessoaId, that.pessoaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, empresa, cargo, segmento, porte, inicio, fim, salario, beneficios, criado, pessoaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExperienciaProfissionalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (empresa != null ? "empresa=" + empresa + ", " : "") +
            (cargo != null ? "cargo=" + cargo + ", " : "") +
            (segmento != null ? "segmento=" + segmento + ", " : "") +
            (porte != null ? "porte=" + porte + ", " : "") +
            (inicio != null ? "inicio=" + inicio + ", " : "") +
            (fim != null ? "fim=" + fim + ", " : "") +
            (salario != null ? "salario=" + salario + ", " : "") +
            (beneficios != null ? "beneficios=" + beneficios + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (pessoaId != null ? "pessoaId=" + pessoaId + ", " : "") +
            "}";
    }
}
