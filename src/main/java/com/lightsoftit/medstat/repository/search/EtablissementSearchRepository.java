package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Etablissement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Etablissement entity.
 */
public interface EtablissementSearchRepository extends ElasticsearchRepository<Etablissement, Long> {
}
