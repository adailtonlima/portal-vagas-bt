package br.com.vagas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AreaAtuacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AreaAtuacao.class);
        AreaAtuacao areaAtuacao1 = new AreaAtuacao();
        areaAtuacao1.setId(1L);
        AreaAtuacao areaAtuacao2 = new AreaAtuacao();
        areaAtuacao2.setId(areaAtuacao1.getId());
        assertThat(areaAtuacao1).isEqualTo(areaAtuacao2);
        areaAtuacao2.setId(2L);
        assertThat(areaAtuacao1).isNotEqualTo(areaAtuacao2);
        areaAtuacao1.setId(null);
        assertThat(areaAtuacao1).isNotEqualTo(areaAtuacao2);
    }
}
