package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.Endereco;
import br.com.vagas.domain.Pessoa;
import br.com.vagas.domain.User;
import br.com.vagas.repository.PessoaRepository;
import br.com.vagas.service.criteria.PessoaCriteria;
import br.com.vagas.service.dto.PessoaDTO;
import br.com.vagas.service.mapper.PessoaMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PessoaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PessoaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_NACIONALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_NACIONALIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_NATURALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_NATURALIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_SEXO = "AAAAAAAAAA";
    private static final String UPDATED_SEXO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO_CIVIL = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO_CIVIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PCD = false;
    private static final Boolean UPDATED_PCD = true;

    private static final String DEFAULT_PCD_CID = "AAAAAAAAAA";
    private static final String UPDATED_PCD_CID = "BBBBBBBBBB";

    private static final String DEFAULT_CNH = "AAAAAAAAAA";
    private static final String UPDATED_CNH = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_FOTO_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/pessoas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPessoaMockMvc;

    private Pessoa pessoa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pessoa createEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa()
            .nome(DEFAULT_NOME)
            .email(DEFAULT_EMAIL)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .cpf(DEFAULT_CPF)
            .telefone(DEFAULT_TELEFONE)
            .nacionalidade(DEFAULT_NACIONALIDADE)
            .naturalidade(DEFAULT_NATURALIDADE)
            .sexo(DEFAULT_SEXO)
            .estadoCivil(DEFAULT_ESTADO_CIVIL)
            .pcd(DEFAULT_PCD)
            .pcdCID(DEFAULT_PCD_CID)
            .cnh(DEFAULT_CNH)
            .fotoUrl(DEFAULT_FOTO_URL)
            .criado(DEFAULT_CRIADO);
        return pessoa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pessoa createUpdatedEntity(EntityManager em) {
        Pessoa pessoa = new Pessoa()
            .nome(UPDATED_NOME)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cpf(UPDATED_CPF)
            .telefone(UPDATED_TELEFONE)
            .nacionalidade(UPDATED_NACIONALIDADE)
            .naturalidade(UPDATED_NATURALIDADE)
            .sexo(UPDATED_SEXO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .pcd(UPDATED_PCD)
            .pcdCID(UPDATED_PCD_CID)
            .cnh(UPDATED_CNH)
            .fotoUrl(UPDATED_FOTO_URL)
            .criado(UPDATED_CRIADO);
        return pessoa;
    }

    @BeforeEach
    public void initTest() {
        pessoa = createEntity(em);
    }

    @Test
    @Transactional
    void createPessoa() throws Exception {
        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();
        // Create the Pessoa
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);
        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
            .andExpect(status().isCreated());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate + 1);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPessoa.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPessoa.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testPessoa.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPessoa.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testPessoa.getNacionalidade()).isEqualTo(DEFAULT_NACIONALIDADE);
        assertThat(testPessoa.getNaturalidade()).isEqualTo(DEFAULT_NATURALIDADE);
        assertThat(testPessoa.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPessoa.getEstadoCivil()).isEqualTo(DEFAULT_ESTADO_CIVIL);
        assertThat(testPessoa.getPcd()).isEqualTo(DEFAULT_PCD);
        assertThat(testPessoa.getPcdCID()).isEqualTo(DEFAULT_PCD_CID);
        assertThat(testPessoa.getCnh()).isEqualTo(DEFAULT_CNH);
        assertThat(testPessoa.getFotoUrl()).isEqualTo(DEFAULT_FOTO_URL);
        assertThat(testPessoa.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createPessoaWithExistingId() throws Exception {
        // Create the Pessoa with an existing ID
        pessoa.setId(1L);
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        int databaseSizeBeforeCreate = pessoaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setNome(null);

        // Create the Pessoa, which fails.
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pessoaRepository.findAll().size();
        // set the field null
        pessoa.setCriado(null);

        // Create the Pessoa, which fails.
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        restPessoaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
            .andExpect(status().isBadRequest());

        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPessoas() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].nacionalidade").value(hasItem(DEFAULT_NACIONALIDADE)))
            .andExpect(jsonPath("$.[*].naturalidade").value(hasItem(DEFAULT_NATURALIDADE)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO)))
            .andExpect(jsonPath("$.[*].estadoCivil").value(hasItem(DEFAULT_ESTADO_CIVIL)))
            .andExpect(jsonPath("$.[*].pcd").value(hasItem(DEFAULT_PCD.booleanValue())))
            .andExpect(jsonPath("$.[*].pcdCID").value(hasItem(DEFAULT_PCD_CID)))
            .andExpect(jsonPath("$.[*].cnh").value(hasItem(DEFAULT_CNH)))
            .andExpect(jsonPath("$.[*].fotoUrl").value(hasItem(DEFAULT_FOTO_URL)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get the pessoa
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL_ID, pessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pessoa.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.nacionalidade").value(DEFAULT_NACIONALIDADE))
            .andExpect(jsonPath("$.naturalidade").value(DEFAULT_NATURALIDADE))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO))
            .andExpect(jsonPath("$.estadoCivil").value(DEFAULT_ESTADO_CIVIL))
            .andExpect(jsonPath("$.pcd").value(DEFAULT_PCD.booleanValue()))
            .andExpect(jsonPath("$.pcdCID").value(DEFAULT_PCD_CID))
            .andExpect(jsonPath("$.cnh").value(DEFAULT_CNH))
            .andExpect(jsonPath("$.fotoUrl").value(DEFAULT_FOTO_URL))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getPessoasByIdFiltering() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        Long id = pessoa.getId();

        defaultPessoaShouldBeFound("id.equals=" + id);
        defaultPessoaShouldNotBeFound("id.notEquals=" + id);

        defaultPessoaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPessoaShouldNotBeFound("id.greaterThan=" + id);

        defaultPessoaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPessoaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPessoasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nome equals to DEFAULT_NOME
        defaultPessoaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the pessoaList where nome equals to UPDATED_NOME
        defaultPessoaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPessoasByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nome not equals to DEFAULT_NOME
        defaultPessoaShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the pessoaList where nome not equals to UPDATED_NOME
        defaultPessoaShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPessoasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultPessoaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the pessoaList where nome equals to UPDATED_NOME
        defaultPessoaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPessoasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nome is not null
        defaultPessoaShouldBeFound("nome.specified=true");

        // Get all the pessoaList where nome is null
        defaultPessoaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByNomeContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nome contains DEFAULT_NOME
        defaultPessoaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the pessoaList where nome contains UPDATED_NOME
        defaultPessoaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPessoasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nome does not contain DEFAULT_NOME
        defaultPessoaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the pessoaList where nome does not contain UPDATED_NOME
        defaultPessoaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPessoasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where email equals to DEFAULT_EMAIL
        defaultPessoaShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the pessoaList where email equals to UPDATED_EMAIL
        defaultPessoaShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where email not equals to DEFAULT_EMAIL
        defaultPessoaShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the pessoaList where email not equals to UPDATED_EMAIL
        defaultPessoaShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPessoaShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the pessoaList where email equals to UPDATED_EMAIL
        defaultPessoaShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where email is not null
        defaultPessoaShouldBeFound("email.specified=true");

        // Get all the pessoaList where email is null
        defaultPessoaShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByEmailContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where email contains DEFAULT_EMAIL
        defaultPessoaShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the pessoaList where email contains UPDATED_EMAIL
        defaultPessoaShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where email does not contain DEFAULT_EMAIL
        defaultPessoaShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the pessoaList where email does not contain UPDATED_EMAIL
        defaultPessoaShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento equals to DEFAULT_DATA_NASCIMENTO
        defaultPessoaShouldBeFound("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the pessoaList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultPessoaShouldNotBeFound("dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento not equals to DEFAULT_DATA_NASCIMENTO
        defaultPessoaShouldNotBeFound("dataNascimento.notEquals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the pessoaList where dataNascimento not equals to UPDATED_DATA_NASCIMENTO
        defaultPessoaShouldBeFound("dataNascimento.notEquals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento in DEFAULT_DATA_NASCIMENTO or UPDATED_DATA_NASCIMENTO
        defaultPessoaShouldBeFound("dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO);

        // Get all the pessoaList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultPessoaShouldNotBeFound("dataNascimento.in=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento is not null
        defaultPessoaShouldBeFound("dataNascimento.specified=true");

        // Get all the pessoaList where dataNascimento is null
        defaultPessoaShouldNotBeFound("dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento is greater than or equal to DEFAULT_DATA_NASCIMENTO
        defaultPessoaShouldBeFound("dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the pessoaList where dataNascimento is greater than or equal to UPDATED_DATA_NASCIMENTO
        defaultPessoaShouldNotBeFound("dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento is less than or equal to DEFAULT_DATA_NASCIMENTO
        defaultPessoaShouldBeFound("dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the pessoaList where dataNascimento is less than or equal to SMALLER_DATA_NASCIMENTO
        defaultPessoaShouldNotBeFound("dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento is less than DEFAULT_DATA_NASCIMENTO
        defaultPessoaShouldNotBeFound("dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the pessoaList where dataNascimento is less than UPDATED_DATA_NASCIMENTO
        defaultPessoaShouldBeFound("dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPessoasByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where dataNascimento is greater than DEFAULT_DATA_NASCIMENTO
        defaultPessoaShouldNotBeFound("dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the pessoaList where dataNascimento is greater than SMALLER_DATA_NASCIMENTO
        defaultPessoaShouldBeFound("dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllPessoasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cpf equals to DEFAULT_CPF
        defaultPessoaShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the pessoaList where cpf equals to UPDATED_CPF
        defaultPessoaShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoasByCpfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cpf not equals to DEFAULT_CPF
        defaultPessoaShouldNotBeFound("cpf.notEquals=" + DEFAULT_CPF);

        // Get all the pessoaList where cpf not equals to UPDATED_CPF
        defaultPessoaShouldBeFound("cpf.notEquals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoasByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultPessoaShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the pessoaList where cpf equals to UPDATED_CPF
        defaultPessoaShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoasByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cpf is not null
        defaultPessoaShouldBeFound("cpf.specified=true");

        // Get all the pessoaList where cpf is null
        defaultPessoaShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByCpfContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cpf contains DEFAULT_CPF
        defaultPessoaShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the pessoaList where cpf contains UPDATED_CPF
        defaultPessoaShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoasByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cpf does not contain DEFAULT_CPF
        defaultPessoaShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the pessoaList where cpf does not contain UPDATED_CPF
        defaultPessoaShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoasByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where telefone equals to DEFAULT_TELEFONE
        defaultPessoaShouldBeFound("telefone.equals=" + DEFAULT_TELEFONE);

        // Get all the pessoaList where telefone equals to UPDATED_TELEFONE
        defaultPessoaShouldNotBeFound("telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoasByTelefoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where telefone not equals to DEFAULT_TELEFONE
        defaultPessoaShouldNotBeFound("telefone.notEquals=" + DEFAULT_TELEFONE);

        // Get all the pessoaList where telefone not equals to UPDATED_TELEFONE
        defaultPessoaShouldBeFound("telefone.notEquals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoasByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where telefone in DEFAULT_TELEFONE or UPDATED_TELEFONE
        defaultPessoaShouldBeFound("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE);

        // Get all the pessoaList where telefone equals to UPDATED_TELEFONE
        defaultPessoaShouldNotBeFound("telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoasByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where telefone is not null
        defaultPessoaShouldBeFound("telefone.specified=true");

        // Get all the pessoaList where telefone is null
        defaultPessoaShouldNotBeFound("telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where telefone contains DEFAULT_TELEFONE
        defaultPessoaShouldBeFound("telefone.contains=" + DEFAULT_TELEFONE);

        // Get all the pessoaList where telefone contains UPDATED_TELEFONE
        defaultPessoaShouldNotBeFound("telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoasByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where telefone does not contain DEFAULT_TELEFONE
        defaultPessoaShouldNotBeFound("telefone.doesNotContain=" + DEFAULT_TELEFONE);

        // Get all the pessoaList where telefone does not contain UPDATED_TELEFONE
        defaultPessoaShouldBeFound("telefone.doesNotContain=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoasByNacionalidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nacionalidade equals to DEFAULT_NACIONALIDADE
        defaultPessoaShouldBeFound("nacionalidade.equals=" + DEFAULT_NACIONALIDADE);

        // Get all the pessoaList where nacionalidade equals to UPDATED_NACIONALIDADE
        defaultPessoaShouldNotBeFound("nacionalidade.equals=" + UPDATED_NACIONALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNacionalidadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nacionalidade not equals to DEFAULT_NACIONALIDADE
        defaultPessoaShouldNotBeFound("nacionalidade.notEquals=" + DEFAULT_NACIONALIDADE);

        // Get all the pessoaList where nacionalidade not equals to UPDATED_NACIONALIDADE
        defaultPessoaShouldBeFound("nacionalidade.notEquals=" + UPDATED_NACIONALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNacionalidadeIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nacionalidade in DEFAULT_NACIONALIDADE or UPDATED_NACIONALIDADE
        defaultPessoaShouldBeFound("nacionalidade.in=" + DEFAULT_NACIONALIDADE + "," + UPDATED_NACIONALIDADE);

        // Get all the pessoaList where nacionalidade equals to UPDATED_NACIONALIDADE
        defaultPessoaShouldNotBeFound("nacionalidade.in=" + UPDATED_NACIONALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNacionalidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nacionalidade is not null
        defaultPessoaShouldBeFound("nacionalidade.specified=true");

        // Get all the pessoaList where nacionalidade is null
        defaultPessoaShouldNotBeFound("nacionalidade.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByNacionalidadeContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nacionalidade contains DEFAULT_NACIONALIDADE
        defaultPessoaShouldBeFound("nacionalidade.contains=" + DEFAULT_NACIONALIDADE);

        // Get all the pessoaList where nacionalidade contains UPDATED_NACIONALIDADE
        defaultPessoaShouldNotBeFound("nacionalidade.contains=" + UPDATED_NACIONALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNacionalidadeNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where nacionalidade does not contain DEFAULT_NACIONALIDADE
        defaultPessoaShouldNotBeFound("nacionalidade.doesNotContain=" + DEFAULT_NACIONALIDADE);

        // Get all the pessoaList where nacionalidade does not contain UPDATED_NACIONALIDADE
        defaultPessoaShouldBeFound("nacionalidade.doesNotContain=" + UPDATED_NACIONALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNaturalidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where naturalidade equals to DEFAULT_NATURALIDADE
        defaultPessoaShouldBeFound("naturalidade.equals=" + DEFAULT_NATURALIDADE);

        // Get all the pessoaList where naturalidade equals to UPDATED_NATURALIDADE
        defaultPessoaShouldNotBeFound("naturalidade.equals=" + UPDATED_NATURALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNaturalidadeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where naturalidade not equals to DEFAULT_NATURALIDADE
        defaultPessoaShouldNotBeFound("naturalidade.notEquals=" + DEFAULT_NATURALIDADE);

        // Get all the pessoaList where naturalidade not equals to UPDATED_NATURALIDADE
        defaultPessoaShouldBeFound("naturalidade.notEquals=" + UPDATED_NATURALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNaturalidadeIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where naturalidade in DEFAULT_NATURALIDADE or UPDATED_NATURALIDADE
        defaultPessoaShouldBeFound("naturalidade.in=" + DEFAULT_NATURALIDADE + "," + UPDATED_NATURALIDADE);

        // Get all the pessoaList where naturalidade equals to UPDATED_NATURALIDADE
        defaultPessoaShouldNotBeFound("naturalidade.in=" + UPDATED_NATURALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNaturalidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where naturalidade is not null
        defaultPessoaShouldBeFound("naturalidade.specified=true");

        // Get all the pessoaList where naturalidade is null
        defaultPessoaShouldNotBeFound("naturalidade.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByNaturalidadeContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where naturalidade contains DEFAULT_NATURALIDADE
        defaultPessoaShouldBeFound("naturalidade.contains=" + DEFAULT_NATURALIDADE);

        // Get all the pessoaList where naturalidade contains UPDATED_NATURALIDADE
        defaultPessoaShouldNotBeFound("naturalidade.contains=" + UPDATED_NATURALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasByNaturalidadeNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where naturalidade does not contain DEFAULT_NATURALIDADE
        defaultPessoaShouldNotBeFound("naturalidade.doesNotContain=" + DEFAULT_NATURALIDADE);

        // Get all the pessoaList where naturalidade does not contain UPDATED_NATURALIDADE
        defaultPessoaShouldBeFound("naturalidade.doesNotContain=" + UPDATED_NATURALIDADE);
    }

    @Test
    @Transactional
    void getAllPessoasBySexoIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where sexo equals to DEFAULT_SEXO
        defaultPessoaShouldBeFound("sexo.equals=" + DEFAULT_SEXO);

        // Get all the pessoaList where sexo equals to UPDATED_SEXO
        defaultPessoaShouldNotBeFound("sexo.equals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPessoasBySexoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where sexo not equals to DEFAULT_SEXO
        defaultPessoaShouldNotBeFound("sexo.notEquals=" + DEFAULT_SEXO);

        // Get all the pessoaList where sexo not equals to UPDATED_SEXO
        defaultPessoaShouldBeFound("sexo.notEquals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPessoasBySexoIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where sexo in DEFAULT_SEXO or UPDATED_SEXO
        defaultPessoaShouldBeFound("sexo.in=" + DEFAULT_SEXO + "," + UPDATED_SEXO);

        // Get all the pessoaList where sexo equals to UPDATED_SEXO
        defaultPessoaShouldNotBeFound("sexo.in=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPessoasBySexoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where sexo is not null
        defaultPessoaShouldBeFound("sexo.specified=true");

        // Get all the pessoaList where sexo is null
        defaultPessoaShouldNotBeFound("sexo.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasBySexoContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where sexo contains DEFAULT_SEXO
        defaultPessoaShouldBeFound("sexo.contains=" + DEFAULT_SEXO);

        // Get all the pessoaList where sexo contains UPDATED_SEXO
        defaultPessoaShouldNotBeFound("sexo.contains=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPessoasBySexoNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where sexo does not contain DEFAULT_SEXO
        defaultPessoaShouldNotBeFound("sexo.doesNotContain=" + DEFAULT_SEXO);

        // Get all the pessoaList where sexo does not contain UPDATED_SEXO
        defaultPessoaShouldBeFound("sexo.doesNotContain=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPessoasByEstadoCivilIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where estadoCivil equals to DEFAULT_ESTADO_CIVIL
        defaultPessoaShouldBeFound("estadoCivil.equals=" + DEFAULT_ESTADO_CIVIL);

        // Get all the pessoaList where estadoCivil equals to UPDATED_ESTADO_CIVIL
        defaultPessoaShouldNotBeFound("estadoCivil.equals=" + UPDATED_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEstadoCivilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where estadoCivil not equals to DEFAULT_ESTADO_CIVIL
        defaultPessoaShouldNotBeFound("estadoCivil.notEquals=" + DEFAULT_ESTADO_CIVIL);

        // Get all the pessoaList where estadoCivil not equals to UPDATED_ESTADO_CIVIL
        defaultPessoaShouldBeFound("estadoCivil.notEquals=" + UPDATED_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEstadoCivilIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where estadoCivil in DEFAULT_ESTADO_CIVIL or UPDATED_ESTADO_CIVIL
        defaultPessoaShouldBeFound("estadoCivil.in=" + DEFAULT_ESTADO_CIVIL + "," + UPDATED_ESTADO_CIVIL);

        // Get all the pessoaList where estadoCivil equals to UPDATED_ESTADO_CIVIL
        defaultPessoaShouldNotBeFound("estadoCivil.in=" + UPDATED_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEstadoCivilIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where estadoCivil is not null
        defaultPessoaShouldBeFound("estadoCivil.specified=true");

        // Get all the pessoaList where estadoCivil is null
        defaultPessoaShouldNotBeFound("estadoCivil.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByEstadoCivilContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where estadoCivil contains DEFAULT_ESTADO_CIVIL
        defaultPessoaShouldBeFound("estadoCivil.contains=" + DEFAULT_ESTADO_CIVIL);

        // Get all the pessoaList where estadoCivil contains UPDATED_ESTADO_CIVIL
        defaultPessoaShouldNotBeFound("estadoCivil.contains=" + UPDATED_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllPessoasByEstadoCivilNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where estadoCivil does not contain DEFAULT_ESTADO_CIVIL
        defaultPessoaShouldNotBeFound("estadoCivil.doesNotContain=" + DEFAULT_ESTADO_CIVIL);

        // Get all the pessoaList where estadoCivil does not contain UPDATED_ESTADO_CIVIL
        defaultPessoaShouldBeFound("estadoCivil.doesNotContain=" + UPDATED_ESTADO_CIVIL);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcd equals to DEFAULT_PCD
        defaultPessoaShouldBeFound("pcd.equals=" + DEFAULT_PCD);

        // Get all the pessoaList where pcd equals to UPDATED_PCD
        defaultPessoaShouldNotBeFound("pcd.equals=" + UPDATED_PCD);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcd not equals to DEFAULT_PCD
        defaultPessoaShouldNotBeFound("pcd.notEquals=" + DEFAULT_PCD);

        // Get all the pessoaList where pcd not equals to UPDATED_PCD
        defaultPessoaShouldBeFound("pcd.notEquals=" + UPDATED_PCD);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcd in DEFAULT_PCD or UPDATED_PCD
        defaultPessoaShouldBeFound("pcd.in=" + DEFAULT_PCD + "," + UPDATED_PCD);

        // Get all the pessoaList where pcd equals to UPDATED_PCD
        defaultPessoaShouldNotBeFound("pcd.in=" + UPDATED_PCD);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcd is not null
        defaultPessoaShouldBeFound("pcd.specified=true");

        // Get all the pessoaList where pcd is null
        defaultPessoaShouldNotBeFound("pcd.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByPcdCIDIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcdCID equals to DEFAULT_PCD_CID
        defaultPessoaShouldBeFound("pcdCID.equals=" + DEFAULT_PCD_CID);

        // Get all the pessoaList where pcdCID equals to UPDATED_PCD_CID
        defaultPessoaShouldNotBeFound("pcdCID.equals=" + UPDATED_PCD_CID);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdCIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcdCID not equals to DEFAULT_PCD_CID
        defaultPessoaShouldNotBeFound("pcdCID.notEquals=" + DEFAULT_PCD_CID);

        // Get all the pessoaList where pcdCID not equals to UPDATED_PCD_CID
        defaultPessoaShouldBeFound("pcdCID.notEquals=" + UPDATED_PCD_CID);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdCIDIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcdCID in DEFAULT_PCD_CID or UPDATED_PCD_CID
        defaultPessoaShouldBeFound("pcdCID.in=" + DEFAULT_PCD_CID + "," + UPDATED_PCD_CID);

        // Get all the pessoaList where pcdCID equals to UPDATED_PCD_CID
        defaultPessoaShouldNotBeFound("pcdCID.in=" + UPDATED_PCD_CID);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdCIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcdCID is not null
        defaultPessoaShouldBeFound("pcdCID.specified=true");

        // Get all the pessoaList where pcdCID is null
        defaultPessoaShouldNotBeFound("pcdCID.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByPcdCIDContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcdCID contains DEFAULT_PCD_CID
        defaultPessoaShouldBeFound("pcdCID.contains=" + DEFAULT_PCD_CID);

        // Get all the pessoaList where pcdCID contains UPDATED_PCD_CID
        defaultPessoaShouldNotBeFound("pcdCID.contains=" + UPDATED_PCD_CID);
    }

    @Test
    @Transactional
    void getAllPessoasByPcdCIDNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where pcdCID does not contain DEFAULT_PCD_CID
        defaultPessoaShouldNotBeFound("pcdCID.doesNotContain=" + DEFAULT_PCD_CID);

        // Get all the pessoaList where pcdCID does not contain UPDATED_PCD_CID
        defaultPessoaShouldBeFound("pcdCID.doesNotContain=" + UPDATED_PCD_CID);
    }

    @Test
    @Transactional
    void getAllPessoasByCnhIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cnh equals to DEFAULT_CNH
        defaultPessoaShouldBeFound("cnh.equals=" + DEFAULT_CNH);

        // Get all the pessoaList where cnh equals to UPDATED_CNH
        defaultPessoaShouldNotBeFound("cnh.equals=" + UPDATED_CNH);
    }

    @Test
    @Transactional
    void getAllPessoasByCnhIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cnh not equals to DEFAULT_CNH
        defaultPessoaShouldNotBeFound("cnh.notEquals=" + DEFAULT_CNH);

        // Get all the pessoaList where cnh not equals to UPDATED_CNH
        defaultPessoaShouldBeFound("cnh.notEquals=" + UPDATED_CNH);
    }

    @Test
    @Transactional
    void getAllPessoasByCnhIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cnh in DEFAULT_CNH or UPDATED_CNH
        defaultPessoaShouldBeFound("cnh.in=" + DEFAULT_CNH + "," + UPDATED_CNH);

        // Get all the pessoaList where cnh equals to UPDATED_CNH
        defaultPessoaShouldNotBeFound("cnh.in=" + UPDATED_CNH);
    }

    @Test
    @Transactional
    void getAllPessoasByCnhIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cnh is not null
        defaultPessoaShouldBeFound("cnh.specified=true");

        // Get all the pessoaList where cnh is null
        defaultPessoaShouldNotBeFound("cnh.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByCnhContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cnh contains DEFAULT_CNH
        defaultPessoaShouldBeFound("cnh.contains=" + DEFAULT_CNH);

        // Get all the pessoaList where cnh contains UPDATED_CNH
        defaultPessoaShouldNotBeFound("cnh.contains=" + UPDATED_CNH);
    }

    @Test
    @Transactional
    void getAllPessoasByCnhNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where cnh does not contain DEFAULT_CNH
        defaultPessoaShouldNotBeFound("cnh.doesNotContain=" + DEFAULT_CNH);

        // Get all the pessoaList where cnh does not contain UPDATED_CNH
        defaultPessoaShouldBeFound("cnh.doesNotContain=" + UPDATED_CNH);
    }

    @Test
    @Transactional
    void getAllPessoasByFotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where fotoUrl equals to DEFAULT_FOTO_URL
        defaultPessoaShouldBeFound("fotoUrl.equals=" + DEFAULT_FOTO_URL);

        // Get all the pessoaList where fotoUrl equals to UPDATED_FOTO_URL
        defaultPessoaShouldNotBeFound("fotoUrl.equals=" + UPDATED_FOTO_URL);
    }

    @Test
    @Transactional
    void getAllPessoasByFotoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where fotoUrl not equals to DEFAULT_FOTO_URL
        defaultPessoaShouldNotBeFound("fotoUrl.notEquals=" + DEFAULT_FOTO_URL);

        // Get all the pessoaList where fotoUrl not equals to UPDATED_FOTO_URL
        defaultPessoaShouldBeFound("fotoUrl.notEquals=" + UPDATED_FOTO_URL);
    }

    @Test
    @Transactional
    void getAllPessoasByFotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where fotoUrl in DEFAULT_FOTO_URL or UPDATED_FOTO_URL
        defaultPessoaShouldBeFound("fotoUrl.in=" + DEFAULT_FOTO_URL + "," + UPDATED_FOTO_URL);

        // Get all the pessoaList where fotoUrl equals to UPDATED_FOTO_URL
        defaultPessoaShouldNotBeFound("fotoUrl.in=" + UPDATED_FOTO_URL);
    }

    @Test
    @Transactional
    void getAllPessoasByFotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where fotoUrl is not null
        defaultPessoaShouldBeFound("fotoUrl.specified=true");

        // Get all the pessoaList where fotoUrl is null
        defaultPessoaShouldNotBeFound("fotoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByFotoUrlContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where fotoUrl contains DEFAULT_FOTO_URL
        defaultPessoaShouldBeFound("fotoUrl.contains=" + DEFAULT_FOTO_URL);

        // Get all the pessoaList where fotoUrl contains UPDATED_FOTO_URL
        defaultPessoaShouldNotBeFound("fotoUrl.contains=" + UPDATED_FOTO_URL);
    }

    @Test
    @Transactional
    void getAllPessoasByFotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where fotoUrl does not contain DEFAULT_FOTO_URL
        defaultPessoaShouldNotBeFound("fotoUrl.doesNotContain=" + DEFAULT_FOTO_URL);

        // Get all the pessoaList where fotoUrl does not contain UPDATED_FOTO_URL
        defaultPessoaShouldBeFound("fotoUrl.doesNotContain=" + UPDATED_FOTO_URL);
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado equals to DEFAULT_CRIADO
        defaultPessoaShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the pessoaList where criado equals to UPDATED_CRIADO
        defaultPessoaShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado not equals to DEFAULT_CRIADO
        defaultPessoaShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the pessoaList where criado not equals to UPDATED_CRIADO
        defaultPessoaShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultPessoaShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the pessoaList where criado equals to UPDATED_CRIADO
        defaultPessoaShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado is not null
        defaultPessoaShouldBeFound("criado.specified=true");

        // Get all the pessoaList where criado is null
        defaultPessoaShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado is greater than or equal to DEFAULT_CRIADO
        defaultPessoaShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the pessoaList where criado is greater than or equal to UPDATED_CRIADO
        defaultPessoaShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado is less than or equal to DEFAULT_CRIADO
        defaultPessoaShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the pessoaList where criado is less than or equal to SMALLER_CRIADO
        defaultPessoaShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado is less than DEFAULT_CRIADO
        defaultPessoaShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the pessoaList where criado is less than UPDATED_CRIADO
        defaultPessoaShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPessoasByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        // Get all the pessoaList where criado is greater than DEFAULT_CRIADO
        defaultPessoaShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the pessoaList where criado is greater than SMALLER_CRIADO
        defaultPessoaShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllPessoasByEnderecoIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);
        Endereco endereco = EnderecoResourceIT.createEntity(em);
        em.persist(endereco);
        em.flush();
        pessoa.setEndereco(endereco);
        pessoaRepository.saveAndFlush(pessoa);
        Long enderecoId = endereco.getId();

        // Get all the pessoaList where endereco equals to enderecoId
        defaultPessoaShouldBeFound("enderecoId.equals=" + enderecoId);

        // Get all the pessoaList where endereco equals to (enderecoId + 1)
        defaultPessoaShouldNotBeFound("enderecoId.equals=" + (enderecoId + 1));
    }

    @Test
    @Transactional
    void getAllPessoasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        pessoa.setUser(user);
        pessoaRepository.saveAndFlush(pessoa);
        Long userId = user.getId();

        // Get all the pessoaList where user equals to userId
        defaultPessoaShouldBeFound("userId.equals=" + userId);

        // Get all the pessoaList where user equals to (userId + 1)
        defaultPessoaShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPessoaShouldBeFound(String filter) throws Exception {
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].nacionalidade").value(hasItem(DEFAULT_NACIONALIDADE)))
            .andExpect(jsonPath("$.[*].naturalidade").value(hasItem(DEFAULT_NATURALIDADE)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO)))
            .andExpect(jsonPath("$.[*].estadoCivil").value(hasItem(DEFAULT_ESTADO_CIVIL)))
            .andExpect(jsonPath("$.[*].pcd").value(hasItem(DEFAULT_PCD.booleanValue())))
            .andExpect(jsonPath("$.[*].pcdCID").value(hasItem(DEFAULT_PCD_CID)))
            .andExpect(jsonPath("$.[*].cnh").value(hasItem(DEFAULT_CNH)))
            .andExpect(jsonPath("$.[*].fotoUrl").value(hasItem(DEFAULT_FOTO_URL)))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPessoaShouldNotBeFound(String filter) throws Exception {
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPessoaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPessoa() throws Exception {
        // Get the pessoa
        restPessoaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Update the pessoa
        Pessoa updatedPessoa = pessoaRepository.findById(pessoa.getId()).get();
        // Disconnect from session so that the updates on updatedPessoa are not directly saved in db
        em.detach(updatedPessoa);
        updatedPessoa
            .nome(UPDATED_NOME)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cpf(UPDATED_CPF)
            .telefone(UPDATED_TELEFONE)
            .nacionalidade(UPDATED_NACIONALIDADE)
            .naturalidade(UPDATED_NATURALIDADE)
            .sexo(UPDATED_SEXO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .pcd(UPDATED_PCD)
            .pcdCID(UPDATED_PCD_CID)
            .cnh(UPDATED_CNH)
            .fotoUrl(UPDATED_FOTO_URL)
            .criado(UPDATED_CRIADO);
        PessoaDTO pessoaDTO = pessoaMapper.toDto(updatedPessoa);

        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pessoaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pessoaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPessoa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPessoa.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testPessoa.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPessoa.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPessoa.getNacionalidade()).isEqualTo(UPDATED_NACIONALIDADE);
        assertThat(testPessoa.getNaturalidade()).isEqualTo(UPDATED_NATURALIDADE);
        assertThat(testPessoa.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPessoa.getEstadoCivil()).isEqualTo(UPDATED_ESTADO_CIVIL);
        assertThat(testPessoa.getPcd()).isEqualTo(UPDATED_PCD);
        assertThat(testPessoa.getPcdCID()).isEqualTo(UPDATED_PCD_CID);
        assertThat(testPessoa.getCnh()).isEqualTo(UPDATED_CNH);
        assertThat(testPessoa.getFotoUrl()).isEqualTo(UPDATED_FOTO_URL);
        assertThat(testPessoa.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        pessoa.setId(count.incrementAndGet());

        // Create the Pessoa
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pessoaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        pessoa.setId(count.incrementAndGet());

        // Create the Pessoa
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        pessoa.setId(count.incrementAndGet());

        // Create the Pessoa
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pessoaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePessoaWithPatch() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Update the pessoa using partial update
        Pessoa partialUpdatedPessoa = new Pessoa();
        partialUpdatedPessoa.setId(pessoa.getId());

        partialUpdatedPessoa
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .pcd(UPDATED_PCD)
            .pcdCID(UPDATED_PCD_CID)
            .cnh(UPDATED_CNH)
            .fotoUrl(UPDATED_FOTO_URL)
            .criado(UPDATED_CRIADO);

        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPessoa.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPessoa.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testPessoa.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPessoa.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testPessoa.getNacionalidade()).isEqualTo(DEFAULT_NACIONALIDADE);
        assertThat(testPessoa.getNaturalidade()).isEqualTo(DEFAULT_NATURALIDADE);
        assertThat(testPessoa.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPessoa.getEstadoCivil()).isEqualTo(DEFAULT_ESTADO_CIVIL);
        assertThat(testPessoa.getPcd()).isEqualTo(UPDATED_PCD);
        assertThat(testPessoa.getPcdCID()).isEqualTo(UPDATED_PCD_CID);
        assertThat(testPessoa.getCnh()).isEqualTo(UPDATED_CNH);
        assertThat(testPessoa.getFotoUrl()).isEqualTo(UPDATED_FOTO_URL);
        assertThat(testPessoa.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdatePessoaWithPatch() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();

        // Update the pessoa using partial update
        Pessoa partialUpdatedPessoa = new Pessoa();
        partialUpdatedPessoa.setId(pessoa.getId());

        partialUpdatedPessoa
            .nome(UPDATED_NOME)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .cpf(UPDATED_CPF)
            .telefone(UPDATED_TELEFONE)
            .nacionalidade(UPDATED_NACIONALIDADE)
            .naturalidade(UPDATED_NATURALIDADE)
            .sexo(UPDATED_SEXO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .pcd(UPDATED_PCD)
            .pcdCID(UPDATED_PCD_CID)
            .cnh(UPDATED_CNH)
            .fotoUrl(UPDATED_FOTO_URL)
            .criado(UPDATED_CRIADO);

        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPessoa))
            )
            .andExpect(status().isOk());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
        Pessoa testPessoa = pessoaList.get(pessoaList.size() - 1);
        assertThat(testPessoa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPessoa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPessoa.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testPessoa.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPessoa.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPessoa.getNacionalidade()).isEqualTo(UPDATED_NACIONALIDADE);
        assertThat(testPessoa.getNaturalidade()).isEqualTo(UPDATED_NATURALIDADE);
        assertThat(testPessoa.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPessoa.getEstadoCivil()).isEqualTo(UPDATED_ESTADO_CIVIL);
        assertThat(testPessoa.getPcd()).isEqualTo(UPDATED_PCD);
        assertThat(testPessoa.getPcdCID()).isEqualTo(UPDATED_PCD_CID);
        assertThat(testPessoa.getCnh()).isEqualTo(UPDATED_CNH);
        assertThat(testPessoa.getFotoUrl()).isEqualTo(UPDATED_FOTO_URL);
        assertThat(testPessoa.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        pessoa.setId(count.incrementAndGet());

        // Create the Pessoa
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pessoaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        pessoa.setId(count.incrementAndGet());

        // Create the Pessoa
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPessoa() throws Exception {
        int databaseSizeBeforeUpdate = pessoaRepository.findAll().size();
        pessoa.setId(count.incrementAndGet());

        // Create the Pessoa
        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pessoaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pessoa in the database
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePessoa() throws Exception {
        // Initialize the database
        pessoaRepository.saveAndFlush(pessoa);

        int databaseSizeBeforeDelete = pessoaRepository.findAll().size();

        // Delete the pessoa
        restPessoaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pessoa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pessoa> pessoaList = pessoaRepository.findAll();
        assertThat(pessoaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
