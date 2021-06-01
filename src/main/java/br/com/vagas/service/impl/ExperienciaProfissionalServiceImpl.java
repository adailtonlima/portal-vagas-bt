package br.com.vagas.service.impl;

import br.com.vagas.domain.ExperienciaProfissional;
import br.com.vagas.repository.ExperienciaProfissionalRepository;
import br.com.vagas.service.ExperienciaProfissionalService;
import br.com.vagas.service.dto.ExperienciaProfissionalDTO;
import br.com.vagas.service.mapper.ExperienciaProfissionalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExperienciaProfissional}.
 */
@Service
@Transactional
public class ExperienciaProfissionalServiceImpl implements ExperienciaProfissionalService {

    private final Logger log = LoggerFactory.getLogger(ExperienciaProfissionalServiceImpl.class);

    private final ExperienciaProfissionalRepository experienciaProfissionalRepository;

    private final ExperienciaProfissionalMapper experienciaProfissionalMapper;

    public ExperienciaProfissionalServiceImpl(
        ExperienciaProfissionalRepository experienciaProfissionalRepository,
        ExperienciaProfissionalMapper experienciaProfissionalMapper
    ) {
        this.experienciaProfissionalRepository = experienciaProfissionalRepository;
        this.experienciaProfissionalMapper = experienciaProfissionalMapper;
    }

    @Override
    public ExperienciaProfissionalDTO save(ExperienciaProfissionalDTO experienciaProfissionalDTO) {
        log.debug("Request to save ExperienciaProfissional : {}", experienciaProfissionalDTO);
        ExperienciaProfissional experienciaProfissional = experienciaProfissionalMapper.toEntity(experienciaProfissionalDTO);
        experienciaProfissional = experienciaProfissionalRepository.save(experienciaProfissional);
        return experienciaProfissionalMapper.toDto(experienciaProfissional);
    }

    @Override
    public Optional<ExperienciaProfissionalDTO> partialUpdate(ExperienciaProfissionalDTO experienciaProfissionalDTO) {
        log.debug("Request to partially update ExperienciaProfissional : {}", experienciaProfissionalDTO);

        return experienciaProfissionalRepository
            .findById(experienciaProfissionalDTO.getId())
            .map(
                existingExperienciaProfissional -> {
                    experienciaProfissionalMapper.partialUpdate(existingExperienciaProfissional, experienciaProfissionalDTO);
                    return existingExperienciaProfissional;
                }
            )
            .map(experienciaProfissionalRepository::save)
            .map(experienciaProfissionalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExperienciaProfissionalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExperienciaProfissionals");
        return experienciaProfissionalRepository.findAll(pageable).map(experienciaProfissionalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExperienciaProfissionalDTO> findOne(Long id) {
        log.debug("Request to get ExperienciaProfissional : {}", id);
        return experienciaProfissionalRepository.findById(id).map(experienciaProfissionalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExperienciaProfissional : {}", id);
        experienciaProfissionalRepository.deleteById(id);
    }
}
