package br.com.vagas.repository;

import br.com.vagas.domain.FuncaoPessoa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FuncaoPessoa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuncaoPessoaRepository extends JpaRepository<FuncaoPessoa, Long>, JpaSpecificationExecutor<FuncaoPessoa> {}
