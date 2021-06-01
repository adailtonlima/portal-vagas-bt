package br.com.vagas.repository;

import br.com.vagas.domain.PerfilProfissional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PerfilProfissional entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerfilProfissionalRepository
    extends JpaRepository<PerfilProfissional, Long>, JpaSpecificationExecutor<PerfilProfissional> {}
