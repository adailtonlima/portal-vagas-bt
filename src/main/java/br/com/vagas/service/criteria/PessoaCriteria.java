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
 * Criteria class for the {@link br.com.vagas.domain.Pessoa} entity. This class is used
 * in {@link br.com.vagas.web.rest.PessoaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pessoas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PessoaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter email;

    private LocalDateFilter dataNascimento;

    private StringFilter cpf;

    private StringFilter telefone;

    private StringFilter nacionalidade;

    private StringFilter naturalidade;

    private StringFilter sexo;

    private StringFilter estadoCivil;

    private BooleanFilter pcd;

    private StringFilter pcdCID;

    private StringFilter cnh;

    private StringFilter fotoUrl;

    private ZonedDateTimeFilter criado;

    private LongFilter enderecoId;

    private LongFilter userId;

    public PessoaCriteria() {}

    public PessoaCriteria(PessoaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.dataNascimento = other.dataNascimento == null ? null : other.dataNascimento.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.telefone = other.telefone == null ? null : other.telefone.copy();
        this.nacionalidade = other.nacionalidade == null ? null : other.nacionalidade.copy();
        this.naturalidade = other.naturalidade == null ? null : other.naturalidade.copy();
        this.sexo = other.sexo == null ? null : other.sexo.copy();
        this.estadoCivil = other.estadoCivil == null ? null : other.estadoCivil.copy();
        this.pcd = other.pcd == null ? null : other.pcd.copy();
        this.pcdCID = other.pcdCID == null ? null : other.pcdCID.copy();
        this.cnh = other.cnh == null ? null : other.cnh.copy();
        this.fotoUrl = other.fotoUrl == null ? null : other.fotoUrl.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.enderecoId = other.enderecoId == null ? null : other.enderecoId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public PessoaCriteria copy() {
        return new PessoaCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getDataNascimento() {
        return dataNascimento;
    }

    public LocalDateFilter dataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = new LocalDateFilter();
        }
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public StringFilter telefone() {
        if (telefone == null) {
            telefone = new StringFilter();
        }
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public StringFilter getNacionalidade() {
        return nacionalidade;
    }

    public StringFilter nacionalidade() {
        if (nacionalidade == null) {
            nacionalidade = new StringFilter();
        }
        return nacionalidade;
    }

    public void setNacionalidade(StringFilter nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public StringFilter getNaturalidade() {
        return naturalidade;
    }

    public StringFilter naturalidade() {
        if (naturalidade == null) {
            naturalidade = new StringFilter();
        }
        return naturalidade;
    }

    public void setNaturalidade(StringFilter naturalidade) {
        this.naturalidade = naturalidade;
    }

    public StringFilter getSexo() {
        return sexo;
    }

    public StringFilter sexo() {
        if (sexo == null) {
            sexo = new StringFilter();
        }
        return sexo;
    }

    public void setSexo(StringFilter sexo) {
        this.sexo = sexo;
    }

    public StringFilter getEstadoCivil() {
        return estadoCivil;
    }

    public StringFilter estadoCivil() {
        if (estadoCivil == null) {
            estadoCivil = new StringFilter();
        }
        return estadoCivil;
    }

    public void setEstadoCivil(StringFilter estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public BooleanFilter getPcd() {
        return pcd;
    }

    public BooleanFilter pcd() {
        if (pcd == null) {
            pcd = new BooleanFilter();
        }
        return pcd;
    }

    public void setPcd(BooleanFilter pcd) {
        this.pcd = pcd;
    }

    public StringFilter getPcdCID() {
        return pcdCID;
    }

    public StringFilter pcdCID() {
        if (pcdCID == null) {
            pcdCID = new StringFilter();
        }
        return pcdCID;
    }

    public void setPcdCID(StringFilter pcdCID) {
        this.pcdCID = pcdCID;
    }

    public StringFilter getCnh() {
        return cnh;
    }

    public StringFilter cnh() {
        if (cnh == null) {
            cnh = new StringFilter();
        }
        return cnh;
    }

    public void setCnh(StringFilter cnh) {
        this.cnh = cnh;
    }

    public StringFilter getFotoUrl() {
        return fotoUrl;
    }

    public StringFilter fotoUrl() {
        if (fotoUrl == null) {
            fotoUrl = new StringFilter();
        }
        return fotoUrl;
    }

    public void setFotoUrl(StringFilter fotoUrl) {
        this.fotoUrl = fotoUrl;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PessoaCriteria that = (PessoaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(nacionalidade, that.nacionalidade) &&
            Objects.equals(naturalidade, that.naturalidade) &&
            Objects.equals(sexo, that.sexo) &&
            Objects.equals(estadoCivil, that.estadoCivil) &&
            Objects.equals(pcd, that.pcd) &&
            Objects.equals(pcdCID, that.pcdCID) &&
            Objects.equals(cnh, that.cnh) &&
            Objects.equals(fotoUrl, that.fotoUrl) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(enderecoId, that.enderecoId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            email,
            dataNascimento,
            cpf,
            telefone,
            nacionalidade,
            naturalidade,
            sexo,
            estadoCivil,
            pcd,
            pcdCID,
            cnh,
            fotoUrl,
            criado,
            enderecoId,
            userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PessoaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (dataNascimento != null ? "dataNascimento=" + dataNascimento + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (telefone != null ? "telefone=" + telefone + ", " : "") +
            (nacionalidade != null ? "nacionalidade=" + nacionalidade + ", " : "") +
            (naturalidade != null ? "naturalidade=" + naturalidade + ", " : "") +
            (sexo != null ? "sexo=" + sexo + ", " : "") +
            (estadoCivil != null ? "estadoCivil=" + estadoCivil + ", " : "") +
            (pcd != null ? "pcd=" + pcd + ", " : "") +
            (pcdCID != null ? "pcdCID=" + pcdCID + ", " : "") +
            (cnh != null ? "cnh=" + cnh + ", " : "") +
            (fotoUrl != null ? "fotoUrl=" + fotoUrl + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (enderecoId != null ? "enderecoId=" + enderecoId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
