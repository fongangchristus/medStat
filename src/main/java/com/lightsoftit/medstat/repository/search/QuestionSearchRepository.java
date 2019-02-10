package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Question entity.
 */
public interface QuestionSearchRepository extends ElasticsearchRepository<Question, Long> {
}
