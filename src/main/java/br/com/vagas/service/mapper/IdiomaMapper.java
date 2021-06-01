package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.IdiomaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Idioma} and its DTO {@link IdiomaDTO}.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface IdiomaMapper extends EntityMapper<IdiomaDTO, Idioma> {
    @Mapping(target = "pessoa", source = "pessoa", qualifiedByName = "nome")
    IdiomaDTO toDto(Idioma s);
}
