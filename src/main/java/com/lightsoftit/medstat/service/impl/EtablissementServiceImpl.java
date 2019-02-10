package com.lightsoftit.medstat.service.impl;

import com.lightsoftit.medstat.service.EtablissementService;
import com.lightsoftit.medstat.domain.Etablissement;
import com.lightsoftit.medstat.repository.EtablissementRepository;
import com.lightsoftit.medstat.repository.search.EtablissementSearchRepository;
import com.lightsoftit.medstat.service.dto.EtablissementDTO;
import com.lightsoftit.medstat.service.mapper.EtablissementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Etablissement.
 */
@Service
@Transactional
public class EtablissementServiceImpl implements EtablissementService {

    private final Logger log = LoggerFactory.getLogger(EtablissementServiceImpl.class);

    private final EtablissementRepository etablissementRepository;

    private final EtablissementMapper etablissementMapper;

    private final EtablissementSearchRepository etablissementSearchRepository;

    public EtablissementServiceImpl(EtablissementRepository etablissementRepository, EtablissementMapper etablissementMapper, EtablissementSearchRepository etablissementSearchRepository) {
        this.etablissementRepository = etablissementRepository;
        this.etablissementMapper = etablissementMapper;
        this.etablissementSearchRepository = etablissementSearchRepository;
    }

    /**
     * Save a etablissement.
     *
     * @param etablissementDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EtablissementDTO save(EtablissementDTO etablissementDTO) {
        log.debug("Request to save Etablissement : {}", etablissementDTO);
        Etablissement etablissement = etablissementMapper.toEntity(etablissementDTO);
        etablissement = etablissementRepository.save(etablissement);
        EtablissementDTO result = etablissementMapper.toDto(etablissement);
        etablissementSearchRepository.save(etablissement);
        return result;
    }

    /**
     * Get all the etablissements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EtablissementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Etablissements");
        return etablissementRepository.findAll(pageable)
            .map(etablissementMapper::toDto);
    }


    /**
     * Get one etablissement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EtablissementDTO> findOne(Long id) {
        log.debug("Request to get Etablissement : {}", id);
        return etablissementRepository.findById(id)
            .map(etablissementMapper::toDto);
    }

    /**
     * Delete the etablissement by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etablissement : {}", id);        etablissementRepository.deleteById(id);
        etablissementSearchRepository.deleteById(id);
    }

    /**
     * Search for the etablissement corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EtablissementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etablissements for query {}", query);
        return etablissementSearchRepository.search(queryStringQuery(query), pageable)
            .map(etablissementMapper::toDto);
    }
}
