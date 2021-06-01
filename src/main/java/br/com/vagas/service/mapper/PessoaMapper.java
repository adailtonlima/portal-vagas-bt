package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.PessoaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pessoa} and its DTO {@link PessoaDTO}.
 */
@Mapper(componentModel = "spring", uses = { EnderecoMapper.class, UserMapper.class })
public interface PessoaMapper extends EntityMapper<PessoaDTO, Pessoa> {
    @Mapping(target = "endereco", source = "endereco", qualifiedByName = "cep")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    PessoaDTO toDto(Pessoa s);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    PessoaDTO toDtoNome(Pessoa pessoa);
}
