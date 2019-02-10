package com.lightsoftit.medstat.web.rest;
import com.lightsoftit.medstat.service.CategorieService;
import com.lightsoftit.medstat.web.rest.errors.BadRequestAlertException;
import com.lightsoftit.medstat.web.rest.util.HeaderUtil;
import com.lightsoftit.medstat.web.rest.util.PaginationUtil;
import com.lightsoftit.medstat.service.dto.CategorieDTO;
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
 * REST controller for managing Categorie.
 */
@RestController
@RequestMapping("/api")
public class CategorieResource {

    private final Logger log = LoggerFactory.getLogger(CategorieResource.class);

    private static final String ENTITY_NAME = "categorie";

    private final CategorieService categorieService;

    public CategorieResource(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    /**
     * POST  /categories : Create a new categorie.
     *
     * @param categorieDTO the categorieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categorieDTO, or with status 400 (Bad Request) if the categorie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/categories")
    public ResponseEntity<CategorieDTO> createCategorie(@Valid @RequestBody CategorieDTO categorieDTO) throws URISyntaxException {
        log.debug("REST request to save Categorie : {}", categorieDTO);
        if (categorieDTO.getId() != null) {
            throw new BadRequestAlertException("A new categorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategorieDTO result = categorieService.save(categorieDTO);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categories : Updates an existing categorie.
     *
     * @param categorieDTO the categorieDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categorieDTO,
     * or with status 400 (Bad Request) if the categorieDTO is not valid,
     * or with status 500 (Internal Server Error) if the categorieDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/categories")
    public ResponseEntity<CategorieDTO> updateCategorie(@Valid @RequestBody CategorieDTO categorieDTO) throws URISyntaxException {
        log.debug("REST request to update Categorie : {}", categorieDTO);
        if (categorieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CategorieDTO result = categorieService.save(categorieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categorieDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categories : get all the categories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categories in body
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategorieDTO>> getAllCategories(Pageable pageable) {
        log.debug("REST request to get a page of Categories");
        Page<CategorieDTO> page = categorieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /categories/:id : get the "id" categorie.
     *
     * @param id the id of the categorieDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categorieDTO, or with status 404 (Not Found)
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategorieDTO> getCategorie(@PathVariable Long id) {
        log.debug("REST request to get Categorie : {}", id);
        Optional<CategorieDTO> categorieDTO = categorieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categorieDTO);
    }

    /**
     * DELETE  /categories/:id : delete the "id" categorie.
     *
     * @param id the id of the categorieDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        log.debug("REST request to delete Categorie : {}", id);
        categorieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/categories?query=:query : search for the categorie corresponding
     * to the query.
     *
     * @param query the query of the categorie search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/categories")
    public ResponseEntity<List<CategorieDTO>> searchCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Categories for query {}", query);
        Page<CategorieDTO> page = categorieService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/categories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
