package com.lightsoftit.medstat.web.rest;
import com.lightsoftit.medstat.service.SyntheseEvaluationService;
import com.lightsoftit.medstat.web.rest.errors.BadRequestAlertException;
import com.lightsoftit.medstat.web.rest.util.HeaderUtil;
import com.lightsoftit.medstat.web.rest.util.PaginationUtil;
import com.lightsoftit.medstat.service.dto.SyntheseEvaluationDTO;
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
 * REST controller for managing SyntheseEvaluation.
 */
@RestController
@RequestMapping("/api")
public class SyntheseEvaluationResource {

    private final Logger log = LoggerFactory.getLogger(SyntheseEvaluationResource.class);

    private static final String ENTITY_NAME = "syntheseEvaluation";

    private final SyntheseEvaluationService syntheseEvaluationService;

    public SyntheseEvaluationResource(SyntheseEvaluationService syntheseEvaluationService) {
        this.syntheseEvaluationService = syntheseEvaluationService;
    }

    /**
     * POST  /synthese-evaluations : Create a new syntheseEvaluation.
     *
     * @param syntheseEvaluationDTO the syntheseEvaluationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new syntheseEvaluationDTO, or with status 400 (Bad Request) if the syntheseEvaluation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/synthese-evaluations")
    public ResponseEntity<SyntheseEvaluationDTO> createSyntheseEvaluation(@Valid @RequestBody SyntheseEvaluationDTO syntheseEvaluationDTO) throws URISyntaxException {
        log.debug("REST request to save SyntheseEvaluation : {}", syntheseEvaluationDTO);
        if (syntheseEvaluationDTO.getId() != null) {
            throw new BadRequestAlertException("A new syntheseEvaluation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SyntheseEvaluationDTO result = syntheseEvaluationService.save(syntheseEvaluationDTO);
        return ResponseEntity.created(new URI("/api/synthese-evaluations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /synthese-evaluations : Updates an existing syntheseEvaluation.
     *
     * @param syntheseEvaluationDTO the syntheseEvaluationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated syntheseEvaluationDTO,
     * or with status 400 (Bad Request) if the syntheseEvaluationDTO is not valid,
     * or with status 500 (Internal Server Error) if the syntheseEvaluationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/synthese-evaluations")
    public ResponseEntity<SyntheseEvaluationDTO> updateSyntheseEvaluation(@Valid @RequestBody SyntheseEvaluationDTO syntheseEvaluationDTO) throws URISyntaxException {
        log.debug("REST request to update SyntheseEvaluation : {}", syntheseEvaluationDTO);
        if (syntheseEvaluationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SyntheseEvaluationDTO result = syntheseEvaluationService.save(syntheseEvaluationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, syntheseEvaluationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /synthese-evaluations : get all the syntheseEvaluations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of syntheseEvaluations in body
     */
    @GetMapping("/synthese-evaluations")
    public ResponseEntity<List<SyntheseEvaluationDTO>> getAllSyntheseEvaluations(Pageable pageable) {
        log.debug("REST request to get a page of SyntheseEvaluations");
        Page<SyntheseEvaluationDTO> page = syntheseEvaluationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/synthese-evaluations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /synthese-evaluations/:id : get the "id" syntheseEvaluation.
     *
     * @param id the id of the syntheseEvaluationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the syntheseEvaluationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/synthese-evaluations/{id}")
    public ResponseEntity<SyntheseEvaluationDTO> getSyntheseEvaluation(@PathVariable Long id) {
        log.debug("REST request to get SyntheseEvaluation : {}", id);
        Optional<SyntheseEvaluationDTO> syntheseEvaluationDTO = syntheseEvaluationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(syntheseEvaluationDTO);
    }

    /**
     * DELETE  /synthese-evaluations/:id : delete the "id" syntheseEvaluation.
     *
     * @param id the id of the syntheseEvaluationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/synthese-evaluations/{id}")
    public ResponseEntity<Void> deleteSyntheseEvaluation(@PathVariable Long id) {
        log.debug("REST request to delete SyntheseEvaluation : {}", id);
        syntheseEvaluationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/synthese-evaluations?query=:query : search for the syntheseEvaluation corresponding
     * to the query.
     *
     * @param query the query of the syntheseEvaluation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/synthese-evaluations")
    public ResponseEntity<List<SyntheseEvaluationDTO>> searchSyntheseEvaluations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SyntheseEvaluations for query {}", query);
        Page<SyntheseEvaluationDTO> page = syntheseEvaluationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/synthese-evaluations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
