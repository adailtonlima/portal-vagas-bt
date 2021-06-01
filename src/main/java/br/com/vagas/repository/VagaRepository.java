package br.com.vagas.repository;

import br.com.vagas.domain.Vaga;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vaga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long>, JpaSpecificationExecutor<Vaga> {}
