package br.com.vagas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AreaAtuacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AreaAtuacaoDTO.class);
        AreaAtuacaoDTO areaAtuacaoDTO1 = new AreaAtuacaoDTO();
        areaAtuacaoDTO1.setId(1L);
        AreaAtuacaoDTO areaAtuacaoDTO2 = new AreaAtuacaoDTO();
        assertThat(areaAtuacaoDTO1).isNotEqualTo(areaAtuacaoDTO2);
        areaAtuacaoDTO2.setId(areaAtuacaoDTO1.getId());
        assertThat(areaAtuacaoDTO1).isEqualTo(areaAtuacaoDTO2);
        areaAtuacaoDTO2.setId(2L);
        assertThat(areaAtuacaoDTO1).isNotEqualTo(areaAtuacaoDTO2);
        areaAtuacaoDTO1.setId(null);
        assertThat(areaAtuacaoDTO1).isNotEqualTo(areaAtuacaoDTO2);
    }
}
