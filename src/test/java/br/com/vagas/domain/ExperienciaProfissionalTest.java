package br.com.vagas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExperienciaProfissionalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperienciaProfissional.class);
        ExperienciaProfissional experienciaProfissional1 = new ExperienciaProfissional();
        experienciaProfissional1.setId(1L);
        ExperienciaProfissional experienciaProfissional2 = new ExperienciaProfissional();
        experienciaProfissional2.setId(experienciaProfissional1.getId());
        assertThat(experienciaProfissional1).isEqualTo(experienciaProfissional2);
        experienciaProfissional2.setId(2L);
        assertThat(experienciaProfissional1).isNotEqualTo(experienciaProfissional2);
        experienciaProfissional1.setId(null);
        assertThat(experienciaProfissional1).isNotEqualTo(experienciaProfissional2);
    }
}
