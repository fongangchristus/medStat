package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Evaluation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Evaluation entity.
 */
public interface EvaluationSearchRepository extends ElasticsearchRepository<Evaluation, Long> {
}
