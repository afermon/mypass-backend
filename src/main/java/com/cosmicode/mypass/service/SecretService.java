package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.Secret;
import com.cosmicode.mypass.repository.SecretRepository;
import com.cosmicode.mypass.repository.search.SecretSearchRepository;
import com.cosmicode.mypass.service.dto.SecretDTO;
import com.cosmicode.mypass.service.mapper.SecretMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Secret.
 */
@Service
@Transactional
public class SecretService {

    private final Logger log = LoggerFactory.getLogger(SecretService.class);

    private final SecretRepository secretRepository;

    private final SecretMapper secretMapper;

    private final SecretSearchRepository secretSearchRepository;

    public SecretService(SecretRepository secretRepository, SecretMapper secretMapper, SecretSearchRepository secretSearchRepository) {
        this.secretRepository = secretRepository;
        this.secretMapper = secretMapper;
        this.secretSearchRepository = secretSearchRepository;
    }

    /**
     * Save a secret.
     *
     * @param secretDTO the entity to save
     * @return the persisted entity
     */
    public SecretDTO save(SecretDTO secretDTO) {
        log.debug("Request to save Secret : {}", secretDTO);

        Secret secret = secretMapper.toEntity(secretDTO);
        secret.setModified(Instant.now());
        secret = secretRepository.save(secret);
        SecretDTO result = secretMapper.toDto(secret);
        secretSearchRepository.save(secret);
        return result;
    }

    /**
     * Get all the secrets.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SecretDTO> findAll() {
        log.debug("Request to get all Secrets");
        return secretRepository.findAll().stream()
            .map(secretMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one secret by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SecretDTO> findOne(Long id) {
        log.debug("Request to get Secret : {}", id);
        return secretRepository.findById(id)
            .map(secretMapper::toDto);
    }

    /**
     * Delete the secret by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Secret : {}", id);
        secretRepository.deleteById(id);
        secretSearchRepository.deleteById(id);
    }

    /**
     * Search for the secret corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SecretDTO> search(String query) {
        log.debug("Request to search Secrets for query {}", query);
        return StreamSupport
            .stream(secretSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(secretMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<SecretDTO> getCurrentUserSecrets(){
        log.debug("Request to get Secrets for current user");
        return secretRepository.findByOwnerIsCurrentUser().stream()
            .map(secretMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<SecretDTO> getFolderSecrets(Long id){
        log.debug("Request to get Secrets for folder");
        return secretRepository.findByFolderId(id).stream()
            .map(secretMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
