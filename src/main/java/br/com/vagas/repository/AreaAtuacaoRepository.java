package br.com.vagas.repository;

import br.com.vagas.domain.AreaAtuacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AreaAtuacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaAtuacaoRepository extends JpaRepository<AreaAtuacao, Long>, JpaSpecificationExecutor<AreaAtuacao> {}
