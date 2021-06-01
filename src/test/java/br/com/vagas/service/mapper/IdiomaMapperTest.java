package br.com.vagas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdiomaMapperTest {

    private IdiomaMapper idiomaMapper;

    @BeforeEach
    public void setUp() {
        idiomaMapper = new IdiomaMapperImpl();
    }
}
