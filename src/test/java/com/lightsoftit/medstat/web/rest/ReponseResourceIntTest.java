package com.lightsoftit.medstat.web.rest;

import com.lightsoftit.medstat.MedStatApp;

import com.lightsoftit.medstat.domain.Reponse;
import com.lightsoftit.medstat.repository.ReponseRepository;
import com.lightsoftit.medstat.repository.search.ReponseSearchRepository;
import com.lightsoftit.medstat.service.ReponseService;
import com.lightsoftit.medstat.service.dto.ReponseDTO;
import com.lightsoftit.medstat.service.mapper.ReponseMapper;
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
 * Test class for the ReponseResource REST controller.
 *
 * @see ReponseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedStatApp.class)
public class ReponseResourceIntTest {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    @Autowired
    private ReponseRepository reponseRepository;

    @Autowired
    private ReponseMapper reponseMapper;

    @Autowired
    private ReponseService reponseService;

    /**
     * This repository is mocked in the com.lightsoftit.medstat.repository.search test package.
     *
     * @see com.lightsoftit.medstat.repository.search.ReponseSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReponseSearchRepository mockReponseSearchRepository;

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

    private MockMvc restReponseMockMvc;

    private Reponse reponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReponseResource reponseResource = new ReponseResource(reponseService);
        this.restReponseMockMvc = MockMvcBuilders.standaloneSetup(reponseResource)
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
    public static Reponse createEntity(EntityManager em) {
        Reponse reponse = new Reponse()
            .libele(DEFAULT_LIBELE);
        return reponse;
    }

    @Before
    public void initTest() {
        reponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createReponse() throws Exception {
        int databaseSizeBeforeCreate = reponseRepository.findAll().size();

        // Create the Reponse
        ReponseDTO reponseDTO = reponseMapper.toDto(reponse);
        restReponseMockMvc.perform(post("/api/reponses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reponseDTO)))
            .andExpect(status().isCreated());

        // Validate the Reponse in the database
        List<Reponse> reponseList = reponseRepository.findAll();
        assertThat(reponseList).hasSize(databaseSizeBeforeCreate + 1);
        Reponse testReponse = reponseList.get(reponseList.size() - 1);
        assertThat(testReponse.getLibele()).isEqualTo(DEFAULT_LIBELE);

        // Validate the Reponse in Elasticsearch
        verify(mockReponseSearchRepository, times(1)).save(testReponse);
    }

    @Test
    @Transactional
    public void createReponseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reponseRepository.findAll().size();

        // Create the Reponse with an existing ID
        reponse.setId(1L);
        ReponseDTO reponseDTO = reponseMapper.toDto(reponse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReponseMockMvc.perform(post("/api/reponses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reponseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reponse in the database
        List<Reponse> reponseList = reponseRepository.findAll();
        assertThat(reponseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Reponse in Elasticsearch
        verify(mockReponseSearchRepository, times(0)).save(reponse);
    }

    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = reponseRepository.findAll().size();
        // set the field null
        reponse.setLibele(null);

        // Create the Reponse, which fails.
        ReponseDTO reponseDTO = reponseMapper.toDto(reponse);

        restReponseMockMvc.perform(post("/api/reponses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reponseDTO)))
            .andExpect(status().isBadRequest());

        List<Reponse> reponseList = reponseRepository.findAll();
        assertThat(reponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReponses() throws Exception {
        // Initialize the database
        reponseRepository.saveAndFlush(reponse);

        // Get all the reponseList
        restReponseMockMvc.perform(get("/api/reponses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE.toString())));
    }
    
    @Test
    @Transactional
    public void getReponse() throws Exception {
        // Initialize the database
        reponseRepository.saveAndFlush(reponse);

        // Get the reponse
        restReponseMockMvc.perform(get("/api/reponses/{id}", reponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reponse.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReponse() throws Exception {
        // Get the reponse
        restReponseMockMvc.perform(get("/api/reponses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReponse() throws Exception {
        // Initialize the database
        reponseRepository.saveAndFlush(reponse);

        int databaseSizeBeforeUpdate = reponseRepository.findAll().size();

        // Update the reponse
        Reponse updatedReponse = reponseRepository.findById(reponse.getId()).get();
        // Disconnect from session so that the updates on updatedReponse are not directly saved in db
        em.detach(updatedReponse);
        updatedReponse
            .libele(UPDATED_LIBELE);
        ReponseDTO reponseDTO = reponseMapper.toDto(updatedReponse);

        restReponseMockMvc.perform(put("/api/reponses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reponseDTO)))
            .andExpect(status().isOk());

        // Validate the Reponse in the database
        List<Reponse> reponseList = reponseRepository.findAll();
        assertThat(reponseList).hasSize(databaseSizeBeforeUpdate);
        Reponse testReponse = reponseList.get(reponseList.size() - 1);
        assertThat(testReponse.getLibele()).isEqualTo(UPDATED_LIBELE);

        // Validate the Reponse in Elasticsearch
        verify(mockReponseSearchRepository, times(1)).save(testReponse);
    }

    @Test
    @Transactional
    public void updateNonExistingReponse() throws Exception {
        int databaseSizeBeforeUpdate = reponseRepository.findAll().size();

        // Create the Reponse
        ReponseDTO reponseDTO = reponseMapper.toDto(reponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReponseMockMvc.perform(put("/api/reponses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reponseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reponse in the database
        List<Reponse> reponseList = reponseRepository.findAll();
        assertThat(reponseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reponse in Elasticsearch
        verify(mockReponseSearchRepository, times(0)).save(reponse);
    }

    @Test
    @Transactional
    public void deleteReponse() throws Exception {
        // Initialize the database
        reponseRepository.saveAndFlush(reponse);

        int databaseSizeBeforeDelete = reponseRepository.findAll().size();

        // Delete the reponse
        restReponseMockMvc.perform(delete("/api/reponses/{id}", reponse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reponse> reponseList = reponseRepository.findAll();
        assertThat(reponseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Reponse in Elasticsearch
        verify(mockReponseSearchRepository, times(1)).deleteById(reponse.getId());
    }

    @Test
    @Transactional
    public void searchReponse() throws Exception {
        // Initialize the database
        reponseRepository.saveAndFlush(reponse);
        when(mockReponseSearchRepository.search(queryStringQuery("id:" + reponse.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reponse), PageRequest.of(0, 1), 1));
        // Search the reponse
        restReponseMockMvc.perform(get("/api/_search/reponses?query=id:" + reponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reponse.class);
        Reponse reponse1 = new Reponse();
        reponse1.setId(1L);
        Reponse reponse2 = new Reponse();
        reponse2.setId(reponse1.getId());
        assertThat(reponse1).isEqualTo(reponse2);
        reponse2.setId(2L);
        assertThat(reponse1).isNotEqualTo(reponse2);
        reponse1.setId(null);
        assertThat(reponse1).isNotEqualTo(reponse2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReponseDTO.class);
        ReponseDTO reponseDTO1 = new ReponseDTO();
        reponseDTO1.setId(1L);
        ReponseDTO reponseDTO2 = new ReponseDTO();
        assertThat(reponseDTO1).isNotEqualTo(reponseDTO2);
        reponseDTO2.setId(reponseDTO1.getId());
        assertThat(reponseDTO1).isEqualTo(reponseDTO2);
        reponseDTO2.setId(2L);
        assertThat(reponseDTO1).isNotEqualTo(reponseDTO2);
        reponseDTO1.setId(null);
        assertThat(reponseDTO1).isNotEqualTo(reponseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reponseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reponseMapper.fromId(null)).isNull();
    }
}
