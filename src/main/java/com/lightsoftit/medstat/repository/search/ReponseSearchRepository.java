package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Reponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reponse entity.
 */
public interface ReponseSearchRepository extends ElasticsearchRepository<Reponse, Long> {
}
