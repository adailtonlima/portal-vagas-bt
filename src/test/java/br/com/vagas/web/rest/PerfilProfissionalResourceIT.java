package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.AreaAtuacao;
import br.com.vagas.domain.PerfilProfissional;
import br.com.vagas.domain.Pessoa;
import br.com.vagas.repository.PerfilProfissionalRepository;
import br.com.vagas.service.criteria.PerfilProfissionalCriteria;
import br.com.vagas.service.dto.PerfilProfissionalDTO;
import br.com.vagas.service.mapper.PerfilProfissionalMapper;
import java.time.Instant;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PerfilProfissionalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PerfilProfissionalResourceIT {

    private static final Boolean DEFAULT_ESTAGIO = false;
    private static final Boolean UPDATED_ESTAGIO = true;

    private static final Boolean DEFAULT_PROCURANDO_EMPREGO = false;
    private static final Boolean UPDATED_PROCURANDO_EMPREGO = true;

    private static final String DEFAULT_OBJETIVOS_PESSOAIS = "AAAAAAAAAA";
    private static final String UPDATED_OBJETIVOS_PESSOAIS = "BBBBBBBBBB";

    private static final Double DEFAULT_PRETENSAO_SALARIAL = 1D;
    private static final Double UPDATED_PRETENSAO_SALARIAL = 2D;
    private static final Double SMALLER_PRETENSAO_SALARIAL = 1D - 1D;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/perfil-profissionals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Autowired
    private PerfilProfissionalMapper perfilProfissionalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerfilProfissionalMockMvc;

    private PerfilProfissional perfilProfissional;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilProfissional createEntity(EntityManager em) {
        PerfilProfissional perfilProfissional = new PerfilProfissional()
            .estagio(DEFAULT_ESTAGIO)
            .procurandoEmprego(DEFAULT_PROCURANDO_EMPREGO)
            .objetivosPessoais(DEFAULT_OBJETIVOS_PESSOAIS)
            .pretensaoSalarial(DEFAULT_PRETENSAO_SALARIAL)
            .criado(DEFAULT_CRIADO);
        return perfilProfissional;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilProfissional createUpdatedEntity(EntityManager em) {
        PerfilProfissional perfilProfissional = new PerfilProfissional()
            .estagio(UPDATED_ESTAGIO)
            .procurandoEmprego(UPDATED_PROCURANDO_EMPREGO)
            .objetivosPessoais(UPDATED_OBJETIVOS_PESSOAIS)
            .pretensaoSalarial(UPDATED_PRETENSAO_SALARIAL)
            .criado(UPDATED_CRIADO);
        return perfilProfissional;
    }

    @BeforeEach
    public void initTest() {
        perfilProfissional = createEntity(em);
    }

    @Test
    @Transactional
    void createPerfilProfissional() throws Exception {
        int databaseSizeBeforeCreate = perfilProfissionalRepository.findAll().size();
        // Create the PerfilProfissional
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);
        restPerfilProfissionalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeCreate + 1);
        PerfilProfissional testPerfilProfissional = perfilProfissionalList.get(perfilProfissionalList.size() - 1);
        assertThat(testPerfilProfissional.getEstagio()).isEqualTo(DEFAULT_ESTAGIO);
        assertThat(testPerfilProfissional.getProcurandoEmprego()).isEqualTo(DEFAULT_PROCURANDO_EMPREGO);
        assertThat(testPerfilProfissional.getObjetivosPessoais()).isEqualTo(DEFAULT_OBJETIVOS_PESSOAIS);
        assertThat(testPerfilProfissional.getPretensaoSalarial()).isEqualTo(DEFAULT_PRETENSAO_SALARIAL);
        assertThat(testPerfilProfissional.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createPerfilProfissionalWithExistingId() throws Exception {
        // Create the PerfilProfissional with an existing ID
        perfilProfissional.setId(1L);
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        int databaseSizeBeforeCreate = perfilProfissionalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilProfissionalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = perfilProfissionalRepository.findAll().size();
        // set the field null
        perfilProfissional.setCriado(null);

        // Create the PerfilProfissional, which fails.
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        restPerfilProfissionalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionals() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList
        restPerfilProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfilProfissional.getId().intValue())))
            .andExpect(jsonPath("$.[*].estagio").value(hasItem(DEFAULT_ESTAGIO.booleanValue())))
            .andExpect(jsonPath("$.[*].procurandoEmprego").value(hasItem(DEFAULT_PROCURANDO_EMPREGO.booleanValue())))
            .andExpect(jsonPath("$.[*].objetivosPessoais").value(hasItem(DEFAULT_OBJETIVOS_PESSOAIS.toString())))
            .andExpect(jsonPath("$.[*].pretensaoSalarial").value(hasItem(DEFAULT_PRETENSAO_SALARIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getPerfilProfissional() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get the perfilProfissional
        restPerfilProfissionalMockMvc
            .perform(get(ENTITY_API_URL_ID, perfilProfissional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perfilProfissional.getId().intValue()))
            .andExpect(jsonPath("$.estagio").value(DEFAULT_ESTAGIO.booleanValue()))
            .andExpect(jsonPath("$.procurandoEmprego").value(DEFAULT_PROCURANDO_EMPREGO.booleanValue()))
            .andExpect(jsonPath("$.objetivosPessoais").value(DEFAULT_OBJETIVOS_PESSOAIS.toString()))
            .andExpect(jsonPath("$.pretensaoSalarial").value(DEFAULT_PRETENSAO_SALARIAL.doubleValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getPerfilProfissionalsByIdFiltering() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        Long id = perfilProfissional.getId();

        defaultPerfilProfissionalShouldBeFound("id.equals=" + id);
        defaultPerfilProfissionalShouldNotBeFound("id.notEquals=" + id);

        defaultPerfilProfissionalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPerfilProfissionalShouldNotBeFound("id.greaterThan=" + id);

        defaultPerfilProfissionalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPerfilProfissionalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByEstagioIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where estagio equals to DEFAULT_ESTAGIO
        defaultPerfilProfissionalShouldBeFound("estagio.equals=" + DEFAULT_ESTAGIO);

        // Get all the perfilProfissionalList where estagio equals to UPDATED_ESTAGIO
        defaultPerfilProfissionalShouldNotBeFound("estagio.equals=" + UPDATED_ESTAGIO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByEstagioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where estagio not equals to DEFAULT_ESTAGIO
        defaultPerfilProfissionalShouldNotBeFound("estagio.notEquals=" + DEFAULT_ESTAGIO);

        // Get all the perfilProfissionalList where estagio not equals to UPDATED_ESTAGIO
        defaultPerfilProfissionalShouldBeFound("estagio.notEquals=" + UPDATED_ESTAGIO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByEstagioIsInShouldWork() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where estagio in DEFAULT_ESTAGIO or UPDATED_ESTAGIO
        defaultPerfilProfissionalShouldBeFound("estagio.in=" + DEFAULT_ESTAGIO + "," + UPDATED_ESTAGIO);

        // Get all the perfilProfissionalList where estagio equals to UPDATED_ESTAGIO
        defaultPerfilProfissionalShouldNotBeFound("estagio.in=" + UPDATED_ESTAGIO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByEstagioIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where estagio is not null
        defaultPerfilProfissionalShouldBeFound("estagio.specified=true");

        // Get all the perfilProfissionalList where estagio is null
        defaultPerfilProfissionalShouldNotBeFound("estagio.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByProcurandoEmpregoIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where procurandoEmprego equals to DEFAULT_PROCURANDO_EMPREGO
        defaultPerfilProfissionalShouldBeFound("procurandoEmprego.equals=" + DEFAULT_PROCURANDO_EMPREGO);

        // Get all the perfilProfissionalList where procurandoEmprego equals to UPDATED_PROCURANDO_EMPREGO
        defaultPerfilProfissionalShouldNotBeFound("procurandoEmprego.equals=" + UPDATED_PROCURANDO_EMPREGO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByProcurandoEmpregoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where procurandoEmprego not equals to DEFAULT_PROCURANDO_EMPREGO
        defaultPerfilProfissionalShouldNotBeFound("procurandoEmprego.notEquals=" + DEFAULT_PROCURANDO_EMPREGO);

        // Get all the perfilProfissionalList where procurandoEmprego not equals to UPDATED_PROCURANDO_EMPREGO
        defaultPerfilProfissionalShouldBeFound("procurandoEmprego.notEquals=" + UPDATED_PROCURANDO_EMPREGO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByProcurandoEmpregoIsInShouldWork() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where procurandoEmprego in DEFAULT_PROCURANDO_EMPREGO or UPDATED_PROCURANDO_EMPREGO
        defaultPerfilProfissionalShouldBeFound("procurandoEmprego.in=" + DEFAULT_PROCURANDO_EMPREGO + "," + UPDATED_PROCURANDO_EMPREGO);

        // Get all the perfilProfissionalList where procurandoEmprego equals to UPDATED_PROCURANDO_EMPREGO
        defaultPerfilProfissionalShouldNotBeFound("procurandoEmprego.in=" + UPDATED_PROCURANDO_EMPREGO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByProcurandoEmpregoIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where procurandoEmprego is not null
        defaultPerfilProfissionalShouldBeFound("procurandoEmprego.specified=true");

        // Get all the perfilProfissionalList where procurandoEmprego is null
        defaultPerfilProfissionalShouldNotBeFound("procurandoEmprego.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial equals to DEFAULT_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.equals=" + DEFAULT_PRETENSAO_SALARIAL);

        // Get all the perfilProfissionalList where pretensaoSalarial equals to UPDATED_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.equals=" + UPDATED_PRETENSAO_SALARIAL);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial not equals to DEFAULT_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.notEquals=" + DEFAULT_PRETENSAO_SALARIAL);

        // Get all the perfilProfissionalList where pretensaoSalarial not equals to UPDATED_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.notEquals=" + UPDATED_PRETENSAO_SALARIAL);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsInShouldWork() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial in DEFAULT_PRETENSAO_SALARIAL or UPDATED_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.in=" + DEFAULT_PRETENSAO_SALARIAL + "," + UPDATED_PRETENSAO_SALARIAL);

        // Get all the perfilProfissionalList where pretensaoSalarial equals to UPDATED_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.in=" + UPDATED_PRETENSAO_SALARIAL);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial is not null
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.specified=true");

        // Get all the perfilProfissionalList where pretensaoSalarial is null
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial is greater than or equal to DEFAULT_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.greaterThanOrEqual=" + DEFAULT_PRETENSAO_SALARIAL);

        // Get all the perfilProfissionalList where pretensaoSalarial is greater than or equal to UPDATED_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.greaterThanOrEqual=" + UPDATED_PRETENSAO_SALARIAL);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial is less than or equal to DEFAULT_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.lessThanOrEqual=" + DEFAULT_PRETENSAO_SALARIAL);

        // Get all the perfilProfissionalList where pretensaoSalarial is less than or equal to SMALLER_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.lessThanOrEqual=" + SMALLER_PRETENSAO_SALARIAL);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsLessThanSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial is less than DEFAULT_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.lessThan=" + DEFAULT_PRETENSAO_SALARIAL);

        // Get all the perfilProfissionalList where pretensaoSalarial is less than UPDATED_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.lessThan=" + UPDATED_PRETENSAO_SALARIAL);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPretensaoSalarialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where pretensaoSalarial is greater than DEFAULT_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldNotBeFound("pretensaoSalarial.greaterThan=" + DEFAULT_PRETENSAO_SALARIAL);

        // Get all the perfilProfissionalList where pretensaoSalarial is greater than SMALLER_PRETENSAO_SALARIAL
        defaultPerfilProfissionalShouldBeFound("pretensaoSalarial.greaterThan=" + SMALLER_PRETENSAO_SALARIAL);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado equals to DEFAULT_CRIADO
        defaultPerfilProfissionalShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the perfilProfissionalList where criado equals to UPDATED_CRIADO
        defaultPerfilProfissionalShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado not equals to DEFAULT_CRIADO
        defaultPerfilProfissionalShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the perfilProfissionalList where criado not equals to UPDATED_CRIADO
        defaultPerfilProfissionalShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultPerfilProfissionalShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the perfilProfissionalList where criado equals to UPDATED_CRIADO
        defaultPerfilProfissionalShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado is not null
        defaultPerfilProfissionalShouldBeFound("criado.specified=true");

        // Get all the perfilProfissionalList where criado is null
        defaultPerfilProfissionalShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado is greater than or equal to DEFAULT_CRIADO
        defaultPerfilProfissionalShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the perfilProfissionalList where criado is greater than or equal to UPDATED_CRIADO
        defaultPerfilProfissionalShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado is less than or equal to DEFAULT_CRIADO
        defaultPerfilProfissionalShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the perfilProfissionalList where criado is less than or equal to SMALLER_CRIADO
        defaultPerfilProfissionalShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado is less than DEFAULT_CRIADO
        defaultPerfilProfissionalShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the perfilProfissionalList where criado is less than UPDATED_CRIADO
        defaultPerfilProfissionalShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        // Get all the perfilProfissionalList where criado is greater than DEFAULT_CRIADO
        defaultPerfilProfissionalShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the perfilProfissionalList where criado is greater than SMALLER_CRIADO
        defaultPerfilProfissionalShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);
        AreaAtuacao area = AreaAtuacaoResourceIT.createEntity(em);
        em.persist(area);
        em.flush();
        perfilProfissional.setArea(area);
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);
        Long areaId = area.getId();

        // Get all the perfilProfissionalList where area equals to areaId
        defaultPerfilProfissionalShouldBeFound("areaId.equals=" + areaId);

        // Get all the perfilProfissionalList where area equals to (areaId + 1)
        defaultPerfilProfissionalShouldNotBeFound("areaId.equals=" + (areaId + 1));
    }

    @Test
    @Transactional
    void getAllPerfilProfissionalsByPessoaIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);
        Pessoa pessoa = PessoaResourceIT.createEntity(em);
        em.persist(pessoa);
        em.flush();
        perfilProfissional.setPessoa(pessoa);
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);
        Long pessoaId = pessoa.getId();

        // Get all the perfilProfissionalList where pessoa equals to pessoaId
        defaultPerfilProfissionalShouldBeFound("pessoaId.equals=" + pessoaId);

        // Get all the perfilProfissionalList where pessoa equals to (pessoaId + 1)
        defaultPerfilProfissionalShouldNotBeFound("pessoaId.equals=" + (pessoaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPerfilProfissionalShouldBeFound(String filter) throws Exception {
        restPerfilProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfilProfissional.getId().intValue())))
            .andExpect(jsonPath("$.[*].estagio").value(hasItem(DEFAULT_ESTAGIO.booleanValue())))
            .andExpect(jsonPath("$.[*].procurandoEmprego").value(hasItem(DEFAULT_PROCURANDO_EMPREGO.booleanValue())))
            .andExpect(jsonPath("$.[*].objetivosPessoais").value(hasItem(DEFAULT_OBJETIVOS_PESSOAIS.toString())))
            .andExpect(jsonPath("$.[*].pretensaoSalarial").value(hasItem(DEFAULT_PRETENSAO_SALARIAL.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restPerfilProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPerfilProfissionalShouldNotBeFound(String filter) throws Exception {
        restPerfilProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPerfilProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerfilProfissional() throws Exception {
        // Get the perfilProfissional
        restPerfilProfissionalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerfilProfissional() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();

        // Update the perfilProfissional
        PerfilProfissional updatedPerfilProfissional = perfilProfissionalRepository.findById(perfilProfissional.getId()).get();
        // Disconnect from session so that the updates on updatedPerfilProfissional are not directly saved in db
        em.detach(updatedPerfilProfissional);
        updatedPerfilProfissional
            .estagio(UPDATED_ESTAGIO)
            .procurandoEmprego(UPDATED_PROCURANDO_EMPREGO)
            .objetivosPessoais(UPDATED_OBJETIVOS_PESSOAIS)
            .pretensaoSalarial(UPDATED_PRETENSAO_SALARIAL)
            .criado(UPDATED_CRIADO);
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(updatedPerfilProfissional);

        restPerfilProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilProfissionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isOk());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
        PerfilProfissional testPerfilProfissional = perfilProfissionalList.get(perfilProfissionalList.size() - 1);
        assertThat(testPerfilProfissional.getEstagio()).isEqualTo(UPDATED_ESTAGIO);
        assertThat(testPerfilProfissional.getProcurandoEmprego()).isEqualTo(UPDATED_PROCURANDO_EMPREGO);
        assertThat(testPerfilProfissional.getObjetivosPessoais()).isEqualTo(UPDATED_OBJETIVOS_PESSOAIS);
        assertThat(testPerfilProfissional.getPretensaoSalarial()).isEqualTo(UPDATED_PRETENSAO_SALARIAL);
        assertThat(testPerfilProfissional.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingPerfilProfissional() throws Exception {
        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();
        perfilProfissional.setId(count.incrementAndGet());

        // Create the PerfilProfissional
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilProfissionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerfilProfissional() throws Exception {
        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();
        perfilProfissional.setId(count.incrementAndGet());

        // Create the PerfilProfissional
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerfilProfissional() throws Exception {
        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();
        perfilProfissional.setId(count.incrementAndGet());

        // Create the PerfilProfissional
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerfilProfissionalWithPatch() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();

        // Update the perfilProfissional using partial update
        PerfilProfissional partialUpdatedPerfilProfissional = new PerfilProfissional();
        partialUpdatedPerfilProfissional.setId(perfilProfissional.getId());

        partialUpdatedPerfilProfissional.objetivosPessoais(UPDATED_OBJETIVOS_PESSOAIS);

        restPerfilProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilProfissional.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfilProfissional))
            )
            .andExpect(status().isOk());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
        PerfilProfissional testPerfilProfissional = perfilProfissionalList.get(perfilProfissionalList.size() - 1);
        assertThat(testPerfilProfissional.getEstagio()).isEqualTo(DEFAULT_ESTAGIO);
        assertThat(testPerfilProfissional.getProcurandoEmprego()).isEqualTo(DEFAULT_PROCURANDO_EMPREGO);
        assertThat(testPerfilProfissional.getObjetivosPessoais()).isEqualTo(UPDATED_OBJETIVOS_PESSOAIS);
        assertThat(testPerfilProfissional.getPretensaoSalarial()).isEqualTo(DEFAULT_PRETENSAO_SALARIAL);
        assertThat(testPerfilProfissional.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdatePerfilProfissionalWithPatch() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();

        // Update the perfilProfissional using partial update
        PerfilProfissional partialUpdatedPerfilProfissional = new PerfilProfissional();
        partialUpdatedPerfilProfissional.setId(perfilProfissional.getId());

        partialUpdatedPerfilProfissional
            .estagio(UPDATED_ESTAGIO)
            .procurandoEmprego(UPDATED_PROCURANDO_EMPREGO)
            .objetivosPessoais(UPDATED_OBJETIVOS_PESSOAIS)
            .pretensaoSalarial(UPDATED_PRETENSAO_SALARIAL)
            .criado(UPDATED_CRIADO);

        restPerfilProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilProfissional.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfilProfissional))
            )
            .andExpect(status().isOk());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
        PerfilProfissional testPerfilProfissional = perfilProfissionalList.get(perfilProfissionalList.size() - 1);
        assertThat(testPerfilProfissional.getEstagio()).isEqualTo(UPDATED_ESTAGIO);
        assertThat(testPerfilProfissional.getProcurandoEmprego()).isEqualTo(UPDATED_PROCURANDO_EMPREGO);
        assertThat(testPerfilProfissional.getObjetivosPessoais()).isEqualTo(UPDATED_OBJETIVOS_PESSOAIS);
        assertThat(testPerfilProfissional.getPretensaoSalarial()).isEqualTo(UPDATED_PRETENSAO_SALARIAL);
        assertThat(testPerfilProfissional.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingPerfilProfissional() throws Exception {
        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();
        perfilProfissional.setId(count.incrementAndGet());

        // Create the PerfilProfissional
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, perfilProfissionalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerfilProfissional() throws Exception {
        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();
        perfilProfissional.setId(count.incrementAndGet());

        // Create the PerfilProfissional
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerfilProfissional() throws Exception {
        int databaseSizeBeforeUpdate = perfilProfissionalRepository.findAll().size();
        perfilProfissional.setId(count.incrementAndGet());

        // Create the PerfilProfissional
        PerfilProfissionalDTO perfilProfissionalDTO = perfilProfissionalMapper.toDto(perfilProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilProfissionalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilProfissional in the database
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerfilProfissional() throws Exception {
        // Initialize the database
        perfilProfissionalRepository.saveAndFlush(perfilProfissional);

        int databaseSizeBeforeDelete = perfilProfissionalRepository.findAll().size();

        // Delete the perfilProfissional
        restPerfilProfissionalMockMvc
            .perform(delete(ENTITY_API_URL_ID, perfilProfissional.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PerfilProfissional> perfilProfissionalList = perfilProfissionalRepository.findAll();
        assertThat(perfilProfissionalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
