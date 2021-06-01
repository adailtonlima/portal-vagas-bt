package br.com.vagas.service.impl;

import br.com.vagas.domain.Formacao;
import br.com.vagas.repository.FormacaoRepository;
import br.com.vagas.service.FormacaoService;
import br.com.vagas.service.dto.FormacaoDTO;
import br.com.vagas.service.mapper.FormacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Formacao}.
 */
@Service
@Transactional
public class FormacaoServiceImpl implements FormacaoService {

    private final Logger log = LoggerFactory.getLogger(FormacaoServiceImpl.class);

    private final FormacaoRepository formacaoRepository;

    private final FormacaoMapper formacaoMapper;

    public FormacaoServiceImpl(FormacaoRepository formacaoRepository, FormacaoMapper formacaoMapper) {
        this.formacaoRepository = formacaoRepository;
        this.formacaoMapper = formacaoMapper;
    }

    @Override
    public FormacaoDTO save(FormacaoDTO formacaoDTO) {
        log.debug("Request to save Formacao : {}", formacaoDTO);
        Formacao formacao = formacaoMapper.toEntity(formacaoDTO);
        formacao = formacaoRepository.save(formacao);
        return formacaoMapper.toDto(formacao);
    }

    @Override
    public Optional<FormacaoDTO> partialUpdate(FormacaoDTO formacaoDTO) {
        log.debug("Request to partially update Formacao : {}", formacaoDTO);

        return formacaoRepository
            .findById(formacaoDTO.getId())
            .map(
                existingFormacao -> {
                    formacaoMapper.partialUpdate(existingFormacao, formacaoDTO);
                    return existingFormacao;
                }
            )
            .map(formacaoRepository::save)
            .map(formacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formacaos");
        return formacaoRepository.findAll(pageable).map(formacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormacaoDTO> findOne(Long id) {
        log.debug("Request to get Formacao : {}", id);
        return formacaoRepository.findById(id).map(formacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formacao : {}", id);
        formacaoRepository.deleteById(id);
    }
}
