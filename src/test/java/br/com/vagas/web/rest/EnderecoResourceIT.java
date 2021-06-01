package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.Endereco;
import br.com.vagas.repository.EnderecoRepository;
import br.com.vagas.service.criteria.EnderecoCriteria;
import br.com.vagas.service.dto.EnderecoDTO;
import br.com.vagas.service.mapper.EnderecoMapper;
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

/**
 * Integration tests for the {@link EnderecoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnderecoResourceIT {

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_UF = "AAAAAAAAAA";
    private static final String UPDATED_UF = "BBBBBBBBBB";

    private static final String DEFAULT_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/enderecos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .cep(DEFAULT_CEP)
            .logradouro(DEFAULT_LOGRADOURO)
            .complemento(DEFAULT_COMPLEMENTO)
            .numero(DEFAULT_NUMERO)
            .uf(DEFAULT_UF)
            .municipio(DEFAULT_MUNICIPIO)
            .bairro(DEFAULT_BAIRRO)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .criado(DEFAULT_CRIADO);
        return endereco;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createUpdatedEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .complemento(UPDATED_COMPLEMENTO)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF)
            .municipio(UPDATED_MUNICIPIO)
            .bairro(UPDATED_BAIRRO)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .criado(UPDATED_CRIADO);
        return endereco;
    }

    @BeforeEach
    public void initTest() {
        endereco = createEntity(em);
    }

    @Test
    @Transactional
    void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();
        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);
        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate + 1);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEndereco.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testEndereco.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testEndereco.getMunicipio()).isEqualTo(DEFAULT_MUNICIPIO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEndereco.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testEndereco.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testEndereco.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createEnderecoWithExistingId() throws Exception {
        // Create the Endereco with an existing ID
        endereco.setId(1L);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setCep(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setCriado(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnderecos() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL_ID, endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF))
            .andExpect(jsonPath("$.municipio").value(DEFAULT_MUNICIPIO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getEnderecosByIdFiltering() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        Long id = endereco.getId();

        defaultEnderecoShouldBeFound("id.equals=" + id);
        defaultEnderecoShouldNotBeFound("id.notEquals=" + id);

        defaultEnderecoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEnderecoShouldNotBeFound("id.greaterThan=" + id);

        defaultEnderecoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEnderecoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep equals to DEFAULT_CEP
        defaultEnderecoShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the enderecoList where cep equals to UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep not equals to DEFAULT_CEP
        defaultEnderecoShouldNotBeFound("cep.notEquals=" + DEFAULT_CEP);

        // Get all the enderecoList where cep not equals to UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.notEquals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the enderecoList where cep equals to UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep is not null
        defaultEnderecoShouldBeFound("cep.specified=true");

        // Get all the enderecoList where cep is null
        defaultEnderecoShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByCepContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep contains DEFAULT_CEP
        defaultEnderecoShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the enderecoList where cep contains UPDATED_CEP
        defaultEnderecoShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByCepNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where cep does not contain DEFAULT_CEP
        defaultEnderecoShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the enderecoList where cep does not contain UPDATED_CEP
        defaultEnderecoShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllEnderecosByLogradouroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where logradouro equals to DEFAULT_LOGRADOURO
        defaultEnderecoShouldBeFound("logradouro.equals=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoList where logradouro equals to UPDATED_LOGRADOURO
        defaultEnderecoShouldNotBeFound("logradouro.equals=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecosByLogradouroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where logradouro not equals to DEFAULT_LOGRADOURO
        defaultEnderecoShouldNotBeFound("logradouro.notEquals=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoList where logradouro not equals to UPDATED_LOGRADOURO
        defaultEnderecoShouldBeFound("logradouro.notEquals=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecosByLogradouroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where logradouro in DEFAULT_LOGRADOURO or UPDATED_LOGRADOURO
        defaultEnderecoShouldBeFound("logradouro.in=" + DEFAULT_LOGRADOURO + "," + UPDATED_LOGRADOURO);

        // Get all the enderecoList where logradouro equals to UPDATED_LOGRADOURO
        defaultEnderecoShouldNotBeFound("logradouro.in=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecosByLogradouroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where logradouro is not null
        defaultEnderecoShouldBeFound("logradouro.specified=true");

        // Get all the enderecoList where logradouro is null
        defaultEnderecoShouldNotBeFound("logradouro.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByLogradouroContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where logradouro contains DEFAULT_LOGRADOURO
        defaultEnderecoShouldBeFound("logradouro.contains=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoList where logradouro contains UPDATED_LOGRADOURO
        defaultEnderecoShouldNotBeFound("logradouro.contains=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecosByLogradouroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where logradouro does not contain DEFAULT_LOGRADOURO
        defaultEnderecoShouldNotBeFound("logradouro.doesNotContain=" + DEFAULT_LOGRADOURO);

        // Get all the enderecoList where logradouro does not contain UPDATED_LOGRADOURO
        defaultEnderecoShouldBeFound("logradouro.doesNotContain=" + UPDATED_LOGRADOURO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento equals to DEFAULT_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.equals=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.equals=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento not equals to DEFAULT_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.notEquals=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento not equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.notEquals=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento in DEFAULT_COMPLEMENTO or UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.in=" + DEFAULT_COMPLEMENTO + "," + UPDATED_COMPLEMENTO);

        // Get all the enderecoList where complemento equals to UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.in=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento is not null
        defaultEnderecoShouldBeFound("complemento.specified=true");

        // Get all the enderecoList where complemento is null
        defaultEnderecoShouldNotBeFound("complemento.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento contains DEFAULT_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.contains=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento contains UPDATED_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.contains=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByComplementoNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where complemento does not contain DEFAULT_COMPLEMENTO
        defaultEnderecoShouldNotBeFound("complemento.doesNotContain=" + DEFAULT_COMPLEMENTO);

        // Get all the enderecoList where complemento does not contain UPDATED_COMPLEMENTO
        defaultEnderecoShouldBeFound("complemento.doesNotContain=" + UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero equals to DEFAULT_NUMERO
        defaultEnderecoShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero equals to UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero not equals to DEFAULT_NUMERO
        defaultEnderecoShouldNotBeFound("numero.notEquals=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero not equals to UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.notEquals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the enderecoList where numero equals to UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero is not null
        defaultEnderecoShouldBeFound("numero.specified=true");

        // Get all the enderecoList where numero is null
        defaultEnderecoShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero contains DEFAULT_NUMERO
        defaultEnderecoShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero contains UPDATED_NUMERO
        defaultEnderecoShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where numero does not contain DEFAULT_NUMERO
        defaultEnderecoShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the enderecoList where numero does not contain UPDATED_NUMERO
        defaultEnderecoShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllEnderecosByUfIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where uf equals to DEFAULT_UF
        defaultEnderecoShouldBeFound("uf.equals=" + DEFAULT_UF);

        // Get all the enderecoList where uf equals to UPDATED_UF
        defaultEnderecoShouldNotBeFound("uf.equals=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllEnderecosByUfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where uf not equals to DEFAULT_UF
        defaultEnderecoShouldNotBeFound("uf.notEquals=" + DEFAULT_UF);

        // Get all the enderecoList where uf not equals to UPDATED_UF
        defaultEnderecoShouldBeFound("uf.notEquals=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllEnderecosByUfIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where uf in DEFAULT_UF or UPDATED_UF
        defaultEnderecoShouldBeFound("uf.in=" + DEFAULT_UF + "," + UPDATED_UF);

        // Get all the enderecoList where uf equals to UPDATED_UF
        defaultEnderecoShouldNotBeFound("uf.in=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllEnderecosByUfIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where uf is not null
        defaultEnderecoShouldBeFound("uf.specified=true");

        // Get all the enderecoList where uf is null
        defaultEnderecoShouldNotBeFound("uf.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByUfContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where uf contains DEFAULT_UF
        defaultEnderecoShouldBeFound("uf.contains=" + DEFAULT_UF);

        // Get all the enderecoList where uf contains UPDATED_UF
        defaultEnderecoShouldNotBeFound("uf.contains=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllEnderecosByUfNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where uf does not contain DEFAULT_UF
        defaultEnderecoShouldNotBeFound("uf.doesNotContain=" + DEFAULT_UF);

        // Get all the enderecoList where uf does not contain UPDATED_UF
        defaultEnderecoShouldBeFound("uf.doesNotContain=" + UPDATED_UF);
    }

    @Test
    @Transactional
    void getAllEnderecosByMunicipioIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where municipio equals to DEFAULT_MUNICIPIO
        defaultEnderecoShouldBeFound("municipio.equals=" + DEFAULT_MUNICIPIO);

        // Get all the enderecoList where municipio equals to UPDATED_MUNICIPIO
        defaultEnderecoShouldNotBeFound("municipio.equals=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllEnderecosByMunicipioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where municipio not equals to DEFAULT_MUNICIPIO
        defaultEnderecoShouldNotBeFound("municipio.notEquals=" + DEFAULT_MUNICIPIO);

        // Get all the enderecoList where municipio not equals to UPDATED_MUNICIPIO
        defaultEnderecoShouldBeFound("municipio.notEquals=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllEnderecosByMunicipioIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where municipio in DEFAULT_MUNICIPIO or UPDATED_MUNICIPIO
        defaultEnderecoShouldBeFound("municipio.in=" + DEFAULT_MUNICIPIO + "," + UPDATED_MUNICIPIO);

        // Get all the enderecoList where municipio equals to UPDATED_MUNICIPIO
        defaultEnderecoShouldNotBeFound("municipio.in=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllEnderecosByMunicipioIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where municipio is not null
        defaultEnderecoShouldBeFound("municipio.specified=true");

        // Get all the enderecoList where municipio is null
        defaultEnderecoShouldNotBeFound("municipio.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByMunicipioContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where municipio contains DEFAULT_MUNICIPIO
        defaultEnderecoShouldBeFound("municipio.contains=" + DEFAULT_MUNICIPIO);

        // Get all the enderecoList where municipio contains UPDATED_MUNICIPIO
        defaultEnderecoShouldNotBeFound("municipio.contains=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllEnderecosByMunicipioNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where municipio does not contain DEFAULT_MUNICIPIO
        defaultEnderecoShouldNotBeFound("municipio.doesNotContain=" + DEFAULT_MUNICIPIO);

        // Get all the enderecoList where municipio does not contain UPDATED_MUNICIPIO
        defaultEnderecoShouldBeFound("municipio.doesNotContain=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro equals to DEFAULT_BAIRRO
        defaultEnderecoShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro not equals to DEFAULT_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.notEquals=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro not equals to UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.notEquals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the enderecoList where bairro equals to UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro is not null
        defaultEnderecoShouldBeFound("bairro.specified=true");

        // Get all the enderecoList where bairro is null
        defaultEnderecoShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro contains DEFAULT_BAIRRO
        defaultEnderecoShouldBeFound("bairro.contains=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro contains UPDATED_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.contains=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByBairroNotContainsSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where bairro does not contain DEFAULT_BAIRRO
        defaultEnderecoShouldNotBeFound("bairro.doesNotContain=" + DEFAULT_BAIRRO);

        // Get all the enderecoList where bairro does not contain UPDATED_BAIRRO
        defaultEnderecoShouldBeFound("bairro.doesNotContain=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude equals to DEFAULT_LATITUDE
        defaultEnderecoShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the enderecoList where latitude equals to UPDATED_LATITUDE
        defaultEnderecoShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude not equals to DEFAULT_LATITUDE
        defaultEnderecoShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the enderecoList where latitude not equals to UPDATED_LATITUDE
        defaultEnderecoShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultEnderecoShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the enderecoList where latitude equals to UPDATED_LATITUDE
        defaultEnderecoShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude is not null
        defaultEnderecoShouldBeFound("latitude.specified=true");

        // Get all the enderecoList where latitude is null
        defaultEnderecoShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultEnderecoShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the enderecoList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultEnderecoShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultEnderecoShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the enderecoList where latitude is less than or equal to SMALLER_LATITUDE
        defaultEnderecoShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude is less than DEFAULT_LATITUDE
        defaultEnderecoShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the enderecoList where latitude is less than UPDATED_LATITUDE
        defaultEnderecoShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where latitude is greater than DEFAULT_LATITUDE
        defaultEnderecoShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the enderecoList where latitude is greater than SMALLER_LATITUDE
        defaultEnderecoShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude equals to DEFAULT_LONGITUDE
        defaultEnderecoShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the enderecoList where longitude equals to UPDATED_LONGITUDE
        defaultEnderecoShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude not equals to DEFAULT_LONGITUDE
        defaultEnderecoShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the enderecoList where longitude not equals to UPDATED_LONGITUDE
        defaultEnderecoShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultEnderecoShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the enderecoList where longitude equals to UPDATED_LONGITUDE
        defaultEnderecoShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude is not null
        defaultEnderecoShouldBeFound("longitude.specified=true");

        // Get all the enderecoList where longitude is null
        defaultEnderecoShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultEnderecoShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the enderecoList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultEnderecoShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultEnderecoShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the enderecoList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultEnderecoShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude is less than DEFAULT_LONGITUDE
        defaultEnderecoShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the enderecoList where longitude is less than UPDATED_LONGITUDE
        defaultEnderecoShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where longitude is greater than DEFAULT_LONGITUDE
        defaultEnderecoShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the enderecoList where longitude is greater than SMALLER_LONGITUDE
        defaultEnderecoShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado equals to DEFAULT_CRIADO
        defaultEnderecoShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the enderecoList where criado equals to UPDATED_CRIADO
        defaultEnderecoShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado not equals to DEFAULT_CRIADO
        defaultEnderecoShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the enderecoList where criado not equals to UPDATED_CRIADO
        defaultEnderecoShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultEnderecoShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the enderecoList where criado equals to UPDATED_CRIADO
        defaultEnderecoShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado is not null
        defaultEnderecoShouldBeFound("criado.specified=true");

        // Get all the enderecoList where criado is null
        defaultEnderecoShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado is greater than or equal to DEFAULT_CRIADO
        defaultEnderecoShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the enderecoList where criado is greater than or equal to UPDATED_CRIADO
        defaultEnderecoShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado is less than or equal to DEFAULT_CRIADO
        defaultEnderecoShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the enderecoList where criado is less than or equal to SMALLER_CRIADO
        defaultEnderecoShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado is less than DEFAULT_CRIADO
        defaultEnderecoShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the enderecoList where criado is less than UPDATED_CRIADO
        defaultEnderecoShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEnderecosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList where criado is greater than DEFAULT_CRIADO
        defaultEnderecoShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the enderecoList where criado is greater than SMALLER_CRIADO
        defaultEnderecoShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnderecoShouldBeFound(String filter) throws Exception {
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF)))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnderecoShouldNotBeFound(String filter) throws Exception {
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findById(endereco.getId()).get();
        // Disconnect from session so that the updates on updatedEndereco are not directly saved in db
        em.detach(updatedEndereco);
        updatedEndereco
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .complemento(UPDATED_COMPLEMENTO)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF)
            .municipio(UPDATED_MUNICIPIO)
            .bairro(UPDATED_BAIRRO)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .criado(UPDATED_CRIADO);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(updatedEndereco);

        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testEndereco.getMunicipio()).isEqualTo(UPDATED_MUNICIPIO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEndereco.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEndereco.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEndereco.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        endereco.setId(count.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        endereco.setId(count.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        endereco.setId(count.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enderecoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco.cep(UPDATED_CEP).complemento(UPDATED_COMPLEMENTO).latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testEndereco.getMunicipio()).isEqualTo(DEFAULT_MUNICIPIO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEndereco.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEndereco.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEndereco.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .complemento(UPDATED_COMPLEMENTO)
            .numero(UPDATED_NUMERO)
            .uf(UPDATED_UF)
            .municipio(UPDATED_MUNICIPIO)
            .bairro(UPDATED_BAIRRO)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .criado(UPDATED_CRIADO);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testEndereco.getMunicipio()).isEqualTo(UPDATED_MUNICIPIO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEndereco.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEndereco.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEndereco.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        endereco.setId(count.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        endereco.setId(count.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();
        endereco.setId(count.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enderecoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        int databaseSizeBeforeDelete = enderecoRepository.findAll().size();

        // Delete the endereco
        restEnderecoMockMvc
            .perform(delete(ENTITY_API_URL_ID, endereco.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
