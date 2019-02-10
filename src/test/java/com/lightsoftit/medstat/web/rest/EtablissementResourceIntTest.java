package com.lightsoftit.medstat.web.rest;

import com.lightsoftit.medstat.MedStatApp;

import com.lightsoftit.medstat.domain.Etablissement;
import com.lightsoftit.medstat.repository.EtablissementRepository;
import com.lightsoftit.medstat.repository.search.EtablissementSearchRepository;
import com.lightsoftit.medstat.service.EtablissementService;
import com.lightsoftit.medstat.service.dto.EtablissementDTO;
import com.lightsoftit.medstat.service.mapper.EtablissementMapper;
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

import com.lightsoftit.medstat.domain.enumeration.TypeEtab;
/**
 * Test class for the EtablissementResource REST controller.
 *
 * @see EtablissementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MedStatApp.class)
public class EtablissementResourceIntTest {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TypeEtab DEFAULT_TYPE = TypeEtab.PUBLIQUE;
    private static final TypeEtab UPDATED_TYPE = TypeEtab.PRIVEE;

    private static final Double DEFAULT_NBR_MEDECIN = 1D;
    private static final Double UPDATED_NBR_MEDECIN = 2D;

    private static final Double DEFAULT_NBR_PERSONNEL_SF_IS_IDE_TMS = 1D;
    private static final Double UPDATED_NBR_PERSONNEL_SF_IS_IDE_TMS = 2D;

    private static final Double DEFAULT_NBR_PERSONNEL_IB_IBA_TAL = 1D;
    private static final Double UPDATED_NBR_PERSONNEL_IB_IBA_TAL = 2D;

    private static final Double DEFAULT_NBR_PERSONNEL_AS_ATMS = 1D;
    private static final Double UPDATED_NBR_PERSONNEL_AS_ATMS = 2D;

    private static final Double DEFAULT_NBR_PERSONNEL_ADMINISTRATIF = 1D;
    private static final Double UPDATED_NBR_PERSONNEL_ADMINISTRATIF = 2D;

    private static final Double DEFAULT_NBR_PERSONNEL_NOM_QUALIFIE = 1D;
    private static final Double UPDATED_NBR_PERSONNEL_NOM_QUALIFIE = 2D;

    private static final Double DEFAULT_RATIO_PERSONNEL_QUALIFIEPAR_HAB = 1D;
    private static final Double UPDATED_RATIO_PERSONNEL_QUALIFIEPAR_HAB = 2D;

    private static final Double DEFAULT_NBR_LITPAR_HABITANT = 1D;
    private static final Double UPDATED_NBR_LITPAR_HABITANT = 2D;

    private static final Double DEFAULT_PENTA_1 = 1D;
    private static final Double UPDATED_PENTA_1 = 2D;

    private static final Double DEFAULT_PENTA_3 = 1D;
    private static final Double UPDATED_PENTA_3 = 2D;

    private static final Double DEFAULT_DIF_PENTA_1_ET_3 = 1D;
    private static final Double UPDATED_DIF_PENTA_1_ET_3 = 2D;

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private EtablissementMapper etablissementMapper;

    @Autowired
    private EtablissementService etablissementService;

    /**
     * This repository is mocked in the com.lightsoftit.medstat.repository.search test package.
     *
     * @see com.lightsoftit.medstat.repository.search.EtablissementSearchRepositoryMockConfiguration
     */
    @Autowired
    private EtablissementSearchRepository mockEtablissementSearchRepository;

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

    private MockMvc restEtablissementMockMvc;

    private Etablissement etablissement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EtablissementResource etablissementResource = new EtablissementResource(etablissementService);
        this.restEtablissementMockMvc = MockMvcBuilders.standaloneSetup(etablissementResource)
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
    public static Etablissement createEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .libele(DEFAULT_LIBELE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .nbrMedecin(DEFAULT_NBR_MEDECIN)
            .nbrPersonnelSfIsIdeTms(DEFAULT_NBR_PERSONNEL_SF_IS_IDE_TMS)
            .nbrPersonnelIbIbaTal(DEFAULT_NBR_PERSONNEL_IB_IBA_TAL)
            .nbrPersonnelAsAtms(DEFAULT_NBR_PERSONNEL_AS_ATMS)
            .nbrPersonnelAdministratif(DEFAULT_NBR_PERSONNEL_ADMINISTRATIF)
            .nbrPersonnelNomQualifie(DEFAULT_NBR_PERSONNEL_NOM_QUALIFIE)
            .ratioPersonnelQualifieparHab(DEFAULT_RATIO_PERSONNEL_QUALIFIEPAR_HAB)
            .nbrLitparHabitant(DEFAULT_NBR_LITPAR_HABITANT)
            .penta1(DEFAULT_PENTA_1)
            .penta3(DEFAULT_PENTA_3)
            .difPenta1Et3(DEFAULT_DIF_PENTA_1_ET_3);
        return etablissement;
    }

    @Before
    public void initTest() {
        etablissement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtablissement() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // Create the Etablissement
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);
        restEtablissementMockMvc.perform(post("/api/etablissements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etablissementDTO)))
            .andExpect(status().isCreated());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate + 1);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testEtablissement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEtablissement.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEtablissement.getNbrMedecin()).isEqualTo(DEFAULT_NBR_MEDECIN);
        assertThat(testEtablissement.getNbrPersonnelSfIsIdeTms()).isEqualTo(DEFAULT_NBR_PERSONNEL_SF_IS_IDE_TMS);
        assertThat(testEtablissement.getNbrPersonnelIbIbaTal()).isEqualTo(DEFAULT_NBR_PERSONNEL_IB_IBA_TAL);
        assertThat(testEtablissement.getNbrPersonnelAsAtms()).isEqualTo(DEFAULT_NBR_PERSONNEL_AS_ATMS);
        assertThat(testEtablissement.getNbrPersonnelAdministratif()).isEqualTo(DEFAULT_NBR_PERSONNEL_ADMINISTRATIF);
        assertThat(testEtablissement.getNbrPersonnelNomQualifie()).isEqualTo(DEFAULT_NBR_PERSONNEL_NOM_QUALIFIE);
        assertThat(testEtablissement.getRatioPersonnelQualifieparHab()).isEqualTo(DEFAULT_RATIO_PERSONNEL_QUALIFIEPAR_HAB);
        assertThat(testEtablissement.getNbrLitparHabitant()).isEqualTo(DEFAULT_NBR_LITPAR_HABITANT);
        assertThat(testEtablissement.getPenta1()).isEqualTo(DEFAULT_PENTA_1);
        assertThat(testEtablissement.getPenta3()).isEqualTo(DEFAULT_PENTA_3);
        assertThat(testEtablissement.getDifPenta1Et3()).isEqualTo(DEFAULT_DIF_PENTA_1_ET_3);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).save(testEtablissement);
    }

    @Test
    @Transactional
    public void createEtablissementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // Create the Etablissement with an existing ID
        etablissement.setId(1L);
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtablissementMockMvc.perform(post("/api/etablissements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etablissementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(0)).save(etablissement);
    }

    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setLibele(null);

        // Create the Etablissement, which fails.
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);

        restEtablissementMockMvc.perform(post("/api/etablissements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etablissementDTO)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setType(null);

        // Create the Etablissement, which fails.
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);

        restEtablissementMockMvc.perform(post("/api/etablissements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etablissementDTO)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEtablissements() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList
        restEtablissementMockMvc.perform(get("/api/etablissements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nbrMedecin").value(hasItem(DEFAULT_NBR_MEDECIN.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelSfIsIdeTms").value(hasItem(DEFAULT_NBR_PERSONNEL_SF_IS_IDE_TMS.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelIbIbaTal").value(hasItem(DEFAULT_NBR_PERSONNEL_IB_IBA_TAL.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelAsAtms").value(hasItem(DEFAULT_NBR_PERSONNEL_AS_ATMS.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelAdministratif").value(hasItem(DEFAULT_NBR_PERSONNEL_ADMINISTRATIF.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelNomQualifie").value(hasItem(DEFAULT_NBR_PERSONNEL_NOM_QUALIFIE.doubleValue())))
            .andExpect(jsonPath("$.[*].ratioPersonnelQualifieparHab").value(hasItem(DEFAULT_RATIO_PERSONNEL_QUALIFIEPAR_HAB.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrLitparHabitant").value(hasItem(DEFAULT_NBR_LITPAR_HABITANT.doubleValue())))
            .andExpect(jsonPath("$.[*].penta1").value(hasItem(DEFAULT_PENTA_1.doubleValue())))
            .andExpect(jsonPath("$.[*].penta3").value(hasItem(DEFAULT_PENTA_3.doubleValue())))
            .andExpect(jsonPath("$.[*].difPenta1Et3").value(hasItem(DEFAULT_DIF_PENTA_1_ET_3.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get the etablissement
        restEtablissementMockMvc.perform(get("/api/etablissements/{id}", etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(etablissement.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.nbrMedecin").value(DEFAULT_NBR_MEDECIN.doubleValue()))
            .andExpect(jsonPath("$.nbrPersonnelSfIsIdeTms").value(DEFAULT_NBR_PERSONNEL_SF_IS_IDE_TMS.doubleValue()))
            .andExpect(jsonPath("$.nbrPersonnelIbIbaTal").value(DEFAULT_NBR_PERSONNEL_IB_IBA_TAL.doubleValue()))
            .andExpect(jsonPath("$.nbrPersonnelAsAtms").value(DEFAULT_NBR_PERSONNEL_AS_ATMS.doubleValue()))
            .andExpect(jsonPath("$.nbrPersonnelAdministratif").value(DEFAULT_NBR_PERSONNEL_ADMINISTRATIF.doubleValue()))
            .andExpect(jsonPath("$.nbrPersonnelNomQualifie").value(DEFAULT_NBR_PERSONNEL_NOM_QUALIFIE.doubleValue()))
            .andExpect(jsonPath("$.ratioPersonnelQualifieparHab").value(DEFAULT_RATIO_PERSONNEL_QUALIFIEPAR_HAB.doubleValue()))
            .andExpect(jsonPath("$.nbrLitparHabitant").value(DEFAULT_NBR_LITPAR_HABITANT.doubleValue()))
            .andExpect(jsonPath("$.penta1").value(DEFAULT_PENTA_1.doubleValue()))
            .andExpect(jsonPath("$.penta3").value(DEFAULT_PENTA_3.doubleValue()))
            .andExpect(jsonPath("$.difPenta1Et3").value(DEFAULT_DIF_PENTA_1_ET_3.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEtablissement() throws Exception {
        // Get the etablissement
        restEtablissementMockMvc.perform(get("/api/etablissements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement
        Etablissement updatedEtablissement = etablissementRepository.findById(etablissement.getId()).get();
        // Disconnect from session so that the updates on updatedEtablissement are not directly saved in db
        em.detach(updatedEtablissement);
        updatedEtablissement
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .nbrMedecin(UPDATED_NBR_MEDECIN)
            .nbrPersonnelSfIsIdeTms(UPDATED_NBR_PERSONNEL_SF_IS_IDE_TMS)
            .nbrPersonnelIbIbaTal(UPDATED_NBR_PERSONNEL_IB_IBA_TAL)
            .nbrPersonnelAsAtms(UPDATED_NBR_PERSONNEL_AS_ATMS)
            .nbrPersonnelAdministratif(UPDATED_NBR_PERSONNEL_ADMINISTRATIF)
            .nbrPersonnelNomQualifie(UPDATED_NBR_PERSONNEL_NOM_QUALIFIE)
            .ratioPersonnelQualifieparHab(UPDATED_RATIO_PERSONNEL_QUALIFIEPAR_HAB)
            .nbrLitparHabitant(UPDATED_NBR_LITPAR_HABITANT)
            .penta1(UPDATED_PENTA_1)
            .penta3(UPDATED_PENTA_3)
            .difPenta1Et3(UPDATED_DIF_PENTA_1_ET_3);
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(updatedEtablissement);

        restEtablissementMockMvc.perform(put("/api/etablissements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etablissementDTO)))
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testEtablissement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEtablissement.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEtablissement.getNbrMedecin()).isEqualTo(UPDATED_NBR_MEDECIN);
        assertThat(testEtablissement.getNbrPersonnelSfIsIdeTms()).isEqualTo(UPDATED_NBR_PERSONNEL_SF_IS_IDE_TMS);
        assertThat(testEtablissement.getNbrPersonnelIbIbaTal()).isEqualTo(UPDATED_NBR_PERSONNEL_IB_IBA_TAL);
        assertThat(testEtablissement.getNbrPersonnelAsAtms()).isEqualTo(UPDATED_NBR_PERSONNEL_AS_ATMS);
        assertThat(testEtablissement.getNbrPersonnelAdministratif()).isEqualTo(UPDATED_NBR_PERSONNEL_ADMINISTRATIF);
        assertThat(testEtablissement.getNbrPersonnelNomQualifie()).isEqualTo(UPDATED_NBR_PERSONNEL_NOM_QUALIFIE);
        assertThat(testEtablissement.getRatioPersonnelQualifieparHab()).isEqualTo(UPDATED_RATIO_PERSONNEL_QUALIFIEPAR_HAB);
        assertThat(testEtablissement.getNbrLitparHabitant()).isEqualTo(UPDATED_NBR_LITPAR_HABITANT);
        assertThat(testEtablissement.getPenta1()).isEqualTo(UPDATED_PENTA_1);
        assertThat(testEtablissement.getPenta3()).isEqualTo(UPDATED_PENTA_3);
        assertThat(testEtablissement.getDifPenta1Et3()).isEqualTo(UPDATED_DIF_PENTA_1_ET_3);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).save(testEtablissement);
    }

    @Test
    @Transactional
    public void updateNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Create the Etablissement
        EtablissementDTO etablissementDTO = etablissementMapper.toDto(etablissement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtablissementMockMvc.perform(put("/api/etablissements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etablissementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(0)).save(etablissement);
    }

    @Test
    @Transactional
    public void deleteEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeDelete = etablissementRepository.findAll().size();

        // Delete the etablissement
        restEtablissementMockMvc.perform(delete("/api/etablissements/{id}", etablissement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Etablissement in Elasticsearch
        verify(mockEtablissementSearchRepository, times(1)).deleteById(etablissement.getId());
    }

    @Test
    @Transactional
    public void searchEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);
        when(mockEtablissementSearchRepository.search(queryStringQuery("id:" + etablissement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(etablissement), PageRequest.of(0, 1), 1));
        // Search the etablissement
        restEtablissementMockMvc.perform(get("/api/_search/etablissements?query=id:" + etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nbrMedecin").value(hasItem(DEFAULT_NBR_MEDECIN.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelSfIsIdeTms").value(hasItem(DEFAULT_NBR_PERSONNEL_SF_IS_IDE_TMS.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelIbIbaTal").value(hasItem(DEFAULT_NBR_PERSONNEL_IB_IBA_TAL.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelAsAtms").value(hasItem(DEFAULT_NBR_PERSONNEL_AS_ATMS.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelAdministratif").value(hasItem(DEFAULT_NBR_PERSONNEL_ADMINISTRATIF.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrPersonnelNomQualifie").value(hasItem(DEFAULT_NBR_PERSONNEL_NOM_QUALIFIE.doubleValue())))
            .andExpect(jsonPath("$.[*].ratioPersonnelQualifieparHab").value(hasItem(DEFAULT_RATIO_PERSONNEL_QUALIFIEPAR_HAB.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrLitparHabitant").value(hasItem(DEFAULT_NBR_LITPAR_HABITANT.doubleValue())))
            .andExpect(jsonPath("$.[*].penta1").value(hasItem(DEFAULT_PENTA_1.doubleValue())))
            .andExpect(jsonPath("$.[*].penta3").value(hasItem(DEFAULT_PENTA_3.doubleValue())))
            .andExpect(jsonPath("$.[*].difPenta1Et3").value(hasItem(DEFAULT_DIF_PENTA_1_ET_3.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etablissement.class);
        Etablissement etablissement1 = new Etablissement();
        etablissement1.setId(1L);
        Etablissement etablissement2 = new Etablissement();
        etablissement2.setId(etablissement1.getId());
        assertThat(etablissement1).isEqualTo(etablissement2);
        etablissement2.setId(2L);
        assertThat(etablissement1).isNotEqualTo(etablissement2);
        etablissement1.setId(null);
        assertThat(etablissement1).isNotEqualTo(etablissement2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtablissementDTO.class);
        EtablissementDTO etablissementDTO1 = new EtablissementDTO();
        etablissementDTO1.setId(1L);
        EtablissementDTO etablissementDTO2 = new EtablissementDTO();
        assertThat(etablissementDTO1).isNotEqualTo(etablissementDTO2);
        etablissementDTO2.setId(etablissementDTO1.getId());
        assertThat(etablissementDTO1).isEqualTo(etablissementDTO2);
        etablissementDTO2.setId(2L);
        assertThat(etablissementDTO1).isNotEqualTo(etablissementDTO2);
        etablissementDTO1.setId(null);
        assertThat(etablissementDTO1).isNotEqualTo(etablissementDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(etablissementMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(etablissementMapper.fromId(null)).isNull();
    }
}
