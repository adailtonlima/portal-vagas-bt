package br.com.vagas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FuncaoPessoaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FuncaoPessoa.class);
        FuncaoPessoa funcaoPessoa1 = new FuncaoPessoa();
        funcaoPessoa1.setId(1L);
        FuncaoPessoa funcaoPessoa2 = new FuncaoPessoa();
        funcaoPessoa2.setId(funcaoPessoa1.getId());
        assertThat(funcaoPessoa1).isEqualTo(funcaoPessoa2);
        funcaoPessoa2.setId(2L);
        assertThat(funcaoPessoa1).isNotEqualTo(funcaoPessoa2);
        funcaoPessoa1.setId(null);
        assertThat(funcaoPessoa1).isNotEqualTo(funcaoPessoa2);
    }
}
