package com.lightsoftit.medstat.service.impl;

import com.lightsoftit.medstat.service.MedecinService;
import com.lightsoftit.medstat.domain.Medecin;
import com.lightsoftit.medstat.repository.MedecinRepository;
import com.lightsoftit.medstat.repository.search.MedecinSearchRepository;
import com.lightsoftit.medstat.service.dto.MedecinDTO;
import com.lightsoftit.medstat.service.mapper.MedecinMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Medecin.
 */
@Service
@Transactional
public class MedecinServiceImpl implements MedecinService {

    private final Logger log = LoggerFactory.getLogger(MedecinServiceImpl.class);

    private final MedecinRepository medecinRepository;

    private final MedecinMapper medecinMapper;

    private final MedecinSearchRepository medecinSearchRepository;

    public MedecinServiceImpl(MedecinRepository medecinRepository, MedecinMapper medecinMapper, MedecinSearchRepository medecinSearchRepository) {
        this.medecinRepository = medecinRepository;
        this.medecinMapper = medecinMapper;
        this.medecinSearchRepository = medecinSearchRepository;
    }

    /**
     * Save a medecin.
     *
     * @param medecinDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MedecinDTO save(MedecinDTO medecinDTO) {
        log.debug("Request to save Medecin : {}", medecinDTO);
        Medecin medecin = medecinMapper.toEntity(medecinDTO);
        medecin = medecinRepository.save(medecin);
        MedecinDTO result = medecinMapper.toDto(medecin);
        medecinSearchRepository.save(medecin);
        return result;
    }

    /**
     * Get all the medecins.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MedecinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medecins");
        return medecinRepository.findAll(pageable)
            .map(medecinMapper::toDto);
    }


    /**
     * Get one medecin by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MedecinDTO> findOne(Long id) {
        log.debug("Request to get Medecin : {}", id);
        return medecinRepository.findById(id)
            .map(medecinMapper::toDto);
    }

    /**
     * Delete the medecin by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medecin : {}", id);        medecinRepository.deleteById(id);
        medecinSearchRepository.deleteById(id);
    }

    /**
     * Search for the medecin corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MedecinDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Medecins for query {}", query);
        return medecinSearchRepository.search(queryStringQuery(query), pageable)
            .map(medecinMapper::toDto);
    }
}
