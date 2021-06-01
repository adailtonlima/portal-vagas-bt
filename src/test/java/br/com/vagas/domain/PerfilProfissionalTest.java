package br.com.vagas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilProfissionalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerfilProfissional.class);
        PerfilProfissional perfilProfissional1 = new PerfilProfissional();
        perfilProfissional1.setId(1L);
        PerfilProfissional perfilProfissional2 = new PerfilProfissional();
        perfilProfissional2.setId(perfilProfissional1.getId());
        assertThat(perfilProfissional1).isEqualTo(perfilProfissional2);
        perfilProfissional2.setId(2L);
        assertThat(perfilProfissional1).isNotEqualTo(perfilProfissional2);
        perfilProfissional1.setId(null);
        assertThat(perfilProfissional1).isNotEqualTo(perfilProfissional2);
    }
}
