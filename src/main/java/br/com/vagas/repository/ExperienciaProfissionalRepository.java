package br.com.vagas.repository;

import br.com.vagas.domain.ExperienciaProfissional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExperienciaProfissional entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperienciaProfissionalRepository
    extends JpaRepository<ExperienciaProfissional, Long>, JpaSpecificationExecutor<ExperienciaProfissional> {}
