package br.com.vagas.service.impl;

import br.com.vagas.domain.Vaga;
import br.com.vagas.repository.VagaRepository;
import br.com.vagas.service.VagaService;
import br.com.vagas.service.dto.VagaDTO;
import br.com.vagas.service.mapper.VagaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vaga}.
 */
@Service
@Transactional
public class VagaServiceImpl implements VagaService {

    private final Logger log = LoggerFactory.getLogger(VagaServiceImpl.class);

    private final VagaRepository vagaRepository;

    private final VagaMapper vagaMapper;

    public VagaServiceImpl(VagaRepository vagaRepository, VagaMapper vagaMapper) {
        this.vagaRepository = vagaRepository;
        this.vagaMapper = vagaMapper;
    }

    @Override
    public VagaDTO save(VagaDTO vagaDTO) {
        log.debug("Request to save Vaga : {}", vagaDTO);
        Vaga vaga = vagaMapper.toEntity(vagaDTO);
        vaga = vagaRepository.save(vaga);
        return vagaMapper.toDto(vaga);
    }

    @Override
    public Optional<VagaDTO> partialUpdate(VagaDTO vagaDTO) {
        log.debug("Request to partially update Vaga : {}", vagaDTO);

        return vagaRepository
            .findById(vagaDTO.getId())
            .map(
                existingVaga -> {
                    vagaMapper.partialUpdate(existingVaga, vagaDTO);
                    return existingVaga;
                }
            )
            .map(vagaRepository::save)
            .map(vagaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VagaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vagas");
        return vagaRepository.findAll(pageable).map(vagaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VagaDTO> findOne(Long id) {
        log.debug("Request to get Vaga : {}", id);
        return vagaRepository.findById(id).map(vagaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vaga : {}", id);
        vagaRepository.deleteById(id);
    }
}
