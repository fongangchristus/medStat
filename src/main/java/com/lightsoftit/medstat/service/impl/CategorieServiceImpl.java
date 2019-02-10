package com.lightsoftit.medstat.service.impl;

import com.lightsoftit.medstat.service.CategorieService;
import com.lightsoftit.medstat.domain.Categorie;
import com.lightsoftit.medstat.repository.CategorieRepository;
import com.lightsoftit.medstat.repository.search.CategorieSearchRepository;
import com.lightsoftit.medstat.service.dto.CategorieDTO;
import com.lightsoftit.medstat.service.mapper.CategorieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Categorie.
 */
@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieServiceImpl.class);

    private final CategorieRepository categorieRepository;

    private final CategorieMapper categorieMapper;

    private final CategorieSearchRepository categorieSearchRepository;

    public CategorieServiceImpl(CategorieRepository categorieRepository, CategorieMapper categorieMapper, CategorieSearchRepository categorieSearchRepository) {
        this.categorieRepository = categorieRepository;
        this.categorieMapper = categorieMapper;
        this.categorieSearchRepository = categorieSearchRepository;
    }

    /**
     * Save a categorie.
     *
     * @param categorieDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CategorieDTO save(CategorieDTO categorieDTO) {
        log.debug("Request to save Categorie : {}", categorieDTO);
        Categorie categorie = categorieMapper.toEntity(categorieDTO);
        categorie = categorieRepository.save(categorie);
        CategorieDTO result = categorieMapper.toDto(categorie);
        categorieSearchRepository.save(categorie);
        return result;
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CategorieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categorieRepository.findAll(pageable)
            .map(categorieMapper::toDto);
    }


    /**
     * Get one categorie by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CategorieDTO> findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        return categorieRepository.findById(id)
            .map(categorieMapper::toDto);
    }

    /**
     * Delete the categorie by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);        categorieRepository.deleteById(id);
        categorieSearchRepository.deleteById(id);
    }

    /**
     * Search for the categorie corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CategorieDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Categories for query {}", query);
        return categorieSearchRepository.search(queryStringQuery(query), pageable)
            .map(categorieMapper::toDto);
    }
}
