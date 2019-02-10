package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Medecin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Medecin entity.
 */
public interface MedecinSearchRepository extends ElasticsearchRepository<Medecin, Long> {
}
