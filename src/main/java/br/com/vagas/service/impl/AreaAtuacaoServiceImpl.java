package br.com.vagas.service.impl;

import br.com.vagas.domain.AreaAtuacao;
import br.com.vagas.repository.AreaAtuacaoRepository;
import br.com.vagas.service.AreaAtuacaoService;
import br.com.vagas.service.dto.AreaAtuacaoDTO;
import br.com.vagas.service.mapper.AreaAtuacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AreaAtuacao}.
 */
@Service
@Transactional
public class AreaAtuacaoServiceImpl implements AreaAtuacaoService {

    private final Logger log = LoggerFactory.getLogger(AreaAtuacaoServiceImpl.class);

    private final AreaAtuacaoRepository areaAtuacaoRepository;

    private final AreaAtuacaoMapper areaAtuacaoMapper;

    public AreaAtuacaoServiceImpl(AreaAtuacaoRepository areaAtuacaoRepository, AreaAtuacaoMapper areaAtuacaoMapper) {
        this.areaAtuacaoRepository = areaAtuacaoRepository;
        this.areaAtuacaoMapper = areaAtuacaoMapper;
    }

    @Override
    public AreaAtuacaoDTO save(AreaAtuacaoDTO areaAtuacaoDTO) {
        log.debug("Request to save AreaAtuacao : {}", areaAtuacaoDTO);
        AreaAtuacao areaAtuacao = areaAtuacaoMapper.toEntity(areaAtuacaoDTO);
        areaAtuacao = areaAtuacaoRepository.save(areaAtuacao);
        return areaAtuacaoMapper.toDto(areaAtuacao);
    }

    @Override
    public Optional<AreaAtuacaoDTO> partialUpdate(AreaAtuacaoDTO areaAtuacaoDTO) {
        log.debug("Request to partially update AreaAtuacao : {}", areaAtuacaoDTO);

        return areaAtuacaoRepository
            .findById(areaAtuacaoDTO.getId())
            .map(
                existingAreaAtuacao -> {
                    areaAtuacaoMapper.partialUpdate(existingAreaAtuacao, areaAtuacaoDTO);
                    return existingAreaAtuacao;
                }
            )
            .map(areaAtuacaoRepository::save)
            .map(areaAtuacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AreaAtuacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AreaAtuacaos");
        return areaAtuacaoRepository.findAll(pageable).map(areaAtuacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AreaAtuacaoDTO> findOne(Long id) {
        log.debug("Request to get AreaAtuacao : {}", id);
        return areaAtuacaoRepository.findById(id).map(areaAtuacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AreaAtuacao : {}", id);
        areaAtuacaoRepository.deleteById(id);
    }
}
