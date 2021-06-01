package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.AreaAtuacao;
import br.com.vagas.repository.AreaAtuacaoRepository;
import br.com.vagas.service.criteria.AreaAtuacaoCriteria;
import br.com.vagas.service.dto.AreaAtuacaoDTO;
import br.com.vagas.service.mapper.AreaAtuacaoMapper;
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
 * Integration tests for the {@link AreaAtuacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AreaAtuacaoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_ICONE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ICONE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/area-atuacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AreaAtuacaoRepository areaAtuacaoRepository;

    @Autowired
    private AreaAtuacaoMapper areaAtuacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAreaAtuacaoMockMvc;

    private AreaAtuacao areaAtuacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaAtuacao createEntity(EntityManager em) {
        AreaAtuacao areaAtuacao = new AreaAtuacao()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .iconeUrl(DEFAULT_ICONE_URL)
            .ativo(DEFAULT_ATIVO)
            .criado(DEFAULT_CRIADO);
        return areaAtuacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaAtuacao createUpdatedEntity(EntityManager em) {
        AreaAtuacao areaAtuacao = new AreaAtuacao()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .iconeUrl(UPDATED_ICONE_URL)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO);
        return areaAtuacao;
    }

    @BeforeEach
    public void initTest() {
        areaAtuacao = createEntity(em);
    }

    @Test
    @Transactional
    void createAreaAtuacao() throws Exception {
        int databaseSizeBeforeCreate = areaAtuacaoRepository.findAll().size();
        // Create the AreaAtuacao
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);
        restAreaAtuacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeCreate + 1);
        AreaAtuacao testAreaAtuacao = areaAtuacaoList.get(areaAtuacaoList.size() - 1);
        assertThat(testAreaAtuacao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAreaAtuacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testAreaAtuacao.getIconeUrl()).isEqualTo(DEFAULT_ICONE_URL);
        assertThat(testAreaAtuacao.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testAreaAtuacao.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createAreaAtuacaoWithExistingId() throws Exception {
        // Create the AreaAtuacao with an existing ID
        areaAtuacao.setId(1L);
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        int databaseSizeBeforeCreate = areaAtuacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAreaAtuacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = areaAtuacaoRepository.findAll().size();
        // set the field null
        areaAtuacao.setCriado(null);

        // Create the AreaAtuacao, which fails.
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        restAreaAtuacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaos() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList
        restAreaAtuacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(areaAtuacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].iconeUrl").value(hasItem(DEFAULT_ICONE_URL)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getAreaAtuacao() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get the areaAtuacao
        restAreaAtuacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, areaAtuacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(areaAtuacao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.iconeUrl").value(DEFAULT_ICONE_URL))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getAreaAtuacaosByIdFiltering() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        Long id = areaAtuacao.getId();

        defaultAreaAtuacaoShouldBeFound("id.equals=" + id);
        defaultAreaAtuacaoShouldNotBeFound("id.notEquals=" + id);

        defaultAreaAtuacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAreaAtuacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultAreaAtuacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAreaAtuacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where nome equals to DEFAULT_NOME
        defaultAreaAtuacaoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the areaAtuacaoList where nome equals to UPDATED_NOME
        defaultAreaAtuacaoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where nome not equals to DEFAULT_NOME
        defaultAreaAtuacaoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the areaAtuacaoList where nome not equals to UPDATED_NOME
        defaultAreaAtuacaoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultAreaAtuacaoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the areaAtuacaoList where nome equals to UPDATED_NOME
        defaultAreaAtuacaoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where nome is not null
        defaultAreaAtuacaoShouldBeFound("nome.specified=true");

        // Get all the areaAtuacaoList where nome is null
        defaultAreaAtuacaoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByNomeContainsSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where nome contains DEFAULT_NOME
        defaultAreaAtuacaoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the areaAtuacaoList where nome contains UPDATED_NOME
        defaultAreaAtuacaoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where nome does not contain DEFAULT_NOME
        defaultAreaAtuacaoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the areaAtuacaoList where nome does not contain UPDATED_NOME
        defaultAreaAtuacaoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where descricao equals to DEFAULT_DESCRICAO
        defaultAreaAtuacaoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the areaAtuacaoList where descricao equals to UPDATED_DESCRICAO
        defaultAreaAtuacaoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByDescricaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where descricao not equals to DEFAULT_DESCRICAO
        defaultAreaAtuacaoShouldNotBeFound("descricao.notEquals=" + DEFAULT_DESCRICAO);

        // Get all the areaAtuacaoList where descricao not equals to UPDATED_DESCRICAO
        defaultAreaAtuacaoShouldBeFound("descricao.notEquals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultAreaAtuacaoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the areaAtuacaoList where descricao equals to UPDATED_DESCRICAO
        defaultAreaAtuacaoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where descricao is not null
        defaultAreaAtuacaoShouldBeFound("descricao.specified=true");

        // Get all the areaAtuacaoList where descricao is null
        defaultAreaAtuacaoShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where descricao contains DEFAULT_DESCRICAO
        defaultAreaAtuacaoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the areaAtuacaoList where descricao contains UPDATED_DESCRICAO
        defaultAreaAtuacaoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where descricao does not contain DEFAULT_DESCRICAO
        defaultAreaAtuacaoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the areaAtuacaoList where descricao does not contain UPDATED_DESCRICAO
        defaultAreaAtuacaoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByIconeUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where iconeUrl equals to DEFAULT_ICONE_URL
        defaultAreaAtuacaoShouldBeFound("iconeUrl.equals=" + DEFAULT_ICONE_URL);

        // Get all the areaAtuacaoList where iconeUrl equals to UPDATED_ICONE_URL
        defaultAreaAtuacaoShouldNotBeFound("iconeUrl.equals=" + UPDATED_ICONE_URL);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByIconeUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where iconeUrl not equals to DEFAULT_ICONE_URL
        defaultAreaAtuacaoShouldNotBeFound("iconeUrl.notEquals=" + DEFAULT_ICONE_URL);

        // Get all the areaAtuacaoList where iconeUrl not equals to UPDATED_ICONE_URL
        defaultAreaAtuacaoShouldBeFound("iconeUrl.notEquals=" + UPDATED_ICONE_URL);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByIconeUrlIsInShouldWork() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where iconeUrl in DEFAULT_ICONE_URL or UPDATED_ICONE_URL
        defaultAreaAtuacaoShouldBeFound("iconeUrl.in=" + DEFAULT_ICONE_URL + "," + UPDATED_ICONE_URL);

        // Get all the areaAtuacaoList where iconeUrl equals to UPDATED_ICONE_URL
        defaultAreaAtuacaoShouldNotBeFound("iconeUrl.in=" + UPDATED_ICONE_URL);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByIconeUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where iconeUrl is not null
        defaultAreaAtuacaoShouldBeFound("iconeUrl.specified=true");

        // Get all the areaAtuacaoList where iconeUrl is null
        defaultAreaAtuacaoShouldNotBeFound("iconeUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByIconeUrlContainsSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where iconeUrl contains DEFAULT_ICONE_URL
        defaultAreaAtuacaoShouldBeFound("iconeUrl.contains=" + DEFAULT_ICONE_URL);

        // Get all the areaAtuacaoList where iconeUrl contains UPDATED_ICONE_URL
        defaultAreaAtuacaoShouldNotBeFound("iconeUrl.contains=" + UPDATED_ICONE_URL);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByIconeUrlNotContainsSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where iconeUrl does not contain DEFAULT_ICONE_URL
        defaultAreaAtuacaoShouldNotBeFound("iconeUrl.doesNotContain=" + DEFAULT_ICONE_URL);

        // Get all the areaAtuacaoList where iconeUrl does not contain UPDATED_ICONE_URL
        defaultAreaAtuacaoShouldBeFound("iconeUrl.doesNotContain=" + UPDATED_ICONE_URL);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where ativo equals to DEFAULT_ATIVO
        defaultAreaAtuacaoShouldBeFound("ativo.equals=" + DEFAULT_ATIVO);

        // Get all the areaAtuacaoList where ativo equals to UPDATED_ATIVO
        defaultAreaAtuacaoShouldNotBeFound("ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByAtivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where ativo not equals to DEFAULT_ATIVO
        defaultAreaAtuacaoShouldNotBeFound("ativo.notEquals=" + DEFAULT_ATIVO);

        // Get all the areaAtuacaoList where ativo not equals to UPDATED_ATIVO
        defaultAreaAtuacaoShouldBeFound("ativo.notEquals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where ativo in DEFAULT_ATIVO or UPDATED_ATIVO
        defaultAreaAtuacaoShouldBeFound("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO);

        // Get all the areaAtuacaoList where ativo equals to UPDATED_ATIVO
        defaultAreaAtuacaoShouldNotBeFound("ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where ativo is not null
        defaultAreaAtuacaoShouldBeFound("ativo.specified=true");

        // Get all the areaAtuacaoList where ativo is null
        defaultAreaAtuacaoShouldNotBeFound("ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado equals to DEFAULT_CRIADO
        defaultAreaAtuacaoShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the areaAtuacaoList where criado equals to UPDATED_CRIADO
        defaultAreaAtuacaoShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado not equals to DEFAULT_CRIADO
        defaultAreaAtuacaoShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the areaAtuacaoList where criado not equals to UPDATED_CRIADO
        defaultAreaAtuacaoShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultAreaAtuacaoShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the areaAtuacaoList where criado equals to UPDATED_CRIADO
        defaultAreaAtuacaoShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado is not null
        defaultAreaAtuacaoShouldBeFound("criado.specified=true");

        // Get all the areaAtuacaoList where criado is null
        defaultAreaAtuacaoShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado is greater than or equal to DEFAULT_CRIADO
        defaultAreaAtuacaoShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the areaAtuacaoList where criado is greater than or equal to UPDATED_CRIADO
        defaultAreaAtuacaoShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado is less than or equal to DEFAULT_CRIADO
        defaultAreaAtuacaoShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the areaAtuacaoList where criado is less than or equal to SMALLER_CRIADO
        defaultAreaAtuacaoShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado is less than DEFAULT_CRIADO
        defaultAreaAtuacaoShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the areaAtuacaoList where criado is less than UPDATED_CRIADO
        defaultAreaAtuacaoShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllAreaAtuacaosByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        // Get all the areaAtuacaoList where criado is greater than DEFAULT_CRIADO
        defaultAreaAtuacaoShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the areaAtuacaoList where criado is greater than SMALLER_CRIADO
        defaultAreaAtuacaoShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAreaAtuacaoShouldBeFound(String filter) throws Exception {
        restAreaAtuacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(areaAtuacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].iconeUrl").value(hasItem(DEFAULT_ICONE_URL)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restAreaAtuacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAreaAtuacaoShouldNotBeFound(String filter) throws Exception {
        restAreaAtuacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAreaAtuacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAreaAtuacao() throws Exception {
        // Get the areaAtuacao
        restAreaAtuacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAreaAtuacao() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();

        // Update the areaAtuacao
        AreaAtuacao updatedAreaAtuacao = areaAtuacaoRepository.findById(areaAtuacao.getId()).get();
        // Disconnect from session so that the updates on updatedAreaAtuacao are not directly saved in db
        em.detach(updatedAreaAtuacao);
        updatedAreaAtuacao
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .iconeUrl(UPDATED_ICONE_URL)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO);
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(updatedAreaAtuacao);

        restAreaAtuacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, areaAtuacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
        AreaAtuacao testAreaAtuacao = areaAtuacaoList.get(areaAtuacaoList.size() - 1);
        assertThat(testAreaAtuacao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAreaAtuacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAreaAtuacao.getIconeUrl()).isEqualTo(UPDATED_ICONE_URL);
        assertThat(testAreaAtuacao.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testAreaAtuacao.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingAreaAtuacao() throws Exception {
        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();
        areaAtuacao.setId(count.incrementAndGet());

        // Create the AreaAtuacao
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaAtuacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, areaAtuacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAreaAtuacao() throws Exception {
        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();
        areaAtuacao.setId(count.incrementAndGet());

        // Create the AreaAtuacao
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaAtuacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAreaAtuacao() throws Exception {
        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();
        areaAtuacao.setId(count.incrementAndGet());

        // Create the AreaAtuacao
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaAtuacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAreaAtuacaoWithPatch() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();

        // Update the areaAtuacao using partial update
        AreaAtuacao partialUpdatedAreaAtuacao = new AreaAtuacao();
        partialUpdatedAreaAtuacao.setId(areaAtuacao.getId());

        partialUpdatedAreaAtuacao.descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO).criado(UPDATED_CRIADO);

        restAreaAtuacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAreaAtuacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAreaAtuacao))
            )
            .andExpect(status().isOk());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
        AreaAtuacao testAreaAtuacao = areaAtuacaoList.get(areaAtuacaoList.size() - 1);
        assertThat(testAreaAtuacao.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAreaAtuacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAreaAtuacao.getIconeUrl()).isEqualTo(DEFAULT_ICONE_URL);
        assertThat(testAreaAtuacao.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testAreaAtuacao.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateAreaAtuacaoWithPatch() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();

        // Update the areaAtuacao using partial update
        AreaAtuacao partialUpdatedAreaAtuacao = new AreaAtuacao();
        partialUpdatedAreaAtuacao.setId(areaAtuacao.getId());

        partialUpdatedAreaAtuacao
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .iconeUrl(UPDATED_ICONE_URL)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO);

        restAreaAtuacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAreaAtuacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAreaAtuacao))
            )
            .andExpect(status().isOk());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
        AreaAtuacao testAreaAtuacao = areaAtuacaoList.get(areaAtuacaoList.size() - 1);
        assertThat(testAreaAtuacao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAreaAtuacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAreaAtuacao.getIconeUrl()).isEqualTo(UPDATED_ICONE_URL);
        assertThat(testAreaAtuacao.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testAreaAtuacao.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingAreaAtuacao() throws Exception {
        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();
        areaAtuacao.setId(count.incrementAndGet());

        // Create the AreaAtuacao
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaAtuacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, areaAtuacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAreaAtuacao() throws Exception {
        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();
        areaAtuacao.setId(count.incrementAndGet());

        // Create the AreaAtuacao
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaAtuacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAreaAtuacao() throws Exception {
        int databaseSizeBeforeUpdate = areaAtuacaoRepository.findAll().size();
        areaAtuacao.setId(count.incrementAndGet());

        // Create the AreaAtuacao
        AreaAtuacaoDTO areaAtuacaoDTO = areaAtuacaoMapper.toDto(areaAtuacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaAtuacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(areaAtuacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AreaAtuacao in the database
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAreaAtuacao() throws Exception {
        // Initialize the database
        areaAtuacaoRepository.saveAndFlush(areaAtuacao);

        int databaseSizeBeforeDelete = areaAtuacaoRepository.findAll().size();

        // Delete the areaAtuacao
        restAreaAtuacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, areaAtuacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AreaAtuacao> areaAtuacaoList = areaAtuacaoRepository.findAll();
        assertThat(areaAtuacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
