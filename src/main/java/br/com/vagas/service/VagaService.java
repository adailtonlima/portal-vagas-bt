package br.com.vagas.service;

import br.com.vagas.service.dto.VagaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.vagas.domain.Vaga}.
 */
public interface VagaService {
    /**
     * Save a vaga.
     *
     * @param vagaDTO the entity to save.
     * @return the persisted entity.
     */
    VagaDTO save(VagaDTO vagaDTO);

    /**
     * Partially updates a vaga.
     *
     * @param vagaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VagaDTO> partialUpdate(VagaDTO vagaDTO);

    /**
     * Get all the vagas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VagaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vaga.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VagaDTO> findOne(Long id);

    /**
     * Delete the "id" vaga.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
