package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Localisation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Localisation entity.
 */
public interface LocalisationSearchRepository extends ElasticsearchRepository<Localisation, Long> {
}
