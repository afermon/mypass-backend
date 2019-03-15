package com.cosmicode.mypass.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cosmicode.mypass.service.FolderService;
import com.cosmicode.mypass.service.SecretService;
import com.cosmicode.mypass.web.rest.errors.BadRequestAlertException;
import com.cosmicode.mypass.web.rest.util.HeaderUtil;
import com.cosmicode.mypass.service.dto.FolderDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Folder.
 */
@RestController
@RequestMapping("/api")
public class FolderResource {

    private final Logger log = LoggerFactory.getLogger(FolderResource.class);

    private static final String ENTITY_NAME = "folder";

    private final FolderService folderService;

    private final SecretService secretService;

    public FolderResource(FolderService folderService, SecretService secretService) {
        this.folderService = folderService;
        this.secretService = secretService;
    }

    /**
     * POST  /folders : Create a new folder.
     *
     * @param folderDTO the folderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new folderDTO, or with status 400 (Bad Request) if the folder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/folders")
    @Timed
    public ResponseEntity<FolderDTO> createFolder(@Valid @RequestBody FolderDTO folderDTO) throws URISyntaxException {
        log.debug("REST request to save Folder : {}", folderDTO);
        if (folderDTO.getId() != null) {
            throw new BadRequestAlertException("A new folder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FolderDTO result = folderService.save(folderDTO);
        return ResponseEntity.created(new URI("/api/folders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /folders : Updates an existing folder.
     *
     * @param folderDTO the folderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated folderDTO,
     * or with status 400 (Bad Request) if the folderDTO is not valid,
     * or with status 500 (Internal Server Error) if the folderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/folders")
    @Timed
    public ResponseEntity<FolderDTO> updateFolder(@Valid @RequestBody FolderDTO folderDTO) throws URISyntaxException {
        log.debug("REST request to update Folder : {}", folderDTO);
        if (folderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FolderDTO result = folderService.save(folderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, folderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /folders : get all the folders.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of folders in body
     */
    @GetMapping("/folders")
    @Timed
    public List<FolderDTO> getAllFolders(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Folders");
        return folderService.findAll();
    }

    /**
     * GET  /folders/:id : get the "id" folder.
     *
     * @param id the id of the folderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the folderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/folders/{id}")
    @Timed
    public ResponseEntity<FolderDTO> getFolder(@PathVariable Long id) {
        log.debug("REST request to get Folder : {}", id);
        Optional<FolderDTO> folderDTO = folderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(folderDTO);
    }

    /**
     * DELETE  /folders/:id : delete the "id" folder.
     *
     * @param id the id of the folderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/folders/{id}")
    @Timed
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        log.debug("REST request to delete Folder : {}", id);
        folderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /folders/user : get all the user folders.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of folders in body
     */
    @GetMapping("/folders/user")
    @Timed
    public List<FolderDTO> getCurrentUserFolders(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Folders");
        List<FolderDTO>  folders = folderService.getCurrentUserFolders();
        if (eagerload){
            for (FolderDTO folder: folders) {
                folder.setSecrets(new HashSet<>(secretService.getFolderSecrets(folder.getId())));
            }
        }
        return folders;
    }

}
