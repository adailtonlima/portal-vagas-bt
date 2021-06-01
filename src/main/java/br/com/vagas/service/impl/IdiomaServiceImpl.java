package br.com.vagas.service.impl;

import br.com.vagas.domain.Idioma;
import br.com.vagas.repository.IdiomaRepository;
import br.com.vagas.service.IdiomaService;
import br.com.vagas.service.dto.IdiomaDTO;
import br.com.vagas.service.mapper.IdiomaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Idioma}.
 */
@Service
@Transactional
public class IdiomaServiceImpl implements IdiomaService {

    private final Logger log = LoggerFactory.getLogger(IdiomaServiceImpl.class);

    private final IdiomaRepository idiomaRepository;

    private final IdiomaMapper idiomaMapper;

    public IdiomaServiceImpl(IdiomaRepository idiomaRepository, IdiomaMapper idiomaMapper) {
        this.idiomaRepository = idiomaRepository;
        this.idiomaMapper = idiomaMapper;
    }

    @Override
    public IdiomaDTO save(IdiomaDTO idiomaDTO) {
        log.debug("Request to save Idioma : {}", idiomaDTO);
        Idioma idioma = idiomaMapper.toEntity(idiomaDTO);
        idioma = idiomaRepository.save(idioma);
        return idiomaMapper.toDto(idioma);
    }

    @Override
    public Optional<IdiomaDTO> partialUpdate(IdiomaDTO idiomaDTO) {
        log.debug("Request to partially update Idioma : {}", idiomaDTO);

        return idiomaRepository
            .findById(idiomaDTO.getId())
            .map(
                existingIdioma -> {
                    idiomaMapper.partialUpdate(existingIdioma, idiomaDTO);
                    return existingIdioma;
                }
            )
            .map(idiomaRepository::save)
            .map(idiomaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdiomaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Idiomas");
        return idiomaRepository.findAll(pageable).map(idiomaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdiomaDTO> findOne(Long id) {
        log.debug("Request to get Idioma : {}", id);
        return idiomaRepository.findById(id).map(idiomaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Idioma : {}", id);
        idiomaRepository.deleteById(id);
    }
}
