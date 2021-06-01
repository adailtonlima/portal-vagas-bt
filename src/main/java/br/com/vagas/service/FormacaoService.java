package br.com.vagas.service;

import br.com.vagas.service.dto.FormacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.vagas.domain.Formacao}.
 */
public interface FormacaoService {
    /**
     * Save a formacao.
     *
     * @param formacaoDTO the entity to save.
     * @return the persisted entity.
     */
    FormacaoDTO save(FormacaoDTO formacaoDTO);

    /**
     * Partially updates a formacao.
     *
     * @param formacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormacaoDTO> partialUpdate(FormacaoDTO formacaoDTO);

    /**
     * Get all the formacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormacaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormacaoDTO> findOne(Long id);

    /**
     * Delete the "id" formacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
