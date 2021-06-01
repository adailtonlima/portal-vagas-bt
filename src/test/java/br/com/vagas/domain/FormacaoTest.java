package br.com.vagas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formacao.class);
        Formacao formacao1 = new Formacao();
        formacao1.setId(1L);
        Formacao formacao2 = new Formacao();
        formacao2.setId(formacao1.getId());
        assertThat(formacao1).isEqualTo(formacao2);
        formacao2.setId(2L);
        assertThat(formacao1).isNotEqualTo(formacao2);
        formacao1.setId(null);
        assertThat(formacao1).isNotEqualTo(formacao2);
    }
}
