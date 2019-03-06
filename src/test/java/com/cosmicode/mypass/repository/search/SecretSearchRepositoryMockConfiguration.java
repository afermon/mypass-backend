package com.cosmicode.mypass.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SecretSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SecretSearchRepositoryMockConfiguration {

    @MockBean
    private SecretSearchRepository mockSecretSearchRepository;

}
