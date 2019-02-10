package com.lightsoftit.medstat.web.rest;

import com.lightsoftit.medstat.MedStatApp;

import com.lightsoftit.medstat.domain.Questionnaire;
import com.lightsoftit.medstat.repository.QuestionnaireRepository;
import com.lightsoftit.medstat.repository.search.QuestionnaireSearchRepository;
import com.lightsoftit.medstat.service.QuestionnaireService;
import com.lightsoftit.medstat.service.dto.QuestionnaireDTO;
import com.lightsoftit.medstat.service.mapper.QuestionnaireMapper;
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
 * Test class for the QuestionnaireResource REST controller.
 *
 * @see QuestionnaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedStatApp.class)
public class QuestionnaireResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private QuestionnaireService questionnaireService;

    /**
     * This repository is mocked in the com.lightsoftit.medstat.repository.search test package.
     *
     * @see com.lightsoftit.medstat.repository.search.QuestionnaireSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuestionnaireSearchRepository mockQuestionnaireSearchRepository;

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

    private MockMvc restQuestionnaireMockMvc;

    private Questionnaire questionnaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionnaireResource questionnaireResource = new QuestionnaireResource(questionnaireService);
        this.restQuestionnaireMockMvc = MockMvcBuilders.standaloneSetup(questionnaireResource)
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
    public static Questionnaire createEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .libele(DEFAULT_LIBELE);
        return questionnaire;
    }

    @Before
    public void initTest() {
        questionnaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestionnaire() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);
        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO)))
            .andExpect(status().isCreated());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuestionnaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuestionnaire.getLibele()).isEqualTo(DEFAULT_LIBELE);

        // Validate the Questionnaire in Elasticsearch
        verify(mockQuestionnaireSearchRepository, times(1)).save(testQuestionnaire);
    }

    @Test
    @Transactional
    public void createQuestionnaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // Create the Questionnaire with an existing ID
        questionnaire.setId(1L);
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate);

        // Validate the Questionnaire in Elasticsearch
        verify(mockQuestionnaireSearchRepository, times(0)).save(questionnaire);
    }

    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionnaireRepository.findAll().size();
        // set the field null
        questionnaire.setLibele(null);

        // Create the Questionnaire, which fails.
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO)))
            .andExpect(status().isBadRequest());

        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestionnaires() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList
        restQuestionnaireMockMvc.perform(get("/api/questionnaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE.toString())));
    }
    
    @Test
    @Transactional
    public void getQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", questionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questionnaire.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionnaire() throws Exception {
        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire
        Questionnaire updatedQuestionnaire = questionnaireRepository.findById(questionnaire.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionnaire are not directly saved in db
        em.detach(updatedQuestionnaire);
        updatedQuestionnaire
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .libele(UPDATED_LIBELE);
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(updatedQuestionnaire);

        restQuestionnaireMockMvc.perform(put("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO)))
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuestionnaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuestionnaire.getLibele()).isEqualTo(UPDATED_LIBELE);

        // Validate the Questionnaire in Elasticsearch
        verify(mockQuestionnaireSearchRepository, times(1)).save(testQuestionnaire);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Create the Questionnaire
        QuestionnaireDTO questionnaireDTO = questionnaireMapper.toDto(questionnaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc.perform(put("/api/questionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionnaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Questionnaire in Elasticsearch
        verify(mockQuestionnaireSearchRepository, times(0)).save(questionnaire);
    }

    @Test
    @Transactional
    public void deleteQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeDelete = questionnaireRepository.findAll().size();

        // Delete the questionnaire
        restQuestionnaireMockMvc.perform(delete("/api/questionnaires/{id}", questionnaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Questionnaire in Elasticsearch
        verify(mockQuestionnaireSearchRepository, times(1)).deleteById(questionnaire.getId());
    }

    @Test
    @Transactional
    public void searchQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);
        when(mockQuestionnaireSearchRepository.search(queryStringQuery("id:" + questionnaire.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(questionnaire), PageRequest.of(0, 1), 1));
        // Search the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/_search/questionnaires?query=id:" + questionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questionnaire.class);
        Questionnaire questionnaire1 = new Questionnaire();
        questionnaire1.setId(1L);
        Questionnaire questionnaire2 = new Questionnaire();
        questionnaire2.setId(questionnaire1.getId());
        assertThat(questionnaire1).isEqualTo(questionnaire2);
        questionnaire2.setId(2L);
        assertThat(questionnaire1).isNotEqualTo(questionnaire2);
        questionnaire1.setId(null);
        assertThat(questionnaire1).isNotEqualTo(questionnaire2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionnaireDTO.class);
        QuestionnaireDTO questionnaireDTO1 = new QuestionnaireDTO();
        questionnaireDTO1.setId(1L);
        QuestionnaireDTO questionnaireDTO2 = new QuestionnaireDTO();
        assertThat(questionnaireDTO1).isNotEqualTo(questionnaireDTO2);
        questionnaireDTO2.setId(questionnaireDTO1.getId());
        assertThat(questionnaireDTO1).isEqualTo(questionnaireDTO2);
        questionnaireDTO2.setId(2L);
        assertThat(questionnaireDTO1).isNotEqualTo(questionnaireDTO2);
        questionnaireDTO1.setId(null);
        assertThat(questionnaireDTO1).isNotEqualTo(questionnaireDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(questionnaireMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(questionnaireMapper.fromId(null)).isNull();
    }
}
