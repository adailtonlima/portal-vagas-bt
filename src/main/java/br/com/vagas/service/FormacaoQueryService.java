package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.Formacao;
import br.com.vagas.repository.FormacaoRepository;
import br.com.vagas.service.criteria.FormacaoCriteria;
import br.com.vagas.service.dto.FormacaoDTO;
import br.com.vagas.service.mapper.FormacaoMapper;
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
 * Service for executing complex queries for {@link Formacao} entities in the database.
 * The main input is a {@link FormacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormacaoDTO} or a {@link Page} of {@link FormacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormacaoQueryService extends QueryService<Formacao> {

    private final Logger log = LoggerFactory.getLogger(FormacaoQueryService.class);

    private final FormacaoRepository formacaoRepository;

    private final FormacaoMapper formacaoMapper;

    public FormacaoQueryService(FormacaoRepository formacaoRepository, FormacaoMapper formacaoMapper) {
        this.formacaoRepository = formacaoRepository;
        this.formacaoMapper = formacaoMapper;
    }

    /**
     * Return a {@link List} of {@link FormacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormacaoDTO> findByCriteria(FormacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Formacao> specification = createSpecification(criteria);
        return formacaoMapper.toDto(formacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormacaoDTO> findByCriteria(FormacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Formacao> specification = createSpecification(criteria);
        return formacaoRepository.findAll(specification, page).map(formacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Formacao> specification = createSpecification(criteria);
        return formacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link FormacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Formacao> createSpecification(FormacaoCriteria criteria) {
        Specification<Formacao> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Formacao_.id));
            }
            if (criteria.getInstituicao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInstituicao(), Formacao_.instituicao));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTipo(), Formacao_.tipo));
            }
            if (criteria.getInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInicio(), Formacao_.inicio));
            }
            if (criteria.getConclusao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConclusao(), Formacao_.conclusao));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Formacao_.criado));
            }
            if (criteria.getPessoaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPessoaId(), root -> root.join(Formacao_.pessoa, JoinType.LEFT).get(Pessoa_.id))
                    );
            }
        }
        return specification;
    }
}
