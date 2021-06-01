package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.EnderecoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Endereco} and its DTO {@link EnderecoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EnderecoMapper extends EntityMapper<EnderecoDTO, Endereco> {
    @Named("cep")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cep", source = "cep")
    EnderecoDTO toDtoCep(Endereco endereco);
}
