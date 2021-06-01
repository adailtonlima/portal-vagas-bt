package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.Empresa;
import br.com.vagas.domain.Pessoa;
import br.com.vagas.domain.Vaga;
import br.com.vagas.repository.VagaRepository;
import br.com.vagas.service.criteria.VagaCriteria;
import br.com.vagas.service.dto.VagaDTO;
import br.com.vagas.service.mapper.VagaMapper;
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
 * Integration tests for the {@link VagaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VagaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ESTAGIO = false;
    private static final Boolean UPDATED_ESTAGIO = true;

    private static final Double DEFAULT_SALARIO = 1D;
    private static final Double UPDATED_SALARIO = 2D;
    private static final Double SMALLER_SALARIO = 1D - 1D;

    private static final Double DEFAULT_BENEFICIOS = 1D;
    private static final Double UPDATED_BENEFICIOS = 2D;
    private static final Double SMALLER_BENEFICIOS = 1D - 1D;

    private static final Double DEFAULT_JORNADA_SEMANAL = 1D;
    private static final Double UPDATED_JORNADA_SEMANAL = 2D;
    private static final Double SMALLER_JORNADA_SEMANAL = 1D - 1D;

    private static final String DEFAULT_BANNER_URL = "AAAAAAAAAA";
    private static final String UPDATED_BANNER_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FONTE = "AAAAAAAAAA";
    private static final String UPDATED_FONTE = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_RECRUTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_LINK_RECRUTAMENTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Boolean DEFAULT_PREENCHIDA = false;
    private static final Boolean UPDATED_PREENCHIDA = true;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_PRAZO_ANUNCIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PRAZO_ANUNCIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PRAZO_ANUNCIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/vagas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private VagaMapper vagaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVagaMockMvc;

    private Vaga vaga;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaga createEntity(EntityManager em) {
        Vaga vaga = new Vaga()
            .descricao(DEFAULT_DESCRICAO)
            .titulo(DEFAULT_TITULO)
            .estagio(DEFAULT_ESTAGIO)
            .salario(DEFAULT_SALARIO)
            .beneficios(DEFAULT_BENEFICIOS)
            .jornadaSemanal(DEFAULT_JORNADA_SEMANAL)
            .bannerUrl(DEFAULT_BANNER_URL)
            .fonte(DEFAULT_FONTE)
            .linkRecrutamento(DEFAULT_LINK_RECRUTAMENTO)
            .ativo(DEFAULT_ATIVO)
            .preenchida(DEFAULT_PREENCHIDA)
            .criado(DEFAULT_CRIADO)
            .prazoAnuncio(DEFAULT_PRAZO_ANUNCIO);
        return vaga;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaga createUpdatedEntity(EntityManager em) {
        Vaga vaga = new Vaga()
            .descricao(UPDATED_DESCRICAO)
            .titulo(UPDATED_TITULO)
            .estagio(UPDATED_ESTAGIO)
            .salario(UPDATED_SALARIO)
            .beneficios(UPDATED_BENEFICIOS)
            .jornadaSemanal(UPDATED_JORNADA_SEMANAL)
            .bannerUrl(UPDATED_BANNER_URL)
            .fonte(UPDATED_FONTE)
            .linkRecrutamento(UPDATED_LINK_RECRUTAMENTO)
            .ativo(UPDATED_ATIVO)
            .preenchida(UPDATED_PREENCHIDA)
            .criado(UPDATED_CRIADO)
            .prazoAnuncio(UPDATED_PRAZO_ANUNCIO);
        return vaga;
    }

    @BeforeEach
    public void initTest() {
        vaga = createEntity(em);
    }

    @Test
    @Transactional
    void createVaga() throws Exception {
        int databaseSizeBeforeCreate = vagaRepository.findAll().size();
        // Create the Vaga
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);
        restVagaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vagaDTO)))
            .andExpect(status().isCreated());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeCreate + 1);
        Vaga testVaga = vagaList.get(vagaList.size() - 1);
        assertThat(testVaga.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testVaga.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testVaga.getEstagio()).isEqualTo(DEFAULT_ESTAGIO);
        assertThat(testVaga.getSalario()).isEqualTo(DEFAULT_SALARIO);
        assertThat(testVaga.getBeneficios()).isEqualTo(DEFAULT_BENEFICIOS);
        assertThat(testVaga.getJornadaSemanal()).isEqualTo(DEFAULT_JORNADA_SEMANAL);
        assertThat(testVaga.getBannerUrl()).isEqualTo(DEFAULT_BANNER_URL);
        assertThat(testVaga.getFonte()).isEqualTo(DEFAULT_FONTE);
        assertThat(testVaga.getLinkRecrutamento()).isEqualTo(DEFAULT_LINK_RECRUTAMENTO);
        assertThat(testVaga.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testVaga.getPreenchida()).isEqualTo(DEFAULT_PREENCHIDA);
        assertThat(testVaga.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testVaga.getPrazoAnuncio()).isEqualTo(DEFAULT_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void createVagaWithExistingId() throws Exception {
        // Create the Vaga with an existing ID
        vaga.setId(1L);
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        int databaseSizeBeforeCreate = vagaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVagaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vagaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = vagaRepository.findAll().size();
        // set the field null
        vaga.setTitulo(null);

        // Create the Vaga, which fails.
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        restVagaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vagaDTO)))
            .andExpect(status().isBadRequest());

        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vagaRepository.findAll().size();
        // set the field null
        vaga.setCriado(null);

        // Create the Vaga, which fails.
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        restVagaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vagaDTO)))
            .andExpect(status().isBadRequest());

        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVagas() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList
        restVagaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaga.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].estagio").value(hasItem(DEFAULT_ESTAGIO.booleanValue())))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.doubleValue())))
            .andExpect(jsonPath("$.[*].beneficios").value(hasItem(DEFAULT_BENEFICIOS.doubleValue())))
            .andExpect(jsonPath("$.[*].jornadaSemanal").value(hasItem(DEFAULT_JORNADA_SEMANAL.doubleValue())))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)))
            .andExpect(jsonPath("$.[*].fonte").value(hasItem(DEFAULT_FONTE)))
            .andExpect(jsonPath("$.[*].linkRecrutamento").value(hasItem(DEFAULT_LINK_RECRUTAMENTO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].preenchida").value(hasItem(DEFAULT_PREENCHIDA.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].prazoAnuncio").value(hasItem(sameInstant(DEFAULT_PRAZO_ANUNCIO))));
    }

    @Test
    @Transactional
    void getVaga() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get the vaga
        restVagaMockMvc
            .perform(get(ENTITY_API_URL_ID, vaga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaga.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.estagio").value(DEFAULT_ESTAGIO.booleanValue()))
            .andExpect(jsonPath("$.salario").value(DEFAULT_SALARIO.doubleValue()))
            .andExpect(jsonPath("$.beneficios").value(DEFAULT_BENEFICIOS.doubleValue()))
            .andExpect(jsonPath("$.jornadaSemanal").value(DEFAULT_JORNADA_SEMANAL.doubleValue()))
            .andExpect(jsonPath("$.bannerUrl").value(DEFAULT_BANNER_URL))
            .andExpect(jsonPath("$.fonte").value(DEFAULT_FONTE))
            .andExpect(jsonPath("$.linkRecrutamento").value(DEFAULT_LINK_RECRUTAMENTO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.preenchida").value(DEFAULT_PREENCHIDA.booleanValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.prazoAnuncio").value(sameInstant(DEFAULT_PRAZO_ANUNCIO)));
    }

    @Test
    @Transactional
    void getVagasByIdFiltering() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        Long id = vaga.getId();

        defaultVagaShouldBeFound("id.equals=" + id);
        defaultVagaShouldNotBeFound("id.notEquals=" + id);

        defaultVagaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVagaShouldNotBeFound("id.greaterThan=" + id);

        defaultVagaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVagaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVagasByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where titulo equals to DEFAULT_TITULO
        defaultVagaShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the vagaList where titulo equals to UPDATED_TITULO
        defaultVagaShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllVagasByTituloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where titulo not equals to DEFAULT_TITULO
        defaultVagaShouldNotBeFound("titulo.notEquals=" + DEFAULT_TITULO);

        // Get all the vagaList where titulo not equals to UPDATED_TITULO
        defaultVagaShouldBeFound("titulo.notEquals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllVagasByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultVagaShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the vagaList where titulo equals to UPDATED_TITULO
        defaultVagaShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllVagasByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where titulo is not null
        defaultVagaShouldBeFound("titulo.specified=true");

        // Get all the vagaList where titulo is null
        defaultVagaShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByTituloContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where titulo contains DEFAULT_TITULO
        defaultVagaShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the vagaList where titulo contains UPDATED_TITULO
        defaultVagaShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllVagasByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where titulo does not contain DEFAULT_TITULO
        defaultVagaShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the vagaList where titulo does not contain UPDATED_TITULO
        defaultVagaShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllVagasByEstagioIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where estagio equals to DEFAULT_ESTAGIO
        defaultVagaShouldBeFound("estagio.equals=" + DEFAULT_ESTAGIO);

        // Get all the vagaList where estagio equals to UPDATED_ESTAGIO
        defaultVagaShouldNotBeFound("estagio.equals=" + UPDATED_ESTAGIO);
    }

    @Test
    @Transactional
    void getAllVagasByEstagioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where estagio not equals to DEFAULT_ESTAGIO
        defaultVagaShouldNotBeFound("estagio.notEquals=" + DEFAULT_ESTAGIO);

        // Get all the vagaList where estagio not equals to UPDATED_ESTAGIO
        defaultVagaShouldBeFound("estagio.notEquals=" + UPDATED_ESTAGIO);
    }

    @Test
    @Transactional
    void getAllVagasByEstagioIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where estagio in DEFAULT_ESTAGIO or UPDATED_ESTAGIO
        defaultVagaShouldBeFound("estagio.in=" + DEFAULT_ESTAGIO + "," + UPDATED_ESTAGIO);

        // Get all the vagaList where estagio equals to UPDATED_ESTAGIO
        defaultVagaShouldNotBeFound("estagio.in=" + UPDATED_ESTAGIO);
    }

    @Test
    @Transactional
    void getAllVagasByEstagioIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where estagio is not null
        defaultVagaShouldBeFound("estagio.specified=true");

        // Get all the vagaList where estagio is null
        defaultVagaShouldNotBeFound("estagio.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario equals to DEFAULT_SALARIO
        defaultVagaShouldBeFound("salario.equals=" + DEFAULT_SALARIO);

        // Get all the vagaList where salario equals to UPDATED_SALARIO
        defaultVagaShouldNotBeFound("salario.equals=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario not equals to DEFAULT_SALARIO
        defaultVagaShouldNotBeFound("salario.notEquals=" + DEFAULT_SALARIO);

        // Get all the vagaList where salario not equals to UPDATED_SALARIO
        defaultVagaShouldBeFound("salario.notEquals=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario in DEFAULT_SALARIO or UPDATED_SALARIO
        defaultVagaShouldBeFound("salario.in=" + DEFAULT_SALARIO + "," + UPDATED_SALARIO);

        // Get all the vagaList where salario equals to UPDATED_SALARIO
        defaultVagaShouldNotBeFound("salario.in=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario is not null
        defaultVagaShouldBeFound("salario.specified=true");

        // Get all the vagaList where salario is null
        defaultVagaShouldNotBeFound("salario.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario is greater than or equal to DEFAULT_SALARIO
        defaultVagaShouldBeFound("salario.greaterThanOrEqual=" + DEFAULT_SALARIO);

        // Get all the vagaList where salario is greater than or equal to UPDATED_SALARIO
        defaultVagaShouldNotBeFound("salario.greaterThanOrEqual=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario is less than or equal to DEFAULT_SALARIO
        defaultVagaShouldBeFound("salario.lessThanOrEqual=" + DEFAULT_SALARIO);

        // Get all the vagaList where salario is less than or equal to SMALLER_SALARIO
        defaultVagaShouldNotBeFound("salario.lessThanOrEqual=" + SMALLER_SALARIO);
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsLessThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario is less than DEFAULT_SALARIO
        defaultVagaShouldNotBeFound("salario.lessThan=" + DEFAULT_SALARIO);

        // Get all the vagaList where salario is less than UPDATED_SALARIO
        defaultVagaShouldBeFound("salario.lessThan=" + UPDATED_SALARIO);
    }

    @Test
    @Transactional
    void getAllVagasBySalarioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where salario is greater than DEFAULT_SALARIO
        defaultVagaShouldNotBeFound("salario.greaterThan=" + DEFAULT_SALARIO);

        // Get all the vagaList where salario is greater than SMALLER_SALARIO
        defaultVagaShouldBeFound("salario.greaterThan=" + SMALLER_SALARIO);
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios equals to DEFAULT_BENEFICIOS
        defaultVagaShouldBeFound("beneficios.equals=" + DEFAULT_BENEFICIOS);

        // Get all the vagaList where beneficios equals to UPDATED_BENEFICIOS
        defaultVagaShouldNotBeFound("beneficios.equals=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios not equals to DEFAULT_BENEFICIOS
        defaultVagaShouldNotBeFound("beneficios.notEquals=" + DEFAULT_BENEFICIOS);

        // Get all the vagaList where beneficios not equals to UPDATED_BENEFICIOS
        defaultVagaShouldBeFound("beneficios.notEquals=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios in DEFAULT_BENEFICIOS or UPDATED_BENEFICIOS
        defaultVagaShouldBeFound("beneficios.in=" + DEFAULT_BENEFICIOS + "," + UPDATED_BENEFICIOS);

        // Get all the vagaList where beneficios equals to UPDATED_BENEFICIOS
        defaultVagaShouldNotBeFound("beneficios.in=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios is not null
        defaultVagaShouldBeFound("beneficios.specified=true");

        // Get all the vagaList where beneficios is null
        defaultVagaShouldNotBeFound("beneficios.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios is greater than or equal to DEFAULT_BENEFICIOS
        defaultVagaShouldBeFound("beneficios.greaterThanOrEqual=" + DEFAULT_BENEFICIOS);

        // Get all the vagaList where beneficios is greater than or equal to UPDATED_BENEFICIOS
        defaultVagaShouldNotBeFound("beneficios.greaterThanOrEqual=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios is less than or equal to DEFAULT_BENEFICIOS
        defaultVagaShouldBeFound("beneficios.lessThanOrEqual=" + DEFAULT_BENEFICIOS);

        // Get all the vagaList where beneficios is less than or equal to SMALLER_BENEFICIOS
        defaultVagaShouldNotBeFound("beneficios.lessThanOrEqual=" + SMALLER_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsLessThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios is less than DEFAULT_BENEFICIOS
        defaultVagaShouldNotBeFound("beneficios.lessThan=" + DEFAULT_BENEFICIOS);

        // Get all the vagaList where beneficios is less than UPDATED_BENEFICIOS
        defaultVagaShouldBeFound("beneficios.lessThan=" + UPDATED_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllVagasByBeneficiosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where beneficios is greater than DEFAULT_BENEFICIOS
        defaultVagaShouldNotBeFound("beneficios.greaterThan=" + DEFAULT_BENEFICIOS);

        // Get all the vagaList where beneficios is greater than SMALLER_BENEFICIOS
        defaultVagaShouldBeFound("beneficios.greaterThan=" + SMALLER_BENEFICIOS);
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal equals to DEFAULT_JORNADA_SEMANAL
        defaultVagaShouldBeFound("jornadaSemanal.equals=" + DEFAULT_JORNADA_SEMANAL);

        // Get all the vagaList where jornadaSemanal equals to UPDATED_JORNADA_SEMANAL
        defaultVagaShouldNotBeFound("jornadaSemanal.equals=" + UPDATED_JORNADA_SEMANAL);
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal not equals to DEFAULT_JORNADA_SEMANAL
        defaultVagaShouldNotBeFound("jornadaSemanal.notEquals=" + DEFAULT_JORNADA_SEMANAL);

        // Get all the vagaList where jornadaSemanal not equals to UPDATED_JORNADA_SEMANAL
        defaultVagaShouldBeFound("jornadaSemanal.notEquals=" + UPDATED_JORNADA_SEMANAL);
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal in DEFAULT_JORNADA_SEMANAL or UPDATED_JORNADA_SEMANAL
        defaultVagaShouldBeFound("jornadaSemanal.in=" + DEFAULT_JORNADA_SEMANAL + "," + UPDATED_JORNADA_SEMANAL);

        // Get all the vagaList where jornadaSemanal equals to UPDATED_JORNADA_SEMANAL
        defaultVagaShouldNotBeFound("jornadaSemanal.in=" + UPDATED_JORNADA_SEMANAL);
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal is not null
        defaultVagaShouldBeFound("jornadaSemanal.specified=true");

        // Get all the vagaList where jornadaSemanal is null
        defaultVagaShouldNotBeFound("jornadaSemanal.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal is greater than or equal to DEFAULT_JORNADA_SEMANAL
        defaultVagaShouldBeFound("jornadaSemanal.greaterThanOrEqual=" + DEFAULT_JORNADA_SEMANAL);

        // Get all the vagaList where jornadaSemanal is greater than or equal to UPDATED_JORNADA_SEMANAL
        defaultVagaShouldNotBeFound("jornadaSemanal.greaterThanOrEqual=" + UPDATED_JORNADA_SEMANAL);
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal is less than or equal to DEFAULT_JORNADA_SEMANAL
        defaultVagaShouldBeFound("jornadaSemanal.lessThanOrEqual=" + DEFAULT_JORNADA_SEMANAL);

        // Get all the vagaList where jornadaSemanal is less than or equal to SMALLER_JORNADA_SEMANAL
        defaultVagaShouldNotBeFound("jornadaSemanal.lessThanOrEqual=" + SMALLER_JORNADA_SEMANAL);
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsLessThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal is less than DEFAULT_JORNADA_SEMANAL
        defaultVagaShouldNotBeFound("jornadaSemanal.lessThan=" + DEFAULT_JORNADA_SEMANAL);

        // Get all the vagaList where jornadaSemanal is less than UPDATED_JORNADA_SEMANAL
        defaultVagaShouldBeFound("jornadaSemanal.lessThan=" + UPDATED_JORNADA_SEMANAL);
    }

    @Test
    @Transactional
    void getAllVagasByJornadaSemanalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where jornadaSemanal is greater than DEFAULT_JORNADA_SEMANAL
        defaultVagaShouldNotBeFound("jornadaSemanal.greaterThan=" + DEFAULT_JORNADA_SEMANAL);

        // Get all the vagaList where jornadaSemanal is greater than SMALLER_JORNADA_SEMANAL
        defaultVagaShouldBeFound("jornadaSemanal.greaterThan=" + SMALLER_JORNADA_SEMANAL);
    }

    @Test
    @Transactional
    void getAllVagasByBannerUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where bannerUrl equals to DEFAULT_BANNER_URL
        defaultVagaShouldBeFound("bannerUrl.equals=" + DEFAULT_BANNER_URL);

        // Get all the vagaList where bannerUrl equals to UPDATED_BANNER_URL
        defaultVagaShouldNotBeFound("bannerUrl.equals=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllVagasByBannerUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where bannerUrl not equals to DEFAULT_BANNER_URL
        defaultVagaShouldNotBeFound("bannerUrl.notEquals=" + DEFAULT_BANNER_URL);

        // Get all the vagaList where bannerUrl not equals to UPDATED_BANNER_URL
        defaultVagaShouldBeFound("bannerUrl.notEquals=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllVagasByBannerUrlIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where bannerUrl in DEFAULT_BANNER_URL or UPDATED_BANNER_URL
        defaultVagaShouldBeFound("bannerUrl.in=" + DEFAULT_BANNER_URL + "," + UPDATED_BANNER_URL);

        // Get all the vagaList where bannerUrl equals to UPDATED_BANNER_URL
        defaultVagaShouldNotBeFound("bannerUrl.in=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllVagasByBannerUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where bannerUrl is not null
        defaultVagaShouldBeFound("bannerUrl.specified=true");

        // Get all the vagaList where bannerUrl is null
        defaultVagaShouldNotBeFound("bannerUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByBannerUrlContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where bannerUrl contains DEFAULT_BANNER_URL
        defaultVagaShouldBeFound("bannerUrl.contains=" + DEFAULT_BANNER_URL);

        // Get all the vagaList where bannerUrl contains UPDATED_BANNER_URL
        defaultVagaShouldNotBeFound("bannerUrl.contains=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllVagasByBannerUrlNotContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where bannerUrl does not contain DEFAULT_BANNER_URL
        defaultVagaShouldNotBeFound("bannerUrl.doesNotContain=" + DEFAULT_BANNER_URL);

        // Get all the vagaList where bannerUrl does not contain UPDATED_BANNER_URL
        defaultVagaShouldBeFound("bannerUrl.doesNotContain=" + UPDATED_BANNER_URL);
    }

    @Test
    @Transactional
    void getAllVagasByFonteIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where fonte equals to DEFAULT_FONTE
        defaultVagaShouldBeFound("fonte.equals=" + DEFAULT_FONTE);

        // Get all the vagaList where fonte equals to UPDATED_FONTE
        defaultVagaShouldNotBeFound("fonte.equals=" + UPDATED_FONTE);
    }

    @Test
    @Transactional
    void getAllVagasByFonteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where fonte not equals to DEFAULT_FONTE
        defaultVagaShouldNotBeFound("fonte.notEquals=" + DEFAULT_FONTE);

        // Get all the vagaList where fonte not equals to UPDATED_FONTE
        defaultVagaShouldBeFound("fonte.notEquals=" + UPDATED_FONTE);
    }

    @Test
    @Transactional
    void getAllVagasByFonteIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where fonte in DEFAULT_FONTE or UPDATED_FONTE
        defaultVagaShouldBeFound("fonte.in=" + DEFAULT_FONTE + "," + UPDATED_FONTE);

        // Get all the vagaList where fonte equals to UPDATED_FONTE
        defaultVagaShouldNotBeFound("fonte.in=" + UPDATED_FONTE);
    }

    @Test
    @Transactional
    void getAllVagasByFonteIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where fonte is not null
        defaultVagaShouldBeFound("fonte.specified=true");

        // Get all the vagaList where fonte is null
        defaultVagaShouldNotBeFound("fonte.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByFonteContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where fonte contains DEFAULT_FONTE
        defaultVagaShouldBeFound("fonte.contains=" + DEFAULT_FONTE);

        // Get all the vagaList where fonte contains UPDATED_FONTE
        defaultVagaShouldNotBeFound("fonte.contains=" + UPDATED_FONTE);
    }

    @Test
    @Transactional
    void getAllVagasByFonteNotContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where fonte does not contain DEFAULT_FONTE
        defaultVagaShouldNotBeFound("fonte.doesNotContain=" + DEFAULT_FONTE);

        // Get all the vagaList where fonte does not contain UPDATED_FONTE
        defaultVagaShouldBeFound("fonte.doesNotContain=" + UPDATED_FONTE);
    }

    @Test
    @Transactional
    void getAllVagasByLinkRecrutamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where linkRecrutamento equals to DEFAULT_LINK_RECRUTAMENTO
        defaultVagaShouldBeFound("linkRecrutamento.equals=" + DEFAULT_LINK_RECRUTAMENTO);

        // Get all the vagaList where linkRecrutamento equals to UPDATED_LINK_RECRUTAMENTO
        defaultVagaShouldNotBeFound("linkRecrutamento.equals=" + UPDATED_LINK_RECRUTAMENTO);
    }

    @Test
    @Transactional
    void getAllVagasByLinkRecrutamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where linkRecrutamento not equals to DEFAULT_LINK_RECRUTAMENTO
        defaultVagaShouldNotBeFound("linkRecrutamento.notEquals=" + DEFAULT_LINK_RECRUTAMENTO);

        // Get all the vagaList where linkRecrutamento not equals to UPDATED_LINK_RECRUTAMENTO
        defaultVagaShouldBeFound("linkRecrutamento.notEquals=" + UPDATED_LINK_RECRUTAMENTO);
    }

    @Test
    @Transactional
    void getAllVagasByLinkRecrutamentoIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where linkRecrutamento in DEFAULT_LINK_RECRUTAMENTO or UPDATED_LINK_RECRUTAMENTO
        defaultVagaShouldBeFound("linkRecrutamento.in=" + DEFAULT_LINK_RECRUTAMENTO + "," + UPDATED_LINK_RECRUTAMENTO);

        // Get all the vagaList where linkRecrutamento equals to UPDATED_LINK_RECRUTAMENTO
        defaultVagaShouldNotBeFound("linkRecrutamento.in=" + UPDATED_LINK_RECRUTAMENTO);
    }

    @Test
    @Transactional
    void getAllVagasByLinkRecrutamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where linkRecrutamento is not null
        defaultVagaShouldBeFound("linkRecrutamento.specified=true");

        // Get all the vagaList where linkRecrutamento is null
        defaultVagaShouldNotBeFound("linkRecrutamento.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByLinkRecrutamentoContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where linkRecrutamento contains DEFAULT_LINK_RECRUTAMENTO
        defaultVagaShouldBeFound("linkRecrutamento.contains=" + DEFAULT_LINK_RECRUTAMENTO);

        // Get all the vagaList where linkRecrutamento contains UPDATED_LINK_RECRUTAMENTO
        defaultVagaShouldNotBeFound("linkRecrutamento.contains=" + UPDATED_LINK_RECRUTAMENTO);
    }

    @Test
    @Transactional
    void getAllVagasByLinkRecrutamentoNotContainsSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where linkRecrutamento does not contain DEFAULT_LINK_RECRUTAMENTO
        defaultVagaShouldNotBeFound("linkRecrutamento.doesNotContain=" + DEFAULT_LINK_RECRUTAMENTO);

        // Get all the vagaList where linkRecrutamento does not contain UPDATED_LINK_RECRUTAMENTO
        defaultVagaShouldBeFound("linkRecrutamento.doesNotContain=" + UPDATED_LINK_RECRUTAMENTO);
    }

    @Test
    @Transactional
    void getAllVagasByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where ativo equals to DEFAULT_ATIVO
        defaultVagaShouldBeFound("ativo.equals=" + DEFAULT_ATIVO);

        // Get all the vagaList where ativo equals to UPDATED_ATIVO
        defaultVagaShouldNotBeFound("ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllVagasByAtivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where ativo not equals to DEFAULT_ATIVO
        defaultVagaShouldNotBeFound("ativo.notEquals=" + DEFAULT_ATIVO);

        // Get all the vagaList where ativo not equals to UPDATED_ATIVO
        defaultVagaShouldBeFound("ativo.notEquals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllVagasByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where ativo in DEFAULT_ATIVO or UPDATED_ATIVO
        defaultVagaShouldBeFound("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO);

        // Get all the vagaList where ativo equals to UPDATED_ATIVO
        defaultVagaShouldNotBeFound("ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllVagasByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where ativo is not null
        defaultVagaShouldBeFound("ativo.specified=true");

        // Get all the vagaList where ativo is null
        defaultVagaShouldNotBeFound("ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByPreenchidaIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where preenchida equals to DEFAULT_PREENCHIDA
        defaultVagaShouldBeFound("preenchida.equals=" + DEFAULT_PREENCHIDA);

        // Get all the vagaList where preenchida equals to UPDATED_PREENCHIDA
        defaultVagaShouldNotBeFound("preenchida.equals=" + UPDATED_PREENCHIDA);
    }

    @Test
    @Transactional
    void getAllVagasByPreenchidaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where preenchida not equals to DEFAULT_PREENCHIDA
        defaultVagaShouldNotBeFound("preenchida.notEquals=" + DEFAULT_PREENCHIDA);

        // Get all the vagaList where preenchida not equals to UPDATED_PREENCHIDA
        defaultVagaShouldBeFound("preenchida.notEquals=" + UPDATED_PREENCHIDA);
    }

    @Test
    @Transactional
    void getAllVagasByPreenchidaIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where preenchida in DEFAULT_PREENCHIDA or UPDATED_PREENCHIDA
        defaultVagaShouldBeFound("preenchida.in=" + DEFAULT_PREENCHIDA + "," + UPDATED_PREENCHIDA);

        // Get all the vagaList where preenchida equals to UPDATED_PREENCHIDA
        defaultVagaShouldNotBeFound("preenchida.in=" + UPDATED_PREENCHIDA);
    }

    @Test
    @Transactional
    void getAllVagasByPreenchidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where preenchida is not null
        defaultVagaShouldBeFound("preenchida.specified=true");

        // Get all the vagaList where preenchida is null
        defaultVagaShouldNotBeFound("preenchida.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado equals to DEFAULT_CRIADO
        defaultVagaShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the vagaList where criado equals to UPDATED_CRIADO
        defaultVagaShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado not equals to DEFAULT_CRIADO
        defaultVagaShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the vagaList where criado not equals to UPDATED_CRIADO
        defaultVagaShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultVagaShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the vagaList where criado equals to UPDATED_CRIADO
        defaultVagaShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado is not null
        defaultVagaShouldBeFound("criado.specified=true");

        // Get all the vagaList where criado is null
        defaultVagaShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado is greater than or equal to DEFAULT_CRIADO
        defaultVagaShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the vagaList where criado is greater than or equal to UPDATED_CRIADO
        defaultVagaShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado is less than or equal to DEFAULT_CRIADO
        defaultVagaShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the vagaList where criado is less than or equal to SMALLER_CRIADO
        defaultVagaShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado is less than DEFAULT_CRIADO
        defaultVagaShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the vagaList where criado is less than UPDATED_CRIADO
        defaultVagaShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllVagasByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where criado is greater than DEFAULT_CRIADO
        defaultVagaShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the vagaList where criado is greater than SMALLER_CRIADO
        defaultVagaShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio equals to DEFAULT_PRAZO_ANUNCIO
        defaultVagaShouldBeFound("prazoAnuncio.equals=" + DEFAULT_PRAZO_ANUNCIO);

        // Get all the vagaList where prazoAnuncio equals to UPDATED_PRAZO_ANUNCIO
        defaultVagaShouldNotBeFound("prazoAnuncio.equals=" + UPDATED_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio not equals to DEFAULT_PRAZO_ANUNCIO
        defaultVagaShouldNotBeFound("prazoAnuncio.notEquals=" + DEFAULT_PRAZO_ANUNCIO);

        // Get all the vagaList where prazoAnuncio not equals to UPDATED_PRAZO_ANUNCIO
        defaultVagaShouldBeFound("prazoAnuncio.notEquals=" + UPDATED_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsInShouldWork() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio in DEFAULT_PRAZO_ANUNCIO or UPDATED_PRAZO_ANUNCIO
        defaultVagaShouldBeFound("prazoAnuncio.in=" + DEFAULT_PRAZO_ANUNCIO + "," + UPDATED_PRAZO_ANUNCIO);

        // Get all the vagaList where prazoAnuncio equals to UPDATED_PRAZO_ANUNCIO
        defaultVagaShouldNotBeFound("prazoAnuncio.in=" + UPDATED_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsNullOrNotNull() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio is not null
        defaultVagaShouldBeFound("prazoAnuncio.specified=true");

        // Get all the vagaList where prazoAnuncio is null
        defaultVagaShouldNotBeFound("prazoAnuncio.specified=false");
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio is greater than or equal to DEFAULT_PRAZO_ANUNCIO
        defaultVagaShouldBeFound("prazoAnuncio.greaterThanOrEqual=" + DEFAULT_PRAZO_ANUNCIO);

        // Get all the vagaList where prazoAnuncio is greater than or equal to UPDATED_PRAZO_ANUNCIO
        defaultVagaShouldNotBeFound("prazoAnuncio.greaterThanOrEqual=" + UPDATED_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio is less than or equal to DEFAULT_PRAZO_ANUNCIO
        defaultVagaShouldBeFound("prazoAnuncio.lessThanOrEqual=" + DEFAULT_PRAZO_ANUNCIO);

        // Get all the vagaList where prazoAnuncio is less than or equal to SMALLER_PRAZO_ANUNCIO
        defaultVagaShouldNotBeFound("prazoAnuncio.lessThanOrEqual=" + SMALLER_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsLessThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio is less than DEFAULT_PRAZO_ANUNCIO
        defaultVagaShouldNotBeFound("prazoAnuncio.lessThan=" + DEFAULT_PRAZO_ANUNCIO);

        // Get all the vagaList where prazoAnuncio is less than UPDATED_PRAZO_ANUNCIO
        defaultVagaShouldBeFound("prazoAnuncio.lessThan=" + UPDATED_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void getAllVagasByPrazoAnuncioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        // Get all the vagaList where prazoAnuncio is greater than DEFAULT_PRAZO_ANUNCIO
        defaultVagaShouldNotBeFound("prazoAnuncio.greaterThan=" + DEFAULT_PRAZO_ANUNCIO);

        // Get all the vagaList where prazoAnuncio is greater than SMALLER_PRAZO_ANUNCIO
        defaultVagaShouldBeFound("prazoAnuncio.greaterThan=" + SMALLER_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void getAllVagasByCadastrouIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);
        Pessoa cadastrou = PessoaResourceIT.createEntity(em);
        em.persist(cadastrou);
        em.flush();
        vaga.setCadastrou(cadastrou);
        vagaRepository.saveAndFlush(vaga);
        Long cadastrouId = cadastrou.getId();

        // Get all the vagaList where cadastrou equals to cadastrouId
        defaultVagaShouldBeFound("cadastrouId.equals=" + cadastrouId);

        // Get all the vagaList where cadastrou equals to (cadastrouId + 1)
        defaultVagaShouldNotBeFound("cadastrouId.equals=" + (cadastrouId + 1));
    }

    @Test
    @Transactional
    void getAllVagasByEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);
        Empresa empresa = EmpresaResourceIT.createEntity(em);
        em.persist(empresa);
        em.flush();
        vaga.setEmpresa(empresa);
        vagaRepository.saveAndFlush(vaga);
        Long empresaId = empresa.getId();

        // Get all the vagaList where empresa equals to empresaId
        defaultVagaShouldBeFound("empresaId.equals=" + empresaId);

        // Get all the vagaList where empresa equals to (empresaId + 1)
        defaultVagaShouldNotBeFound("empresaId.equals=" + (empresaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVagaShouldBeFound(String filter) throws Exception {
        restVagaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaga.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].estagio").value(hasItem(DEFAULT_ESTAGIO.booleanValue())))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.doubleValue())))
            .andExpect(jsonPath("$.[*].beneficios").value(hasItem(DEFAULT_BENEFICIOS.doubleValue())))
            .andExpect(jsonPath("$.[*].jornadaSemanal").value(hasItem(DEFAULT_JORNADA_SEMANAL.doubleValue())))
            .andExpect(jsonPath("$.[*].bannerUrl").value(hasItem(DEFAULT_BANNER_URL)))
            .andExpect(jsonPath("$.[*].fonte").value(hasItem(DEFAULT_FONTE)))
            .andExpect(jsonPath("$.[*].linkRecrutamento").value(hasItem(DEFAULT_LINK_RECRUTAMENTO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].preenchida").value(hasItem(DEFAULT_PREENCHIDA.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].prazoAnuncio").value(hasItem(sameInstant(DEFAULT_PRAZO_ANUNCIO))));

        // Check, that the count call also returns 1
        restVagaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVagaShouldNotBeFound(String filter) throws Exception {
        restVagaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVagaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVaga() throws Exception {
        // Get the vaga
        restVagaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaga() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();

        // Update the vaga
        Vaga updatedVaga = vagaRepository.findById(vaga.getId()).get();
        // Disconnect from session so that the updates on updatedVaga are not directly saved in db
        em.detach(updatedVaga);
        updatedVaga
            .descricao(UPDATED_DESCRICAO)
            .titulo(UPDATED_TITULO)
            .estagio(UPDATED_ESTAGIO)
            .salario(UPDATED_SALARIO)
            .beneficios(UPDATED_BENEFICIOS)
            .jornadaSemanal(UPDATED_JORNADA_SEMANAL)
            .bannerUrl(UPDATED_BANNER_URL)
            .fonte(UPDATED_FONTE)
            .linkRecrutamento(UPDATED_LINK_RECRUTAMENTO)
            .ativo(UPDATED_ATIVO)
            .preenchida(UPDATED_PREENCHIDA)
            .criado(UPDATED_CRIADO)
            .prazoAnuncio(UPDATED_PRAZO_ANUNCIO);
        VagaDTO vagaDTO = vagaMapper.toDto(updatedVaga);

        restVagaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vagaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vagaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
        Vaga testVaga = vagaList.get(vagaList.size() - 1);
        assertThat(testVaga.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testVaga.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testVaga.getEstagio()).isEqualTo(UPDATED_ESTAGIO);
        assertThat(testVaga.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testVaga.getBeneficios()).isEqualTo(UPDATED_BENEFICIOS);
        assertThat(testVaga.getJornadaSemanal()).isEqualTo(UPDATED_JORNADA_SEMANAL);
        assertThat(testVaga.getBannerUrl()).isEqualTo(UPDATED_BANNER_URL);
        assertThat(testVaga.getFonte()).isEqualTo(UPDATED_FONTE);
        assertThat(testVaga.getLinkRecrutamento()).isEqualTo(UPDATED_LINK_RECRUTAMENTO);
        assertThat(testVaga.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testVaga.getPreenchida()).isEqualTo(UPDATED_PREENCHIDA);
        assertThat(testVaga.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testVaga.getPrazoAnuncio()).isEqualTo(UPDATED_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void putNonExistingVaga() throws Exception {
        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();
        vaga.setId(count.incrementAndGet());

        // Create the Vaga
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVagaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vagaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaga() throws Exception {
        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();
        vaga.setId(count.incrementAndGet());

        // Create the Vaga
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVagaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaga() throws Exception {
        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();
        vaga.setId(count.incrementAndGet());

        // Create the Vaga
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVagaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vagaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVagaWithPatch() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();

        // Update the vaga using partial update
        Vaga partialUpdatedVaga = new Vaga();
        partialUpdatedVaga.setId(vaga.getId());

        partialUpdatedVaga
            .titulo(UPDATED_TITULO)
            .jornadaSemanal(UPDATED_JORNADA_SEMANAL)
            .fonte(UPDATED_FONTE)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO);

        restVagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaga))
            )
            .andExpect(status().isOk());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
        Vaga testVaga = vagaList.get(vagaList.size() - 1);
        assertThat(testVaga.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testVaga.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testVaga.getEstagio()).isEqualTo(DEFAULT_ESTAGIO);
        assertThat(testVaga.getSalario()).isEqualTo(DEFAULT_SALARIO);
        assertThat(testVaga.getBeneficios()).isEqualTo(DEFAULT_BENEFICIOS);
        assertThat(testVaga.getJornadaSemanal()).isEqualTo(UPDATED_JORNADA_SEMANAL);
        assertThat(testVaga.getBannerUrl()).isEqualTo(DEFAULT_BANNER_URL);
        assertThat(testVaga.getFonte()).isEqualTo(UPDATED_FONTE);
        assertThat(testVaga.getLinkRecrutamento()).isEqualTo(DEFAULT_LINK_RECRUTAMENTO);
        assertThat(testVaga.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testVaga.getPreenchida()).isEqualTo(DEFAULT_PREENCHIDA);
        assertThat(testVaga.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testVaga.getPrazoAnuncio()).isEqualTo(DEFAULT_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void fullUpdateVagaWithPatch() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();

        // Update the vaga using partial update
        Vaga partialUpdatedVaga = new Vaga();
        partialUpdatedVaga.setId(vaga.getId());

        partialUpdatedVaga
            .descricao(UPDATED_DESCRICAO)
            .titulo(UPDATED_TITULO)
            .estagio(UPDATED_ESTAGIO)
            .salario(UPDATED_SALARIO)
            .beneficios(UPDATED_BENEFICIOS)
            .jornadaSemanal(UPDATED_JORNADA_SEMANAL)
            .bannerUrl(UPDATED_BANNER_URL)
            .fonte(UPDATED_FONTE)
            .linkRecrutamento(UPDATED_LINK_RECRUTAMENTO)
            .ativo(UPDATED_ATIVO)
            .preenchida(UPDATED_PREENCHIDA)
            .criado(UPDATED_CRIADO)
            .prazoAnuncio(UPDATED_PRAZO_ANUNCIO);

        restVagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaga))
            )
            .andExpect(status().isOk());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
        Vaga testVaga = vagaList.get(vagaList.size() - 1);
        assertThat(testVaga.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testVaga.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testVaga.getEstagio()).isEqualTo(UPDATED_ESTAGIO);
        assertThat(testVaga.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testVaga.getBeneficios()).isEqualTo(UPDATED_BENEFICIOS);
        assertThat(testVaga.getJornadaSemanal()).isEqualTo(UPDATED_JORNADA_SEMANAL);
        assertThat(testVaga.getBannerUrl()).isEqualTo(UPDATED_BANNER_URL);
        assertThat(testVaga.getFonte()).isEqualTo(UPDATED_FONTE);
        assertThat(testVaga.getLinkRecrutamento()).isEqualTo(UPDATED_LINK_RECRUTAMENTO);
        assertThat(testVaga.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testVaga.getPreenchida()).isEqualTo(UPDATED_PREENCHIDA);
        assertThat(testVaga.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testVaga.getPrazoAnuncio()).isEqualTo(UPDATED_PRAZO_ANUNCIO);
    }

    @Test
    @Transactional
    void patchNonExistingVaga() throws Exception {
        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();
        vaga.setId(count.incrementAndGet());

        // Create the Vaga
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vagaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaga() throws Exception {
        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();
        vaga.setId(count.incrementAndGet());

        // Create the Vaga
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVagaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vagaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaga() throws Exception {
        int databaseSizeBeforeUpdate = vagaRepository.findAll().size();
        vaga.setId(count.incrementAndGet());

        // Create the Vaga
        VagaDTO vagaDTO = vagaMapper.toDto(vaga);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVagaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vagaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaga in the database
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaga() throws Exception {
        // Initialize the database
        vagaRepository.saveAndFlush(vaga);

        int databaseSizeBeforeDelete = vagaRepository.findAll().size();

        // Delete the vaga
        restVagaMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaga.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vaga> vagaList = vagaRepository.findAll();
        assertThat(vagaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
