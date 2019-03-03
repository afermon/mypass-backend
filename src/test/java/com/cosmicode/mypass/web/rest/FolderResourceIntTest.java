package com.cosmicode.mypass.web.rest;

import com.cosmicode.mypass.MyPassApp;

import com.cosmicode.mypass.domain.Folder;
import com.cosmicode.mypass.repository.FolderRepository;
import com.cosmicode.mypass.repository.search.FolderSearchRepository;
import com.cosmicode.mypass.service.FolderService;
import com.cosmicode.mypass.service.dto.FolderDTO;
import com.cosmicode.mypass.service.mapper.FolderMapper;
import com.cosmicode.mypass.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.cosmicode.mypass.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FolderResource REST controller.
 *
 * @see FolderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyPassApp.class)
public class FolderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FolderRepository folderRepository;

    @Mock
    private FolderRepository folderRepositoryMock;

    @Autowired
    private FolderMapper folderMapper;

    @Mock
    private FolderService folderServiceMock;

    @Autowired
    private FolderService folderService;

    /**
     * This repository is mocked in the com.cosmicode.mypass.repository.search test package.
     *
     * @see com.cosmicode.mypass.repository.search.FolderSearchRepositoryMockConfiguration
     */
    @Autowired
    private FolderSearchRepository mockFolderSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFolderMockMvc;

    private Folder folder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FolderResource folderResource = new FolderResource(folderService);
        this.restFolderMockMvc = MockMvcBuilders.standaloneSetup(folderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Folder createEntity(EntityManager em) {
        Folder folder = new Folder()
            .name(DEFAULT_NAME)
            .icon(DEFAULT_ICON)
            .key(DEFAULT_KEY)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED);
        return folder;
    }

    @Before
    public void initTest() {
        folder = createEntity(em);
    }

    @Test
    @Transactional
    public void createFolder() throws Exception {
        int databaseSizeBeforeCreate = folderRepository.findAll().size();

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);
        restFolderMockMvc.perform(post("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isCreated());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeCreate + 1);
        Folder testFolder = folderList.get(folderList.size() - 1);
        assertThat(testFolder.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFolder.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testFolder.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testFolder.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFolder.getModified()).isEqualTo(DEFAULT_MODIFIED);

        // Validate the Folder in Elasticsearch
        verify(mockFolderSearchRepository, times(1)).save(testFolder);
    }

    @Test
    @Transactional
    public void createFolderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = folderRepository.findAll().size();

        // Create the Folder with an existing ID
        folder.setId(1L);
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFolderMockMvc.perform(post("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeCreate);

        // Validate the Folder in Elasticsearch
        verify(mockFolderSearchRepository, times(0)).save(folder);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = folderRepository.findAll().size();
        // set the field null
        folder.setName(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc.perform(post("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIconIsRequired() throws Exception {
        int databaseSizeBeforeTest = folderRepository.findAll().size();
        // set the field null
        folder.setIcon(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc.perform(post("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = folderRepository.findAll().size();
        // set the field null
        folder.setKey(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc.perform(post("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = folderRepository.findAll().size();
        // set the field null
        folder.setCreated(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc.perform(post("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = folderRepository.findAll().size();
        // set the field null
        folder.setModified(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc.perform(post("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFolders() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        // Get all the folderList
        restFolderMockMvc.perform(get("/api/folders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(folder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFoldersWithEagerRelationshipsIsEnabled() throws Exception {
        FolderResource folderResource = new FolderResource(folderServiceMock);
        when(folderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restFolderMockMvc = MockMvcBuilders.standaloneSetup(folderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFolderMockMvc.perform(get("/api/folders?eagerload=true"))
        .andExpect(status().isOk());

        verify(folderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFoldersWithEagerRelationshipsIsNotEnabled() throws Exception {
        FolderResource folderResource = new FolderResource(folderServiceMock);
            when(folderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restFolderMockMvc = MockMvcBuilders.standaloneSetup(folderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFolderMockMvc.perform(get("/api/folders?eagerload=true"))
        .andExpect(status().isOk());

            verify(folderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFolder() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        // Get the folder
        restFolderMockMvc.perform(get("/api/folders/{id}", folder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(folder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFolder() throws Exception {
        // Get the folder
        restFolderMockMvc.perform(get("/api/folders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFolder() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        int databaseSizeBeforeUpdate = folderRepository.findAll().size();

        // Update the folder
        Folder updatedFolder = folderRepository.findById(folder.getId()).get();
        // Disconnect from session so that the updates on updatedFolder are not directly saved in db
        em.detach(updatedFolder);
        updatedFolder
            .name(UPDATED_NAME)
            .icon(UPDATED_ICON)
            .key(UPDATED_KEY)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED);
        FolderDTO folderDTO = folderMapper.toDto(updatedFolder);

        restFolderMockMvc.perform(put("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isOk());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeUpdate);
        Folder testFolder = folderList.get(folderList.size() - 1);
        assertThat(testFolder.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFolder.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testFolder.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testFolder.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFolder.getModified()).isEqualTo(UPDATED_MODIFIED);

        // Validate the Folder in Elasticsearch
        verify(mockFolderSearchRepository, times(1)).save(testFolder);
    }

    @Test
    @Transactional
    public void updateNonExistingFolder() throws Exception {
        int databaseSizeBeforeUpdate = folderRepository.findAll().size();

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFolderMockMvc.perform(put("/api/folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Folder in Elasticsearch
        verify(mockFolderSearchRepository, times(0)).save(folder);
    }

    @Test
    @Transactional
    public void deleteFolder() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);

        int databaseSizeBeforeDelete = folderRepository.findAll().size();

        // Get the folder
        restFolderMockMvc.perform(delete("/api/folders/{id}", folder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Folder> folderList = folderRepository.findAll();
        assertThat(folderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Folder in Elasticsearch
        verify(mockFolderSearchRepository, times(1)).deleteById(folder.getId());
    }

    @Test
    @Transactional
    public void searchFolder() throws Exception {
        // Initialize the database
        folderRepository.saveAndFlush(folder);
        when(mockFolderSearchRepository.search(queryStringQuery("id:" + folder.getId())))
            .thenReturn(Collections.singletonList(folder));
        // Search the folder
        restFolderMockMvc.perform(get("/api/_search/folders?query=id:" + folder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(folder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Folder.class);
        Folder folder1 = new Folder();
        folder1.setId(1L);
        Folder folder2 = new Folder();
        folder2.setId(folder1.getId());
        assertThat(folder1).isEqualTo(folder2);
        folder2.setId(2L);
        assertThat(folder1).isNotEqualTo(folder2);
        folder1.setId(null);
        assertThat(folder1).isNotEqualTo(folder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FolderDTO.class);
        FolderDTO folderDTO1 = new FolderDTO();
        folderDTO1.setId(1L);
        FolderDTO folderDTO2 = new FolderDTO();
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
        folderDTO2.setId(folderDTO1.getId());
        assertThat(folderDTO1).isEqualTo(folderDTO2);
        folderDTO2.setId(2L);
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
        folderDTO1.setId(null);
        assertThat(folderDTO1).isNotEqualTo(folderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(folderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(folderMapper.fromId(null)).isNull();
    }
}
