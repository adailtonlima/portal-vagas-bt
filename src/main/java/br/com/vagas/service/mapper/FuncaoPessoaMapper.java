package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.FuncaoPessoaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FuncaoPessoa} and its DTO {@link FuncaoPessoaDTO}.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface FuncaoPessoaMapper extends EntityMapper<FuncaoPessoaDTO, FuncaoPessoa> {
    @Mapping(target = "pessoa", source = "pessoa", qualifiedByName = "nome")
    FuncaoPessoaDTO toDto(FuncaoPessoa s);
}
