package com.lightsoftit.medstat.web.rest;
import com.lightsoftit.medstat.service.LocalisationService;
import com.lightsoftit.medstat.web.rest.errors.BadRequestAlertException;
import com.lightsoftit.medstat.web.rest.util.HeaderUtil;
import com.lightsoftit.medstat.web.rest.util.PaginationUtil;
import com.lightsoftit.medstat.service.dto.LocalisationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Localisation.
 */
@RestController
@RequestMapping("/api")
public class LocalisationResource {

    private final Logger log = LoggerFactory.getLogger(LocalisationResource.class);

    private static final String ENTITY_NAME = "localisation";

    private final LocalisationService localisationService;

    public LocalisationResource(LocalisationService localisationService) {
        this.localisationService = localisationService;
    }

    /**
     * POST  /localisations : Create a new localisation.
     *
     * @param localisationDTO the localisationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new localisationDTO, or with status 400 (Bad Request) if the localisation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/localisations")
    public ResponseEntity<LocalisationDTO> createLocalisation(@Valid @RequestBody LocalisationDTO localisationDTO) throws URISyntaxException {
        log.debug("REST request to save Localisation : {}", localisationDTO);
        if (localisationDTO.getId() != null) {
            throw new BadRequestAlertException("A new localisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocalisationDTO result = localisationService.save(localisationDTO);
        return ResponseEntity.created(new URI("/api/localisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /localisations : Updates an existing localisation.
     *
     * @param localisationDTO the localisationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated localisationDTO,
     * or with status 400 (Bad Request) if the localisationDTO is not valid,
     * or with status 500 (Internal Server Error) if the localisationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/localisations")
    public ResponseEntity<LocalisationDTO> updateLocalisation(@Valid @RequestBody LocalisationDTO localisationDTO) throws URISyntaxException {
        log.debug("REST request to update Localisation : {}", localisationDTO);
        if (localisationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LocalisationDTO result = localisationService.save(localisationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, localisationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /localisations : get all the localisations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of localisations in body
     */
    @GetMapping("/localisations")
    public ResponseEntity<List<LocalisationDTO>> getAllLocalisations(Pageable pageable) {
        log.debug("REST request to get a page of Localisations");
        Page<LocalisationDTO> page = localisationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/localisations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /localisations/:id : get the "id" localisation.
     *
     * @param id the id of the localisationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the localisationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/localisations/{id}")
    public ResponseEntity<LocalisationDTO> getLocalisation(@PathVariable Long id) {
        log.debug("REST request to get Localisation : {}", id);
        Optional<LocalisationDTO> localisationDTO = localisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localisationDTO);
    }

    /**
     * DELETE  /localisations/:id : delete the "id" localisation.
     *
     * @param id the id of the localisationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/localisations/{id}")
    public ResponseEntity<Void> deleteLocalisation(@PathVariable Long id) {
        log.debug("REST request to delete Localisation : {}", id);
        localisationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/localisations?query=:query : search for the localisation corresponding
     * to the query.
     *
     * @param query the query of the localisation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/localisations")
    public ResponseEntity<List<LocalisationDTO>> searchLocalisations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Localisations for query {}", query);
        Page<LocalisationDTO> page = localisationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/localisations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
