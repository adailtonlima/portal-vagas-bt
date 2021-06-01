package br.com.vagas.web.rest;

import br.com.vagas.repository.AreaAtuacaoRepository;
import br.com.vagas.service.AreaAtuacaoQueryService;
import br.com.vagas.service.AreaAtuacaoService;
import br.com.vagas.service.criteria.AreaAtuacaoCriteria;
import br.com.vagas.service.dto.AreaAtuacaoDTO;
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
 * REST controller for managing {@link br.com.vagas.domain.AreaAtuacao}.
 */
@RestController
@RequestMapping("/api")
public class AreaAtuacaoResource {

    private final Logger log = LoggerFactory.getLogger(AreaAtuacaoResource.class);

    private static final String ENTITY_NAME = "areaAtuacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AreaAtuacaoService areaAtuacaoService;

    private final AreaAtuacaoRepository areaAtuacaoRepository;

    private final AreaAtuacaoQueryService areaAtuacaoQueryService;

    public AreaAtuacaoResource(
        AreaAtuacaoService areaAtuacaoService,
        AreaAtuacaoRepository areaAtuacaoRepository,
        AreaAtuacaoQueryService areaAtuacaoQueryService
    ) {
        this.areaAtuacaoService = areaAtuacaoService;
        this.areaAtuacaoRepository = areaAtuacaoRepository;
        this.areaAtuacaoQueryService = areaAtuacaoQueryService;
    }

    /**
     * {@code POST  /area-atuacaos} : Create a new areaAtuacao.
     *
     * @param areaAtuacaoDTO the areaAtuacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new areaAtuacaoDTO, or with status {@code 400 (Bad Request)} if the areaAtuacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/area-atuacaos")
    public ResponseEntity<AreaAtuacaoDTO> createAreaAtuacao(@Valid @RequestBody AreaAtuacaoDTO areaAtuacaoDTO) throws URISyntaxException {
        log.debug("REST request to save AreaAtuacao : {}", areaAtuacaoDTO);
        if (areaAtuacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new areaAtuacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AreaAtuacaoDTO result = areaAtuacaoService.save(areaAtuacaoDTO);
        return ResponseEntity
            .created(new URI("/api/area-atuacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /area-atuacaos/:id} : Updates an existing areaAtuacao.
     *
     * @param id the id of the areaAtuacaoDTO to save.
     * @param areaAtuacaoDTO the areaAtuacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaAtuacaoDTO,
     * or with status {@code 400 (Bad Request)} if the areaAtuacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the areaAtuacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/area-atuacaos/{id}")
    public ResponseEntity<AreaAtuacaoDTO> updateAreaAtuacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AreaAtuacaoDTO areaAtuacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AreaAtuacao : {}, {}", id, areaAtuacaoDTO);
        if (areaAtuacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, areaAtuacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!areaAtuacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AreaAtuacaoDTO result = areaAtuacaoService.save(areaAtuacaoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, areaAtuacaoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /area-atuacaos/:id} : Partial updates given fields of an existing areaAtuacao, field will ignore if it is null
     *
     * @param id the id of the areaAtuacaoDTO to save.
     * @param areaAtuacaoDTO the areaAtuacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaAtuacaoDTO,
     * or with status {@code 400 (Bad Request)} if the areaAtuacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the areaAtuacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the areaAtuacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/area-atuacaos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AreaAtuacaoDTO> partialUpdateAreaAtuacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AreaAtuacaoDTO areaAtuacaoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AreaAtuacao partially : {}, {}", id, areaAtuacaoDTO);
        if (areaAtuacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, areaAtuacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!areaAtuacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AreaAtuacaoDTO> result = areaAtuacaoService.partialUpdate(areaAtuacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, areaAtuacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /area-atuacaos} : get all the areaAtuacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of areaAtuacaos in body.
     */
    @GetMapping("/area-atuacaos")
    public ResponseEntity<List<AreaAtuacaoDTO>> getAllAreaAtuacaos(AreaAtuacaoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AreaAtuacaos by criteria: {}", criteria);
        Page<AreaAtuacaoDTO> page = areaAtuacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /area-atuacaos/count} : count all the areaAtuacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/area-atuacaos/count")
    public ResponseEntity<Long> countAreaAtuacaos(AreaAtuacaoCriteria criteria) {
        log.debug("REST request to count AreaAtuacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(areaAtuacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /area-atuacaos/:id} : get the "id" areaAtuacao.
     *
     * @param id the id of the areaAtuacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the areaAtuacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/area-atuacaos/{id}")
    public ResponseEntity<AreaAtuacaoDTO> getAreaAtuacao(@PathVariable Long id) {
        log.debug("REST request to get AreaAtuacao : {}", id);
        Optional<AreaAtuacaoDTO> areaAtuacaoDTO = areaAtuacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(areaAtuacaoDTO);
    }

    /**
     * {@code DELETE  /area-atuacaos/:id} : delete the "id" areaAtuacao.
     *
     * @param id the id of the areaAtuacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/area-atuacaos/{id}")
    public ResponseEntity<Void> deleteAreaAtuacao(@PathVariable Long id) {
        log.debug("REST request to delete AreaAtuacao : {}", id);
        areaAtuacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
