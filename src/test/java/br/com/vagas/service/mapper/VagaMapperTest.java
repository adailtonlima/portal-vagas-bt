package br.com.vagas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VagaMapperTest {

    private VagaMapper vagaMapper;

    @BeforeEach
    public void setUp() {
        vagaMapper = new VagaMapperImpl();
    }
}
