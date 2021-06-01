package br.com.vagas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.vagas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VagaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VagaDTO.class);
        VagaDTO vagaDTO1 = new VagaDTO();
        vagaDTO1.setId(1L);
        VagaDTO vagaDTO2 = new VagaDTO();
        assertThat(vagaDTO1).isNotEqualTo(vagaDTO2);
        vagaDTO2.setId(vagaDTO1.getId());
        assertThat(vagaDTO1).isEqualTo(vagaDTO2);
        vagaDTO2.setId(2L);
        assertThat(vagaDTO1).isNotEqualTo(vagaDTO2);
        vagaDTO1.setId(null);
        assertThat(vagaDTO1).isNotEqualTo(vagaDTO2);
    }
}
