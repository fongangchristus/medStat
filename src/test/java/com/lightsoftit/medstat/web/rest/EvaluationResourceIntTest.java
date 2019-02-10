package com.lightsoftit.medstat.web.rest;

import com.lightsoftit.medstat.MedStatApp;

import com.lightsoftit.medstat.domain.Evaluation;
import com.lightsoftit.medstat.repository.EvaluationRepository;
import com.lightsoftit.medstat.repository.search.EvaluationSearchRepository;
import com.lightsoftit.medstat.service.EvaluationService;
import com.lightsoftit.medstat.service.dto.EvaluationDTO;
import com.lightsoftit.medstat.service.mapper.EvaluationMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the EvaluationResource REST controller.
 *
 * @see EvaluationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedStatApp.class)
public class EvaluationResourceIntTest {

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    private static final Double DEFAULT_NOTE = 1D;
    private static final Double UPDATED_NOTE = 2D;

    private static final Instant DEFAULT_DATE_EVALUATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EVALUATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EvaluationMapper evaluationMapper;

    @Autowired
    private EvaluationService evaluationService;

    /**
     * This repository is mocked in the com.lightsoftit.medstat.repository.search test package.
     *
     * @see com.lightsoftit.medstat.repository.search.EvaluationSearchRepositoryMockConfiguration
     */
    @Autowired
    private EvaluationSearchRepository mockEvaluationSearchRepository;

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

    private MockMvc restEvaluationMockMvc;

    private Evaluation evaluation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EvaluationResource evaluationResource = new EvaluationResource(evaluationService);
        this.restEvaluationMockMvc = MockMvcBuilders.standaloneSetup(evaluationResource)
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
    public static Evaluation createEntity(EntityManager em) {
        Evaluation evaluation = new Evaluation()
            .lieu(DEFAULT_LIEU)
            .note(DEFAULT_NOTE)
            .dateEvaluation(DEFAULT_DATE_EVALUATION);
        return evaluation;
    }

    @Before
    public void initTest() {
        evaluation = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvaluation() throws Exception {
        int databaseSizeBeforeCreate = evaluationRepository.findAll().size();

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);
        restEvaluationMockMvc.perform(post("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationDTO)))
            .andExpect(status().isCreated());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeCreate + 1);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getLieu()).isEqualTo(DEFAULT_LIEU);
        assertThat(testEvaluation.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testEvaluation.getDateEvaluation()).isEqualTo(DEFAULT_DATE_EVALUATION);

        // Validate the Evaluation in Elasticsearch
        verify(mockEvaluationSearchRepository, times(1)).save(testEvaluation);
    }

    @Test
    @Transactional
    public void createEvaluationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = evaluationRepository.findAll().size();

        // Create the Evaluation with an existing ID
        evaluation.setId(1L);
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationMockMvc.perform(post("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Evaluation in Elasticsearch
        verify(mockEvaluationSearchRepository, times(0)).save(evaluation);
    }

    @Test
    @Transactional
    public void checkNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluationRepository.findAll().size();
        // set the field null
        evaluation.setNote(null);

        // Create the Evaluation, which fails.
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        restEvaluationMockMvc.perform(post("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvaluations() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList
        restEvaluationMockMvc.perform(get("/api/evaluations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateEvaluation").value(hasItem(DEFAULT_DATE_EVALUATION.toString())));
    }
    
    @Test
    @Transactional
    public void getEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get the evaluation
        restEvaluationMockMvc.perform(get("/api/evaluations/{id}", evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(evaluation.getId().intValue()))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.dateEvaluation").value(DEFAULT_DATE_EVALUATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvaluation() throws Exception {
        // Get the evaluation
        restEvaluationMockMvc.perform(get("/api/evaluations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Update the evaluation
        Evaluation updatedEvaluation = evaluationRepository.findById(evaluation.getId()).get();
        // Disconnect from session so that the updates on updatedEvaluation are not directly saved in db
        em.detach(updatedEvaluation);
        updatedEvaluation
            .lieu(UPDATED_LIEU)
            .note(UPDATED_NOTE)
            .dateEvaluation(UPDATED_DATE_EVALUATION);
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(updatedEvaluation);

        restEvaluationMockMvc.perform(put("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationDTO)))
            .andExpect(status().isOk());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testEvaluation.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testEvaluation.getDateEvaluation()).isEqualTo(UPDATED_DATE_EVALUATION);

        // Validate the Evaluation in Elasticsearch
        verify(mockEvaluationSearchRepository, times(1)).save(testEvaluation);
    }

    @Test
    @Transactional
    public void updateNonExistingEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Create the Evaluation
        EvaluationDTO evaluationDTO = evaluationMapper.toDto(evaluation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationMockMvc.perform(put("/api/evaluations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(evaluationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Evaluation in Elasticsearch
        verify(mockEvaluationSearchRepository, times(0)).save(evaluation);
    }

    @Test
    @Transactional
    public void deleteEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        int databaseSizeBeforeDelete = evaluationRepository.findAll().size();

        // Delete the evaluation
        restEvaluationMockMvc.perform(delete("/api/evaluations/{id}", evaluation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Evaluation in Elasticsearch
        verify(mockEvaluationSearchRepository, times(1)).deleteById(evaluation.getId());
    }

    @Test
    @Transactional
    public void searchEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);
        when(mockEvaluationSearchRepository.search(queryStringQuery("id:" + evaluation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(evaluation), PageRequest.of(0, 1), 1));
        // Search the evaluation
        restEvaluationMockMvc.perform(get("/api/_search/evaluations?query=id:" + evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateEvaluation").value(hasItem(DEFAULT_DATE_EVALUATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluation.class);
        Evaluation evaluation1 = new Evaluation();
        evaluation1.setId(1L);
        Evaluation evaluation2 = new Evaluation();
        evaluation2.setId(evaluation1.getId());
        assertThat(evaluation1).isEqualTo(evaluation2);
        evaluation2.setId(2L);
        assertThat(evaluation1).isNotEqualTo(evaluation2);
        evaluation1.setId(null);
        assertThat(evaluation1).isNotEqualTo(evaluation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluationDTO.class);
        EvaluationDTO evaluationDTO1 = new EvaluationDTO();
        evaluationDTO1.setId(1L);
        EvaluationDTO evaluationDTO2 = new EvaluationDTO();
        assertThat(evaluationDTO1).isNotEqualTo(evaluationDTO2);
        evaluationDTO2.setId(evaluationDTO1.getId());
        assertThat(evaluationDTO1).isEqualTo(evaluationDTO2);
        evaluationDTO2.setId(2L);
        assertThat(evaluationDTO1).isNotEqualTo(evaluationDTO2);
        evaluationDTO1.setId(null);
        assertThat(evaluationDTO1).isNotEqualTo(evaluationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(evaluationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(evaluationMapper.fromId(null)).isNull();
    }
}
