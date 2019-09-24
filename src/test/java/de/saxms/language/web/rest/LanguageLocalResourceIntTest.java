package de.saxms.language.web.rest;

import de.saxms.language.LocaleServerApp;

import de.saxms.language.domain.LanguageLocale;
import de.saxms.language.repository.LanguageLocalRepository;
import de.saxms.language.service.LanguageLocalService;
import de.saxms.language.web.rest.errors.ExceptionTranslator;

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
import java.util.List;


import static de.saxms.language.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LanguageLocalResource REST controller.
 *
 * @see LanguageLocalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LocaleServerApp.class)
public class LanguageLocalResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

    @Autowired
    private LanguageLocalRepository languageLocalRepository;

    @Autowired
    private LanguageLocalService languageLocalService;

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

    private MockMvc restLanguageLocalMockMvc;

    private LanguageLocale languageLocale;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LanguageLocalResource languageLocalResource = new LanguageLocalResource(languageLocalService);
        this.restLanguageLocalMockMvc = MockMvcBuilders.standaloneSetup(languageLocalResource)
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
    public static LanguageLocale createEntity(EntityManager em) {
        LanguageLocale languageLocale = new LanguageLocale()
            .name(DEFAULT_NAME)
            .abbreviation(DEFAULT_ABBREVIATION);
        return languageLocale;
    }

    @Before
    public void initTest() {
        languageLocale = createEntity(em);
    }

    @Test
    @Transactional
    public void createLanguageLocal() throws Exception {
        int databaseSizeBeforeCreate = languageLocalRepository.findAll().size();

        // Create the LanguageLocale
        restLanguageLocalMockMvc.perform(post("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLocale)))
            .andExpect(status().isCreated());

        // Validate the LanguageLocale in the database
        List<LanguageLocale> languageLocaleList = languageLocalRepository.findAll();
        assertThat(languageLocaleList).hasSize(databaseSizeBeforeCreate + 1);
        LanguageLocale testLanguageLocale = languageLocaleList.get(languageLocaleList.size() - 1);
        assertThat(testLanguageLocale.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLanguageLocale.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
    }

    @Test
    @Transactional
    public void createLanguageLocalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = languageLocalRepository.findAll().size();

        // Create the LanguageLocale with an existing ID
        languageLocale.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageLocalMockMvc.perform(post("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLocale)))
            .andExpect(status().isBadRequest());

        // Validate the LanguageLocale in the database
        List<LanguageLocale> languageLocaleList = languageLocalRepository.findAll();
        assertThat(languageLocaleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLanguageLocals() throws Exception {
        // Initialize the database
        languageLocalRepository.saveAndFlush(languageLocale);

        // Get all the languageLocalList
        restLanguageLocalMockMvc.perform(get("/api/language-locals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languageLocale.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())));
    }
    
    @Test
    @Transactional
    public void getLanguageLocal() throws Exception {
        // Initialize the database
        languageLocalRepository.saveAndFlush(languageLocale);

        // Get the languageLocale
        restLanguageLocalMockMvc.perform(get("/api/language-locals/{id}", languageLocale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(languageLocale.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLanguageLocal() throws Exception {
        // Get the languageLocale
        restLanguageLocalMockMvc.perform(get("/api/language-locals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguageLocal() throws Exception {
        // Initialize the database
        languageLocalService.save(languageLocale);

        int databaseSizeBeforeUpdate = languageLocalRepository.findAll().size();

        // Update the languageLocale
        LanguageLocale updatedLanguageLocale = languageLocalRepository.findById(languageLocale.getId()).get();
        // Disconnect from session so that the updates on updatedLanguageLocale are not directly saved in db
        em.detach(updatedLanguageLocale);
        updatedLanguageLocale
            .name(UPDATED_NAME)
            .abbreviation(UPDATED_ABBREVIATION);

        restLanguageLocalMockMvc.perform(put("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLanguageLocale)))
            .andExpect(status().isOk());

        // Validate the LanguageLocale in the database
        List<LanguageLocale> languageLocaleList = languageLocalRepository.findAll();
        assertThat(languageLocaleList).hasSize(databaseSizeBeforeUpdate);
        LanguageLocale testLanguageLocale = languageLocaleList.get(languageLocaleList.size() - 1);
        assertThat(testLanguageLocale.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLanguageLocale.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    public void updateNonExistingLanguageLocal() throws Exception {
        int databaseSizeBeforeUpdate = languageLocalRepository.findAll().size();

        // Create the LanguageLocale

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageLocalMockMvc.perform(put("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLocale)))
            .andExpect(status().isBadRequest());

        // Validate the LanguageLocale in the database
        List<LanguageLocale> languageLocaleList = languageLocalRepository.findAll();
        assertThat(languageLocaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLanguageLocal() throws Exception {
        // Initialize the database
        languageLocalService.save(languageLocale);

        int databaseSizeBeforeDelete = languageLocalRepository.findAll().size();

        // Delete the languageLocale
        restLanguageLocalMockMvc.perform(delete("/api/language-locals/{id}", languageLocale.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LanguageLocale> languageLocaleList = languageLocalRepository.findAll();
        assertThat(languageLocaleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguageLocale.class);
        LanguageLocale languageLocale1 = new LanguageLocale();
        languageLocale1.setId(1L);
        LanguageLocale languageLocale2 = new LanguageLocale();
        languageLocale2.setId(languageLocale1.getId());
        assertThat(languageLocale1).isEqualTo(languageLocale2);
        languageLocale2.setId(2L);
        assertThat(languageLocale1).isNotEqualTo(languageLocale2);
        languageLocale1.setId(null);
        assertThat(languageLocale1).isNotEqualTo(languageLocale2);
    }
}
