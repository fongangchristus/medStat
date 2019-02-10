package com.lightsoftit.medstat.web.rest;

import com.lightsoftit.medstat.MedStatApp;

import com.lightsoftit.medstat.domain.Localisation;
import com.lightsoftit.medstat.repository.LocalisationRepository;
import com.lightsoftit.medstat.repository.search.LocalisationSearchRepository;
import com.lightsoftit.medstat.service.LocalisationService;
import com.lightsoftit.medstat.service.dto.LocalisationDTO;
import com.lightsoftit.medstat.service.mapper.LocalisationMapper;
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
 * Test class for the LocalisationResource REST controller.
 *
 * @see LocalisationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedStatApp.class)
public class LocalisationResourceIntTest {

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTEMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    @Autowired
    private LocalisationRepository localisationRepository;

    @Autowired
    private LocalisationMapper localisationMapper;

    @Autowired
    private LocalisationService localisationService;

    /**
     * This repository is mocked in the com.lightsoftit.medstat.repository.search test package.
     *
     * @see com.lightsoftit.medstat.repository.search.LocalisationSearchRepositoryMockConfiguration
     */
    @Autowired
    private LocalisationSearchRepository mockLocalisationSearchRepository;

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

    private MockMvc restLocalisationMockMvc;

    private Localisation localisation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocalisationResource localisationResource = new LocalisationResource(localisationService);
        this.restLocalisationMockMvc = MockMvcBuilders.standaloneSetup(localisationResource)
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
    public static Localisation createEntity(EntityManager em) {
        Localisation localisation = new Localisation()
            .region(DEFAULT_REGION)
            .departement(DEFAULT_DEPARTEMENT)
            .ville(DEFAULT_VILLE)
            .district(DEFAULT_DISTRICT);
        return localisation;
    }

