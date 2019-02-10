package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Questionnaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Questionnaire entity.
 */
public interface QuestionnaireSearchRepository extends ElasticsearchRepository<Questionnaire, Long> {
}
