package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.Idioma;
import br.com.vagas.repository.IdiomaRepository;
import br.com.vagas.service.criteria.IdiomaCriteria;
import br.com.vagas.service.dto.IdiomaDTO;
import br.com.vagas.service.mapper.IdiomaMapper;
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
 * Service for executing complex queries for {@link Idioma} entities in the database.
 * The main input is a {@link IdiomaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IdiomaDTO} or a {@link Page} of {@link IdiomaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IdiomaQueryService extends QueryService<Idioma> {

    private final Logger log = LoggerFactory.getLogger(IdiomaQueryService.class);

    private final IdiomaRepository idiomaRepository;

    private final IdiomaMapper idiomaMapper;

    public IdiomaQueryService(IdiomaRepository idiomaRepository, IdiomaMapper idiomaMapper) {
        this.idiomaRepository = idiomaRepository;
        this.idiomaMapper = idiomaMapper;
    }

    /**
     * Return a {@link List} of {@link IdiomaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IdiomaDTO> findByCriteria(IdiomaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Idioma> specification = createSpecification(criteria);
        return idiomaMapper.toDto(idiomaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IdiomaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IdiomaDTO> findByCriteria(IdiomaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Idioma> specification = createSpecification(criteria);
        return idiomaRepository.findAll(specification, page).map(idiomaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IdiomaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Idioma> specification = createSpecification(criteria);
        return idiomaRepository.count(specification);
    }

    /**
     * Function to convert {@link IdiomaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Idioma> createSpecification(IdiomaCriteria criteria) {
        Specification<Idioma> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Idioma_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Idioma_.nome));
            }
            if (criteria.getNivel() != null) {
                specification = specification.and(buildSpecification(criteria.getNivel(), Idioma_.nivel));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Idioma_.criado));
            }
            if (criteria.getPessoaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPessoaId(), root -> root.join(Idioma_.pessoa, JoinType.LEFT).get(Pessoa_.id))
                    );
            }
        }
        return specification;
    }
}
