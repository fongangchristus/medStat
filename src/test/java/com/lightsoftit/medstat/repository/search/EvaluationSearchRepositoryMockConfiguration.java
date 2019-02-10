package com.lightsoftit.medstat.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of EvaluationSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EvaluationSearchRepositoryMockConfiguration {

    @MockBean
    private EvaluationSearchRepository mockEvaluationSearchRepository;

}
