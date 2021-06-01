package br.com.vagas.web.rest;

import br.com.vagas.repository.VagaRepository;
import br.com.vagas.service.VagaQueryService;
import br.com.vagas.service.VagaService;
import br.com.vagas.service.criteria.VagaCriteria;
import br.com.vagas.service.dto.VagaDTO;
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
 * REST controller for managing {@link br.com.vagas.domain.Vaga}.
 */
@RestController
@RequestMapping("/api")
public class VagaResource {

    private final Logger log = LoggerFactory.getLogger(VagaResource.class);

    private static final String ENTITY_NAME = "vaga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VagaService vagaService;

    private final VagaRepository vagaRepository;

    private final VagaQueryService vagaQueryService;

    public VagaResource(VagaService vagaService, VagaRepository vagaRepository, VagaQueryService vagaQueryService) {
        this.vagaService = vagaService;
        this.vagaRepository = vagaRepository;
        this.vagaQueryService = vagaQueryService;
    }

    /**
     * {@code POST  /vagas} : Create a new vaga.
     *
     * @param vagaDTO the vagaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vagaDTO, or with status {@code 400 (Bad Request)} if the vaga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vagas")
    public ResponseEntity<VagaDTO> createVaga(@Valid @RequestBody VagaDTO vagaDTO) throws URISyntaxException {
        log.debug("REST request to save Vaga : {}", vagaDTO);
        if (vagaDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VagaDTO result = vagaService.save(vagaDTO);
        return ResponseEntity
            .created(new URI("/api/vagas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vagas/:id} : Updates an existing vaga.
     *
     * @param id the id of the vagaDTO to save.
     * @param vagaDTO the vagaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vagaDTO,
     * or with status {@code 400 (Bad Request)} if the vagaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vagaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vagas/{id}")
    public ResponseEntity<VagaDTO> updateVaga(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VagaDTO vagaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vaga : {}, {}", id, vagaDTO);
        if (vagaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vagaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vagaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VagaDTO result = vagaService.save(vagaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vagaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vagas/:id} : Partial updates given fields of an existing vaga, field will ignore if it is null
     *
     * @param id the id of the vagaDTO to save.
     * @param vagaDTO the vagaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vagaDTO,
     * or with status {@code 400 (Bad Request)} if the vagaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vagaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vagaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vagas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VagaDTO> partialUpdateVaga(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VagaDTO vagaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vaga partially : {}, {}", id, vagaDTO);
        if (vagaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vagaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vagaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VagaDTO> result = vagaService.partialUpdate(vagaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vagaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vagas} : get all the vagas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vagas in body.
     */
    @GetMapping("/vagas")
    public ResponseEntity<List<VagaDTO>> getAllVagas(VagaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vagas by criteria: {}", criteria);
        Page<VagaDTO> page = vagaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vagas/count} : count all the vagas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vagas/count")
    public ResponseEntity<Long> countVagas(VagaCriteria criteria) {
        log.debug("REST request to count Vagas by criteria: {}", criteria);
        return ResponseEntity.ok().body(vagaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vagas/:id} : get the "id" vaga.
     *
     * @param id the id of the vagaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vagaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vagas/{id}")
    public ResponseEntity<VagaDTO> getVaga(@PathVariable Long id) {
        log.debug("REST request to get Vaga : {}", id);
        Optional<VagaDTO> vagaDTO = vagaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vagaDTO);
    }

    /**
     * {@code DELETE  /vagas/:id} : delete the "id" vaga.
     *
     * @param id the id of the vagaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vagas/{id}")
    public ResponseEntity<Void> deleteVaga(@PathVariable Long id) {
        log.debug("REST request to delete Vaga : {}", id);
        vagaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
