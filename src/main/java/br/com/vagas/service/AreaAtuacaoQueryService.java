package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.AreaAtuacao;
import br.com.vagas.repository.AreaAtuacaoRepository;
import br.com.vagas.service.criteria.AreaAtuacaoCriteria;
import br.com.vagas.service.dto.AreaAtuacaoDTO;
import br.com.vagas.service.mapper.AreaAtuacaoMapper;
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
 * Service for executing complex queries for {@link AreaAtuacao} entities in the database.
 * The main input is a {@link AreaAtuacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AreaAtuacaoDTO} or a {@link Page} of {@link AreaAtuacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AreaAtuacaoQueryService extends QueryService<AreaAtuacao> {

    private final Logger log = LoggerFactory.getLogger(AreaAtuacaoQueryService.class);

    private final AreaAtuacaoRepository areaAtuacaoRepository;

    private final AreaAtuacaoMapper areaAtuacaoMapper;

    public AreaAtuacaoQueryService(AreaAtuacaoRepository areaAtuacaoRepository, AreaAtuacaoMapper areaAtuacaoMapper) {
        this.areaAtuacaoRepository = areaAtuacaoRepository;
        this.areaAtuacaoMapper = areaAtuacaoMapper;
    }

    /**
     * Return a {@link List} of {@link AreaAtuacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AreaAtuacaoDTO> findByCriteria(AreaAtuacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AreaAtuacao> specification = createSpecification(criteria);
        return areaAtuacaoMapper.toDto(areaAtuacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AreaAtuacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AreaAtuacaoDTO> findByCriteria(AreaAtuacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AreaAtuacao> specification = createSpecification(criteria);
        return areaAtuacaoRepository.findAll(specification, page).map(areaAtuacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AreaAtuacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AreaAtuacao> specification = createSpecification(criteria);
        return areaAtuacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link AreaAtuacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AreaAtuacao> createSpecification(AreaAtuacaoCriteria criteria) {
        Specification<AreaAtuacao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AreaAtuacao_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), AreaAtuacao_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), AreaAtuacao_.descricao));
            }
            if (criteria.getIconeUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIconeUrl(), AreaAtuacao_.iconeUrl));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), AreaAtuacao_.ativo));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), AreaAtuacao_.criado));
            }
        }
        return specification;
    }
}
