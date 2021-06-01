package br.com.vagas.service;

import br.com.vagas.domain.*; // for static metamodels
import br.com.vagas.domain.Pessoa;
import br.com.vagas.repository.PessoaRepository;
import br.com.vagas.service.criteria.PessoaCriteria;
import br.com.vagas.service.dto.PessoaDTO;
import br.com.vagas.service.mapper.PessoaMapper;
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
 * Service for executing complex queries for {@link Pessoa} entities in the database.
 * The main input is a {@link PessoaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PessoaDTO} or a {@link Page} of {@link PessoaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PessoaQueryService extends QueryService<Pessoa> {

    private final Logger log = LoggerFactory.getLogger(PessoaQueryService.class);

    private final PessoaRepository pessoaRepository;

    private final PessoaMapper pessoaMapper;

    public PessoaQueryService(PessoaRepository pessoaRepository, PessoaMapper pessoaMapper) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaMapper = pessoaMapper;
    }

    /**
     * Return a {@link List} of {@link PessoaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PessoaDTO> findByCriteria(PessoaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pessoa> specification = createSpecification(criteria);
        return pessoaMapper.toDto(pessoaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PessoaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PessoaDTO> findByCriteria(PessoaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pessoa> specification = createSpecification(criteria);
        return pessoaRepository.findAll(specification, page).map(pessoaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PessoaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pessoa> specification = createSpecification(criteria);
        return pessoaRepository.count(specification);
    }

    /**
     * Function to convert {@link PessoaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pessoa> createSpecification(PessoaCriteria criteria) {
        Specification<Pessoa> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pessoa_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Pessoa_.nome));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Pessoa_.email));
            }
            if (criteria.getDataNascimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataNascimento(), Pessoa_.dataNascimento));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), Pessoa_.cpf));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefone(), Pessoa_.telefone));
            }
            if (criteria.getNacionalidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNacionalidade(), Pessoa_.nacionalidade));
            }
            if (criteria.getNaturalidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNaturalidade(), Pessoa_.naturalidade));
            }
            if (criteria.getSexo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSexo(), Pessoa_.sexo));
            }
            if (criteria.getEstadoCivil() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstadoCivil(), Pessoa_.estadoCivil));
            }
            if (criteria.getPcd() != null) {
                specification = specification.and(buildSpecification(criteria.getPcd(), Pessoa_.pcd));
            }
            if (criteria.getPcdCID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPcdCID(), Pessoa_.pcdCID));
            }
            if (criteria.getCnh() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnh(), Pessoa_.cnh));
            }
            if (criteria.getFotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFotoUrl(), Pessoa_.fotoUrl));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Pessoa_.criado));
            }
            if (criteria.getEnderecoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEnderecoId(), root -> root.join(Pessoa_.endereco, JoinType.LEFT).get(Endereco_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Pessoa_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
