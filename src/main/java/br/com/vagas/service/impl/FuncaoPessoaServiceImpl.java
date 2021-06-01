package br.com.vagas.service.impl;

import br.com.vagas.domain.FuncaoPessoa;
import br.com.vagas.repository.FuncaoPessoaRepository;
import br.com.vagas.service.FuncaoPessoaService;
import br.com.vagas.service.dto.FuncaoPessoaDTO;
import br.com.vagas.service.mapper.FuncaoPessoaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FuncaoPessoa}.
 */
@Service
@Transactional
public class FuncaoPessoaServiceImpl implements FuncaoPessoaService {

    private final Logger log = LoggerFactory.getLogger(FuncaoPessoaServiceImpl.class);

    private final FuncaoPessoaRepository funcaoPessoaRepository;

    private final FuncaoPessoaMapper funcaoPessoaMapper;

    public FuncaoPessoaServiceImpl(FuncaoPessoaRepository funcaoPessoaRepository, FuncaoPessoaMapper funcaoPessoaMapper) {
        this.funcaoPessoaRepository = funcaoPessoaRepository;
        this.funcaoPessoaMapper = funcaoPessoaMapper;
    }

    @Override
    public FuncaoPessoaDTO save(FuncaoPessoaDTO funcaoPessoaDTO) {
        log.debug("Request to save FuncaoPessoa : {}", funcaoPessoaDTO);
        FuncaoPessoa funcaoPessoa = funcaoPessoaMapper.toEntity(funcaoPessoaDTO);
        funcaoPessoa = funcaoPessoaRepository.save(funcaoPessoa);
        return funcaoPessoaMapper.toDto(funcaoPessoa);
    }

    @Override
    public Optional<FuncaoPessoaDTO> partialUpdate(FuncaoPessoaDTO funcaoPessoaDTO) {
        log.debug("Request to partially update FuncaoPessoa : {}", funcaoPessoaDTO);

        return funcaoPessoaRepository
            .findById(funcaoPessoaDTO.getId())
            .map(
                existingFuncaoPessoa -> {
                    funcaoPessoaMapper.partialUpdate(existingFuncaoPessoa, funcaoPessoaDTO);
                    return existingFuncaoPessoa;
                }
            )
            .map(funcaoPessoaRepository::save)
            .map(funcaoPessoaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FuncaoPessoaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FuncaoPessoas");
        return funcaoPessoaRepository.findAll(pageable).map(funcaoPessoaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FuncaoPessoaDTO> findOne(Long id) {
        log.debug("Request to get FuncaoPessoa : {}", id);
        return funcaoPessoaRepository.findById(id).map(funcaoPessoaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FuncaoPessoa : {}", id);
        funcaoPessoaRepository.deleteById(id);
    }
}
