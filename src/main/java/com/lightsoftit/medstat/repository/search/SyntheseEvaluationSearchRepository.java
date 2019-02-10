package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.SyntheseEvaluation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SyntheseEvaluation entity.
 */
public interface SyntheseEvaluationSearchRepository extends ElasticsearchRepository<SyntheseEvaluation, Long> {
}
