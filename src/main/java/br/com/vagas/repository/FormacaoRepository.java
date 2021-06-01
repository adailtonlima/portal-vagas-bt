package br.com.vagas.repository;

import br.com.vagas.domain.Formacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Formacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormacaoRepository extends JpaRepository<Formacao, Long>, JpaSpecificationExecutor<Formacao> {}
