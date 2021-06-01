package br.com.vagas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VagaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vaga.class);
        Vaga vaga1 = new Vaga();
        vaga1.setId(1L);
        Vaga vaga2 = new Vaga();
        vaga2.setId(vaga1.getId());
        assertThat(vaga1).isEqualTo(vaga2);
        vaga2.setId(2L);
        assertThat(vaga1).isNotEqualTo(vaga2);
        vaga1.setId(null);
        assertThat(vaga1).isNotEqualTo(vaga2);
    }
}
