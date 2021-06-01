package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.Vaga;
import br.com.vagas.repository.VagaRepository;
import br.com.vagas.service.criteria.VagaCriteria;
import br.com.vagas.service.dto.VagaDTO;
import br.com.vagas.service.mapper.VagaMapper;
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
 * Service for executing complex queries for {@link Vaga} entities in the database.
 * The main input is a {@link VagaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VagaDTO} or a {@link Page} of {@link VagaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VagaQueryService extends QueryService<Vaga> {

    private final Logger log = LoggerFactory.getLogger(VagaQueryService.class);

    private final VagaRepository vagaRepository;

    private final VagaMapper vagaMapper;

    public VagaQueryService(VagaRepository vagaRepository, VagaMapper vagaMapper) {
        this.vagaRepository = vagaRepository;
        this.vagaMapper = vagaMapper;
    }

    /**
     * Return a {@link List} of {@link VagaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VagaDTO> findByCriteria(VagaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vaga> specification = createSpecification(criteria);
        return vagaMapper.toDto(vagaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VagaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VagaDTO> findByCriteria(VagaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vaga> specification = createSpecification(criteria);
        return vagaRepository.findAll(specification, page).map(vagaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VagaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vaga> specification = createSpecification(criteria);
        return vagaRepository.count(specification);
    }

    /**
     * Function to convert {@link VagaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vaga> createSpecification(VagaCriteria criteria) {
        Specification<Vaga> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vaga_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Vaga_.titulo));
            }
            if (criteria.getEstagio() != null) {
                specification = specification.and(buildSpecification(criteria.getEstagio(), Vaga_.estagio));
            }
            if (criteria.getSalario() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalario(), Vaga_.salario));
            }
            if (criteria.getBeneficios() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBeneficios(), Vaga_.beneficios));
            }
            if (criteria.getJornadaSemanal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJornadaSemanal(), Vaga_.jornadaSemanal));
            }
            if (criteria.getBannerUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBannerUrl(), Vaga_.bannerUrl));
            }
            if (criteria.getFonte() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFonte(), Vaga_.fonte));
            }
            if (criteria.getLinkRecrutamento() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkRecrutamento(), Vaga_.linkRecrutamento));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), Vaga_.ativo));
            }
            if (criteria.getPreenchida() != null) {
                specification = specification.and(buildSpecification(criteria.getPreenchida(), Vaga_.preenchida));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Vaga_.criado));
            }
            if (criteria.getPrazoAnuncio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrazoAnuncio(), Vaga_.prazoAnuncio));
            }
            if (criteria.getCadastrouId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCadastrouId(), root -> root.join(Vaga_.cadastrou, JoinType.LEFT).get(Pessoa_.id))
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(Vaga_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
        }
        return specification;
    }
}
