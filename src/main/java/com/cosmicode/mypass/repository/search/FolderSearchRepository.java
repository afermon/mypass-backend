package com.cosmicode.mypass.repository.search;

import com.cosmicode.mypass.domain.Folder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Folder entity.
 */
public interface FolderSearchRepository extends ElasticsearchRepository<Folder, Long> {
}
