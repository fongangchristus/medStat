package com.lightsoftit.medstat.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of EtablissementSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EtablissementSearchRepositoryMockConfiguration {

    @MockBean
    private EtablissementSearchRepository mockEtablissementSearchRepository;

}
