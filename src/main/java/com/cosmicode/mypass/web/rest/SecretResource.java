package com.cosmicode.mypass.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cosmicode.mypass.domain.User;
import com.cosmicode.mypass.security.SecurityUtils;
import com.cosmicode.mypass.service.SecretService;
import com.cosmicode.mypass.service.UserService;
import com.cosmicode.mypass.web.rest.errors.BadRequestAlertException;
import com.cosmicode.mypass.web.rest.util.HeaderUtil;
import com.cosmicode.mypass.service.dto.SecretDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Secret.
 */
@RestController
@RequestMapping("/api")
public class SecretResource {

    private final Logger log = LoggerFactory.getLogger(SecretResource.class);

    private static final String ENTITY_NAME = "secret";

    private final SecretService secretService;

    private final UserService userService;

    public SecretResource(SecretService secretService, UserService userService) {
        this.secretService = secretService;
        this.userService = userService;
    }

    /**
     * POST  /secrets : Create a new secret.
     *
     * @param secretDTO the secretDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new secretDTO, or with status 400 (Bad Request) if the secret has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/secrets")
    @Timed
    public ResponseEntity<SecretDTO> createSecret(@Valid @RequestBody SecretDTO secretDTO) throws URISyntaxException {
        log.debug("REST request to save Secret : {}", secretDTO);
        if (secretDTO.getId() != null) {
            throw new BadRequestAlertException("A new secret cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try {
            User user = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
            secretDTO.setOwnerId(user.getId());
            secretDTO.setOwnerLogin(user.getLogin());
        } catch (Exception e){
            log.error(e.toString());
        }

        SecretDTO result = secretService.save(secretDTO);
        return ResponseEntity.created(new URI("/api/secrets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /secrets : Updates an existing secret.
     *
     * @param secretDTO the secretDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated secretDTO,
     * or with status 400 (Bad Request) if the secretDTO is not valid,
     * or with status 500 (Internal Server Error) if the secretDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/secrets")
    @Timed
    public ResponseEntity<SecretDTO> updateSecret(@Valid @RequestBody SecretDTO secretDTO) throws URISyntaxException {
        log.debug("REST request to update Secret : {}", secretDTO);
        if (secretDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SecretDTO result = secretService.save(secretDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, secretDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /secrets : get all the secrets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of secrets in body
     */
    @GetMapping("/secrets")
    @Timed
    public List<SecretDTO> getAllSecrets() {
        log.debug("REST request to get all Secrets");
        return secretService.findAll();
    }

    /**
     * GET  /secrets/:id : get the "id" secret.
     *
     * @param id the id of the secretDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the secretDTO, or with status 404 (Not Found)
     */
    @GetMapping("/secrets/{id}")
    @Timed
    public ResponseEntity<SecretDTO> getSecret(@PathVariable Long id) {
        log.debug("REST request to get Secret : {}", id);
        Optional<SecretDTO> secretDTO = secretService.findOne(id);
        return ResponseUtil.wrapOrNotFound(secretDTO);
    }

    /**
     * DELETE  /secrets/:id : delete the "id" secret.
     *
     * @param id the id of the secretDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/secrets/{id}")
    @Timed
    public ResponseEntity<Void> deleteSecret(@PathVariable Long id) {
        log.debug("REST request to delete Secret : {}", id);
        secretService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /secrets/user : get all current user secrets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of secrets in body
     */
    @GetMapping("/secrets/user")
    @Timed
    public List<SecretDTO> getCurrentUserSecrets() {
        log.debug("REST request to get current user Secrets");
        return secretService.getCurrentUserSecrets();
    }

    /**
     * GET  /secrets/user : get all current user secrets.
     *
     * @param id the id of the folder to retrieve
     * @return the ResponseEntity with status 200 (OK) and the list of secrets in body
     */
    @GetMapping("/secrets/folder/{id}")
    @Timed
    public List<SecretDTO> getFolderSecrets(@PathVariable Long id) {
        log.debug("REST request to get current user Secrets");
        return secretService.getFolderSecrets(id);
    }
}
