package com.lightsoftit.medstat.service.impl;

import com.lightsoftit.medstat.service.ReponseService;
import com.lightsoftit.medstat.domain.Reponse;
import com.lightsoftit.medstat.repository.ReponseRepository;
import com.lightsoftit.medstat.repository.search.ReponseSearchRepository;
import com.lightsoftit.medstat.service.dto.ReponseDTO;
import com.lightsoftit.medstat.service.mapper.ReponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Reponse.
 */
@Service
@Transactional
public class ReponseServiceImpl implements ReponseService {

    private final Logger log = LoggerFactory.getLogger(ReponseServiceImpl.class);

    private final ReponseRepository reponseRepository;

    private final ReponseMapper reponseMapper;

    private final ReponseSearchRepository reponseSearchRepository;

    public ReponseServiceImpl(ReponseRepository reponseRepository, ReponseMapper reponseMapper, ReponseSearchRepository reponseSearchRepository) {
        this.reponseRepository = reponseRepository;
        this.reponseMapper = reponseMapper;
        this.reponseSearchRepository = reponseSearchRepository;
    }

    /**
     * Save a reponse.
     *
     * @param reponseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReponseDTO save(ReponseDTO reponseDTO) {
        log.debug("Request to save Reponse : {}", reponseDTO);
        Reponse reponse = reponseMapper.toEntity(reponseDTO);
        reponse = reponseRepository.save(reponse);
        ReponseDTO result = reponseMapper.toDto(reponse);
        reponseSearchRepository.save(reponse);
        return result;
    }

    /**
     * Get all the reponses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReponseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reponses");
        return reponseRepository.findAll(pageable)
            .map(reponseMapper::toDto);
    }


    /**
     * Get one reponse by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReponseDTO> findOne(Long id) {
        log.debug("Request to get Reponse : {}", id);
        return reponseRepository.findById(id)
            .map(reponseMapper::toDto);
    }

    /**
     * Delete the reponse by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reponse : {}", id);        reponseRepository.deleteById(id);
        reponseSearchRepository.deleteById(id);
    }

    /**
     * Search for the reponse corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReponseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Reponses for query {}", query);
        return reponseSearchRepository.search(queryStringQuery(query), pageable)
            .map(reponseMapper::toDto);
    }
}
