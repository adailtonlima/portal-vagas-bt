package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.FuncaoPessoa;
import br.com.vagas.domain.Pessoa;
import br.com.vagas.domain.enumeration.Funcao;
import br.com.vagas.repository.FuncaoPessoaRepository;
import br.com.vagas.service.criteria.FuncaoPessoaCriteria;
import br.com.vagas.service.dto.FuncaoPessoaDTO;
import br.com.vagas.service.mapper.FuncaoPessoaMapper;
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
 * Integration tests for the {@link FuncaoPessoaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FuncaoPessoaResourceIT {

    private static final Funcao DEFAULT_FUNCAO = Funcao.ADMIN;
    private static final Funcao UPDATED_FUNCAO = Funcao.VAGAS;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/funcao-pessoas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FuncaoPessoaRepository funcaoPessoaRepository;

    @Autowired
    private FuncaoPessoaMapper funcaoPessoaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuncaoPessoaMockMvc;

    private FuncaoPessoa funcaoPessoa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FuncaoPessoa createEntity(EntityManager em) {
        FuncaoPessoa funcaoPessoa = new FuncaoPessoa().funcao(DEFAULT_FUNCAO).criado(DEFAULT_CRIADO).ativo(DEFAULT_ATIVO);
        return funcaoPessoa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FuncaoPessoa createUpdatedEntity(EntityManager em) {
        FuncaoPessoa funcaoPessoa = new FuncaoPessoa().funcao(UPDATED_FUNCAO).criado(UPDATED_CRIADO).ativo(UPDATED_ATIVO);
        return funcaoPessoa;
    }

    @BeforeEach
    public void initTest() {
        funcaoPessoa = createEntity(em);
    }

    @Test
    @Transactional
    void createFuncaoPessoa() throws Exception {
        int databaseSizeBeforeCreate = funcaoPessoaRepository.findAll().size();
        // Create the FuncaoPessoa
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);
        restFuncaoPessoaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeCreate + 1);
        FuncaoPessoa testFuncaoPessoa = funcaoPessoaList.get(funcaoPessoaList.size() - 1);
        assertThat(testFuncaoPessoa.getFuncao()).isEqualTo(DEFAULT_FUNCAO);
        assertThat(testFuncaoPessoa.getCriado()).isEqualTo(DEFAULT_CRIADO);
        assertThat(testFuncaoPessoa.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void createFuncaoPessoaWithExistingId() throws Exception {
        // Create the FuncaoPessoa with an existing ID
        funcaoPessoa.setId(1L);
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        int databaseSizeBeforeCreate = funcaoPessoaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuncaoPessoaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcaoPessoaRepository.findAll().size();
        // set the field null
        funcaoPessoa.setCriado(null);

        // Create the FuncaoPessoa, which fails.
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        restFuncaoPessoaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isBadRequest());

        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoas() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList
        restFuncaoPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcaoPessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].funcao").value(hasItem(DEFAULT_FUNCAO.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }

    @Test
    @Transactional
    void getFuncaoPessoa() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get the funcaoPessoa
        restFuncaoPessoaMockMvc
            .perform(get(ENTITY_API_URL_ID, funcaoPessoa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funcaoPessoa.getId().intValue()))
            .andExpect(jsonPath("$.funcao").value(DEFAULT_FUNCAO.toString()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    void getFuncaoPessoasByIdFiltering() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        Long id = funcaoPessoa.getId();

        defaultFuncaoPessoaShouldBeFound("id.equals=" + id);
        defaultFuncaoPessoaShouldNotBeFound("id.notEquals=" + id);

        defaultFuncaoPessoaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFuncaoPessoaShouldNotBeFound("id.greaterThan=" + id);

        defaultFuncaoPessoaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFuncaoPessoaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByFuncaoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where funcao equals to DEFAULT_FUNCAO
        defaultFuncaoPessoaShouldBeFound("funcao.equals=" + DEFAULT_FUNCAO);

        // Get all the funcaoPessoaList where funcao equals to UPDATED_FUNCAO
        defaultFuncaoPessoaShouldNotBeFound("funcao.equals=" + UPDATED_FUNCAO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByFuncaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where funcao not equals to DEFAULT_FUNCAO
        defaultFuncaoPessoaShouldNotBeFound("funcao.notEquals=" + DEFAULT_FUNCAO);

        // Get all the funcaoPessoaList where funcao not equals to UPDATED_FUNCAO
        defaultFuncaoPessoaShouldBeFound("funcao.notEquals=" + UPDATED_FUNCAO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByFuncaoIsInShouldWork() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where funcao in DEFAULT_FUNCAO or UPDATED_FUNCAO
        defaultFuncaoPessoaShouldBeFound("funcao.in=" + DEFAULT_FUNCAO + "," + UPDATED_FUNCAO);

        // Get all the funcaoPessoaList where funcao equals to UPDATED_FUNCAO
        defaultFuncaoPessoaShouldNotBeFound("funcao.in=" + UPDATED_FUNCAO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByFuncaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where funcao is not null
        defaultFuncaoPessoaShouldBeFound("funcao.specified=true");

        // Get all the funcaoPessoaList where funcao is null
        defaultFuncaoPessoaShouldNotBeFound("funcao.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado equals to DEFAULT_CRIADO
        defaultFuncaoPessoaShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the funcaoPessoaList where criado equals to UPDATED_CRIADO
        defaultFuncaoPessoaShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado not equals to DEFAULT_CRIADO
        defaultFuncaoPessoaShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the funcaoPessoaList where criado not equals to UPDATED_CRIADO
        defaultFuncaoPessoaShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultFuncaoPessoaShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the funcaoPessoaList where criado equals to UPDATED_CRIADO
        defaultFuncaoPessoaShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado is not null
        defaultFuncaoPessoaShouldBeFound("criado.specified=true");

        // Get all the funcaoPessoaList where criado is null
        defaultFuncaoPessoaShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado is greater than or equal to DEFAULT_CRIADO
        defaultFuncaoPessoaShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the funcaoPessoaList where criado is greater than or equal to UPDATED_CRIADO
        defaultFuncaoPessoaShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado is less than or equal to DEFAULT_CRIADO
        defaultFuncaoPessoaShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the funcaoPessoaList where criado is less than or equal to SMALLER_CRIADO
        defaultFuncaoPessoaShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado is less than DEFAULT_CRIADO
        defaultFuncaoPessoaShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the funcaoPessoaList where criado is less than UPDATED_CRIADO
        defaultFuncaoPessoaShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where criado is greater than DEFAULT_CRIADO
        defaultFuncaoPessoaShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the funcaoPessoaList where criado is greater than SMALLER_CRIADO
        defaultFuncaoPessoaShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where ativo equals to DEFAULT_ATIVO
        defaultFuncaoPessoaShouldBeFound("ativo.equals=" + DEFAULT_ATIVO);

        // Get all the funcaoPessoaList where ativo equals to UPDATED_ATIVO
        defaultFuncaoPessoaShouldNotBeFound("ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByAtivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where ativo not equals to DEFAULT_ATIVO
        defaultFuncaoPessoaShouldNotBeFound("ativo.notEquals=" + DEFAULT_ATIVO);

        // Get all the funcaoPessoaList where ativo not equals to UPDATED_ATIVO
        defaultFuncaoPessoaShouldBeFound("ativo.notEquals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where ativo in DEFAULT_ATIVO or UPDATED_ATIVO
        defaultFuncaoPessoaShouldBeFound("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO);

        // Get all the funcaoPessoaList where ativo equals to UPDATED_ATIVO
        defaultFuncaoPessoaShouldNotBeFound("ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        // Get all the funcaoPessoaList where ativo is not null
        defaultFuncaoPessoaShouldBeFound("ativo.specified=true");

        // Get all the funcaoPessoaList where ativo is null
        defaultFuncaoPessoaShouldNotBeFound("ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllFuncaoPessoasByPessoaIsEqualToSomething() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);
        Pessoa pessoa = PessoaResourceIT.createEntity(em);
        em.persist(pessoa);
        em.flush();
        funcaoPessoa.setPessoa(pessoa);
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);
        Long pessoaId = pessoa.getId();

        // Get all the funcaoPessoaList where pessoa equals to pessoaId
        defaultFuncaoPessoaShouldBeFound("pessoaId.equals=" + pessoaId);

        // Get all the funcaoPessoaList where pessoa equals to (pessoaId + 1)
        defaultFuncaoPessoaShouldNotBeFound("pessoaId.equals=" + (pessoaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFuncaoPessoaShouldBeFound(String filter) throws Exception {
        restFuncaoPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcaoPessoa.getId().intValue())))
            .andExpect(jsonPath("$.[*].funcao").value(hasItem(DEFAULT_FUNCAO.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));

        // Check, that the count call also returns 1
        restFuncaoPessoaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFuncaoPessoaShouldNotBeFound(String filter) throws Exception {
        restFuncaoPessoaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFuncaoPessoaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFuncaoPessoa() throws Exception {
        // Get the funcaoPessoa
        restFuncaoPessoaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFuncaoPessoa() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();

        // Update the funcaoPessoa
        FuncaoPessoa updatedFuncaoPessoa = funcaoPessoaRepository.findById(funcaoPessoa.getId()).get();
        // Disconnect from session so that the updates on updatedFuncaoPessoa are not directly saved in db
        em.detach(updatedFuncaoPessoa);
        updatedFuncaoPessoa.funcao(UPDATED_FUNCAO).criado(UPDATED_CRIADO).ativo(UPDATED_ATIVO);
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(updatedFuncaoPessoa);

        restFuncaoPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcaoPessoaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isOk());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
        FuncaoPessoa testFuncaoPessoa = funcaoPessoaList.get(funcaoPessoaList.size() - 1);
        assertThat(testFuncaoPessoa.getFuncao()).isEqualTo(UPDATED_FUNCAO);
        assertThat(testFuncaoPessoa.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testFuncaoPessoa.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void putNonExistingFuncaoPessoa() throws Exception {
        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();
        funcaoPessoa.setId(count.incrementAndGet());

        // Create the FuncaoPessoa
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncaoPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, funcaoPessoaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuncaoPessoa() throws Exception {
        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();
        funcaoPessoa.setId(count.incrementAndGet());

        // Create the FuncaoPessoa
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncaoPessoaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuncaoPessoa() throws Exception {
        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();
        funcaoPessoa.setId(count.incrementAndGet());

        // Create the FuncaoPessoa
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncaoPessoaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuncaoPessoaWithPatch() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();

        // Update the funcaoPessoa using partial update
        FuncaoPessoa partialUpdatedFuncaoPessoa = new FuncaoPessoa();
        partialUpdatedFuncaoPessoa.setId(funcaoPessoa.getId());

        partialUpdatedFuncaoPessoa.funcao(UPDATED_FUNCAO).criado(UPDATED_CRIADO);

        restFuncaoPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncaoPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncaoPessoa))
            )
            .andExpect(status().isOk());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
        FuncaoPessoa testFuncaoPessoa = funcaoPessoaList.get(funcaoPessoaList.size() - 1);
        assertThat(testFuncaoPessoa.getFuncao()).isEqualTo(UPDATED_FUNCAO);
        assertThat(testFuncaoPessoa.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testFuncaoPessoa.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void fullUpdateFuncaoPessoaWithPatch() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();

        // Update the funcaoPessoa using partial update
        FuncaoPessoa partialUpdatedFuncaoPessoa = new FuncaoPessoa();
        partialUpdatedFuncaoPessoa.setId(funcaoPessoa.getId());

        partialUpdatedFuncaoPessoa.funcao(UPDATED_FUNCAO).criado(UPDATED_CRIADO).ativo(UPDATED_ATIVO);

        restFuncaoPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuncaoPessoa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuncaoPessoa))
            )
            .andExpect(status().isOk());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
        FuncaoPessoa testFuncaoPessoa = funcaoPessoaList.get(funcaoPessoaList.size() - 1);
        assertThat(testFuncaoPessoa.getFuncao()).isEqualTo(UPDATED_FUNCAO);
        assertThat(testFuncaoPessoa.getCriado()).isEqualTo(UPDATED_CRIADO);
        assertThat(testFuncaoPessoa.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void patchNonExistingFuncaoPessoa() throws Exception {
        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();
        funcaoPessoa.setId(count.incrementAndGet());

        // Create the FuncaoPessoa
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuncaoPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, funcaoPessoaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuncaoPessoa() throws Exception {
        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();
        funcaoPessoa.setId(count.incrementAndGet());

        // Create the FuncaoPessoa
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncaoPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuncaoPessoa() throws Exception {
        int databaseSizeBeforeUpdate = funcaoPessoaRepository.findAll().size();
        funcaoPessoa.setId(count.incrementAndGet());

        // Create the FuncaoPessoa
        FuncaoPessoaDTO funcaoPessoaDTO = funcaoPessoaMapper.toDto(funcaoPessoa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuncaoPessoaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(funcaoPessoaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FuncaoPessoa in the database
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuncaoPessoa() throws Exception {
        // Initialize the database
        funcaoPessoaRepository.saveAndFlush(funcaoPessoa);

        int databaseSizeBeforeDelete = funcaoPessoaRepository.findAll().size();

        // Delete the funcaoPessoa
        restFuncaoPessoaMockMvc
            .perform(delete(ENTITY_API_URL_ID, funcaoPessoa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FuncaoPessoa> funcaoPessoaList = funcaoPessoaRepository.findAll();
        assertThat(funcaoPessoaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
