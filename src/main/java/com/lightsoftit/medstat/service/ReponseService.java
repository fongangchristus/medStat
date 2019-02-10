package com.lightsoftit.medstat.service;

import com.lightsoftit.medstat.service.dto.ReponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Reponse.
 */
public interface ReponseService {

    /**
     * Save a reponse.
     *
     * @param reponseDTO the entity to save
     * @return the persisted entity
     */
    ReponseDTO save(ReponseDTO reponseDTO);

    /**
     * Get all the reponses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReponseDTO> findAll(Pageable pageable);


    /**
     * Get the "id" reponse.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ReponseDTO> findOne(Long id);

    /**
     * Delete the "id" reponse.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the reponse corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReponseDTO> search(String query, Pageable pageable);
}
