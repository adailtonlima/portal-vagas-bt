package br.com.vagas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormacaoMapperTest {

    private FormacaoMapper formacaoMapper;

    @BeforeEach
    public void setUp() {
        formacaoMapper = new FormacaoMapperImpl();
    }
}
