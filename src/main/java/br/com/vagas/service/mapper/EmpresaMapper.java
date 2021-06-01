package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.EmpresaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empresa} and its DTO {@link EmpresaDTO}.
 */
@Mapper(componentModel = "spring", uses = { EnderecoMapper.class, UserMapper.class, AreaAtuacaoMapper.class })
public interface EmpresaMapper extends EntityMapper<EmpresaDTO, Empresa> {
    @Mapping(target = "endereco", source = "endereco", qualifiedByName = "cep")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "area", source = "area", qualifiedByName = "nome")
    EmpresaDTO toDto(Empresa s);

    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EmpresaDTO toDtoNome(Empresa empresa);
}
