package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.PerfilProfissional;
import br.com.vagas.repository.PerfilProfissionalRepository;
import br.com.vagas.service.criteria.PerfilProfissionalCriteria;
import br.com.vagas.service.dto.PerfilProfissionalDTO;
import br.com.vagas.service.mapper.PerfilProfissionalMapper;
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
 * Service for executing complex queries for {@link PerfilProfissional} entities in the database.
 * The main input is a {@link PerfilProfissionalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PerfilProfissionalDTO} or a {@link Page} of {@link PerfilProfissionalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PerfilProfissionalQueryService extends QueryService<PerfilProfissional> {

    private final Logger log = LoggerFactory.getLogger(PerfilProfissionalQueryService.class);

    private final PerfilProfissionalRepository perfilProfissionalRepository;

    private final PerfilProfissionalMapper perfilProfissionalMapper;

    public PerfilProfissionalQueryService(
        PerfilProfissionalRepository perfilProfissionalRepository,
        PerfilProfissionalMapper perfilProfissionalMapper
    ) {
        this.perfilProfissionalRepository = perfilProfissionalRepository;
        this.perfilProfissionalMapper = perfilProfissionalMapper;
    }

    /**
     * Return a {@link List} of {@link PerfilProfissionalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PerfilProfissionalDTO> findByCriteria(PerfilProfissionalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PerfilProfissional> specification = createSpecification(criteria);
        return perfilProfissionalMapper.toDto(perfilProfissionalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PerfilProfissionalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PerfilProfissionalDTO> findByCriteria(PerfilProfissionalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PerfilProfissional> specification = createSpecification(criteria);
        return perfilProfissionalRepository.findAll(specification, page).map(perfilProfissionalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PerfilProfissionalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PerfilProfissional> specification = createSpecification(criteria);
        return perfilProfissionalRepository.count(specification);
    }

    /**
     * Function to convert {@link PerfilProfissionalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PerfilProfissional> createSpecification(PerfilProfissionalCriteria criteria) {
        Specification<PerfilProfissional> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PerfilProfissional_.id));
            }
            if (criteria.getEstagio() != null) {
                specification = specification.and(buildSpecification(criteria.getEstagio(), PerfilProfissional_.estagio));
            }
            if (criteria.getProcurandoEmprego() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getProcurandoEmprego(), PerfilProfissional_.procurandoEmprego));
            }
            if (criteria.getPretensaoSalarial() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPretensaoSalarial(), PerfilProfissional_.pretensaoSalarial));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), PerfilProfissional_.criado));
            }
            if (criteria.getAreaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAreaId(),
                            root -> root.join(PerfilProfissional_.area, JoinType.LEFT).get(AreaAtuacao_.id)
                        )
                    );
            }
            if (criteria.getPessoaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPessoaId(),
                            root -> root.join(PerfilProfissional_.pessoa, JoinType.LEFT).get(Pessoa_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
