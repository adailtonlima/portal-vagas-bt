package br.com.vagas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilProfissionalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerfilProfissionalDTO.class);
        PerfilProfissionalDTO perfilProfissionalDTO1 = new PerfilProfissionalDTO();
        perfilProfissionalDTO1.setId(1L);
        PerfilProfissionalDTO perfilProfissionalDTO2 = new PerfilProfissionalDTO();
        assertThat(perfilProfissionalDTO1).isNotEqualTo(perfilProfissionalDTO2);
        perfilProfissionalDTO2.setId(perfilProfissionalDTO1.getId());
        assertThat(perfilProfissionalDTO1).isEqualTo(perfilProfissionalDTO2);
        perfilProfissionalDTO2.setId(2L);
        assertThat(perfilProfissionalDTO1).isNotEqualTo(perfilProfissionalDTO2);
        perfilProfissionalDTO1.setId(null);
        assertThat(perfilProfissionalDTO1).isNotEqualTo(perfilProfissionalDTO2);
    }
}
