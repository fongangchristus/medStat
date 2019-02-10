package com.lightsoftit.medstat.service;

import com.lightsoftit.medstat.service.dto.QuestionnaireDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Questionnaire.
 */
public interface QuestionnaireService {

    /**
     * Save a questionnaire.
     *
     * @param questionnaireDTO the entity to save
     * @return the persisted entity
     */
    QuestionnaireDTO save(QuestionnaireDTO questionnaireDTO);

    /**
     * Get all the questionnaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuestionnaireDTO> findAll(Pageable pageable);


    /**
     * Get the "id" questionnaire.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<QuestionnaireDTO> findOne(Long id);

    /**
     * Delete the "id" questionnaire.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the questionnaire corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<QuestionnaireDTO> search(String query, Pageable pageable);
}
