package br.com.vagas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PerfilProfissionalMapperTest {

    private PerfilProfissionalMapper perfilProfissionalMapper;

    @BeforeEach
    public void setUp() {
        perfilProfissionalMapper = new PerfilProfissionalMapperImpl();
    }
}
