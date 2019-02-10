package com.lightsoftit.medstat.service.impl;

import com.lightsoftit.medstat.service.LocalisationService;
import com.lightsoftit.medstat.domain.Localisation;
import com.lightsoftit.medstat.repository.LocalisationRepository;
import com.lightsoftit.medstat.repository.search.LocalisationSearchRepository;
import com.lightsoftit.medstat.service.dto.LocalisationDTO;
import com.lightsoftit.medstat.service.mapper.LocalisationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Localisation.
 */
@Service
@Transactional
public class LocalisationServiceImpl implements LocalisationService {

    private final Logger log = LoggerFactory.getLogger(LocalisationServiceImpl.class);

    private final LocalisationRepository localisationRepository;

    private final LocalisationMapper localisationMapper;

    private final LocalisationSearchRepository localisationSearchRepository;

    public LocalisationServiceImpl(LocalisationRepository localisationRepository, LocalisationMapper localisationMapper, LocalisationSearchRepository localisationSearchRepository) {
        this.localisationRepository = localisationRepository;
        this.localisationMapper = localisationMapper;
        this.localisationSearchRepository = localisationSearchRepository;
    }

    /**
     * Save a localisation.
     *
     * @param localisationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LocalisationDTO save(LocalisationDTO localisationDTO) {
        log.debug("Request to save Localisation : {}", localisationDTO);
        Localisation localisation = localisationMapper.toEntity(localisationDTO);
        localisation = localisationRepository.save(localisation);
        LocalisationDTO result = localisationMapper.toDto(localisation);
        localisationSearchRepository.save(localisation);
        return result;
    }

    /**
     * Get all the localisations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LocalisationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Localisations");
        return localisationRepository.findAll(pageable)
            .map(localisationMapper::toDto);
    }


    /**
     * Get one localisation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LocalisationDTO> findOne(Long id) {
        log.debug("Request to get Localisation : {}", id);
        return localisationRepository.findById(id)
            .map(localisationMapper::toDto);
    }

    /**
     * Delete the localisation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Localisation : {}", id);        localisationRepository.deleteById(id);
        localisationSearchRepository.deleteById(id);
    }

    /**
     * Search for the localisation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LocalisationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Localisations for query {}", query);
        return localisationSearchRepository.search(queryStringQuery(query), pageable)
            .map(localisationMapper::toDto);
    }
}
