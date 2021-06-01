package br.com.vagas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FuncaoPessoaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FuncaoPessoaDTO.class);
        FuncaoPessoaDTO funcaoPessoaDTO1 = new FuncaoPessoaDTO();
        funcaoPessoaDTO1.setId(1L);
        FuncaoPessoaDTO funcaoPessoaDTO2 = new FuncaoPessoaDTO();
        assertThat(funcaoPessoaDTO1).isNotEqualTo(funcaoPessoaDTO2);
        funcaoPessoaDTO2.setId(funcaoPessoaDTO1.getId());
        assertThat(funcaoPessoaDTO1).isEqualTo(funcaoPessoaDTO2);
        funcaoPessoaDTO2.setId(2L);
        assertThat(funcaoPessoaDTO1).isNotEqualTo(funcaoPessoaDTO2);
        funcaoPessoaDTO1.setId(null);
        assertThat(funcaoPessoaDTO1).isNotEqualTo(funcaoPessoaDTO2);
    }
}
