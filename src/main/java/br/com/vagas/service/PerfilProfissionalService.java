package br.com.vagas.service;

import br.com.vagas.service.dto.PerfilProfissionalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.vagas.domain.PerfilProfissional}.
 */
public interface PerfilProfissionalService {
    /**
     * Save a perfilProfissional.
     *
     * @param perfilProfissionalDTO the entity to save.
     * @return the persisted entity.
     */
    PerfilProfissionalDTO save(PerfilProfissionalDTO perfilProfissionalDTO);

    /**
     * Partially updates a perfilProfissional.
     *
     * @param perfilProfissionalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PerfilProfissionalDTO> partialUpdate(PerfilProfissionalDTO perfilProfissionalDTO);

    /**
     * Get all the perfilProfissionals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PerfilProfissionalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" perfilProfissional.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PerfilProfissionalDTO> findOne(Long id);

    /**
     * Delete the "id" perfilProfissional.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