    @Before
    public void initTest() {
        localisation = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocalisation() throws Exception {
        int databaseSizeBeforeCreate = localisationRepository.findAll().size();

        // Create the Localisation
        LocalisationDTO localisationDTO = localisationMapper.toDto(localisation);
        restLocalisationMockMvc.perform(post("/api/localisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localisationDTO)))
            .andExpect(status().isCreated());

        // Validate the Localisation in the database
        List<Localisation> localisationList = localisationRepository.findAll();
        assertThat(localisationList).hasSize(databaseSizeBeforeCreate + 1);
        Localisation testLocalisation = localisationList.get(localisationList.size() - 1);
        assertThat(testLocalisation.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testLocalisation.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testLocalisation.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testLocalisation.getDistrict()).isEqualTo(DEFAULT_DISTRICT);

        // Validate the Localisation in Elasticsearch
        verify(mockLocalisationSearchRepository, times(1)).save(testLocalisation);
    }

    @Test
    @Transactional
    public void createLocalisationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = localisationRepository.findAll().size();

        // Create the Localisation with an existing ID
        localisation.setId(1L);
        LocalisationDTO localisationDTO = localisationMapper.toDto(localisation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalisationMockMvc.perform(post("/api/localisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Localisation in the database
        List<Localisation> localisationList = localisationRepository.findAll();
        assertThat(localisationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Localisation in Elasticsearch
        verify(mockLocalisationSearchRepository, times(0)).save(localisation);
    }

    @Test
    @Transactional
    public void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = localisationRepository.findAll().size();
        // set the field null
        localisation.setRegion(null);

        // Create the Localisation, which fails.
        LocalisationDTO localisationDTO = localisationMapper.toDto(localisation);

        restLocalisationMockMvc.perform(post("/api/localisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localisationDTO)))
            .andExpect(status().isBadRequest());

        List<Localisation> localisationList = localisationRepository.findAll();
        assertThat(localisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDistrictIsRequired() throws Exception {
        int databaseSizeBeforeTest = localisationRepository.findAll().size();
        // set the field null
        localisation.setDistrict(null);

        // Create the Localisation, which fails.
        LocalisationDTO localisationDTO = localisationMapper.toDto(localisation);

        restLocalisationMockMvc.perform(post("/api/localisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localisationDTO)))
            .andExpect(status().isBadRequest());

        List<Localisation> localisationList = localisationRepository.findAll();
        assertThat(localisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocalisations() throws Exception {
        // Initialize the database
        localisationRepository.saveAndFlush(localisation);

        // Get all the localisationList
        restLocalisationMockMvc.perform(get("/api/localisations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT.toString())));
    }
    
    @Test
    @Transactional
    public void getLocalisation() throws Exception {
        // Initialize the database
        localisationRepository.saveAndFlush(localisation);

        // Get the localisation
        restLocalisationMockMvc.perform(get("/api/localisations/{id}", localisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(localisation.getId().intValue()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocalisation() throws Exception {
        // Get the localisation
        restLocalisationMockMvc.perform(get("/api/localisations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocalisation() throws Exception {
        // Initialize the database
        localisationRepository.saveAndFlush(localisation);

        int databaseSizeBeforeUpdate = localisationRepository.findAll().size();

        // Update the localisation
        Localisation updatedLocalisation = localisationRepository.findById(localisation.getId()).get();
        // Disconnect from session so that the updates on updatedLocalisation are not directly saved in db
        em.detach(updatedLocalisation);
        updatedLocalisation
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .ville(UPDATED_VILLE)
            .district(UPDATED_DISTRICT);
        LocalisationDTO localisationDTO = localisationMapper.toDto(updatedLocalisation);

        restLocalisationMockMvc.perform(put("/api/localisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localisationDTO)))
            .andExpect(status().isOk());

        // Validate the Localisation in the database
        List<Localisation> localisationList = localisationRepository.findAll();
        assertThat(localisationList).hasSize(databaseSizeBeforeUpdate);
        Localisation testLocalisation = localisationList.get(localisationList.size() - 1);
        assertThat(testLocalisation.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testLocalisation.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testLocalisation.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testLocalisation.getDistrict()).isEqualTo(UPDATED_DISTRICT);

        // Validate the Localisation in Elasticsearch
        verify(mockLocalisationSearchRepository, times(1)).save(testLocalisation);
    }

    @Test
    @Transactional
    public void updateNonExistingLocalisation() throws Exception {
        int databaseSizeBeforeUpdate = localisationRepository.findAll().size();

        // Create the Localisation
        LocalisationDTO localisationDTO = localisationMapper.toDto(localisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalisationMockMvc.perform(put("/api/localisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Localisation in the database
        List<Localisation> localisationList = localisationRepository.findAll();
        assertThat(localisationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Localisation in Elasticsearch
        verify(mockLocalisationSearchRepository, times(0)).save(localisation);
    }

    @Test
    @Transactional
    public void deleteLocalisation() throws Exception {
        // Initialize the database
        localisationRepository.saveAndFlush(localisation);

        int databaseSizeBeforeDelete = localisationRepository.findAll().size();

        // Delete the localisation
        restLocalisationMockMvc.perform(delete("/api/localisations/{id}", localisation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Localisation> localisationList = localisationRepository.findAll();
        assertThat(localisationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Localisation in Elasticsearch
        verify(mockLocalisationSearchRepository, times(1)).deleteById(localisation.getId());
    }

    @Test
    @Transactional
    public void searchLocalisation() throws Exception {
        // Initialize the database
        localisationRepository.saveAndFlush(localisation);
        when(mockLocalisationSearchRepository.search(queryStringQuery("id:" + localisation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(localisation), PageRequest.of(0, 1), 1));
        // Search the localisation
        restLocalisationMockMvc.perform(get("/api/_search/localisations?query=id:" + localisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Localisation.class);
        Localisation localisation1 = new Localisation();
        localisation1.setId(1L);
        Localisation localisation2 = new Localisation();
        localisation2.setId(localisation1.getId());
        assertThat(localisation1).isEqualTo(localisation2);
        localisation2.setId(2L);
        assertThat(localisation1).isNotEqualTo(localisation2);
        localisation1.setId(null);
        assertThat(localisation1).isNotEqualTo(localisation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalisationDTO.class);
        LocalisationDTO localisationDTO1 = new LocalisationDTO();
        localisationDTO1.setId(1L);
        LocalisationDTO localisationDTO2 = new LocalisationDTO();
        assertThat(localisationDTO1).isNotEqualTo(localisationDTO2);
        localisationDTO2.setId(localisationDTO1.getId());
        assertThat(localisationDTO1).isEqualTo(localisationDTO2);
        localisationDTO2.setId(2L);
        assertThat(localisationDTO1).isNotEqualTo(localisationDTO2);
        localisationDTO1.setId(null);
        assertThat(localisationDTO1).isNotEqualTo(localisationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(localisationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(localisationMapper.fromId(null)).isNull();
    }
}
