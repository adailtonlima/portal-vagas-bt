package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.ExperienciaProfissionalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExperienciaProfissional} and its DTO {@link ExperienciaProfissionalDTO}.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class })
public interface ExperienciaProfissionalMapper extends EntityMapper<ExperienciaProfissionalDTO, ExperienciaProfissional> {
    @Mapping(target = "pessoa", source = "pessoa", qualifiedByName = "nome")
    ExperienciaProfissionalDTO toDto(ExperienciaProfissional s);
}
