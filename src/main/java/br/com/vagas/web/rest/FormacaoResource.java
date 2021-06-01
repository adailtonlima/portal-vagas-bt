package br.com.vagas.web.rest;

import br.com.vagas.repository.FormacaoRepository;
import br.com.vagas.service.FormacaoQueryService;
import br.com.vagas.service.FormacaoService;
import br.com.vagas.service.criteria.FormacaoCriteria;
import br.com.vagas.service.dto.FormacaoDTO;
import br.com.vagas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.vagas.domain.Formacao}.
 */
@RestController
@RequestMapping("/api")
public class FormacaoResource {

    private final Logger log = LoggerFactory.getLogger(FormacaoResource.class);

    private static final String ENTITY_NAME = "formacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormacaoService formacaoService;

    private final FormacaoRepository formacaoRepository;

    private final FormacaoQueryService formacaoQueryService;

    public FormacaoResource(
        FormacaoService formacaoService,
        FormacaoRepository formacaoRepository,
        FormacaoQueryService formacaoQueryService
    ) {
        this.formacaoService = formacaoService;
        this.formacaoRepository = formacaoRepository;
        this.formacaoQueryService = formacaoQueryService;
    }

    /**
     * {@code POST  /formacaos} : Create a new formacao.
     *
     * @param formacaoDTO the formacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formacaoDTO, or with status {@code 400 (Bad Request)} if the formacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formacaos")
    public ResponseEntity<FormacaoDTO> createFormacao(@Valid @RequestBody FormacaoDTO formacaoDTO) throws URISyntaxException {
        log.debug("REST request to save Formacao : {}", formacaoDTO);
        if (formacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new formacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormacaoDTO result = formacaoService.save(formacaoDTO);
        return ResponseEntity
            .created(new URI("/api/formacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formacaos/:id} : Updates an existing formacao.
     *
     * @param id the id of the formacaoDTO to save.
     * @param formacaoDTO the formacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formacaoDTO,
     * or with status {@code 400 (Bad Request)} if the formacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formacaos/{id}")
    public ResponseEntity<FormacaoDTO> updateFormacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormacaoDTO formacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Formacao : {}, {}", id, formacaoDTO);
        if (formacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormacaoDTO result = formacaoService.save(formacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formacaos/:id} : Partial updates given fields of an existing formacao, field will ignore if it is null
     *
     * @param id the id of the formacaoDTO to save.
     * @param formacaoDTO the formacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formacaoDTO,
     * or with status {@code 400 (Bad Request)} if the formacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formacaos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FormacaoDTO> partialUpdateFormacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormacaoDTO formacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Formacao partially : {}, {}", id, formacaoDTO);
        if (formacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormacaoDTO> result = formacaoService.partialUpdate(formacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formacaos} : get all the formacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formacaos in body.
     */
    @GetMapping("/formacaos")
    public ResponseEntity<List<FormacaoDTO>> getAllFormacaos(FormacaoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Formacaos by criteria: {}", criteria);
        Page<FormacaoDTO> page = formacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formacaos/count} : count all the formacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/formacaos/count")
    public ResponseEntity<Long> countFormacaos(FormacaoCriteria criteria) {
        log.debug("REST request to count Formacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(formacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /formacaos/:id} : get the "id" formacao.
     *
     * @param id the id of the formacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formacaos/{id}")
    public ResponseEntity<FormacaoDTO> getFormacao(@PathVariable Long id) {
        log.debug("REST request to get Formacao : {}", id);
        Optional<FormacaoDTO> formacaoDTO = formacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formacaoDTO);
    }

    /**
     * {@code DELETE  /formacaos/:id} : delete the "id" formacao.
     *
     * @param id the id of the formacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formacaos/{id}")
    public ResponseEntity<Void> deleteFormacao(@PathVariable Long id) {
        log.debug("REST request to delete Formacao : {}", id);
        formacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
