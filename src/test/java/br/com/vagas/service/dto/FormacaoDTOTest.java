package br.com.vagas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormacaoDTO.class);
        FormacaoDTO formacaoDTO1 = new FormacaoDTO();
        formacaoDTO1.setId(1L);
        FormacaoDTO formacaoDTO2 = new FormacaoDTO();
        assertThat(formacaoDTO1).isNotEqualTo(formacaoDTO2);
        formacaoDTO2.setId(formacaoDTO1.getId());
        assertThat(formacaoDTO1).isEqualTo(formacaoDTO2);
        formacaoDTO2.setId(2L);
        assertThat(formacaoDTO1).isNotEqualTo(formacaoDTO2);
        formacaoDTO1.setId(null);
        assertThat(formacaoDTO1).isNotEqualTo(formacaoDTO2);
    }
}
