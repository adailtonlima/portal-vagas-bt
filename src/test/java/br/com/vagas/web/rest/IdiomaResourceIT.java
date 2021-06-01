package br.com.vagas.web.rest;

import static br.com.vagas.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.vagas.IntegrationTest;
import br.com.vagas.domain.Idioma;
import br.com.vagas.domain.Pessoa;
import br.com.vagas.domain.enumeration.NivelIdioma;
import br.com.vagas.repository.IdiomaRepository;
import br.com.vagas.service.criteria.IdiomaCriteria;
import br.com.vagas.service.dto.IdiomaDTO;
import br.com.vagas.service.mapper.IdiomaMapper;
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
 * Integration tests for the {@link IdiomaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdiomaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final NivelIdioma DEFAULT_NIVEL = NivelIdioma.LEITURA;
    private static final NivelIdioma UPDATED_NIVEL = NivelIdioma.ESCRITA;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/idiomas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IdiomaRepository idiomaRepository;

    @Autowired
    private IdiomaMapper idiomaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdiomaMockMvc;

    private Idioma idioma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Idioma createEntity(EntityManager em) {
        Idioma idioma = new Idioma().nome(DEFAULT_NOME).nivel(DEFAULT_NIVEL).criado(DEFAULT_CRIADO);
        return idioma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Idioma createUpdatedEntity(EntityManager em) {
        Idioma idioma = new Idioma().nome(UPDATED_NOME).nivel(UPDATED_NIVEL).criado(UPDATED_CRIADO);
        return idioma;
    }

    @BeforeEach
    public void initTest() {
        idioma = createEntity(em);
    }

    @Test
    @Transactional
    void createIdioma() throws Exception {
        int databaseSizeBeforeCreate = idiomaRepository.findAll().size();
        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);
        restIdiomaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idiomaDTO)))
            .andExpect(status().isCreated());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeCreate + 1);
        Idioma testIdioma = idiomaList.get(idiomaList.size() - 1);
        assertThat(testIdioma.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testIdioma.getNivel()).isEqualTo(DEFAULT_NIVEL);
        assertThat(testIdioma.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createIdiomaWithExistingId() throws Exception {
        // Create the Idioma with an existing ID
        idioma.setId(1L);
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        int databaseSizeBeforeCreate = idiomaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdiomaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idiomaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = idiomaRepository.findAll().size();
        // set the field null
        idioma.setCriado(null);

        // Create the Idioma, which fails.
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        restIdiomaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idiomaDTO)))
            .andExpect(status().isBadRequest());

        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdiomas() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList
        restIdiomaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idioma.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getIdioma() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get the idioma
        restIdiomaMockMvc
            .perform(get(ENTITY_API_URL_ID, idioma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(idioma.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.nivel").value(DEFAULT_NIVEL.toString()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getIdiomasByIdFiltering() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        Long id = idioma.getId();

        defaultIdiomaShouldBeFound("id.equals=" + id);
        defaultIdiomaShouldNotBeFound("id.notEquals=" + id);

        defaultIdiomaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIdiomaShouldNotBeFound("id.greaterThan=" + id);

        defaultIdiomaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIdiomaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIdiomasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nome equals to DEFAULT_NOME
        defaultIdiomaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the idiomaList where nome equals to UPDATED_NOME
        defaultIdiomaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIdiomasByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nome not equals to DEFAULT_NOME
        defaultIdiomaShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the idiomaList where nome not equals to UPDATED_NOME
        defaultIdiomaShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIdiomasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultIdiomaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the idiomaList where nome equals to UPDATED_NOME
        defaultIdiomaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIdiomasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nome is not null
        defaultIdiomaShouldBeFound("nome.specified=true");

        // Get all the idiomaList where nome is null
        defaultIdiomaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllIdiomasByNomeContainsSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nome contains DEFAULT_NOME
        defaultIdiomaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the idiomaList where nome contains UPDATED_NOME
        defaultIdiomaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIdiomasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nome does not contain DEFAULT_NOME
        defaultIdiomaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the idiomaList where nome does not contain UPDATED_NOME
        defaultIdiomaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllIdiomasByNivelIsEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nivel equals to DEFAULT_NIVEL
        defaultIdiomaShouldBeFound("nivel.equals=" + DEFAULT_NIVEL);

        // Get all the idiomaList where nivel equals to UPDATED_NIVEL
        defaultIdiomaShouldNotBeFound("nivel.equals=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllIdiomasByNivelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nivel not equals to DEFAULT_NIVEL
        defaultIdiomaShouldNotBeFound("nivel.notEquals=" + DEFAULT_NIVEL);

        // Get all the idiomaList where nivel not equals to UPDATED_NIVEL
        defaultIdiomaShouldBeFound("nivel.notEquals=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllIdiomasByNivelIsInShouldWork() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nivel in DEFAULT_NIVEL or UPDATED_NIVEL
        defaultIdiomaShouldBeFound("nivel.in=" + DEFAULT_NIVEL + "," + UPDATED_NIVEL);

        // Get all the idiomaList where nivel equals to UPDATED_NIVEL
        defaultIdiomaShouldNotBeFound("nivel.in=" + UPDATED_NIVEL);
    }

    @Test
    @Transactional
    void getAllIdiomasByNivelIsNullOrNotNull() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where nivel is not null
        defaultIdiomaShouldBeFound("nivel.specified=true");

        // Get all the idiomaList where nivel is null
        defaultIdiomaShouldNotBeFound("nivel.specified=false");
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado equals to DEFAULT_CRIADO
        defaultIdiomaShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the idiomaList where criado equals to UPDATED_CRIADO
        defaultIdiomaShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado not equals to DEFAULT_CRIADO
        defaultIdiomaShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the idiomaList where criado not equals to UPDATED_CRIADO
        defaultIdiomaShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultIdiomaShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the idiomaList where criado equals to UPDATED_CRIADO
        defaultIdiomaShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado is not null
        defaultIdiomaShouldBeFound("criado.specified=true");

        // Get all the idiomaList where criado is null
        defaultIdiomaShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado is greater than or equal to DEFAULT_CRIADO
        defaultIdiomaShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the idiomaList where criado is greater than or equal to UPDATED_CRIADO
        defaultIdiomaShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado is less than or equal to DEFAULT_CRIADO
        defaultIdiomaShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the idiomaList where criado is less than or equal to SMALLER_CRIADO
        defaultIdiomaShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado is less than DEFAULT_CRIADO
        defaultIdiomaShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the idiomaList where criado is less than UPDATED_CRIADO
        defaultIdiomaShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllIdiomasByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        // Get all the idiomaList where criado is greater than DEFAULT_CRIADO
        defaultIdiomaShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the idiomaList where criado is greater than SMALLER_CRIADO
        defaultIdiomaShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllIdiomasByPessoaIsEqualToSomething() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);
        Pessoa pessoa = PessoaResourceIT.createEntity(em);
        em.persist(pessoa);
        em.flush();
        idioma.setPessoa(pessoa);
        idiomaRepository.saveAndFlush(idioma);
        Long pessoaId = pessoa.getId();

        // Get all the idiomaList where pessoa equals to pessoaId
        defaultIdiomaShouldBeFound("pessoaId.equals=" + pessoaId);

        // Get all the idiomaList where pessoa equals to (pessoaId + 1)
        defaultIdiomaShouldNotBeFound("pessoaId.equals=" + (pessoaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIdiomaShouldBeFound(String filter) throws Exception {
        restIdiomaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idioma.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].nivel").value(hasItem(DEFAULT_NIVEL.toString())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restIdiomaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIdiomaShouldNotBeFound(String filter) throws Exception {
        restIdiomaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIdiomaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIdioma() throws Exception {
        // Get the idioma
        restIdiomaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIdioma() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();

        // Update the idioma
        Idioma updatedIdioma = idiomaRepository.findById(idioma.getId()).get();
        // Disconnect from session so that the updates on updatedIdioma are not directly saved in db
        em.detach(updatedIdioma);
        updatedIdioma.nome(UPDATED_NOME).nivel(UPDATED_NIVEL).criado(UPDATED_CRIADO);
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(updatedIdioma);

        restIdiomaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idiomaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(idiomaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
        Idioma testIdioma = idiomaList.get(idiomaList.size() - 1);
        assertThat(testIdioma.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testIdioma.getNivel()).isEqualTo(UPDATED_NIVEL);
        assertThat(testIdioma.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();
        idioma.setId(count.incrementAndGet());

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdiomaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idiomaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(idiomaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();
        idioma.setId(count.incrementAndGet());

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdiomaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(idiomaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();
        idioma.setId(count.incrementAndGet());

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdiomaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(idiomaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdiomaWithPatch() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();

        // Update the idioma using partial update
        Idioma partialUpdatedIdioma = new Idioma();
        partialUpdatedIdioma.setId(idioma.getId());

        partialUpdatedIdioma.nome(UPDATED_NOME);

        restIdiomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdioma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIdioma))
            )
            .andExpect(status().isOk());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
        Idioma testIdioma = idiomaList.get(idiomaList.size() - 1);
        assertThat(testIdioma.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testIdioma.getNivel()).isEqualTo(DEFAULT_NIVEL);
        assertThat(testIdioma.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateIdiomaWithPatch() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();

        // Update the idioma using partial update
        Idioma partialUpdatedIdioma = new Idioma();
        partialUpdatedIdioma.setId(idioma.getId());

        partialUpdatedIdioma.nome(UPDATED_NOME).nivel(UPDATED_NIVEL).criado(UPDATED_CRIADO);

        restIdiomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdioma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIdioma))
            )
            .andExpect(status().isOk());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
        Idioma testIdioma = idiomaList.get(idiomaList.size() - 1);
        assertThat(testIdioma.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testIdioma.getNivel()).isEqualTo(UPDATED_NIVEL);
        assertThat(testIdioma.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();
        idioma.setId(count.incrementAndGet());

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdiomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, idiomaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(idiomaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();
        idioma.setId(count.incrementAndGet());

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdiomaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(idiomaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdioma() throws Exception {
        int databaseSizeBeforeUpdate = idiomaRepository.findAll().size();
        idioma.setId(count.incrementAndGet());

        // Create the Idioma
        IdiomaDTO idiomaDTO = idiomaMapper.toDto(idioma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdiomaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(idiomaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Idioma in the database
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdioma() throws Exception {
        // Initialize the database
        idiomaRepository.saveAndFlush(idioma);

        int databaseSizeBeforeDelete = idiomaRepository.findAll().size();

        // Delete the idioma
        restIdiomaMockMvc
            .perform(delete(ENTITY_API_URL_ID, idioma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Idioma> idiomaList = idiomaRepository.findAll();
        assertThat(idiomaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
