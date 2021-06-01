package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.ExperienciaProfissional;
import br.com.vagas.domain.Pessoa;
import br.com.vagas.repository.ExperienciaProfissionalRepository;
import br.com.vagas.service.criteria.ExperienciaProfissionalCriteria;
import br.com.vagas.service.dto.ExperienciaProfissionalDTO;
import br.com.vagas.service.mapper.ExperienciaProfissionalMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ExperienciaProfissionalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExperienciaProfissionalResourceIT {

    private static final String DEFAULT_EMPRESA = "AAAAAAAAAA";
    private static final String UPDATED_EMPRESA = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO = "AAAAAAAAAA";
    private static final String UPDATED_CARGO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGMENTO = "AAAAAAAAAA";
    private static final String UPDATED_SEGMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_PORTE = "AAAAAAAAAA";
    private static final String UPDATED_PORTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FIM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FIM = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRICAO_ATIVIDADE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO_ATIVIDADE = "BBBBBBBBBB";

    private static final Double DEFAULT_SALARIO = 1D;
    private static final Double UPDATED_SALARIO = 2D;
    private static final Double SMALLER_SALARIO = 1D - 1D;

    private static final Double DEFAULT_BENEFICIOS = 1D;
    private static final Double UPDATED_BENEFICIOS = 2D;
    private static final Double SMALLER_BENEFICIOS = 1D - 1D;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/experiencia-profissionals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExperienciaProfissionalRepository experienciaProfissionalRepository;

    @Autowired
    private ExperienciaProfissionalMapper experienciaProfissionalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExperienciaProfissionalMockMvc;

    private ExperienciaProfissional experienciaProfissional;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExperienciaProfissional createEntity(EntityManager em) {
        ExperienciaProfissional experienciaProfissional = new ExperienciaProfissional()
            .empresa(DEFAULT_EMPRESA)
            .cargo(DEFAULT_CARGO)
            .segmento(DEFAULT_SEGMENTO)
            .porte(DEFAULT_PORTE)
            .inicio(DEFAULT_INICIO)
            .fim(DEFAULT_FIM)
            .descricaoAtividade(DEFAULT_DESCRICAO_ATIVIDADE)
            .salario(DEFAULT_SALARIO)
            .beneficios(DEFAULT_BENEFICIOS)
            .criado(DEFAULT_CRIADO);
        return experienciaProfissional;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExperienciaProfissional createUpdatedEntity(EntityManager em) {
        ExperienciaProfissional experienciaProfissional = new ExperienciaProfissional()
            .empresa(UPDATED_EMPRESA)
            .cargo(UPDATED_CARGO)
            .segmento(UPDATED_SEGMENTO)
            .porte(UPDATED_PORTE)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .descricaoAtividade(UPDATED_DESCRICAO_ATIVIDADE)
            .salario(UPDATED_SALARIO)
            .beneficios(UPDATED_BENEFICIOS)
            .criado(UPDATED_CRIADO);
        return experienciaProfissional;
    }

    @BeforeEach
    public void initTest() {
        experienciaProfissional = createEntity(em);
    }

    @Test
    @Transactional
    void createExperienciaProfissional() throws Exception {
        int databaseSizeBeforeCreate = experienciaProfissionalRepository.findAll().size();
        // Create the ExperienciaProfissional
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);
        restExperienciaProfissionalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeCreate + 1);
        ExperienciaProfissional testExperienciaProfissional = experienciaProfissionalList.get(experienciaProfissionalList.size() - 1);
        assertThat(testExperienciaProfissional.getEmpresa()).isEqualTo(DEFAULT_EMPRESA);
        assertThat(testExperienciaProfissional.getCargo()).isEqualTo(DEFAULT_CARGO);
        assertThat(testExperienciaProfissional.getSegmento()).isEqualTo(DEFAULT_SEGMENTO);
        assertThat(testExperienciaProfissional.getPorte()).isEqualTo(DEFAULT_PORTE);
        assertThat(testExperienciaProfissional.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testExperienciaProfissional.getFim()).isEqualTo(DEFAULT_FIM);
        assertThat(testExperienciaProfissional.getDescricaoAtividade()).isEqualTo(DEFAULT_DESCRICAO_ATIVIDADE);
        assertThat(testExperienciaProfissional.getSalario()).isEqualTo(DEFAULT_SALARIO);
        assertThat(testExperienciaProfissional.getBeneficios()).isEqualTo(DEFAULT_BENEFICIOS);
        assertThat(testExperienciaProfissional.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createExperienciaProfissionalWithExistingId() throws Exception {
        // Create the ExperienciaProfissional with an existing ID
        experienciaProfissional.setId(1L);
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        int databaseSizeBeforeCreate = experienciaProfissionalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperienciaProfissionalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienciaProfissionalRepository.findAll().size();
        // set the field null
        experienciaProfissional.setCriado(null);

        // Create the ExperienciaProfissional, which fails.
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        restExperienciaProfissionalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionals() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList
        restExperienciaProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experienciaProfissional.getId().intValue())))
            .andExpect(jsonPath("$.[*].empresa").value(hasItem(DEFAULT_EMPRESA)))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO)))
            .andExpect(jsonPath("$.[*].segmento").value(hasItem(DEFAULT_SEGMENTO)))
            .andExpect(jsonPath("$.[*].porte").value(hasItem(DEFAULT_PORTE)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM.toString())))
            .andExpect(jsonPath("$.[*].descricaoAtividade").value(hasItem(DEFAULT_DESCRICAO_ATIVIDADE.toString())))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.doubleValue())))
            .andExpect(jsonPath("$.[*].beneficios").value(hasItem(DEFAULT_BENEFICIOS.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getExperienciaProfissional() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get the experienciaProfissional
        restExperienciaProfissionalMockMvc
            .perform(get(ENTITY_API_URL_ID, experienciaProfissional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(experienciaProfissional.getId().intValue()))
            .andExpect(jsonPath("$.empresa").value(DEFAULT_EMPRESA))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO))
            .andExpect(jsonPath("$.segmento").value(DEFAULT_SEGMENTO))
            .andExpect(jsonPath("$.porte").value(DEFAULT_PORTE))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.fim").value(DEFAULT_FIM.toString()))
            .andExpect(jsonPath("$.descricaoAtividade").value(DEFAULT_DESCRICAO_ATIVIDADE.toString()))
            .andExpect(jsonPath("$.salario").value(DEFAULT_SALARIO.doubleValue()))
            .andExpect(jsonPath("$.beneficios").value(DEFAULT_BENEFICIOS.doubleValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getExperienciaProfissionalsByIdFiltering() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        Long id = experienciaProfissional.getId();

        defaultExperienciaProfissionalShouldBeFound("id.equals=" + id);
        defaultExperienciaProfissionalShouldNotBeFound("id.notEquals=" + id);

        defaultExperienciaProfissionalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExperienciaProfissionalShouldNotBeFound("id.greaterThan=" + id);

        defaultExperienciaProfissionalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExperienciaProfissionalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where empresa equals to DEFAULT_EMPRESA
        defaultExperienciaProfissionalShouldBeFound("empresa.equals=" + DEFAULT_EMPRESA);

        // Get all the experienciaProfissionalList where empresa equals to UPDATED_EMPRESA
        defaultExperienciaProfissionalShouldNotBeFound("empresa.equals=" + UPDATED_EMPRESA);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByEmpresaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where empresa not equals to DEFAULT_EMPRESA
        defaultExperienciaProfissionalShouldNotBeFound("empresa.notEquals=" + DEFAULT_EMPRESA);

        // Get all the experienciaProfissionalList where empresa not equals to UPDATED_EMPRESA
        defaultExperienciaProfissionalShouldBeFound("empresa.notEquals=" + UPDATED_EMPRESA);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByEmpresaIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where empresa in DEFAULT_EMPRESA or UPDATED_EMPRESA
        defaultExperienciaProfissionalShouldBeFound("empresa.in=" + DEFAULT_EMPRESA + "," + UPDATED_EMPRESA);

        // Get all the experienciaProfissionalList where empresa equals to UPDATED_EMPRESA
        defaultExperienciaProfissionalShouldNotBeFound("empresa.in=" + UPDATED_EMPRESA);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByEmpresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where empresa is not null
        defaultExperienciaProfissionalShouldBeFound("empresa.specified=true");

        // Get all the experienciaProfissionalList where empresa is null
        defaultExperienciaProfissionalShouldNotBeFound("empresa.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByEmpresaContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where empresa contains DEFAULT_EMPRESA
        defaultExperienciaProfissionalShouldBeFound("empresa.contains=" + DEFAULT_EMPRESA);

        // Get all the experienciaProfissionalList where empresa contains UPDATED_EMPRESA
        defaultExperienciaProfissionalShouldNotBeFound("empresa.contains=" + UPDATED_EMPRESA);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByEmpresaNotContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where empresa does not contain DEFAULT_EMPRESA
        defaultExperienciaProfissionalShouldNotBeFound("empresa.doesNotContain=" + DEFAULT_EMPRESA);

        // Get all the experienciaProfissionalList where empresa does not contain UPDATED_EMPRESA
        defaultExperienciaProfissionalShouldBeFound("empresa.doesNotContain=" + UPDATED_EMPRESA);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCargoIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where cargo equals to DEFAULT_CARGO
        defaultExperienciaProfissionalShouldBeFound("cargo.equals=" + DEFAULT_CARGO);

        // Get all the experienciaProfissionalList where cargo equals to UPDATED_CARGO
        defaultExperienciaProfissionalShouldNotBeFound("cargo.equals=" + UPDATED_CARGO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCargoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where cargo not equals to DEFAULT_CARGO
        defaultExperienciaProfissionalShouldNotBeFound("cargo.notEquals=" + DEFAULT_CARGO);

        // Get all the experienciaProfissionalList where cargo not equals to UPDATED_CARGO
        defaultExperienciaProfissionalShouldBeFound("cargo.notEquals=" + UPDATED_CARGO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCargoIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where cargo in DEFAULT_CARGO or UPDATED_CARGO
        defaultExperienciaProfissionalShouldBeFound("cargo.in=" + DEFAULT_CARGO + "," + UPDATED_CARGO);

        // Get all the experienciaProfissionalList where cargo equals to UPDATED_CARGO
        defaultExperienciaProfissionalShouldNotBeFound("cargo.in=" + UPDATED_CARGO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCargoIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where cargo is not null
        defaultExperienciaProfissionalShouldBeFound("cargo.specified=true");

        // Get all the experienciaProfissionalList where cargo is null
        defaultExperienciaProfissionalShouldNotBeFound("cargo.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCargoContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where cargo contains DEFAULT_CARGO
        defaultExperienciaProfissionalShouldBeFound("cargo.contains=" + DEFAULT_CARGO);

        // Get all the experienciaProfissionalList where cargo contains UPDATED_CARGO
        defaultExperienciaProfissionalShouldNotBeFound("cargo.contains=" + UPDATED_CARGO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCargoNotContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where cargo does not contain DEFAULT_CARGO
        defaultExperienciaProfissionalShouldNotBeFound("cargo.doesNotContain=" + DEFAULT_CARGO);

        // Get all the experienciaProfissionalList where cargo does not contain UPDATED_CARGO
        defaultExperienciaProfissionalShouldBeFound("cargo.doesNotContain=" + UPDATED_CARGO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySegmentoIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where segmento equals to DEFAULT_SEGMENTO
        defaultExperienciaProfissionalShouldBeFound("segmento.equals=" + DEFAULT_SEGMENTO);

        // Get all the experienciaProfissionalList where segmento equals to UPDATED_SEGMENTO
        defaultExperienciaProfissionalShouldNotBeFound("segmento.equals=" + UPDATED_SEGMENTO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySegmentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where segmento not equals to DEFAULT_SEGMENTO
        defaultExperienciaProfissionalShouldNotBeFound("segmento.notEquals=" + DEFAULT_SEGMENTO);

        // Get all the experienciaProfissionalList where segmento not equals to UPDATED_SEGMENTO
        defaultExperienciaProfissionalShouldBeFound("segmento.notEquals=" + UPDATED_SEGMENTO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySegmentoIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where segmento in DEFAULT_SEGMENTO or UPDATED_SEGMENTO
        defaultExperienciaProfissionalShouldBeFound("segmento.in=" + DEFAULT_SEGMENTO + "," + UPDATED_SEGMENTO);

        // Get all the experienciaProfissionalList where segmento equals to UPDATED_SEGMENTO
        defaultExperienciaProfissionalShouldNotBeFound("segmento.in=" + UPDATED_SEGMENTO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySegmentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where segmento is not null
        defaultExperienciaProfissionalShouldBeFound("segmento.specified=true");

        // Get all the experienciaProfissionalList where segmento is null
        defaultExperienciaProfissionalShouldNotBeFound("segmento.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySegmentoContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where segmento contains DEFAULT_SEGMENTO
        defaultExperienciaProfissionalShouldBeFound("segmento.contains=" + DEFAULT_SEGMENTO);

        // Get all the experienciaProfissionalList where segmento contains UPDATED_SEGMENTO
        defaultExperienciaProfissionalShouldNotBeFound("segmento.contains=" + UPDATED_SEGMENTO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySegmentoNotContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where segmento does not contain DEFAULT_SEGMENTO
        defaultExperienciaProfissionalShouldNotBeFound("segmento.doesNotContain=" + DEFAULT_SEGMENTO);

        // Get all the experienciaProfissionalList where segmento does not contain UPDATED_SEGMENTO
        defaultExperienciaProfissionalShouldBeFound("segmento.doesNotContain=" + UPDATED_SEGMENTO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByPorteIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where porte equals to DEFAULT_PORTE
        defaultExperienciaProfissionalShouldBeFound("porte.equals=" + DEFAULT_PORTE);

        // Get all the experienciaProfissionalList where porte equals to UPDATED_PORTE
        defaultExperienciaProfissionalShouldNotBeFound("porte.equals=" + UPDATED_PORTE);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByPorteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where porte not equals to DEFAULT_PORTE
        defaultExperienciaProfissionalShouldNotBeFound("porte.notEquals=" + DEFAULT_PORTE);

        // Get all the experienciaProfissionalList where porte not equals to UPDATED_PORTE
        defaultExperienciaProfissionalShouldBeFound("porte.notEquals=" + UPDATED_PORTE);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByPorteIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where porte in DEFAULT_PORTE or UPDATED_PORTE
        defaultExperienciaProfissionalShouldBeFound("porte.in=" + DEFAULT_PORTE + "," + UPDATED_PORTE);

        // Get all the experienciaProfissionalList where porte equals to UPDATED_PORTE
        defaultExperienciaProfissionalShouldNotBeFound("porte.in=" + UPDATED_PORTE);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByPorteIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where porte is not null
        defaultExperienciaProfissionalShouldBeFound("porte.specified=true");

        // Get all the experienciaProfissionalList where porte is null
        defaultExperienciaProfissionalShouldNotBeFound("porte.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByPorteContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where porte contains DEFAULT_PORTE
        defaultExperienciaProfissionalShouldBeFound("porte.contains=" + DEFAULT_PORTE);

        // Get all the experienciaProfissionalList where porte contains UPDATED_PORTE
        defaultExperienciaProfissionalShouldNotBeFound("porte.contains=" + UPDATED_PORTE);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByPorteNotContainsSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where porte does not contain DEFAULT_PORTE
        defaultExperienciaProfissionalShouldNotBeFound("porte.doesNotContain=" + DEFAULT_PORTE);

        // Get all the experienciaProfissionalList where porte does not contain UPDATED_PORTE
        defaultExperienciaProfissionalShouldBeFound("porte.doesNotContain=" + UPDATED_PORTE);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio equals to DEFAULT_INICIO
        defaultExperienciaProfissionalShouldBeFound("inicio.equals=" + DEFAULT_INICIO);

        // Get all the experienciaProfissionalList where inicio equals to UPDATED_INICIO
        defaultExperienciaProfissionalShouldNotBeFound("inicio.equals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio not equals to DEFAULT_INICIO
        defaultExperienciaProfissionalShouldNotBeFound("inicio.notEquals=" + DEFAULT_INICIO);

        // Get all the experienciaProfissionalList where inicio not equals to UPDATED_INICIO
        defaultExperienciaProfissionalShouldBeFound("inicio.notEquals=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio in DEFAULT_INICIO or UPDATED_INICIO
        defaultExperienciaProfissionalShouldBeFound("inicio.in=" + DEFAULT_INICIO + "," + UPDATED_INICIO);

        // Get all the experienciaProfissionalList where inicio equals to UPDATED_INICIO
        defaultExperienciaProfissionalShouldNotBeFound("inicio.in=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio is not null
        defaultExperienciaProfissionalShouldBeFound("inicio.specified=true");

        // Get all the experienciaProfissionalList where inicio is null
        defaultExperienciaProfissionalShouldNotBeFound("inicio.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio is greater than or equal to DEFAULT_INICIO
        defaultExperienciaProfissionalShouldBeFound("inicio.greaterThanOrEqual=" + DEFAULT_INICIO);

        // Get all the experienciaProfissionalList where inicio is greater than or equal to UPDATED_INICIO
        defaultExperienciaProfissionalShouldNotBeFound("inicio.greaterThanOrEqual=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio is less than or equal to DEFAULT_INICIO
        defaultExperienciaProfissionalShouldBeFound("inicio.lessThanOrEqual=" + DEFAULT_INICIO);

        // Get all the experienciaProfissionalList where inicio is less than or equal to SMALLER_INICIO
        defaultExperienciaProfissionalShouldNotBeFound("inicio.lessThanOrEqual=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio is less than DEFAULT_INICIO
        defaultExperienciaProfissionalShouldNotBeFound("inicio.lessThan=" + DEFAULT_INICIO);

        // Get all the experienciaProfissionalList where inicio is less than UPDATED_INICIO
        defaultExperienciaProfissionalShouldBeFound("inicio.lessThan=" + UPDATED_INICIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where inicio is greater than DEFAULT_INICIO
        defaultExperienciaProfissionalShouldNotBeFound("inicio.greaterThan=" + DEFAULT_INICIO);

        // Get all the experienciaProfissionalList where inicio is greater than SMALLER_INICIO
        defaultExperienciaProfissionalShouldBeFound("inicio.greaterThan=" + SMALLER_INICIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim equals to DEFAULT_FIM
        defaultExperienciaProfissionalShouldBeFound("fim.equals=" + DEFAULT_FIM);

        // Get all the experienciaProfissionalList where fim equals to UPDATED_FIM
        defaultExperienciaProfissionalShouldNotBeFound("fim.equals=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim not equals to DEFAULT_FIM
        defaultExperienciaProfissionalShouldNotBeFound("fim.notEquals=" + DEFAULT_FIM);

        // Get all the experienciaProfissionalList where fim not equals to UPDATED_FIM
        defaultExperienciaProfissionalShouldBeFound("fim.notEquals=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim in DEFAULT_FIM or UPDATED_FIM
        defaultExperienciaProfissionalShouldBeFound("fim.in=" + DEFAULT_FIM + "," + UPDATED_FIM);

        // Get all the experienciaProfissionalList where fim equals to UPDATED_FIM
        defaultExperienciaProfissionalShouldNotBeFound("fim.in=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim is not null
        defaultExperienciaProfissionalShouldBeFound("fim.specified=true");

        // Get all the experienciaProfissionalList where fim is null
        defaultExperienciaProfissionalShouldNotBeFound("fim.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim is greater than or equal to DEFAULT_FIM
        defaultExperienciaProfissionalShouldBeFound("fim.greaterThanOrEqual=" + DEFAULT_FIM);

        // Get all the experienciaProfissionalList where fim is greater than or equal to UPDATED_FIM
        defaultExperienciaProfissionalShouldNotBeFound("fim.greaterThanOrEqual=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim is less than or equal to DEFAULT_FIM
        defaultExperienciaProfissionalShouldBeFound("fim.lessThanOrEqual=" + DEFAULT_FIM);

        // Get all the experienciaProfissionalList where fim is less than or equal to SMALLER_FIM
        defaultExperienciaProfissionalShouldNotBeFound("fim.lessThanOrEqual=" + SMALLER_FIM);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsLessThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim is less than DEFAULT_FIM
        defaultExperienciaProfissionalShouldNotBeFound("fim.lessThan=" + DEFAULT_FIM);

        // Get all the experienciaProfissionalList where fim is less than UPDATED_FIM
        defaultExperienciaProfissionalShouldBeFound("fim.lessThan=" + UPDATED_FIM);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByFimIsGreaterThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where fim is greater than DEFAULT_FIM
        defaultExperienciaProfissionalShouldNotBeFound("fim.greaterThan=" + DEFAULT_FIM);

        // Get all the experienciaProfissionalList where fim is greater than SMALLER_FIM
        defaultExperienciaProfissionalShouldBeFound("fim.greaterThan=" + SMALLER_FIM);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario equals to DEFAULT_SALARIO
        defaultExperienciaProfissionalShouldBeFound("salario.equals=" + DEFAULT_SALARIO);

        // Get all the experienciaProfissionalList where salario equals to UPDATED_SALARIO
        defaultExperienciaProfissionalShouldNotBeFound("salario.equals=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario not equals to DEFAULT_SALARIO
        defaultExperienciaProfissionalShouldNotBeFound("salario.notEquals=" + DEFAULT_SALARIO);

        // Get all the experienciaProfissionalList where salario not equals to UPDATED_SALARIO
        defaultExperienciaProfissionalShouldBeFound("salario.notEquals=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario in DEFAULT_SALARIO or UPDATED_SALARIO
        defaultExperienciaProfissionalShouldBeFound("salario.in=" + DEFAULT_SALARIO + "," + UPDATED_SALARIO);

        // Get all the experienciaProfissionalList where salario equals to UPDATED_SALARIO
        defaultExperienciaProfissionalShouldNotBeFound("salario.in=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario is not null
        defaultExperienciaProfissionalShouldBeFound("salario.specified=true");

        // Get all the experienciaProfissionalList where salario is null
        defaultExperienciaProfissionalShouldNotBeFound("salario.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario is greater than or equal to DEFAULT_SALARIO
        defaultExperienciaProfissionalShouldBeFound("salario.greaterThanOrEqual=" + DEFAULT_SALARIO);

        // Get all the experienciaProfissionalList where salario is greater than or equal to UPDATED_SALARIO
        defaultExperienciaProfissionalShouldNotBeFound("salario.greaterThanOrEqual=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario is less than or equal to DEFAULT_SALARIO
        defaultExperienciaProfissionalShouldBeFound("salario.lessThanOrEqual=" + DEFAULT_SALARIO);

        // Get all the experienciaProfissionalList where salario is less than or equal to SMALLER_SALARIO
        defaultExperienciaProfissionalShouldNotBeFound("salario.lessThanOrEqual=" + SMALLER_SALARIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsLessThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario is less than DEFAULT_SALARIO
        defaultExperienciaProfissionalShouldNotBeFound("salario.lessThan=" + DEFAULT_SALARIO);

        // Get all the experienciaProfissionalList where salario is less than UPDATED_SALARIO
        defaultExperienciaProfissionalShouldBeFound("salario.lessThan=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsBySalarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where salario is greater than DEFAULT_SALARIO
        defaultExperienciaProfissionalShouldNotBeFound("salario.greaterThan=" + DEFAULT_SALARIO);

        // Get all the experienciaProfissionalList where salario is greater than SMALLER_SALARIO
        defaultExperienciaProfissionalShouldBeFound("salario.greaterThan=" + SMALLER_SALARIO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios equals to DEFAULT_BENEFICIOS
        defaultExperienciaProfissionalShouldBeFound("beneficios.equals=" + DEFAULT_BENEFICIOS);

        // Get all the experienciaProfissionalList where beneficios equals to UPDATED_BENEFICIOS
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.equals=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios not equals to DEFAULT_BENEFICIOS
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.notEquals=" + DEFAULT_BENEFICIOS);

        // Get all the experienciaProfissionalList where beneficios not equals to UPDATED_BENEFICIOS
        defaultExperienciaProfissionalShouldBeFound("beneficios.notEquals=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios in DEFAULT_BENEFICIOS or UPDATED_BENEFICIOS
        defaultExperienciaProfissionalShouldBeFound("beneficios.in=" + DEFAULT_BENEFICIOS + "," + UPDATED_BENEFICIOS);

        // Get all the experienciaProfissionalList where beneficios equals to UPDATED_BENEFICIOS
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.in=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios is not null
        defaultExperienciaProfissionalShouldBeFound("beneficios.specified=true");

        // Get all the experienciaProfissionalList where beneficios is null
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios is greater than or equal to DEFAULT_BENEFICIOS
        defaultExperienciaProfissionalShouldBeFound("beneficios.greaterThanOrEqual=" + DEFAULT_BENEFICIOS);

        // Get all the experienciaProfissionalList where beneficios is greater than or equal to UPDATED_BENEFICIOS
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.greaterThanOrEqual=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios is less than or equal to DEFAULT_BENEFICIOS
        defaultExperienciaProfissionalShouldBeFound("beneficios.lessThanOrEqual=" + DEFAULT_BENEFICIOS);

        // Get all the experienciaProfissionalList where beneficios is less than or equal to SMALLER_BENEFICIOS
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.lessThanOrEqual=" + SMALLER_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsLessThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios is less than DEFAULT_BENEFICIOS
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.lessThan=" + DEFAULT_BENEFICIOS);

        // Get all the experienciaProfissionalList where beneficios is less than UPDATED_BENEFICIOS
        defaultExperienciaProfissionalShouldBeFound("beneficios.lessThan=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByBeneficiosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where beneficios is greater than DEFAULT_BENEFICIOS
        defaultExperienciaProfissionalShouldNotBeFound("beneficios.greaterThan=" + DEFAULT_BENEFICIOS);

        // Get all the experienciaProfissionalList where beneficios is greater than SMALLER_BENEFICIOS
        defaultExperienciaProfissionalShouldBeFound("beneficios.greaterThan=" + SMALLER_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado equals to DEFAULT_CRIADO
        defaultExperienciaProfissionalShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the experienciaProfissionalList where criado equals to UPDATED_CRIADO
        defaultExperienciaProfissionalShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado not equals to DEFAULT_CRIADO
        defaultExperienciaProfissionalShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the experienciaProfissionalList where criado not equals to UPDATED_CRIADO
        defaultExperienciaProfissionalShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultExperienciaProfissionalShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the experienciaProfissionalList where criado equals to UPDATED_CRIADO
        defaultExperienciaProfissionalShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado is not null
        defaultExperienciaProfissionalShouldBeFound("criado.specified=true");

        // Get all the experienciaProfissionalList where criado is null
        defaultExperienciaProfissionalShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado is greater than or equal to DEFAULT_CRIADO
        defaultExperienciaProfissionalShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the experienciaProfissionalList where criado is greater than or equal to UPDATED_CRIADO
        defaultExperienciaProfissionalShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado is less than or equal to DEFAULT_CRIADO
        defaultExperienciaProfissionalShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the experienciaProfissionalList where criado is less than or equal to SMALLER_CRIADO
        defaultExperienciaProfissionalShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado is less than DEFAULT_CRIADO
        defaultExperienciaProfissionalShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the experienciaProfissionalList where criado is less than UPDATED_CRIADO
        defaultExperienciaProfissionalShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        // Get all the experienciaProfissionalList where criado is greater than DEFAULT_CRIADO
        defaultExperienciaProfissionalShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the experienciaProfissionalList where criado is greater than SMALLER_CRIADO
        defaultExperienciaProfissionalShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllExperienciaProfissionalsByPessoaIsEqualToSomething() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);
        Pessoa pessoa = PessoaResourceIT.createEntity(em);
        em.persist(pessoa);
        em.flush();
        experienciaProfissional.setPessoa(pessoa);
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);
        Long pessoaId = pessoa.getId();

        // Get all the experienciaProfissionalList where pessoa equals to pessoaId
        defaultExperienciaProfissionalShouldBeFound("pessoaId.equals=" + pessoaId);

        // Get all the experienciaProfissionalList where pessoa equals to (pessoaId + 1)
        defaultExperienciaProfissionalShouldNotBeFound("pessoaId.equals=" + (pessoaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExperienciaProfissionalShouldBeFound(String filter) throws Exception {
        restExperienciaProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experienciaProfissional.getId().intValue())))
            .andExpect(jsonPath("$.[*].empresa").value(hasItem(DEFAULT_EMPRESA)))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO)))
            .andExpect(jsonPath("$.[*].segmento").value(hasItem(DEFAULT_SEGMENTO)))
            .andExpect(jsonPath("$.[*].porte").value(hasItem(DEFAULT_PORTE)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fim").value(hasItem(DEFAULT_FIM.toString())))
            .andExpect(jsonPath("$.[*].descricaoAtividade").value(hasItem(DEFAULT_DESCRICAO_ATIVIDADE.toString())))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.doubleValue())))
            .andExpect(jsonPath("$.[*].beneficios").value(hasItem(DEFAULT_BENEFICIOS.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restExperienciaProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExperienciaProfissionalShouldNotBeFound(String filter) throws Exception {
        restExperienciaProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExperienciaProfissionalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExperienciaProfissional() throws Exception {
        // Get the experienciaProfissional
        restExperienciaProfissionalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExperienciaProfissional() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();

        // Update the experienciaProfissional
        ExperienciaProfissional updatedExperienciaProfissional = experienciaProfissionalRepository
            .findById(experienciaProfissional.getId())
            .get();
        // Disconnect from session so that the updates on updatedExperienciaProfissional are not directly saved in db
        em.detach(updatedExperienciaProfissional);
        updatedExperienciaProfissional
            .empresa(UPDATED_EMPRESA)
            .cargo(UPDATED_CARGO)
            .segmento(UPDATED_SEGMENTO)
            .porte(UPDATED_PORTE)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .descricaoAtividade(UPDATED_DESCRICAO_ATIVIDADE)
            .salario(UPDATED_SALARIO)
            .beneficios(UPDATED_BENEFICIOS)
            .criado(UPDATED_CRIADO);
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(updatedExperienciaProfissional);

        restExperienciaProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, experienciaProfissionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
        ExperienciaProfissional testExperienciaProfissional = experienciaProfissionalList.get(experienciaProfissionalList.size() - 1);
        assertThat(testExperienciaProfissional.getEmpresa()).isEqualTo(UPDATED_EMPRESA);
        assertThat(testExperienciaProfissional.getCargo()).isEqualTo(UPDATED_CARGO);
        assertThat(testExperienciaProfissional.getSegmento()).isEqualTo(UPDATED_SEGMENTO);
        assertThat(testExperienciaProfissional.getPorte()).isEqualTo(UPDATED_PORTE);
        assertThat(testExperienciaProfissional.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testExperienciaProfissional.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testExperienciaProfissional.getDescricaoAtividade()).isEqualTo(UPDATED_DESCRICAO_ATIVIDADE);
        assertThat(testExperienciaProfissional.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testExperienciaProfissional.getBeneficios()).isEqualTo(UPDATED_BENEFICIOS);
        assertThat(testExperienciaProfissional.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingExperienciaProfissional() throws Exception {
        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();
        experienciaProfissional.setId(count.incrementAndGet());

        // Create the ExperienciaProfissional
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienciaProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, experienciaProfissionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExperienciaProfissional() throws Exception {
        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();
        experienciaProfissional.setId(count.incrementAndGet());

        // Create the ExperienciaProfissional
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExperienciaProfissional() throws Exception {
        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();
        experienciaProfissional.setId(count.incrementAndGet());

        // Create the ExperienciaProfissional
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaProfissionalMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExperienciaProfissionalWithPatch() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();

        // Update the experienciaProfissional using partial update
        ExperienciaProfissional partialUpdatedExperienciaProfissional = new ExperienciaProfissional();
        partialUpdatedExperienciaProfissional.setId(experienciaProfissional.getId());

        partialUpdatedExperienciaProfissional.inicio(UPDATED_INICIO).fim(UPDATED_FIM).criado(UPDATED_CRIADO);

        restExperienciaProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperienciaProfissional.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperienciaProfissional))
            )
            .andExpect(status().isOk());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
        ExperienciaProfissional testExperienciaProfissional = experienciaProfissionalList.get(experienciaProfissionalList.size() - 1);
        assertThat(testExperienciaProfissional.getEmpresa()).isEqualTo(DEFAULT_EMPRESA);
        assertThat(testExperienciaProfissional.getCargo()).isEqualTo(DEFAULT_CARGO);
        assertThat(testExperienciaProfissional.getSegmento()).isEqualTo(DEFAULT_SEGMENTO);
        assertThat(testExperienciaProfissional.getPorte()).isEqualTo(DEFAULT_PORTE);
        assertThat(testExperienciaProfissional.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testExperienciaProfissional.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testExperienciaProfissional.getDescricaoAtividade()).isEqualTo(DEFAULT_DESCRICAO_ATIVIDADE);
        assertThat(testExperienciaProfissional.getSalario()).isEqualTo(DEFAULT_SALARIO);
        assertThat(testExperienciaProfissional.getBeneficios()).isEqualTo(DEFAULT_BENEFICIOS);
        assertThat(testExperienciaProfissional.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateExperienciaProfissionalWithPatch() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();

        // Update the experienciaProfissional using partial update
        ExperienciaProfissional partialUpdatedExperienciaProfissional = new ExperienciaProfissional();
        partialUpdatedExperienciaProfissional.setId(experienciaProfissional.getId());

        partialUpdatedExperienciaProfissional
            .empresa(UPDATED_EMPRESA)
            .cargo(UPDATED_CARGO)
            .segmento(UPDATED_SEGMENTO)
            .porte(UPDATED_PORTE)
            .inicio(UPDATED_INICIO)
            .fim(UPDATED_FIM)
            .descricaoAtividade(UPDATED_DESCRICAO_ATIVIDADE)
            .salario(UPDATED_SALARIO)
            .beneficios(UPDATED_BENEFICIOS)
            .criado(UPDATED_CRIADO);

        restExperienciaProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExperienciaProfissional.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExperienciaProfissional))
            )
            .andExpect(status().isOk());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
        ExperienciaProfissional testExperienciaProfissional = experienciaProfissionalList.get(experienciaProfissionalList.size() - 1);
        assertThat(testExperienciaProfissional.getEmpresa()).isEqualTo(UPDATED_EMPRESA);
        assertThat(testExperienciaProfissional.getCargo()).isEqualTo(UPDATED_CARGO);
        assertThat(testExperienciaProfissional.getSegmento()).isEqualTo(UPDATED_SEGMENTO);
        assertThat(testExperienciaProfissional.getPorte()).isEqualTo(UPDATED_PORTE);
        assertThat(testExperienciaProfissional.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testExperienciaProfissional.getFim()).isEqualTo(UPDATED_FIM);
        assertThat(testExperienciaProfissional.getDescricaoAtividade()).isEqualTo(UPDATED_DESCRICAO_ATIVIDADE);
        assertThat(testExperienciaProfissional.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testExperienciaProfissional.getBeneficios()).isEqualTo(UPDATED_BENEFICIOS);
        assertThat(testExperienciaProfissional.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingExperienciaProfissional() throws Exception {
        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();
        experienciaProfissional.setId(count.incrementAndGet());

        // Create the ExperienciaProfissional
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienciaProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, experienciaProfissionalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExperienciaProfissional() throws Exception {
        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();
        experienciaProfissional.setId(count.incrementAndGet());

        // Create the ExperienciaProfissional
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExperienciaProfissional() throws Exception {
        int databaseSizeBeforeUpdate = experienciaProfissionalRepository.findAll().size();
        experienciaProfissional.setId(count.incrementAndGet());

        // Create the ExperienciaProfissional
        ExperienciaProfissionalDTO experienciaProfissionalDTO = experienciaProfissionalMapper.toDto(experienciaProfissional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExperienciaProfissionalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(experienciaProfissionalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExperienciaProfissional in the database
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExperienciaProfissional() throws Exception {
        // Initialize the database
        experienciaProfissionalRepository.saveAndFlush(experienciaProfissional);

        int databaseSizeBeforeDelete = experienciaProfissionalRepository.findAll().size();

        // Delete the experienciaProfissional
        restExperienciaProfissionalMockMvc
            .perform(delete(ENTITY_API_URL_ID, experienciaProfissional.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExperienciaProfissional> experienciaProfissionalList = experienciaProfissionalRepository.findAll();
        assertThat(experienciaProfissionalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
