package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.AreaAtuacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AreaAtuacao} and its DTO {@link AreaAtuacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AreaAtuacaoMapper extends EntityMapper<AreaAtuacaoDTO, AreaAtuacao> {
    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AreaAtuacaoDTO toDtoNome(AreaAtuacao areaAtuacao);
}
