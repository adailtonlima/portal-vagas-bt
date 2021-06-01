package br.com.vagas.service;

import br.com.vagas.service.dto.AreaAtuacaoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.vagas.domain.AreaAtuacao}.
 */
public interface AreaAtuacaoService {
    /**
     * Save a areaAtuacao.
     *
     * @param areaAtuacaoDTO the entity to save.
     * @return the persisted entity.
     */
    AreaAtuacaoDTO save(AreaAtuacaoDTO areaAtuacaoDTO);

    /**
     * Partially updates a areaAtuacao.
     *
     * @param areaAtuacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AreaAtuacaoDTO> partialUpdate(AreaAtuacaoDTO areaAtuacaoDTO);

    /**
     * Get all the areaAtuacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AreaAtuacaoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" areaAtuacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AreaAtuacaoDTO> findOne(Long id);

    /**
     * Delete the "id" areaAtuacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
