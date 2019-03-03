package com.cosmicode.mypass.repository.search;

import com.cosmicode.mypass.domain.Secret;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Secret entity.
 */
public interface SecretSearchRepository extends ElasticsearchRepository<Secret, Long> {
}
