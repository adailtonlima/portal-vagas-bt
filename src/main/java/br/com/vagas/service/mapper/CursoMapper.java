package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.CursoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Curso} and its DTO {@link CursoDTO}.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface CursoMapper extends EntityMapper<CursoDTO, Curso> {
    @Mapping(target = "pessoa", source = "pessoa", qualifiedByName = "nome")
    CursoDTO toDto(Curso s);
}
