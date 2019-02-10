package com.lightsoftit.medstat.service;

import com.lightsoftit.medstat.service.dto.MedecinDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Medecin.
 */
public interface MedecinService {

    /**
     * Save a medecin.
     *
     * @param medecinDTO the entity to save
     * @return the persisted entity
     */
    MedecinDTO save(MedecinDTO medecinDTO);

    /**
     * Get all the medecins.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MedecinDTO> findAll(Pageable pageable);


    /**
     * Get the "id" medecin.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MedecinDTO> findOne(Long id);

    /**
     * Delete the "id" medecin.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the medecin corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MedecinDTO> search(String query, Pageable pageable);
}
