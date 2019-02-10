package com.lightsoftit.medstat.service.impl;

import com.lightsoftit.medstat.service.QuestionnaireService;
import com.lightsoftit.medstat.domain.Questionnaire;
import com.lightsoftit.medstat.repository.QuestionnaireRepository;
import com.lightsoftit.medstat.repository.search.QuestionnaireSearchRepository;
import com.lightsoftit.medstat.service.dto.QuestionnaireDTO;
import com.lightsoftit.medstat.service.mapper.QuestionnaireMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Questionnaire.
 */
@Service
@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionnaireMapper questionnaireMapper;

    private final QuestionnaireSearchRepository questionnaireSearchRepository;

    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository, QuestionnaireMapper questionnaireMapper, QuestionnaireSearchRepository questionnaireSearchRepository) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireMapper = questionnaireMapper;
        this.questionnaireSearchRepository = questionnaireSearchRepository;
    }

    /**
     * Save a questionnaire.
     *
     * @param questionnaireDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionnaireDTO save(QuestionnaireDTO questionnaireDTO) {
        log.debug("Request to save Questionnaire : {}", questionnaireDTO);
        Questionnaire questionnaire = questionnaireMapper.toEntity(questionnaireDTO);
        questionnaire = questionnaireRepository.save(questionnaire);
        QuestionnaireDTO result = questionnaireMapper.toDto(questionnaire);
        questionnaireSearchRepository.save(questionnaire);
        return result;
    }

    /**
     * Get all the questionnaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionnaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questionnaires");
        return questionnaireRepository.findAll(pageable)
            .map(questionnaireMapper::toDto);
    }


    /**
     * Get one questionnaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionnaireDTO> findOne(Long id) {
        log.debug("Request to get Questionnaire : {}", id);
        return questionnaireRepository.findById(id)
            .map(questionnaireMapper::toDto);
    }

    /**
     * Delete the questionnaire by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Questionnaire : {}", id);        questionnaireRepository.deleteById(id);
        questionnaireSearchRepository.deleteById(id);
    }

    /**
     * Search for the questionnaire corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionnaireDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Questionnaires for query {}", query);
        return questionnaireSearchRepository.search(queryStringQuery(query), pageable)
            .map(questionnaireMapper::toDto);
    }
}
