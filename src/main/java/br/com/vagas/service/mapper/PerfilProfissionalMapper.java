package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.PerfilProfissionalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PerfilProfissional} and its DTO {@link PerfilProfissionalDTO}.
 */
@Mapper(componentModel = "spring", uses = { AreaAtuacaoMapper.class, PessoaMapper.class })
public interface PerfilProfissionalMapper extends EntityMapper<PerfilProfissionalDTO, PerfilProfissional> {
    @Mapping(target = "area", source = "area", qualifiedByName = "nome")
    @Mapping(target = "pessoa", source = "pessoa", qualifiedByName = "nome")
    PerfilProfissionalDTO toDto(PerfilProfissional s);
}
