package com.lightsoftit.medstat.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of LocalisationSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LocalisationSearchRepositoryMockConfiguration {

    @MockBean
    private LocalisationSearchRepository mockLocalisationSearchRepository;

}
