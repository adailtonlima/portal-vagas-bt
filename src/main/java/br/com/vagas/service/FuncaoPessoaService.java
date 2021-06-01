package br.com.vagas.service;

import br.com.vagas.service.dto.FuncaoPessoaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.vagas.domain.FuncaoPessoa}.
 */
public interface FuncaoPessoaService {
    /**
     * Save a funcaoPessoa.
     *
     * @param funcaoPessoaDTO the entity to save.
     * @return the persisted entity.
     */
    FuncaoPessoaDTO save(FuncaoPessoaDTO funcaoPessoaDTO);

    /**
     * Partially updates a funcaoPessoa.
     *
     * @param funcaoPessoaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FuncaoPessoaDTO> partialUpdate(FuncaoPessoaDTO funcaoPessoaDTO);

    /**
     * Get all the funcaoPessoas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FuncaoPessoaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" funcaoPessoa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FuncaoPessoaDTO> findOne(Long id);

    /**
     * Delete the "id" funcaoPessoa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
