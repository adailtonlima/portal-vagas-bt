package br.com.vagas.service;

import br.com.vagas.service.dto.CursoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.vagas.domain.Curso}.
 */
public interface CursoService {
    /**
     * Save a curso.
     *
     * @param cursoDTO the entity to save.
     * @return the persisted entity.
     */
    CursoDTO save(CursoDTO cursoDTO);

    /**
     * Partially updates a curso.
     *
     * @param cursoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CursoDTO> partialUpdate(CursoDTO cursoDTO);

    /**
     * Get all the cursos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CursoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" curso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CursoDTO> findOne(Long id);

    /**
     * Delete the "id" curso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
