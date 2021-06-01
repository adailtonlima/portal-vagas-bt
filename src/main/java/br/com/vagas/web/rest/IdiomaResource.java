package br.com.vagas.web.rest;

import br.com.vagas.repository.IdiomaRepository;
import br.com.vagas.service.IdiomaQueryService;
import br.com.vagas.service.IdiomaService;
import br.com.vagas.service.criteria.IdiomaCriteria;
import br.com.vagas.service.dto.IdiomaDTO;
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
 * REST controller for managing {@link br.com.vagas.domain.Idioma}.
 */
@RestController
@RequestMapping("/api")
public class IdiomaResource {

    private final Logger log = LoggerFactory.getLogger(IdiomaResource.class);

    private static final String ENTITY_NAME = "idioma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdiomaService idiomaService;

    private final IdiomaRepository idiomaRepository;

    private final IdiomaQueryService idiomaQueryService;

    public IdiomaResource(IdiomaService idiomaService, IdiomaRepository idiomaRepository, IdiomaQueryService idiomaQueryService) {
        this.idiomaService = idiomaService;
        this.idiomaRepository = idiomaRepository;
        this.idiomaQueryService = idiomaQueryService;
    }

    /**
     * {@code POST  /idiomas} : Create a new idioma.
     *
     * @param idiomaDTO the idiomaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new idiomaDTO, or with status {@code 400 (Bad Request)} if the idioma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/idiomas")
    public ResponseEntity<IdiomaDTO> createIdioma(@Valid @RequestBody IdiomaDTO idiomaDTO) throws URISyntaxException {
        log.debug("REST request to save Idioma : {}", idiomaDTO);
        if (idiomaDTO.getId() != null) {
            throw new BadRequestAlertException("A new idioma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IdiomaDTO result = idiomaService.save(idiomaDTO);
        return ResponseEntity
            .created(new URI("/api/idiomas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /idiomas/:id} : Updates an existing idioma.
     *
     * @param id the id of the idiomaDTO to save.
     * @param idiomaDTO the idiomaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idiomaDTO,
     * or with status {@code 400 (Bad Request)} if the idiomaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the idiomaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/idiomas/{id}")
    public ResponseEntity<IdiomaDTO> updateIdioma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdiomaDTO idiomaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Idioma : {}, {}", id, idiomaDTO);
        if (idiomaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idiomaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!idiomaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IdiomaDTO result = idiomaService.save(idiomaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idiomaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /idiomas/:id} : Partial updates given fields of an existing idioma, field will ignore if it is null
     *
     * @param id the id of the idiomaDTO to save.
     * @param idiomaDTO the idiomaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idiomaDTO,
     * or with status {@code 400 (Bad Request)} if the idiomaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the idiomaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the idiomaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/idiomas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IdiomaDTO> partialUpdateIdioma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdiomaDTO idiomaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Idioma partially : {}, {}", id, idiomaDTO);
        if (idiomaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idiomaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!idiomaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdiomaDTO> result = idiomaService.partialUpdate(idiomaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idiomaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /idiomas} : get all the idiomas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of idiomas in body.
     */
    @GetMapping("/idiomas")
    public ResponseEntity<List<IdiomaDTO>> getAllIdiomas(IdiomaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Idiomas by criteria: {}", criteria);
        Page<IdiomaDTO> page = idiomaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /idiomas/count} : count all the idiomas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/idiomas/count")
    public ResponseEntity<Long> countIdiomas(IdiomaCriteria criteria) {
        log.debug("REST request to count Idiomas by criteria: {}", criteria);
        return ResponseEntity.ok().body(idiomaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /idiomas/:id} : get the "id" idioma.
     *
     * @param id the id of the idiomaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the idiomaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/idiomas/{id}")
    public ResponseEntity<IdiomaDTO> getIdioma(@PathVariable Long id) {
        log.debug("REST request to get Idioma : {}", id);
        Optional<IdiomaDTO> idiomaDTO = idiomaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(idiomaDTO);
    }

    /**
     * {@code DELETE  /idiomas/:id} : delete the "id" idioma.
     *
     * @param id the id of the idiomaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/idiomas/{id}")
    public ResponseEntity<Void> deleteIdioma(@PathVariable Long id) {
        log.debug("REST request to delete Idioma : {}", id);
        idiomaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
