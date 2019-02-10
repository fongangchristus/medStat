package com.lightsoftit.medstat.web.rest;

import com.lightsoftit.medstat.MedStatApp;

import com.lightsoftit.medstat.domain.Medecin;
import com.lightsoftit.medstat.repository.MedecinRepository;
import com.lightsoftit.medstat.repository.search.MedecinSearchRepository;
import com.lightsoftit.medstat.service.MedecinService;
import com.lightsoftit.medstat.service.dto.MedecinDTO;
import com.lightsoftit.medstat.service.mapper.MedecinMapper;
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
 * Test class for the MedecinResource REST controller.
 *
 * @see MedecinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedStatApp.class)
public class MedecinResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private MedecinMapper medecinMapper;

    @Autowired
    private MedecinService medecinService;

    /**
     * This repository is mocked in the com.lightsoftit.medstat.repository.search test package.
     *
     * @see com.lightsoftit.medstat.repository.search.MedecinSearchRepositoryMockConfiguration
     */
    @Autowired
    private MedecinSearchRepository mockMedecinSearchRepository;

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

    private MockMvc restMedecinMockMvc;

    private Medecin medecin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedecinResource medecinResource = new MedecinResource(medecinService);
        this.restMedecinMockMvc = MockMvcBuilders.standaloneSetup(medecinResource)
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
    public static Medecin createEntity(EntityManager em) {
        Medecin medecin = new Medecin()
            .nom(DEFAULT_NOM)
            .telephone(DEFAULT_TELEPHONE)
            .email(DEFAULT_EMAIL)
            .qualification(DEFAULT_QUALIFICATION)
            .adresse(DEFAULT_ADRESSE);
        return medecin;
    }

    @Before
    public void initTest() {
        medecin = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedecin() throws Exception {
        int databaseSizeBeforeCreate = medecinRepository.findAll().size();

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);
        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isCreated());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeCreate + 1);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMedecin.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testMedecin.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMedecin.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
        assertThat(testMedecin.getAdresse()).isEqualTo(DEFAULT_ADRESSE);

        // Validate the Medecin in Elasticsearch
        verify(mockMedecinSearchRepository, times(1)).save(testMedecin);
    }

    @Test
    @Transactional
    public void createMedecinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medecinRepository.findAll().size();

        // Create the Medecin with an existing ID
        medecin.setId(1L);
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeCreate);

        // Validate the Medecin in Elasticsearch
        verify(mockMedecinSearchRepository, times(0)).save(medecin);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = medecinRepository.findAll().size();
        // set the field null
        medecin.setNom(null);

        // Create the Medecin, which fails.
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isBadRequest());

        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedecins() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList
        restMedecinMockMvc.perform(get("/api/medecins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())));
    }
    
    @Test
    @Transactional
    public void getMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get the medecin
        restMedecinMockMvc.perform(get("/api/medecins/{id}", medecin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medecin.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedecin() throws Exception {
        // Get the medecin
        restMedecinMockMvc.perform(get("/api/medecins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Update the medecin
        Medecin updatedMedecin = medecinRepository.findById(medecin.getId()).get();
        // Disconnect from session so that the updates on updatedMedecin are not directly saved in db
        em.detach(updatedMedecin);
        updatedMedecin
            .nom(UPDATED_NOM)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .qualification(UPDATED_QUALIFICATION)
            .adresse(UPDATED_ADRESSE);
        MedecinDTO medecinDTO = medecinMapper.toDto(updatedMedecin);

        restMedecinMockMvc.perform(put("/api/medecins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isOk());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMedecin.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testMedecin.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMedecin.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
        assertThat(testMedecin.getAdresse()).isEqualTo(UPDATED_ADRESSE);

        // Validate the Medecin in Elasticsearch
        verify(mockMedecinSearchRepository, times(1)).save(testMedecin);
    }

    @Test
    @Transactional
    public void updateNonExistingMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Create the Medecin
        MedecinDTO medecinDTO = medecinMapper.toDto(medecin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedecinMockMvc.perform(put("/api/medecins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medecinDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Medecin in Elasticsearch
        verify(mockMedecinSearchRepository, times(0)).save(medecin);
    }

    @Test
    @Transactional
    public void deleteMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        int databaseSizeBeforeDelete = medecinRepository.findAll().size();

        // Delete the medecin
        restMedecinMockMvc.perform(delete("/api/medecins/{id}", medecin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Medecin in Elasticsearch
        verify(mockMedecinSearchRepository, times(1)).deleteById(medecin.getId());
    }

    @Test
    @Transactional
    public void searchMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);
        when(mockMedecinSearchRepository.search(queryStringQuery("id:" + medecin.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(medecin), PageRequest.of(0, 1), 1));
        // Search the medecin
        restMedecinMockMvc.perform(get("/api/_search/medecins?query=id:" + medecin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medecin.class);
        Medecin medecin1 = new Medecin();
        medecin1.setId(1L);
        Medecin medecin2 = new Medecin();
        medecin2.setId(medecin1.getId());
        assertThat(medecin1).isEqualTo(medecin2);
        medecin2.setId(2L);
        assertThat(medecin1).isNotEqualTo(medecin2);
        medecin1.setId(null);
        assertThat(medecin1).isNotEqualTo(medecin2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedecinDTO.class);
        MedecinDTO medecinDTO1 = new MedecinDTO();
        medecinDTO1.setId(1L);
        MedecinDTO medecinDTO2 = new MedecinDTO();
        assertThat(medecinDTO1).isNotEqualTo(medecinDTO2);
        medecinDTO2.setId(medecinDTO1.getId());
        assertThat(medecinDTO1).isEqualTo(medecinDTO2);
        medecinDTO2.setId(2L);
        assertThat(medecinDTO1).isNotEqualTo(medecinDTO2);
        medecinDTO1.setId(null);
        assertThat(medecinDTO1).isNotEqualTo(medecinDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(medecinMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(medecinMapper.fromId(null)).isNull();
    }
}
