package br.com.vagas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AreaAtuacaoMapperTest {

    private AreaAtuacaoMapper areaAtuacaoMapper;

    @BeforeEach
    public void setUp() {
        areaAtuacaoMapper = new AreaAtuacaoMapperImpl();
    }
}
