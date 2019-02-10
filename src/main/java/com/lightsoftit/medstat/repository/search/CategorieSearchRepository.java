package com.lightsoftit.medstat.repository.search;

import com.lightsoftit.medstat.domain.Categorie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Categorie entity.
 */
public interface CategorieSearchRepository extends ElasticsearchRepository<Categorie, Long> {
}
