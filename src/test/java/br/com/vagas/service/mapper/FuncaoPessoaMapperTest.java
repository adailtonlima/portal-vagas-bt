package br.com.vagas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncaoPessoaMapperTest {

    private FuncaoPessoaMapper funcaoPessoaMapper;

    @BeforeEach
    public void setUp() {
        funcaoPessoaMapper = new FuncaoPessoaMapperImpl();
    }
}