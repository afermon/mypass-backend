package com.cosmicode.mypass.web.rest;

import com.cosmicode.mypass.MyPassApp;

import com.cosmicode.mypass.domain.Secret;
import com.cosmicode.mypass.repository.SecretRepository;
import com.cosmicode.mypass.service.SecretService;
import com.cosmicode.mypass.service.dto.SecretDTO;
import com.cosmicode.mypass.service.mapper.SecretMapper;
import com.cosmicode.mypass.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.List;


import static com.cosmicode.mypass.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SecretResource REST controller.
 *
 * @see SecretResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyPassApp.class)
public class SecretResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SecretRepository secretRepository;

    @Autowired
    private SecretMapper secretMapper;

    @Autowired
    private SecretService secretService;

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

    private MockMvc restSecretMockMvc;

    private Secret secret;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SecretResource secretResource = new SecretResource(secretService);
        this.restSecretMockMvc = MockMvcBuilders.standaloneSetup(secretResource)
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
    public static Secret createEntity(EntityManager em) {
        Secret secret = new Secret()
            .url(DEFAULT_URL)
            .name(DEFAULT_NAME)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .notes(DEFAULT_NOTES)
            .modified(DEFAULT_MODIFIED);
        return secret;
    }

    @Before
    public void initTest() {
        secret = createEntity(em);
    }

    @Test
    @Transactional
    public void createSecret() throws Exception {
        int databaseSizeBeforeCreate = secretRepository.findAll().size();

        // Create the Secret
        SecretDTO secretDTO = secretMapper.toDto(secret);
        restSecretMockMvc.perform(post("/api/secrets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(secretDTO)))
            .andExpect(status().isCreated());

        // Validate the Secret in the database
        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeCreate + 1);
        Secret testSecret = secretList.get(secretList.size() - 1);
        assertThat(testSecret.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSecret.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecret.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testSecret.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testSecret.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createSecretWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = secretRepository.findAll().size();

        // Create the Secret with an existing ID
        secret.setId(1L);
        SecretDTO secretDTO = secretMapper.toDto(secret);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecretMockMvc.perform(post("/api/secrets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(secretDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Secret in the database
        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = secretRepository.findAll().size();
        // set the field null
        secret.setName(null);

        // Create the Secret, which fails.
        SecretDTO secretDTO = secretMapper.toDto(secret);

        restSecretMockMvc.perform(post("/api/secrets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(secretDTO)))
            .andExpect(status().isBadRequest());

        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = secretRepository.findAll().size();
        // set the field null
        secret.setUsername(null);

        // Create the Secret, which fails.
        SecretDTO secretDTO = secretMapper.toDto(secret);

        restSecretMockMvc.perform(post("/api/secrets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(secretDTO)))
            .andExpect(status().isBadRequest());

        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = secretRepository.findAll().size();
        // set the field null
        secret.setPassword(null);

        // Create the Secret, which fails.
        SecretDTO secretDTO = secretMapper.toDto(secret);

        restSecretMockMvc.perform(post("/api/secrets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(secretDTO)))
            .andExpect(status().isBadRequest());

        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecrets() throws Exception {
        // Initialize the database
        secretRepository.saveAndFlush(secret);

        // Get all the secretList
        restSecretMockMvc.perform(get("/api/secrets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(secret.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.toString())));
    }
    
    @Test
    @Transactional
    public void getSecret() throws Exception {
        // Initialize the database
        secretRepository.saveAndFlush(secret);

        // Get the secret
        restSecretMockMvc.perform(get("/api/secrets/{id}", secret.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(secret.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecret() throws Exception {
        // Get the secret
        restSecretMockMvc.perform(get("/api/secrets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecret() throws Exception {
        // Initialize the database
        secretRepository.saveAndFlush(secret);

        int databaseSizeBeforeUpdate = secretRepository.findAll().size();

        // Update the secret
        Secret updatedSecret = secretRepository.findById(secret.getId()).get();
        // Disconnect from session so that the updates on updatedSecret are not directly saved in db
        em.detach(updatedSecret);
        updatedSecret
            .url(UPDATED_URL)
            .name(UPDATED_NAME)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .notes(UPDATED_NOTES)
            .modified(UPDATED_MODIFIED);
        SecretDTO secretDTO = secretMapper.toDto(updatedSecret);

        restSecretMockMvc.perform(put("/api/secrets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(secretDTO)))
            .andExpect(status().isOk());

        // Validate the Secret in the database
        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeUpdate);
        Secret testSecret = secretList.get(secretList.size() - 1);
        assertThat(testSecret.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSecret.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecret.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSecret.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testSecret.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingSecret() throws Exception {
        int databaseSizeBeforeUpdate = secretRepository.findAll().size();

        // Create the Secret
        SecretDTO secretDTO = secretMapper.toDto(secret);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecretMockMvc.perform(put("/api/secrets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(secretDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Secret in the database
        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSecret() throws Exception {
        // Initialize the database
        secretRepository.saveAndFlush(secret);

        int databaseSizeBeforeDelete = secretRepository.findAll().size();

        // Get the secret
        restSecretMockMvc.perform(delete("/api/secrets/{id}", secret.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Secret> secretList = secretRepository.findAll();
        assertThat(secretList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Secret.class);
        Secret secret1 = new Secret();
        secret1.setId(1L);
        Secret secret2 = new Secret();
        secret2.setId(secret1.getId());
        assertThat(secret1).isEqualTo(secret2);
        secret2.setId(2L);
        assertThat(secret1).isNotEqualTo(secret2);
        secret1.setId(null);
        assertThat(secret1).isNotEqualTo(secret2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecretDTO.class);
        SecretDTO secretDTO1 = new SecretDTO();
        secretDTO1.setId(1L);
        SecretDTO secretDTO2 = new SecretDTO();
        assertThat(secretDTO1).isNotEqualTo(secretDTO2);
        secretDTO2.setId(secretDTO1.getId());
        assertThat(secretDTO1).isEqualTo(secretDTO2);
        secretDTO2.setId(2L);
        assertThat(secretDTO1).isNotEqualTo(secretDTO2);
        secretDTO1.setId(null);
        assertThat(secretDTO1).isNotEqualTo(secretDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(secretMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(secretMapper.fromId(null)).isNull();
    }
}
