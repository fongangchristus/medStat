package com.lightsoftit.medstat.web.rest;

import com.lightsoftit.medstat.MedStatApp;

import com.lightsoftit.medstat.domain.SyntheseEvaluation;
import com.lightsoftit.medstat.repository.SyntheseEvaluationRepository;
import com.lightsoftit.medstat.repository.search.SyntheseEvaluationSearchRepository;
import com.lightsoftit.medstat.service.SyntheseEvaluationService;
import com.lightsoftit.medstat.service.dto.SyntheseEvaluationDTO;
import com.lightsoftit.medstat.service.mapper.SyntheseEvaluationMapper;
import com.lightsoftit.medstat.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.lightsoftit.medstat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SyntheseEvaluationResource REST controller.
 *
 * @see SyntheseEvaluationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedStatApp.class)
public class SyntheseEvaluationResourceIntTest {

    private static final String DEFAULT_COMPOSANTE = "AAAAAAAAAA";
    private static final String UPDATED_COMPOSANTE = "BBBBBBBBBB";

    private static final Double DEFAULT_POINT_DISPONNIBLE = 1D;
    private static final Double UPDATED_POINT_DISPONNIBLE = 2D;

    private static final Double DEFAULT_NBR_INDICATEUR_COMPOSITE = 1D;
    private static final Double UPDATED_NBR_INDICATEUR_COMPOSITE = 2D;

    private static final Double DEFAULT_POURCENTAGE_OBTENUE = 1D;
    private static final Double UPDATED_POURCENTAGE_OBTENUE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SyntheseEvaluationRepository syntheseEvaluationRepository;

    @Autowired
    private SyntheseEvaluationMapper syntheseEvaluationMapper;

    @Autowired
    private SyntheseEvaluationService syntheseEvaluationService;

    /**
     * This repository is mocked in the com.lightsoftit.medstat.repository.search test package.
     *
     * @see com.lightsoftit.medstat.repository.search.SyntheseEvaluationSearchRepositoryMockConfiguration
     */
    @Autowired
    private SyntheseEvaluationSearchRepository mockSyntheseEvaluationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSyntheseEvaluationMockMvc;

