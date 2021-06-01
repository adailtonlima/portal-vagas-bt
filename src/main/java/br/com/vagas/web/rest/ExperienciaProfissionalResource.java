package br.com.vagas.web.rest;

import br.com.vagas.repository.ExperienciaProfissionalRepository;
import br.com.vagas.service.ExperienciaProfissionalQueryService;
import br.com.vagas.service.ExperienciaProfissionalService;
import br.com.vagas.service.criteria.ExperienciaProfissionalCriteria;
import br.com.vagas.service.dto.ExperienciaProfissionalDTO;
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
 * REST controller for managing {@link br.com.vagas.domain.ExperienciaProfissional}.
 */
@RestController
@RequestMapping("/api")
public class ExperienciaProfissionalResource {

    private final Logger log = LoggerFactory.getLogger(ExperienciaProfissionalResource.class);

    private static final String ENTITY_NAME = "experienciaProfissional";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExperienciaProfissionalService experienciaProfissionalService;

    private final ExperienciaProfissionalRepository experienciaProfissionalRepository;

    private final ExperienciaProfissionalQueryService experienciaProfissionalQueryService;

    public ExperienciaProfissionalResource(
        ExperienciaProfissionalService experienciaProfissionalService,
        ExperienciaProfissionalRepository experienciaProfissionalRepository,
        ExperienciaProfissionalQueryService experienciaProfissionalQueryService
    ) {
        this.experienciaProfissionalService = experienciaProfissionalService;
        this.experienciaProfissionalRepository = experienciaProfissionalRepository;
        this.experienciaProfissionalQueryService = experienciaProfissionalQueryService;
    }

    /**
     * {@code POST  /experiencia-profissionals} : Create a new experienciaProfissional.
     *
     * @param experienciaProfissionalDTO the experienciaProfissionalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new experienciaProfissionalDTO, or with status {@code 400 (Bad Request)} if the experienciaProfissional has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/experiencia-profissionals")
    public ResponseEntity<ExperienciaProfissionalDTO> createExperienciaProfissional(
        @Valid @RequestBody ExperienciaProfissionalDTO experienciaProfissionalDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ExperienciaProfissional : {}", experienciaProfissionalDTO);
        if (experienciaProfissionalDTO.getId() != null) {
            throw new BadRequestAlertException("A new experienciaProfissional cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExperienciaProfissionalDTO result = experienciaProfissionalService.save(experienciaProfissionalDTO);
        return ResponseEntity
            .created(new URI("/api/experiencia-profissionals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /experiencia-profissionals/:id} : Updates an existing experienciaProfissional.
     *
     * @param id the id of the experienciaProfissionalDTO to save.
     * @param experienciaProfissionalDTO the experienciaProfissionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experienciaProfissionalDTO,
     * or with status {@code 400 (Bad Request)} if the experienciaProfissionalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the experienciaProfissionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/experiencia-profissionals/{id}")
    public ResponseEntity<ExperienciaProfissionalDTO> updateExperienciaProfissional(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExperienciaProfissionalDTO experienciaProfissionalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExperienciaProfissional : {}, {}", id, experienciaProfissionalDTO);
        if (experienciaProfissionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, experienciaProfissionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!experienciaProfissionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExperienciaProfissionalDTO result = experienciaProfissionalService.save(experienciaProfissionalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, experienciaProfissionalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /experiencia-profissionals/:id} : Partial updates given fields of an existing experienciaProfissional, field will ignore if it is null
     *
     * @param id the id of the experienciaProfissionalDTO to save.
     * @param experienciaProfissionalDTO the experienciaProfissionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experienciaProfissionalDTO,
     * or with status {@code 400 (Bad Request)} if the experienciaProfissionalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the experienciaProfissionalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the experienciaProfissionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/experiencia-profissionals/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExperienciaProfissionalDTO> partialUpdateExperienciaProfissional(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExperienciaProfissionalDTO experienciaProfissionalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExperienciaProfissional partially : {}, {}", id, experienciaProfissionalDTO);
        if (experienciaProfissionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, experienciaProfissionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!experienciaProfissionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExperienciaProfissionalDTO> result = experienciaProfissionalService.partialUpdate(experienciaProfissionalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, experienciaProfissionalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /experiencia-profissionals} : get all the experienciaProfissionals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of experienciaProfissionals in body.
     */
    @GetMapping("/experiencia-profissionals")
    public ResponseEntity<List<ExperienciaProfissionalDTO>> getAllExperienciaProfissionals(
        ExperienciaProfissionalCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ExperienciaProfissionals by criteria: {}", criteria);
        Page<ExperienciaProfissionalDTO> page = experienciaProfissionalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /experiencia-profissionals/count} : count all the experienciaProfissionals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/experiencia-profissionals/count")
    public ResponseEntity<Long> countExperienciaProfissionals(ExperienciaProfissionalCriteria criteria) {
        log.debug("REST request to count ExperienciaProfissionals by criteria: {}", criteria);
        return ResponseEntity.ok().body(experienciaProfissionalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /experiencia-profissionals/:id} : get the "id" experienciaProfissional.
     *
     * @param id the id of the experienciaProfissionalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the experienciaProfissionalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/experiencia-profissionals/{id}")
    public ResponseEntity<ExperienciaProfissionalDTO> getExperienciaProfissional(@PathVariable Long id) {
        log.debug("REST request to get ExperienciaProfissional : {}", id);
        Optional<ExperienciaProfissionalDTO> experienciaProfissionalDTO = experienciaProfissionalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(experienciaProfissionalDTO);
    }

    /**
     * {@code DELETE  /experiencia-profissionals/:id} : delete the "id" experienciaProfissional.
     *
     * @param id the id of the experienciaProfissionalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experiencia-profissionals/{id}")
    public ResponseEntity<Void> deleteExperienciaProfissional(@PathVariable Long id) {
        log.debug("REST request to delete ExperienciaProfissional : {}", id);
        experienciaProfissionalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
