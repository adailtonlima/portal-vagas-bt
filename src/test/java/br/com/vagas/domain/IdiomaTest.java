package br.com.vagas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdiomaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Idioma.class);
        Idioma idioma1 = new Idioma();
        idioma1.setId(1L);
        Idioma idioma2 = new Idioma();
        idioma2.setId(idioma1.getId());
        assertThat(idioma1).isEqualTo(idioma2);
        idioma2.setId(2L);
        assertThat(idioma1).isNotEqualTo(idioma2);
        idioma1.setId(null);
        assertThat(idioma1).isNotEqualTo(idioma2);
    }
}
