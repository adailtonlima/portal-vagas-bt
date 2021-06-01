package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.FormacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formacao} and its DTO {@link FormacaoDTO}.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface FormacaoMapper extends EntityMapper<FormacaoDTO, Formacao> {
    @Mapping(target = "pessoa", source = "pessoa", qualifiedByName = "nome")
    FormacaoDTO toDto(Formacao s);
}
