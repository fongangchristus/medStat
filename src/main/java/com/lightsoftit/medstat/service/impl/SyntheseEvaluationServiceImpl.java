package com.lightsoftit.medstat.service.impl;

import com.lightsoftit.medstat.service.SyntheseEvaluationService;
import com.lightsoftit.medstat.domain.SyntheseEvaluation;
import com.lightsoftit.medstat.repository.SyntheseEvaluationRepository;
import com.lightsoftit.medstat.repository.search.SyntheseEvaluationSearchRepository;
import com.lightsoftit.medstat.service.dto.SyntheseEvaluationDTO;
import com.lightsoftit.medstat.service.mapper.SyntheseEvaluationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SyntheseEvaluation.
 */
@Service
@Transactional
public class SyntheseEvaluationServiceImpl implements SyntheseEvaluationService {

    private final Logger log = LoggerFactory.getLogger(SyntheseEvaluationServiceImpl.class);

    private final SyntheseEvaluationRepository syntheseEvaluationRepository;

    private final SyntheseEvaluationMapper syntheseEvaluationMapper;

    private final SyntheseEvaluationSearchRepository syntheseEvaluationSearchRepository;

    public SyntheseEvaluationServiceImpl(SyntheseEvaluationRepository syntheseEvaluationRepository, SyntheseEvaluationMapper syntheseEvaluationMapper, SyntheseEvaluationSearchRepository syntheseEvaluationSearchRepository) {
        this.syntheseEvaluationRepository = syntheseEvaluationRepository;
        this.syntheseEvaluationMapper = syntheseEvaluationMapper;
        this.syntheseEvaluationSearchRepository = syntheseEvaluationSearchRepository;
    }

    /**
     * Save a syntheseEvaluation.
     *
     * @param syntheseEvaluationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SyntheseEvaluationDTO save(SyntheseEvaluationDTO syntheseEvaluationDTO) {
        log.debug("Request to save SyntheseEvaluation : {}", syntheseEvaluationDTO);
        SyntheseEvaluation syntheseEvaluation = syntheseEvaluationMapper.toEntity(syntheseEvaluationDTO);
        syntheseEvaluation = syntheseEvaluationRepository.save(syntheseEvaluation);
        SyntheseEvaluationDTO result = syntheseEvaluationMapper.toDto(syntheseEvaluation);
        syntheseEvaluationSearchRepository.save(syntheseEvaluation);
        return result;
    }

    /**
     * Get all the syntheseEvaluations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SyntheseEvaluationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SyntheseEvaluations");
        return syntheseEvaluationRepository.findAll(pageable)
            .map(syntheseEvaluationMapper::toDto);
    }


    /**
     * Get one syntheseEvaluation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SyntheseEvaluationDTO> findOne(Long id) {
        log.debug("Request to get SyntheseEvaluation : {}", id);
        return syntheseEvaluationRepository.findById(id)
            .map(syntheseEvaluationMapper::toDto);
    }

    /**
     * Delete the syntheseEvaluation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SyntheseEvaluation : {}", id);        syntheseEvaluationRepository.deleteById(id);
        syntheseEvaluationSearchRepository.deleteById(id);
    }

    /**
     * Search for the syntheseEvaluation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SyntheseEvaluationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SyntheseEvaluations for query {}", query);
        return syntheseEvaluationSearchRepository.search(queryStringQuery(query), pageable)
            .map(syntheseEvaluationMapper::toDto);
    }
}
