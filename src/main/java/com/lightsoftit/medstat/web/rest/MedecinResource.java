package com.lightsoftit.medstat.web.rest;
import com.lightsoftit.medstat.service.MedecinService;
import com.lightsoftit.medstat.web.rest.errors.BadRequestAlertException;
import com.lightsoftit.medstat.web.rest.util.HeaderUtil;
import com.lightsoftit.medstat.web.rest.util.PaginationUtil;
import com.lightsoftit.medstat.service.dto.MedecinDTO;
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
 * REST controller for managing Medecin.
 */
@RestController
@RequestMapping("/api")
public class MedecinResource {

    private final Logger log = LoggerFactory.getLogger(MedecinResource.class);

    private static final String ENTITY_NAME = "medecin";

    private final MedecinService medecinService;

    public MedecinResource(MedecinService medecinService) {
        this.medecinService = medecinService;
    }

    /**
     * POST  /medecins : Create a new medecin.
     *
     * @param medecinDTO the medecinDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medecinDTO, or with status 400 (Bad Request) if the medecin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medecins")
    public ResponseEntity<MedecinDTO> createMedecin(@Valid @RequestBody MedecinDTO medecinDTO) throws URISyntaxException {
        log.debug("REST request to save Medecin : {}", medecinDTO);
        if (medecinDTO.getId() != null) {
            throw new BadRequestAlertException("A new medecin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedecinDTO result = medecinService.save(medecinDTO);
        return ResponseEntity.created(new URI("/api/medecins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medecins : Updates an existing medecin.
     *
     * @param medecinDTO the medecinDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medecinDTO,
     * or with status 400 (Bad Request) if the medecinDTO is not valid,
     * or with status 500 (Internal Server Error) if the medecinDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medecins")
    public ResponseEntity<MedecinDTO> updateMedecin(@Valid @RequestBody MedecinDTO medecinDTO) throws URISyntaxException {
        log.debug("REST request to update Medecin : {}", medecinDTO);
        if (medecinDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MedecinDTO result = medecinService.save(medecinDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medecinDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medecins : get all the medecins.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medecins in body
     */
    @GetMapping("/medecins")
    public ResponseEntity<List<MedecinDTO>> getAllMedecins(Pageable pageable) {
        log.debug("REST request to get a page of Medecins");
        Page<MedecinDTO> page = medecinService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medecins");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /medecins/:id : get the "id" medecin.
     *
     * @param id the id of the medecinDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medecinDTO, or with status 404 (Not Found)
     */
    @GetMapping("/medecins/{id}")
    public ResponseEntity<MedecinDTO> getMedecin(@PathVariable Long id) {
        log.debug("REST request to get Medecin : {}", id);
        Optional<MedecinDTO> medecinDTO = medecinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medecinDTO);
    }

    /**
     * DELETE  /medecins/:id : delete the "id" medecin.
     *
     * @param id the id of the medecinDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medecins/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Long id) {
        log.debug("REST request to delete Medecin : {}", id);
        medecinService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medecins?query=:query : search for the medecin corresponding
     * to the query.
     *
     * @param query the query of the medecin search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medecins")
    public ResponseEntity<List<MedecinDTO>> searchMedecins(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Medecins for query {}", query);
        Page<MedecinDTO> page = medecinService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medecins");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
