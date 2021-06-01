package br.com.vagas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExperienciaProfissionalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperienciaProfissionalDTO.class);
        ExperienciaProfissionalDTO experienciaProfissionalDTO1 = new ExperienciaProfissionalDTO();
        experienciaProfissionalDTO1.setId(1L);
        ExperienciaProfissionalDTO experienciaProfissionalDTO2 = new ExperienciaProfissionalDTO();
        assertThat(experienciaProfissionalDTO1).isNotEqualTo(experienciaProfissionalDTO2);
        experienciaProfissionalDTO2.setId(experienciaProfissionalDTO1.getId());
        assertThat(experienciaProfissionalDTO1).isEqualTo(experienciaProfissionalDTO2);
        experienciaProfissionalDTO2.setId(2L);
        assertThat(experienciaProfissionalDTO1).isNotEqualTo(experienciaProfissionalDTO2);
        experienciaProfissionalDTO1.setId(null);
        assertThat(experienciaProfissionalDTO1).isNotEqualTo(experienciaProfissionalDTO2);
    }
}
