package br.com.vagas.web.rest;

import br.com.vagas.repository.PerfilProfissionalRepository;
import br.com.vagas.service.PerfilProfissionalQueryService;
import br.com.vagas.service.PerfilProfissionalService;
import br.com.vagas.service.criteria.PerfilProfissionalCriteria;
import br.com.vagas.service.dto.PerfilProfissionalDTO;
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
 * REST controller for managing {@link br.com.vagas.domain.PerfilProfissional}.
 */
@RestController
@RequestMapping("/api")
public class PerfilProfissionalResource {

    private final Logger log = LoggerFactory.getLogger(PerfilProfissionalResource.class);

    private static final String ENTITY_NAME = "perfilProfissional";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerfilProfissionalService perfilProfissionalService;

    private final PerfilProfissionalRepository perfilProfissionalRepository;

    private final PerfilProfissionalQueryService perfilProfissionalQueryService;

    public PerfilProfissionalResource(
        PerfilProfissionalService perfilProfissionalService,
        PerfilProfissionalRepository perfilProfissionalRepository,
        PerfilProfissionalQueryService perfilProfissionalQueryService
    ) {
        this.perfilProfissionalService = perfilProfissionalService;
        this.perfilProfissionalRepository = perfilProfissionalRepository;
        this.perfilProfissionalQueryService = perfilProfissionalQueryService;
    }

    /**
     * {@code POST  /perfil-profissionals} : Create a new perfilProfissional.
     *
     * @param perfilProfissionalDTO the perfilProfissionalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perfilProfissionalDTO, or with status {@code 400 (Bad Request)} if the perfilProfissional has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/perfil-profissionals")
    public ResponseEntity<PerfilProfissionalDTO> createPerfilProfissional(@Valid @RequestBody PerfilProfissionalDTO perfilProfissionalDTO)
        throws URISyntaxException {
        log.debug("REST request to save PerfilProfissional : {}", perfilProfissionalDTO);
        if (perfilProfissionalDTO.getId() != null) {
            throw new BadRequestAlertException("A new perfilProfissional cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerfilProfissionalDTO result = perfilProfissionalService.save(perfilProfissionalDTO);
        return ResponseEntity
            .created(new URI("/api/perfil-profissionals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /perfil-profissionals/:id} : Updates an existing perfilProfissional.
     *
     * @param id the id of the perfilProfissionalDTO to save.
     * @param perfilProfissionalDTO the perfilProfissionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilProfissionalDTO,
     * or with status {@code 400 (Bad Request)} if the perfilProfissionalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perfilProfissionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/perfil-profissionals/{id}")
    public ResponseEntity<PerfilProfissionalDTO> updatePerfilProfissional(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PerfilProfissionalDTO perfilProfissionalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PerfilProfissional : {}, {}", id, perfilProfissionalDTO);
        if (perfilProfissionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilProfissionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilProfissionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PerfilProfissionalDTO result = perfilProfissionalService.save(perfilProfissionalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfilProfissionalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /perfil-profissionals/:id} : Partial updates given fields of an existing perfilProfissional, field will ignore if it is null
     *
     * @param id the id of the perfilProfissionalDTO to save.
     * @param perfilProfissionalDTO the perfilProfissionalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilProfissionalDTO,
     * or with status {@code 400 (Bad Request)} if the perfilProfissionalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the perfilProfissionalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the perfilProfissionalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/perfil-profissionals/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PerfilProfissionalDTO> partialUpdatePerfilProfissional(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PerfilProfissionalDTO perfilProfissionalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PerfilProfissional partially : {}, {}", id, perfilProfissionalDTO);
        if (perfilProfissionalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilProfissionalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilProfissionalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerfilProfissionalDTO> result = perfilProfissionalService.partialUpdate(perfilProfissionalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, perfilProfissionalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /perfil-profissionals} : get all the perfilProfissionals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perfilProfissionals in body.
     */
    @GetMapping("/perfil-profissionals")
    public ResponseEntity<List<PerfilProfissionalDTO>> getAllPerfilProfissionals(PerfilProfissionalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PerfilProfissionals by criteria: {}", criteria);
        Page<PerfilProfissionalDTO> page = perfilProfissionalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /perfil-profissionals/count} : count all the perfilProfissionals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/perfil-profissionals/count")
    public ResponseEntity<Long> countPerfilProfissionals(PerfilProfissionalCriteria criteria) {
        log.debug("REST request to count PerfilProfissionals by criteria: {}", criteria);
        return ResponseEntity.ok().body(perfilProfissionalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /perfil-profissionals/:id} : get the "id" perfilProfissional.
     *
     * @param id the id of the perfilProfissionalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfilProfissionalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/perfil-profissionals/{id}")
    public ResponseEntity<PerfilProfissionalDTO> getPerfilProfissional(@PathVariable Long id) {
        log.debug("REST request to get PerfilProfissional : {}", id);
        Optional<PerfilProfissionalDTO> perfilProfissionalDTO = perfilProfissionalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(perfilProfissionalDTO);
    }

    /**
     * {@code DELETE  /perfil-profissionals/:id} : delete the "id" perfilProfissional.
     *
     * @param id the id of the perfilProfissionalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/perfil-profissionals/{id}")
    public ResponseEntity<Void> deletePerfilProfissional(@PathVariable Long id) {
        log.debug("REST request to delete PerfilProfissional : {}", id);
        perfilProfissionalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
