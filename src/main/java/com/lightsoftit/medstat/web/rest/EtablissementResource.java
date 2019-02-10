package com.lightsoftit.medstat.web.rest;
import com.lightsoftit.medstat.service.EtablissementService;
import com.lightsoftit.medstat.web.rest.errors.BadRequestAlertException;
import com.lightsoftit.medstat.web.rest.util.HeaderUtil;
import com.lightsoftit.medstat.web.rest.util.PaginationUtil;
import com.lightsoftit.medstat.service.dto.EtablissementDTO;
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
 * REST controller for managing Etablissement.
 */
@RestController
@RequestMapping("/api")
public class EtablissementResource {

    private final Logger log = LoggerFactory.getLogger(EtablissementResource.class);

    private static final String ENTITY_NAME = "etablissement";

    private final EtablissementService etablissementService;

    public EtablissementResource(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * POST  /etablissements : Create a new etablissement.
     *
     * @param etablissementDTO the etablissementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new etablissementDTO, or with status 400 (Bad Request) if the etablissement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/etablissements")
    public ResponseEntity<EtablissementDTO> createEtablissement(@Valid @RequestBody EtablissementDTO etablissementDTO) throws URISyntaxException {
        log.debug("REST request to save Etablissement : {}", etablissementDTO);
        if (etablissementDTO.getId() != null) {
            throw new BadRequestAlertException("A new etablissement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EtablissementDTO result = etablissementService.save(etablissementDTO);
        return ResponseEntity.created(new URI("/api/etablissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /etablissements : Updates an existing etablissement.
     *
     * @param etablissementDTO the etablissementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated etablissementDTO,
     * or with status 400 (Bad Request) if the etablissementDTO is not valid,
     * or with status 500 (Internal Server Error) if the etablissementDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/etablissements")
    public ResponseEntity<EtablissementDTO> updateEtablissement(@Valid @RequestBody EtablissementDTO etablissementDTO) throws URISyntaxException {
        log.debug("REST request to update Etablissement : {}", etablissementDTO);
        if (etablissementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EtablissementDTO result = etablissementService.save(etablissementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, etablissementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /etablissements : get all the etablissements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of etablissements in body
     */
    @GetMapping("/etablissements")
    public ResponseEntity<List<EtablissementDTO>> getAllEtablissements(Pageable pageable) {
        log.debug("REST request to get a page of Etablissements");
        Page<EtablissementDTO> page = etablissementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/etablissements");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /etablissements/:id : get the "id" etablissement.
     *
     * @param id the id of the etablissementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the etablissementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/etablissements/{id}")
    public ResponseEntity<EtablissementDTO> getEtablissement(@PathVariable Long id) {
        log.debug("REST request to get Etablissement : {}", id);
        Optional<EtablissementDTO> etablissementDTO = etablissementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etablissementDTO);
    }

    /**
     * DELETE  /etablissements/:id : delete the "id" etablissement.
     *
     * @param id the id of the etablissementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/etablissements/{id}")
    public ResponseEntity<Void> deleteEtablissement(@PathVariable Long id) {
        log.debug("REST request to delete Etablissement : {}", id);
        etablissementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/etablissements?query=:query : search for the etablissement corresponding
     * to the query.
     *
     * @param query the query of the etablissement search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/etablissements")
    public ResponseEntity<List<EtablissementDTO>> searchEtablissements(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Etablissements for query {}", query);
        Page<EtablissementDTO> page = etablissementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/etablissements");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
