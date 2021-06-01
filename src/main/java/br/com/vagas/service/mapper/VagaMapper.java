package br.com.vagas.service.mapper;

import br.com.vagas.domain.*;
import br.com.vagas.service.dto.VagaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vaga} and its DTO {@link VagaDTO}.
 */
@Mapper(componentModel = "spring", uses = { PessoaMapper.class, EmpresaMapper.class })
public interface VagaMapper extends EntityMapper<VagaDTO, Vaga> {
    @Mapping(target = "cadastrou", source = "cadastrou", qualifiedByName = "nome")
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "nome")
    VagaDTO toDto(Vaga s);
}
