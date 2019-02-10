package com.lightsoftit.medstat.service;

import com.lightsoftit.medstat.service.dto.SyntheseEvaluationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SyntheseEvaluation.
 */
public interface SyntheseEvaluationService {

    /**
     * Save a syntheseEvaluation.
     *
     * @param syntheseEvaluationDTO the entity to save
     * @return the persisted entity
     */
    SyntheseEvaluationDTO save(SyntheseEvaluationDTO syntheseEvaluationDTO);

    /**
     * Get all the syntheseEvaluations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SyntheseEvaluationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" syntheseEvaluation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SyntheseEvaluationDTO> findOne(Long id);

    /**
     * Delete the "id" syntheseEvaluation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the syntheseEvaluation corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SyntheseEvaluationDTO> search(String query, Pageable pageable);
}
