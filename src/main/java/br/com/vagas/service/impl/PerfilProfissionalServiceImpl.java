package br.com.vagas.service.impl;

import br.com.vagas.domain.PerfilProfissional;
import br.com.vagas.repository.PerfilProfissionalRepository;
import br.com.vagas.service.PerfilProfissionalService;
import br.com.vagas.service.dto.PerfilProfissionalDTO;
import br.com.vagas.service.mapper.PerfilProfissionalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PerfilProfissional}.
 */
@Service
@Transactional
public class PerfilProfissionalServiceImpl implements PerfilProfissionalService {

    private final Logger log = LoggerFactory.getLogger(PerfilProfissionalServiceImpl.class);

    private final PerfilProfissionalRepository perfilProfissionalRepository;

    private final PerfilProfissionalMapper perfilProfissionalMapper;

    public PerfilProfissionalServiceImpl(
        PerfilProfissionalRepository perfilProfissionalRepository,
        PerfilProfissionalMapper perfilProfissionalMapper
    ) {
        this.perfilProfissionalRepository = perfilProfissionalRepository;
        this.perfilProfissionalMapper = perfilProfissionalMapper;
    }

    @Override
    public PerfilProfissionalDTO save(PerfilProfissionalDTO perfilProfissionalDTO) {
        log.debug("Request to save PerfilProfissional : {}", perfilProfissionalDTO);
        PerfilProfissional perfilProfissional = perfilProfissionalMapper.toEntity(perfilProfissionalDTO);
        perfilProfissional = perfilProfissionalRepository.save(perfilProfissional);
        return perfilProfissionalMapper.toDto(perfilProfissional);
    }

    @Override
    public Optional<PerfilProfissionalDTO> partialUpdate(PerfilProfissionalDTO perfilProfissionalDTO) {
        log.debug("Request to partially update PerfilProfissional : {}", perfilProfissionalDTO);

        return perfilProfissionalRepository
            .findById(perfilProfissionalDTO.getId())
            .map(
                existingPerfilProfissional -> {
                    perfilProfissionalMapper.partialUpdate(existingPerfilProfissional, perfilProfissionalDTO);
                    return existingPerfilProfissional;
                }
            )
            .map(perfilProfissionalRepository::save)
            .map(perfilProfissionalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerfilProfissionalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PerfilProfissionals");
        return perfilProfissionalRepository.findAll(pageable).map(perfilProfissionalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerfilProfissionalDTO> findOne(Long id) {
        log.debug("Request to get PerfilProfissional : {}", id);
        return perfilProfissionalRepository.findById(id).map(perfilProfissionalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PerfilProfissional : {}", id);
        perfilProfissionalRepository.deleteById(id);
    }
}
