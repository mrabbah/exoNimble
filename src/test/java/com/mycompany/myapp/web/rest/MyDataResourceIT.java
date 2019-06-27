package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ExoNimbleApp;
import com.mycompany.myapp.domain.MyData;
import com.mycompany.myapp.repository.MyDataRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link MyDataResource} REST controller.
 */
@SpringBootTest(classes = ExoNimbleApp.class)
public class MyDataResourceIT {

    private static final LocalDate DEFAULT_FIRST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIRST_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LASTE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LASTE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MyDataRepository myDataRepository;

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

    private MockMvc restMyDataMockMvc;

    private MyData myData;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MyDataResource myDataResource = new MyDataResource(myDataRepository);
        this.restMyDataMockMvc = MockMvcBuilders.standaloneSetup(myDataResource)
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
    public static MyData createEntity(EntityManager em) {
        MyData myData = new MyData()
            .firstDate(DEFAULT_FIRST_DATE)
            .lasteDate(DEFAULT_LASTE_DATE);
        return myData;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyData createUpdatedEntity(EntityManager em) {
        MyData myData = new MyData()
            .firstDate(UPDATED_FIRST_DATE)
            .lasteDate(UPDATED_LASTE_DATE);
        return myData;
    }

    @BeforeEach
    public void initTest() {
        myData = createEntity(em);
    }

    @Test
    @Transactional
    public void createMyData() throws Exception {
        int databaseSizeBeforeCreate = myDataRepository.findAll().size();

        // Create the MyData
        restMyDataMockMvc.perform(post("/api/my-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myData)))
            .andExpect(status().isCreated());

        // Validate the MyData in the database
        List<MyData> myDataList = myDataRepository.findAll();
        assertThat(myDataList).hasSize(databaseSizeBeforeCreate + 1);
        MyData testMyData = myDataList.get(myDataList.size() - 1);
        assertThat(testMyData.getFirstDate()).isEqualTo(DEFAULT_FIRST_DATE);
        assertThat(testMyData.getLasteDate()).isEqualTo(DEFAULT_LASTE_DATE);
    }

    @Test
    @Transactional
    public void createMyDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = myDataRepository.findAll().size();

        // Create the MyData with an existing ID
        myData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyDataMockMvc.perform(post("/api/my-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myData)))
            .andExpect(status().isBadRequest());

        // Validate the MyData in the database
        List<MyData> myDataList = myDataRepository.findAll();
        assertThat(myDataList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = myDataRepository.findAll().size();
        // set the field null
        myData.setFirstDate(null);

        // Create the MyData, which fails.

        restMyDataMockMvc.perform(post("/api/my-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myData)))
            .andExpect(status().isBadRequest());

        List<MyData> myDataList = myDataRepository.findAll();
        assertThat(myDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLasteDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = myDataRepository.findAll().size();
        // set the field null
        myData.setLasteDate(null);

        // Create the MyData, which fails.

        restMyDataMockMvc.perform(post("/api/my-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myData)))
            .andExpect(status().isBadRequest());

        List<MyData> myDataList = myDataRepository.findAll();
        assertThat(myDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMyData() throws Exception {
        // Initialize the database
        myDataRepository.saveAndFlush(myData);

        // Get all the myDataList
        restMyDataMockMvc.perform(get("/api/my-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myData.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstDate").value(hasItem(DEFAULT_FIRST_DATE.toString())))
            .andExpect(jsonPath("$.[*].lasteDate").value(hasItem(DEFAULT_LASTE_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getMyData() throws Exception {
        // Initialize the database
        myDataRepository.saveAndFlush(myData);

        // Get the myData
        restMyDataMockMvc.perform(get("/api/my-data/{id}", myData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(myData.getId().intValue()))
            .andExpect(jsonPath("$.firstDate").value(DEFAULT_FIRST_DATE.toString()))
            .andExpect(jsonPath("$.lasteDate").value(DEFAULT_LASTE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMyData() throws Exception {
        // Get the myData
        restMyDataMockMvc.perform(get("/api/my-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMyData() throws Exception {
        // Initialize the database
        myDataRepository.saveAndFlush(myData);

        int databaseSizeBeforeUpdate = myDataRepository.findAll().size();

        // Update the myData
        MyData updatedMyData = myDataRepository.findById(myData.getId()).get();
        // Disconnect from session so that the updates on updatedMyData are not directly saved in db
        em.detach(updatedMyData);
        updatedMyData
            .firstDate(UPDATED_FIRST_DATE)
            .lasteDate(UPDATED_LASTE_DATE);

        restMyDataMockMvc.perform(put("/api/my-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMyData)))
            .andExpect(status().isOk());

        // Validate the MyData in the database
        List<MyData> myDataList = myDataRepository.findAll();
        assertThat(myDataList).hasSize(databaseSizeBeforeUpdate);
        MyData testMyData = myDataList.get(myDataList.size() - 1);
        assertThat(testMyData.getFirstDate()).isEqualTo(UPDATED_FIRST_DATE);
        assertThat(testMyData.getLasteDate()).isEqualTo(UPDATED_LASTE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMyData() throws Exception {
        int databaseSizeBeforeUpdate = myDataRepository.findAll().size();

        // Create the MyData

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyDataMockMvc.perform(put("/api/my-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(myData)))
            .andExpect(status().isBadRequest());

        // Validate the MyData in the database
        List<MyData> myDataList = myDataRepository.findAll();
        assertThat(myDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMyData() throws Exception {
        // Initialize the database
        myDataRepository.saveAndFlush(myData);

        int databaseSizeBeforeDelete = myDataRepository.findAll().size();

        // Delete the myData
        restMyDataMockMvc.perform(delete("/api/my-data/{id}", myData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<MyData> myDataList = myDataRepository.findAll();
        assertThat(myDataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyData.class);
        MyData myData1 = new MyData();
        myData1.setId(1L);
        MyData myData2 = new MyData();
        myData2.setId(myData1.getId());
        assertThat(myData1).isEqualTo(myData2);
        myData2.setId(2L);
        assertThat(myData1).isNotEqualTo(myData2);
        myData1.setId(null);
        assertThat(myData1).isNotEqualTo(myData2);
    }
}