    private SyntheseEvaluation syntheseEvaluation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SyntheseEvaluationResource syntheseEvaluationResource = new SyntheseEvaluationResource(syntheseEvaluationService);
        this.restSyntheseEvaluationMockMvc = MockMvcBuilders.standaloneSetup(syntheseEvaluationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SyntheseEvaluation createEntity(EntityManager em) {
        SyntheseEvaluation syntheseEvaluation = new SyntheseEvaluation()
            .composante(DEFAULT_COMPOSANTE)
            .pointDisponnible(DEFAULT_POINT_DISPONNIBLE)
            .nbrIndicateurComposite(DEFAULT_NBR_INDICATEUR_COMPOSITE)
            .pourcentageObtenue(DEFAULT_POURCENTAGE_OBTENUE)
            .description(DEFAULT_DESCRIPTION);
        return syntheseEvaluation;
    }

    @Before
    public void initTest() {
        syntheseEvaluation = createEntity(em);
    }

    @Test
    @Transactional
    public void createSyntheseEvaluation() throws Exception {
        int databaseSizeBeforeCreate = syntheseEvaluationRepository.findAll().size();

        // Create the SyntheseEvaluation
        SyntheseEvaluationDTO syntheseEvaluationDTO = syntheseEvaluationMapper.toDto(syntheseEvaluation);
        restSyntheseEvaluationMockMvc.perform(post("/api/synthese-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(syntheseEvaluationDTO)))
            .andExpect(status().isCreated());

        // Validate the SyntheseEvaluation in the database
        List<SyntheseEvaluation> syntheseEvaluationList = syntheseEvaluationRepository.findAll();
        assertThat(syntheseEvaluationList).hasSize(databaseSizeBeforeCreate + 1);
        SyntheseEvaluation testSyntheseEvaluation = syntheseEvaluationList.get(syntheseEvaluationList.size() - 1);
        assertThat(testSyntheseEvaluation.getComposante()).isEqualTo(DEFAULT_COMPOSANTE);
        assertThat(testSyntheseEvaluation.getPointDisponnible()).isEqualTo(DEFAULT_POINT_DISPONNIBLE);
        assertThat(testSyntheseEvaluation.getNbrIndicateurComposite()).isEqualTo(DEFAULT_NBR_INDICATEUR_COMPOSITE);
        assertThat(testSyntheseEvaluation.getPourcentageObtenue()).isEqualTo(DEFAULT_POURCENTAGE_OBTENUE);
        assertThat(testSyntheseEvaluation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the SyntheseEvaluation in Elasticsearch
        verify(mockSyntheseEvaluationSearchRepository, times(1)).save(testSyntheseEvaluation);
    }

    @Test
    @Transactional
    public void createSyntheseEvaluationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = syntheseEvaluationRepository.findAll().size();

        // Create the SyntheseEvaluation with an existing ID
        syntheseEvaluation.setId(1L);
        SyntheseEvaluationDTO syntheseEvaluationDTO = syntheseEvaluationMapper.toDto(syntheseEvaluation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSyntheseEvaluationMockMvc.perform(post("/api/synthese-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(syntheseEvaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SyntheseEvaluation in the database
        List<SyntheseEvaluation> syntheseEvaluationList = syntheseEvaluationRepository.findAll();
        assertThat(syntheseEvaluationList).hasSize(databaseSizeBeforeCreate);

        // Validate the SyntheseEvaluation in Elasticsearch
        verify(mockSyntheseEvaluationSearchRepository, times(0)).save(syntheseEvaluation);
    }

    @Test
    @Transactional
    public void checkComposanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = syntheseEvaluationRepository.findAll().size();
        // set the field null
        syntheseEvaluation.setComposante(null);

        // Create the SyntheseEvaluation, which fails.
        SyntheseEvaluationDTO syntheseEvaluationDTO = syntheseEvaluationMapper.toDto(syntheseEvaluation);

        restSyntheseEvaluationMockMvc.perform(post("/api/synthese-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(syntheseEvaluationDTO)))
            .andExpect(status().isBadRequest());

        List<SyntheseEvaluation> syntheseEvaluationList = syntheseEvaluationRepository.findAll();
        assertThat(syntheseEvaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSyntheseEvaluations() throws Exception {
        // Initialize the database
        syntheseEvaluationRepository.saveAndFlush(syntheseEvaluation);

        // Get all the syntheseEvaluationList
        restSyntheseEvaluationMockMvc.perform(get("/api/synthese-evaluations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(syntheseEvaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].composante").value(hasItem(DEFAULT_COMPOSANTE.toString())))
            .andExpect(jsonPath("$.[*].pointDisponnible").value(hasItem(DEFAULT_POINT_DISPONNIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrIndicateurComposite").value(hasItem(DEFAULT_NBR_INDICATEUR_COMPOSITE.doubleValue())))
            .andExpect(jsonPath("$.[*].pourcentageObtenue").value(hasItem(DEFAULT_POURCENTAGE_OBTENUE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getSyntheseEvaluation() throws Exception {
        // Initialize the database
        syntheseEvaluationRepository.saveAndFlush(syntheseEvaluation);

        // Get the syntheseEvaluation
        restSyntheseEvaluationMockMvc.perform(get("/api/synthese-evaluations/{id}", syntheseEvaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(syntheseEvaluation.getId().intValue()))
            .andExpect(jsonPath("$.composante").value(DEFAULT_COMPOSANTE.toString()))
            .andExpect(jsonPath("$.pointDisponnible").value(DEFAULT_POINT_DISPONNIBLE.doubleValue()))
            .andExpect(jsonPath("$.nbrIndicateurComposite").value(DEFAULT_NBR_INDICATEUR_COMPOSITE.doubleValue()))
            .andExpect(jsonPath("$.pourcentageObtenue").value(DEFAULT_POURCENTAGE_OBTENUE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSyntheseEvaluation() throws Exception {
        // Get the syntheseEvaluation
        restSyntheseEvaluationMockMvc.perform(get("/api/synthese-evaluations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSyntheseEvaluation() throws Exception {
        // Initialize the database
        syntheseEvaluationRepository.saveAndFlush(syntheseEvaluation);

        int databaseSizeBeforeUpdate = syntheseEvaluationRepository.findAll().size();

        // Update the syntheseEvaluation
        SyntheseEvaluation updatedSyntheseEvaluation = syntheseEvaluationRepository.findById(syntheseEvaluation.getId()).get();
        // Disconnect from session so that the updates on updatedSyntheseEvaluation are not directly saved in db
        em.detach(updatedSyntheseEvaluation);
        updatedSyntheseEvaluation
            .composante(UPDATED_COMPOSANTE)
            .pointDisponnible(UPDATED_POINT_DISPONNIBLE)
            .nbrIndicateurComposite(UPDATED_NBR_INDICATEUR_COMPOSITE)
            .pourcentageObtenue(UPDATED_POURCENTAGE_OBTENUE)
            .description(UPDATED_DESCRIPTION);
        SyntheseEvaluationDTO syntheseEvaluationDTO = syntheseEvaluationMapper.toDto(updatedSyntheseEvaluation);

        restSyntheseEvaluationMockMvc.perform(put("/api/synthese-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(syntheseEvaluationDTO)))
            .andExpect(status().isOk());

        // Validate the SyntheseEvaluation in the database
        List<SyntheseEvaluation> syntheseEvaluationList = syntheseEvaluationRepository.findAll();
        assertThat(syntheseEvaluationList).hasSize(databaseSizeBeforeUpdate);
        SyntheseEvaluation testSyntheseEvaluation = syntheseEvaluationList.get(syntheseEvaluationList.size() - 1);
        assertThat(testSyntheseEvaluation.getComposante()).isEqualTo(UPDATED_COMPOSANTE);
        assertThat(testSyntheseEvaluation.getPointDisponnible()).isEqualTo(UPDATED_POINT_DISPONNIBLE);
        assertThat(testSyntheseEvaluation.getNbrIndicateurComposite()).isEqualTo(UPDATED_NBR_INDICATEUR_COMPOSITE);
        assertThat(testSyntheseEvaluation.getPourcentageObtenue()).isEqualTo(UPDATED_POURCENTAGE_OBTENUE);
        assertThat(testSyntheseEvaluation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the SyntheseEvaluation in Elasticsearch
        verify(mockSyntheseEvaluationSearchRepository, times(1)).save(testSyntheseEvaluation);
    }

    @Test
    @Transactional
    public void updateNonExistingSyntheseEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = syntheseEvaluationRepository.findAll().size();

        // Create the SyntheseEvaluation
        SyntheseEvaluationDTO syntheseEvaluationDTO = syntheseEvaluationMapper.toDto(syntheseEvaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSyntheseEvaluationMockMvc.perform(put("/api/synthese-evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(syntheseEvaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SyntheseEvaluation in the database
        List<SyntheseEvaluation> syntheseEvaluationList = syntheseEvaluationRepository.findAll();
        assertThat(syntheseEvaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SyntheseEvaluation in Elasticsearch
        verify(mockSyntheseEvaluationSearchRepository, times(0)).save(syntheseEvaluation);
    }

    @Test
    @Transactional
    public void deleteSyntheseEvaluation() throws Exception {
        // Initialize the database
        syntheseEvaluationRepository.saveAndFlush(syntheseEvaluation);

        int databaseSizeBeforeDelete = syntheseEvaluationRepository.findAll().size();

        // Delete the syntheseEvaluation
        restSyntheseEvaluationMockMvc.perform(delete("/api/synthese-evaluations/{id}", syntheseEvaluation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SyntheseEvaluation> syntheseEvaluationList = syntheseEvaluationRepository.findAll();
        assertThat(syntheseEvaluationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SyntheseEvaluation in Elasticsearch
        verify(mockSyntheseEvaluationSearchRepository, times(1)).deleteById(syntheseEvaluation.getId());
    }

    @Test
    @Transactional
    public void searchSyntheseEvaluation() throws Exception {
        // Initialize the database
        syntheseEvaluationRepository.saveAndFlush(syntheseEvaluation);
        when(mockSyntheseEvaluationSearchRepository.search(queryStringQuery("id:" + syntheseEvaluation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(syntheseEvaluation), PageRequest.of(0, 1), 1));
        // Search the syntheseEvaluation
        restSyntheseEvaluationMockMvc.perform(get("/api/_search/synthese-evaluations?query=id:" + syntheseEvaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(syntheseEvaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].composante").value(hasItem(DEFAULT_COMPOSANTE)))
            .andExpect(jsonPath("$.[*].pointDisponnible").value(hasItem(DEFAULT_POINT_DISPONNIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrIndicateurComposite").value(hasItem(DEFAULT_NBR_INDICATEUR_COMPOSITE.doubleValue())))
            .andExpect(jsonPath("$.[*].pourcentageObtenue").value(hasItem(DEFAULT_POURCENTAGE_OBTENUE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SyntheseEvaluation.class);
        SyntheseEvaluation syntheseEvaluation1 = new SyntheseEvaluation();
        syntheseEvaluation1.setId(1L);
        SyntheseEvaluation syntheseEvaluation2 = new SyntheseEvaluation();
        syntheseEvaluation2.setId(syntheseEvaluation1.getId());
        assertThat(syntheseEvaluation1).isEqualTo(syntheseEvaluation2);
        syntheseEvaluation2.setId(2L);
        assertThat(syntheseEvaluation1).isNotEqualTo(syntheseEvaluation2);
        syntheseEvaluation1.setId(null);
        assertThat(syntheseEvaluation1).isNotEqualTo(syntheseEvaluation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SyntheseEvaluationDTO.class);
        SyntheseEvaluationDTO syntheseEvaluationDTO1 = new SyntheseEvaluationDTO();
        syntheseEvaluationDTO1.setId(1L);
        SyntheseEvaluationDTO syntheseEvaluationDTO2 = new SyntheseEvaluationDTO();
        assertThat(syntheseEvaluationDTO1).isNotEqualTo(syntheseEvaluationDTO2);
        syntheseEvaluationDTO2.setId(syntheseEvaluationDTO1.getId());
        assertThat(syntheseEvaluationDTO1).isEqualTo(syntheseEvaluationDTO2);
        syntheseEvaluationDTO2.setId(2L);
        assertThat(syntheseEvaluationDTO1).isNotEqualTo(syntheseEvaluationDTO2);
        syntheseEvaluationDTO1.setId(null);
        assertThat(syntheseEvaluationDTO1).isNotEqualTo(syntheseEvaluationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(syntheseEvaluationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(syntheseEvaluationMapper.fromId(null)).isNull();
    }
}
