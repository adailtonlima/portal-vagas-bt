package br.com.vagas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExperienciaProfissionalMapperTest {

    private ExperienciaProfissionalMapper experienciaProfissionalMapper;

    @BeforeEach
    public void setUp() {
        experienciaProfissionalMapper = new ExperienciaProfissionalMapperImpl();
    }
}
