package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.ExperienciaProfissional;
import br.com.vagas.repository.ExperienciaProfissionalRepository;
import br.com.vagas.service.criteria.ExperienciaProfissionalCriteria;
import br.com.vagas.service.dto.ExperienciaProfissionalDTO;
import br.com.vagas.service.mapper.ExperienciaProfissionalMapper;
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
 * Service for executing complex queries for {@link ExperienciaProfissional} entities in the database.
 * The main input is a {@link ExperienciaProfissionalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExperienciaProfissionalDTO} or a {@link Page} of {@link ExperienciaProfissionalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExperienciaProfissionalQueryService extends QueryService<ExperienciaProfissional> {

    private final Logger log = LoggerFactory.getLogger(ExperienciaProfissionalQueryService.class);

    private final ExperienciaProfissionalRepository experienciaProfissionalRepository;

    private final ExperienciaProfissionalMapper experienciaProfissionalMapper;

    public ExperienciaProfissionalQueryService(
        ExperienciaProfissionalRepository experienciaProfissionalRepository,
        ExperienciaProfissionalMapper experienciaProfissionalMapper
    ) {
        this.experienciaProfissionalRepository = experienciaProfissionalRepository;
        this.experienciaProfissionalMapper = experienciaProfissionalMapper;
    }

    /**
     * Return a {@link List} of {@link ExperienciaProfissionalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExperienciaProfissionalDTO> findByCriteria(ExperienciaProfissionalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExperienciaProfissional> specification = createSpecification(criteria);
        return experienciaProfissionalMapper.toDto(experienciaProfissionalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExperienciaProfissionalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExperienciaProfissionalDTO> findByCriteria(ExperienciaProfissionalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExperienciaProfissional> specification = createSpecification(criteria);
        return experienciaProfissionalRepository.findAll(specification, page).map(experienciaProfissionalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExperienciaProfissionalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExperienciaProfissional> specification = createSpecification(criteria);
        return experienciaProfissionalRepository.count(specification);
    }

    /**
     * Function to convert {@link ExperienciaProfissionalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExperienciaProfissional> createSpecification(ExperienciaProfissionalCriteria criteria) {
        Specification<ExperienciaProfissional> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExperienciaProfissional_.id));
            }
            if (criteria.getEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmpresa(), ExperienciaProfissional_.empresa));
            }
            if (criteria.getCargo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCargo(), ExperienciaProfissional_.cargo));
            }
            if (criteria.getSegmento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSegmento(), ExperienciaProfissional_.segmento));
            }
            if (criteria.getPorte() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPorte(), ExperienciaProfissional_.porte));
            }
            if (criteria.getInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInicio(), ExperienciaProfissional_.inicio));
            }
            if (criteria.getFim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFim(), ExperienciaProfissional_.fim));
            }
            if (criteria.getSalario() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalario(), ExperienciaProfissional_.salario));
            }
            if (criteria.getBeneficios() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBeneficios(), ExperienciaProfissional_.beneficios));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), ExperienciaProfissional_.criado));
            }
            if (criteria.getPessoaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPessoaId(),
                            root -> root.join(ExperienciaProfissional_.pessoa, JoinType.LEFT).get(Pessoa_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
