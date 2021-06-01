package br.com.vagas.web.rest;

import br.com.vagas.repository.FuncaoPessoaRepository;
import br.com.vagas.service.FuncaoPessoaQueryService;
import br.com.vagas.service.FuncaoPessoaService;
import br.com.vagas.service.criteria.FuncaoPessoaCriteria;
import br.com.vagas.service.dto.FuncaoPessoaDTO;
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
 * REST controller for managing {@link br.com.vagas.domain.FuncaoPessoa}.
 */
@RestController
@RequestMapping("/api")
public class FuncaoPessoaResource {

    private final Logger log = LoggerFactory.getLogger(FuncaoPessoaResource.class);

    private static final String ENTITY_NAME = "funcaoPessoa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuncaoPessoaService funcaoPessoaService;

    private final FuncaoPessoaRepository funcaoPessoaRepository;

    private final FuncaoPessoaQueryService funcaoPessoaQueryService;

    public FuncaoPessoaResource(
        FuncaoPessoaService funcaoPessoaService,
        FuncaoPessoaRepository funcaoPessoaRepository,
        FuncaoPessoaQueryService funcaoPessoaQueryService
    ) {
        this.funcaoPessoaService = funcaoPessoaService;
        this.funcaoPessoaRepository = funcaoPessoaRepository;
        this.funcaoPessoaQueryService = funcaoPessoaQueryService;
    }

    /**
     * {@code POST  /funcao-pessoas} : Create a new funcaoPessoa.
     *
     * @param funcaoPessoaDTO the funcaoPessoaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funcaoPessoaDTO, or with status {@code 400 (Bad Request)} if the funcaoPessoa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funcao-pessoas")
    public ResponseEntity<FuncaoPessoaDTO> createFuncaoPessoa(@Valid @RequestBody FuncaoPessoaDTO funcaoPessoaDTO)
        throws URISyntaxException {
        log.debug("REST request to save FuncaoPessoa : {}", funcaoPessoaDTO);
        if (funcaoPessoaDTO.getId() != null) {
            throw new BadRequestAlertException("A new funcaoPessoa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FuncaoPessoaDTO result = funcaoPessoaService.save(funcaoPessoaDTO);
        return ResponseEntity
            .created(new URI("/api/funcao-pessoas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /funcao-pessoas/:id} : Updates an existing funcaoPessoa.
     *
     * @param id the id of the funcaoPessoaDTO to save.
     * @param funcaoPessoaDTO the funcaoPessoaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcaoPessoaDTO,
     * or with status {@code 400 (Bad Request)} if the funcaoPessoaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funcaoPessoaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/funcao-pessoas/{id}")
    public ResponseEntity<FuncaoPessoaDTO> updateFuncaoPessoa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FuncaoPessoaDTO funcaoPessoaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FuncaoPessoa : {}, {}", id, funcaoPessoaDTO);
        if (funcaoPessoaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcaoPessoaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcaoPessoaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FuncaoPessoaDTO result = funcaoPessoaService.save(funcaoPessoaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funcaoPessoaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /funcao-pessoas/:id} : Partial updates given fields of an existing funcaoPessoa, field will ignore if it is null
     *
     * @param id the id of the funcaoPessoaDTO to save.
     * @param funcaoPessoaDTO the funcaoPessoaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcaoPessoaDTO,
     * or with status {@code 400 (Bad Request)} if the funcaoPessoaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the funcaoPessoaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the funcaoPessoaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/funcao-pessoas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FuncaoPessoaDTO> partialUpdateFuncaoPessoa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FuncaoPessoaDTO funcaoPessoaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FuncaoPessoa partially : {}, {}", id, funcaoPessoaDTO);
        if (funcaoPessoaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcaoPessoaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcaoPessoaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FuncaoPessoaDTO> result = funcaoPessoaService.partialUpdate(funcaoPessoaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, funcaoPessoaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /funcao-pessoas} : get all the funcaoPessoas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funcaoPessoas in body.
     */
    @GetMapping("/funcao-pessoas")
    public ResponseEntity<List<FuncaoPessoaDTO>> getAllFuncaoPessoas(FuncaoPessoaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FuncaoPessoas by criteria: {}", criteria);
        Page<FuncaoPessoaDTO> page = funcaoPessoaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /funcao-pessoas/count} : count all the funcaoPessoas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/funcao-pessoas/count")
    public ResponseEntity<Long> countFuncaoPessoas(FuncaoPessoaCriteria criteria) {
        log.debug("REST request to count FuncaoPessoas by criteria: {}", criteria);
        return ResponseEntity.ok().body(funcaoPessoaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /funcao-pessoas/:id} : get the "id" funcaoPessoa.
     *
     * @param id the id of the funcaoPessoaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funcaoPessoaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funcao-pessoas/{id}")
    public ResponseEntity<FuncaoPessoaDTO> getFuncaoPessoa(@PathVariable Long id) {
        log.debug("REST request to get FuncaoPessoa : {}", id);
        Optional<FuncaoPessoaDTO> funcaoPessoaDTO = funcaoPessoaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(funcaoPessoaDTO);
    }

    /**
     * {@code DELETE  /funcao-pessoas/:id} : delete the "id" funcaoPessoa.
     *
     * @param id the id of the funcaoPessoaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/funcao-pessoas/{id}")
    public ResponseEntity<Void> deleteFuncaoPessoa(@PathVariable Long id) {
        log.debug("REST request to delete FuncaoPessoa : {}", id);
        funcaoPessoaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
