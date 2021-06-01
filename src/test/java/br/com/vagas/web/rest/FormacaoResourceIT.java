package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.Formacao;
import br.com.vagas.domain.Pessoa;
import br.com.vagas.repository.FormacaoRepository;
import br.com.vagas.service.criteria.FormacaoCriteria;
import br.com.vagas.service.dto.FormacaoDTO;
import br.com.vagas.service.mapper.FormacaoMapper;
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
 * Integration tests for the {@link FormacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormacaoResourceIT {

    private static final String DEFAULT_INSTITUICAO = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUICAO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CONCLUSAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONCLUSAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CONCLUSAO = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/formacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private FormacaoMapper formacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormacaoMockMvc;

    private Formacao formacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formacao createEntity(EntityManager em) {
        Formacao formacao = new Formacao()
            .instituicao(DEFAULT_INSTITUICAO)
            .tipo(DEFAULT_TIPO)
            .inicio(DEFAULT_INICIO)
            .conclusao(DEFAULT_CONCLUSAO)
            .criado(DEFAULT_CRIADO);
        return formacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formacao createUpdatedEntity(EntityManager em) {
        Formacao formacao = new Formacao()
            .instituicao(UPDATED_INSTITUICAO)
            .tipo(UPDATED_TIPO)
            .inicio(UPDATED_INICIO)
            .conclusao(UPDATED_CONCLUSAO)
            .criado(UPDATED_CRIADO);
        return formacao;
    }

    @BeforeEach
    public void initTest() {
        formacao = createEntity(em);
    }

    @Test
    @Transactional
    void createFormacao() throws Exception {
        int databaseSizeBeforeCreate = formacaoRepository.findAll().size();
        // Create the Formacao
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);
        restFormacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formacaoDTO)))
            .andExpect(status().isCreated());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Formacao testFormacao = formacaoList.get(formacaoList.size() - 1);
        assertThat(testFormacao.getInstituicao()).isEqualTo(DEFAULT_INSTITUICAO);
        assertThat(testFormacao.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testFormacao.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testFormacao.getConclusao()).isEqualTo(DEFAULT_CONCLUSAO);
        assertThat(testFormacao.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createFormacaoWithExistingId() throws Exception {
        // Create the Formacao with an existing ID
        formacao.setId(1L);
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        int databaseSizeBeforeCreate = formacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = formacaoRepository.findAll().size();
        // set the field null
        formacao.setCriado(null);

        // Create the Formacao, which fails.
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        restFormacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formacaoDTO)))
            .andExpect(status().isBadRequest());

        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormacaos() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList
        restFormacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].instituicao").value(hasItem(DEFAULT_INSTITUICAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].conclusao").value(hasItem(DEFAULT_CONCLUSAO.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getFormacao() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get the formacao
        restFormacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, formacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formacao.getId().intValue()))
            .andExpect(jsonPath("$.instituicao").value(DEFAULT_INSTITUICAO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.conclusao").value(DEFAULT_CONCLUSAO.toString()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getFormacaosByIdFiltering() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        Long id = formacao.getId();

        defaultFormacaoShouldBeFound("id.equals=" + id);
        defaultFormacaoShouldNotBeFound("id.notEquals=" + id);

        defaultFormacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultFormacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormacaosByInstituicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where instituicao equals to DEFAULT_INSTITUICAO
        defaultFormacaoShouldBeFound("instituicao.equals=" + DEFAULT_INSTITUICAO);

        // Get all the formacaoList where instituicao equals to UPDATED_INSTITUICAO
        defaultFormacaoShouldNotBeFound("instituicao.equals=" + UPDATED_INSTITUICAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInstituicaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where instituicao not equals to DEFAULT_INSTITUICAO
        defaultFormacaoShouldNotBeFound("instituicao.notEquals=" + DEFAULT_INSTITUICAO);

        // Get all the formacaoList where instituicao not equals to UPDATED_INSTITUICAO
        defaultFormacaoShouldBeFound("instituicao.notEquals=" + UPDATED_INSTITUICAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInstituicaoIsInShouldWork() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where instituicao in DEFAULT_INSTITUICAO or UPDATED_INSTITUICAO
        defaultFormacaoShouldBeFound("instituicao.in=" + DEFAULT_INSTITUICAO + "," + UPDATED_INSTITUICAO);

        // Get all the formacaoList where instituicao equals to UPDATED_INSTITUICAO
        defaultFormacaoShouldNotBeFound("instituicao.in=" + UPDATED_INSTITUICAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInstituicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where instituicao is not null
        defaultFormacaoShouldBeFound("instituicao.specified=true");

        // Get all the formacaoList where instituicao is null
        defaultFormacaoShouldNotBeFound("instituicao.specified=false");
    }

    @Test
    @Transactional
    void getAllFormacaosByInstituicaoContainsSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where instituicao contains DEFAULT_INSTITUICAO
        defaultFormacaoShouldBeFound("instituicao.contains=" + DEFAULT_INSTITUICAO);

        // Get all the formacaoList where instituicao contains UPDATED_INSTITUICAO
        defaultFormacaoShouldNotBeFound("instituicao.contains=" + UPDATED_INSTITUICAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInstituicaoNotContainsSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where instituicao does not contain DEFAULT_INSTITUICAO
        defaultFormacaoShouldNotBeFound("instituicao.doesNotContain=" + DEFAULT_INSTITUICAO);

        // Get all the formacaoList where instituicao does not contain UPDATED_INSTITUICAO
        defaultFormacaoShouldBeFound("instituicao.doesNotContain=" + UPDATED_INSTITUICAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where tipo equals to DEFAULT_TIPO
        defaultFormacaoShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the formacaoList where tipo equals to UPDATED_TIPO
        defaultFormacaoShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllFormacaosByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where tipo not equals to DEFAULT_TIPO
        defaultFormacaoShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the formacaoList where tipo not equals to UPDATED_TIPO
        defaultFormacaoShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllFormacaosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultFormacaoShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the formacaoList where tipo equals to UPDATED_TIPO
        defaultFormacaoShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllFormacaosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where tipo is not null
        defaultFormacaoShouldBeFound("tipo.specified=true");

        // Get all the formacaoList where tipo is null
        defaultFormacaoShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllFormacaosByTipoContainsSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where tipo contains DEFAULT_TIPO
        defaultFormacaoShouldBeFound("tipo.contains=" + DEFAULT_TIPO);

        // Get all the formacaoList where tipo contains UPDATED_TIPO
        defaultFormacaoShouldNotBeFound("tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllFormacaosByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where tipo does not contain DEFAULT_TIPO
        defaultFormacaoShouldNotBeFound("tipo.doesNotContain=" + DEFAULT_TIPO);

        // Get all the formacaoList where tipo does not contain UPDATED_TIPO
        defaultFormacaoShouldBeFound("tipo.doesNotContain=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio equals to DEFAULT_INICIO
        defaultFormacaoShouldBeFound("inicio.equals=" + DEFAULT_INICIO);

        // Get all the formacaoList where inicio equals to UPDATED_INICIO
        defaultFormacaoShouldNotBeFound("inicio.equals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio not equals to DEFAULT_INICIO
        defaultFormacaoShouldNotBeFound("inicio.notEquals=" + DEFAULT_INICIO);

        // Get all the formacaoList where inicio not equals to UPDATED_INICIO
        defaultFormacaoShouldBeFound("inicio.notEquals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsInShouldWork() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio in DEFAULT_INICIO or UPDATED_INICIO
        defaultFormacaoShouldBeFound("inicio.in=" + DEFAULT_INICIO + "," + UPDATED_INICIO);

        // Get all the formacaoList where inicio equals to UPDATED_INICIO
        defaultFormacaoShouldNotBeFound("inicio.in=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio is not null
        defaultFormacaoShouldBeFound("inicio.specified=true");

        // Get all the formacaoList where inicio is null
        defaultFormacaoShouldNotBeFound("inicio.specified=false");
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio is greater than or equal to DEFAULT_INICIO
        defaultFormacaoShouldBeFound("inicio.greaterThanOrEqual=" + DEFAULT_INICIO);

        // Get all the formacaoList where inicio is greater than or equal to UPDATED_INICIO
        defaultFormacaoShouldNotBeFound("inicio.greaterThanOrEqual=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio is less than or equal to DEFAULT_INICIO
        defaultFormacaoShouldBeFound("inicio.lessThanOrEqual=" + DEFAULT_INICIO);

        // Get all the formacaoList where inicio is less than or equal to SMALLER_INICIO
        defaultFormacaoShouldNotBeFound("inicio.lessThanOrEqual=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio is less than DEFAULT_INICIO
        defaultFormacaoShouldNotBeFound("inicio.lessThan=" + DEFAULT_INICIO);

        // Get all the formacaoList where inicio is less than UPDATED_INICIO
        defaultFormacaoShouldBeFound("inicio.lessThan=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllFormacaosByInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where inicio is greater than DEFAULT_INICIO
        defaultFormacaoShouldNotBeFound("inicio.greaterThan=" + DEFAULT_INICIO);

        // Get all the formacaoList where inicio is greater than SMALLER_INICIO
        defaultFormacaoShouldBeFound("inicio.greaterThan=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao equals to DEFAULT_CONCLUSAO
        defaultFormacaoShouldBeFound("conclusao.equals=" + DEFAULT_CONCLUSAO);

        // Get all the formacaoList where conclusao equals to UPDATED_CONCLUSAO
        defaultFormacaoShouldNotBeFound("conclusao.equals=" + UPDATED_CONCLUSAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao not equals to DEFAULT_CONCLUSAO
        defaultFormacaoShouldNotBeFound("conclusao.notEquals=" + DEFAULT_CONCLUSAO);

        // Get all the formacaoList where conclusao not equals to UPDATED_CONCLUSAO
        defaultFormacaoShouldBeFound("conclusao.notEquals=" + UPDATED_CONCLUSAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsInShouldWork() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao in DEFAULT_CONCLUSAO or UPDATED_CONCLUSAO
        defaultFormacaoShouldBeFound("conclusao.in=" + DEFAULT_CONCLUSAO + "," + UPDATED_CONCLUSAO);

        // Get all the formacaoList where conclusao equals to UPDATED_CONCLUSAO
        defaultFormacaoShouldNotBeFound("conclusao.in=" + UPDATED_CONCLUSAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao is not null
        defaultFormacaoShouldBeFound("conclusao.specified=true");

        // Get all the formacaoList where conclusao is null
        defaultFormacaoShouldNotBeFound("conclusao.specified=false");
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao is greater than or equal to DEFAULT_CONCLUSAO
        defaultFormacaoShouldBeFound("conclusao.greaterThanOrEqual=" + DEFAULT_CONCLUSAO);

        // Get all the formacaoList where conclusao is greater than or equal to UPDATED_CONCLUSAO
        defaultFormacaoShouldNotBeFound("conclusao.greaterThanOrEqual=" + UPDATED_CONCLUSAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao is less than or equal to DEFAULT_CONCLUSAO
        defaultFormacaoShouldBeFound("conclusao.lessThanOrEqual=" + DEFAULT_CONCLUSAO);

        // Get all the formacaoList where conclusao is less than or equal to SMALLER_CONCLUSAO
        defaultFormacaoShouldNotBeFound("conclusao.lessThanOrEqual=" + SMALLER_CONCLUSAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsLessThanSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao is less than DEFAULT_CONCLUSAO
        defaultFormacaoShouldNotBeFound("conclusao.lessThan=" + DEFAULT_CONCLUSAO);

        // Get all the formacaoList where conclusao is less than UPDATED_CONCLUSAO
        defaultFormacaoShouldBeFound("conclusao.lessThan=" + UPDATED_CONCLUSAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByConclusaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where conclusao is greater than DEFAULT_CONCLUSAO
        defaultFormacaoShouldNotBeFound("conclusao.greaterThan=" + DEFAULT_CONCLUSAO);

        // Get all the formacaoList where conclusao is greater than SMALLER_CONCLUSAO
        defaultFormacaoShouldBeFound("conclusao.greaterThan=" + SMALLER_CONCLUSAO);
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado equals to DEFAULT_CRIADO
        defaultFormacaoShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the formacaoList where criado equals to UPDATED_CRIADO
        defaultFormacaoShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado not equals to DEFAULT_CRIADO
        defaultFormacaoShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the formacaoList where criado not equals to UPDATED_CRIADO
        defaultFormacaoShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultFormacaoShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the formacaoList where criado equals to UPDATED_CRIADO
        defaultFormacaoShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado is not null
        defaultFormacaoShouldBeFound("criado.specified=true");

        // Get all the formacaoList where criado is null
        defaultFormacaoShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado is greater than or equal to DEFAULT_CRIADO
        defaultFormacaoShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the formacaoList where criado is greater than or equal to UPDATED_CRIADO
        defaultFormacaoShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado is less than or equal to DEFAULT_CRIADO
        defaultFormacaoShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the formacaoList where criado is less than or equal to SMALLER_CRIADO
        defaultFormacaoShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado is less than DEFAULT_CRIADO
        defaultFormacaoShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the formacaoList where criado is less than UPDATED_CRIADO
        defaultFormacaoShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFormacaosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        // Get all the formacaoList where criado is greater than DEFAULT_CRIADO
        defaultFormacaoShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the formacaoList where criado is greater than SMALLER_CRIADO
        defaultFormacaoShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFormacaosByPessoaIsEqualToSomething() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);
        Pessoa pessoa = PessoaResourceIT.createEntity(em);
        em.persist(pessoa);
        em.flush();
        formacao.setPessoa(pessoa);
        formacaoRepository.saveAndFlush(formacao);
        Long pessoaId = pessoa.getId();

        // Get all the formacaoList where pessoa equals to pessoaId
        defaultFormacaoShouldBeFound("pessoaId.equals=" + pessoaId);

        // Get all the formacaoList where pessoa equals to (pessoaId + 1)
        defaultFormacaoShouldNotBeFound("pessoaId.equals=" + (pessoaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormacaoShouldBeFound(String filter) throws Exception {
        restFormacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].instituicao").value(hasItem(DEFAULT_INSTITUICAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].conclusao").value(hasItem(DEFAULT_CONCLUSAO.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restFormacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormacaoShouldNotBeFound(String filter) throws Exception {
        restFormacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormacao() throws Exception {
        // Get the formacao
        restFormacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormacao() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();

        // Update the formacao
        Formacao updatedFormacao = formacaoRepository.findById(formacao.getId()).get();
        // Disconnect from session so that the updates on updatedFormacao are not directly saved in db
        em.detach(updatedFormacao);
        updatedFormacao
            .instituicao(UPDATED_INSTITUICAO)
            .tipo(UPDATED_TIPO)
            .inicio(UPDATED_INICIO)
            .conclusao(UPDATED_CONCLUSAO)
            .criado(UPDATED_CRIADO);
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(updatedFormacao);

        restFormacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
        Formacao testFormacao = formacaoList.get(formacaoList.size() - 1);
        assertThat(testFormacao.getInstituicao()).isEqualTo(UPDATED_INSTITUICAO);
        assertThat(testFormacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testFormacao.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testFormacao.getConclusao()).isEqualTo(UPDATED_CONCLUSAO);
        assertThat(testFormacao.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingFormacao() throws Exception {
        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();
        formacao.setId(count.incrementAndGet());

        // Create the Formacao
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormacao() throws Exception {
        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();
        formacao.setId(count.incrementAndGet());

        // Create the Formacao
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormacao() throws Exception {
        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();
        formacao.setId(count.incrementAndGet());

        // Create the Formacao
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormacaoWithPatch() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();

        // Update the formacao using partial update
        Formacao partialUpdatedFormacao = new Formacao();
        partialUpdatedFormacao.setId(formacao.getId());

        partialUpdatedFormacao.instituicao(UPDATED_INSTITUICAO).tipo(UPDATED_TIPO).inicio(UPDATED_INICIO).criado(UPDATED_CRIADO);

        restFormacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormacao))
            )
            .andExpect(status().isOk());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
        Formacao testFormacao = formacaoList.get(formacaoList.size() - 1);
        assertThat(testFormacao.getInstituicao()).isEqualTo(UPDATED_INSTITUICAO);
        assertThat(testFormacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testFormacao.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testFormacao.getConclusao()).isEqualTo(DEFAULT_CONCLUSAO);
        assertThat(testFormacao.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateFormacaoWithPatch() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();

        // Update the formacao using partial update
        Formacao partialUpdatedFormacao = new Formacao();
        partialUpdatedFormacao.setId(formacao.getId());

        partialUpdatedFormacao
            .instituicao(UPDATED_INSTITUICAO)
            .tipo(UPDATED_TIPO)
            .inicio(UPDATED_INICIO)
            .conclusao(UPDATED_CONCLUSAO)
            .criado(UPDATED_CRIADO);

        restFormacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormacao))
            )
            .andExpect(status().isOk());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
        Formacao testFormacao = formacaoList.get(formacaoList.size() - 1);
        assertThat(testFormacao.getInstituicao()).isEqualTo(UPDATED_INSTITUICAO);
        assertThat(testFormacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testFormacao.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testFormacao.getConclusao()).isEqualTo(UPDATED_CONCLUSAO);
        assertThat(testFormacao.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingFormacao() throws Exception {
        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();
        formacao.setId(count.incrementAndGet());

        // Create the Formacao
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormacao() throws Exception {
        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();
        formacao.setId(count.incrementAndGet());

        // Create the Formacao
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormacao() throws Exception {
        int databaseSizeBeforeUpdate = formacaoRepository.findAll().size();
        formacao.setId(count.incrementAndGet());

        // Create the Formacao
        FormacaoDTO formacaoDTO = formacaoMapper.toDto(formacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formacao in the database
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormacao() throws Exception {
        // Initialize the database
        formacaoRepository.saveAndFlush(formacao);

        int databaseSizeBeforeDelete = formacaoRepository.findAll().size();

        // Delete the formacao
        restFormacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, formacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formacao> formacaoList = formacaoRepository.findAll();
        assertThat(formacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
