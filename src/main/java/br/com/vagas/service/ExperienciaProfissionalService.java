package br.com.vagas.service;

import br.com.vagas.service.dto.ExperienciaProfissionalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.vagas.domain.ExperienciaProfissional}.
 */
public interface ExperienciaProfissionalService {
    /**
     * Save a experienciaProfissional.
     *
     * @param experienciaProfissionalDTO the entity to save.
     * @return the persisted entity.
     */
    ExperienciaProfissionalDTO save(ExperienciaProfissionalDTO experienciaProfissionalDTO);

    /**
     * Partially updates a experienciaProfissional.
     *
     * @param experienciaProfissionalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExperienciaProfissionalDTO> partialUpdate(ExperienciaProfissionalDTO experienciaProfissionalDTO);

    /**
     * Get all the experienciaProfissionals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExperienciaProfissionalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" experienciaProfissional.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExperienciaProfissionalDTO> findOne(Long id);

    /**
     * Delete the "id" experienciaProfissional.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
