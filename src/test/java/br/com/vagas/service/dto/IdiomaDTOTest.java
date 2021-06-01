package br.com.vagas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdiomaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdiomaDTO.class);
        IdiomaDTO idiomaDTO1 = new IdiomaDTO();
        idiomaDTO1.setId(1L);
        IdiomaDTO idiomaDTO2 = new IdiomaDTO();
        assertThat(idiomaDTO1).isNotEqualTo(idiomaDTO2);
        idiomaDTO2.setId(idiomaDTO1.getId());
        assertThat(idiomaDTO1).isEqualTo(idiomaDTO2);
        idiomaDTO2.setId(2L);
        assertThat(idiomaDTO1).isNotEqualTo(idiomaDTO2);
        idiomaDTO1.setId(null);
        assertThat(idiomaDTO1).isNotEqualTo(idiomaDTO2);
    }
}
