package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.FuncaoPessoa;
import br.com.vagas.repository.FuncaoPessoaRepository;
import br.com.vagas.service.criteria.FuncaoPessoaCriteria;
import br.com.vagas.service.dto.FuncaoPessoaDTO;
import br.com.vagas.service.mapper.FuncaoPessoaMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FuncaoPessoa} entities in the database.
 * The main input is a {@link FuncaoPessoaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FuncaoPessoaDTO} or a {@link Page} of {@link FuncaoPessoaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FuncaoPessoaQueryService extends QueryService<FuncaoPessoa> {

    private final Logger log = LoggerFactory.getLogger(FuncaoPessoaQueryService.class);

    private final FuncaoPessoaRepository funcaoPessoaRepository;

    private final FuncaoPessoaMapper funcaoPessoaMapper;

    public FuncaoPessoaQueryService(FuncaoPessoaRepository funcaoPessoaRepository, FuncaoPessoaMapper funcaoPessoaMapper) {
        this.funcaoPessoaRepository = funcaoPessoaRepository;
        this.funcaoPessoaMapper = funcaoPessoaMapper;
    }

    /**
     * Return a {@link List} of {@link FuncaoPessoaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FuncaoPessoaDTO> findByCriteria(FuncaoPessoaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FuncaoPessoa> specification = createSpecification(criteria);
        return funcaoPessoaMapper.toDto(funcaoPessoaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FuncaoPessoaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FuncaoPessoaDTO> findByCriteria(FuncaoPessoaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FuncaoPessoa> specification = createSpecification(criteria);
        return funcaoPessoaRepository.findAll(specification, page).map(funcaoPessoaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FuncaoPessoaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FuncaoPessoa> specification = createSpecification(criteria);
        return funcaoPessoaRepository.count(specification);
    }

    /**
     * Function to convert {@link FuncaoPessoaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FuncaoPessoa> createSpecification(FuncaoPessoaCriteria criteria) {
        Specification<FuncaoPessoa> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FuncaoPessoa_.id));
            }
            if (criteria.getFuncao() != null) {
                specification = specification.and(buildSpecification(criteria.getFuncao(), FuncaoPessoa_.funcao));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), FuncaoPessoa_.criado));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), FuncaoPessoa_.ativo));
            }
            if (criteria.getPessoaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPessoaId(), root -> root.join(FuncaoPessoa_.pessoa, JoinType.LEFT).get(Pessoa_.id))
                    );
            }
        }
        return specification;
    }
}
