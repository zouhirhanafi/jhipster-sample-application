package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Parameter;
import com.mycompany.myapp.domain.Parameter;
import com.mycompany.myapp.repository.ParameterRepository;
import com.mycompany.myapp.service.criteria.ParameterCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParameterResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_LIB_2 = "AAAAAAAAAA";
    private static final String UPDATED_LIB_2 = "BBBBBBBBBB";

    private static final String DEFAULT_LIB_3 = "AAAAAAAAAA";
    private static final String UPDATED_LIB_3 = "BBBBBBBBBB";

    private static final String DEFAULT_REF_EXTERNE = "AAAAAAAAAA";
    private static final String UPDATED_REF_EXTERNE = "BBBBBBBBBB";

    private static final String DEFAULT_VAL_1 = "AAAAAAAAAA";
    private static final String UPDATED_VAL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_VAL_2 = "AAAAAAAAAA";
    private static final String UPDATED_VAL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_VAL_3 = "AAAAAAAAAA";
    private static final String UPDATED_VAL_3 = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;
    private static final Integer SMALLER_ORDRE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .label(DEFAULT_LABEL)
            .activated(DEFAULT_ACTIVATED)
            .lib2(DEFAULT_LIB_2)
            .lib3(DEFAULT_LIB_3)
            .refExterne(DEFAULT_REF_EXTERNE)
            .val1(DEFAULT_VAL_1)
            .val2(DEFAULT_VAL_2)
            .val3(DEFAULT_VAL_3)
            .ordre(DEFAULT_ORDRE);
        return parameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createUpdatedEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .label(UPDATED_LABEL)
            .activated(UPDATED_ACTIVATED)
            .lib2(UPDATED_LIB_2)
            .lib3(UPDATED_LIB_3)
            .refExterne(UPDATED_REF_EXTERNE)
            .val1(UPDATED_VAL_1)
            .val2(UPDATED_VAL_2)
            .val3(UPDATED_VAL_3)
            .ordre(UPDATED_ORDRE);
        return parameter;
    }

    @BeforeEach
    public void initTest() {
        parameter = createEntity(em);
    }

    @Test
    @Transactional
    void createParameter() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();
        // Create the Parameter
        restParameterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate + 1);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testParameter.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testParameter.getLib2()).isEqualTo(DEFAULT_LIB_2);
        assertThat(testParameter.getLib3()).isEqualTo(DEFAULT_LIB_3);
        assertThat(testParameter.getRefExterne()).isEqualTo(DEFAULT_REF_EXTERNE);
        assertThat(testParameter.getVal1()).isEqualTo(DEFAULT_VAL_1);
        assertThat(testParameter.getVal2()).isEqualTo(DEFAULT_VAL_2);
        assertThat(testParameter.getVal3()).isEqualTo(DEFAULT_VAL_3);
        assertThat(testParameter.getOrdre()).isEqualTo(DEFAULT_ORDRE);
    }

    @Test
    @Transactional
    void createParameterWithExistingId() throws Exception {
        // Create the Parameter with an existing ID
        parameter.setId(1L);

        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParameters() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList
        restParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].lib2").value(hasItem(DEFAULT_LIB_2)))
            .andExpect(jsonPath("$.[*].lib3").value(hasItem(DEFAULT_LIB_3)))
            .andExpect(jsonPath("$.[*].refExterne").value(hasItem(DEFAULT_REF_EXTERNE)))
            .andExpect(jsonPath("$.[*].val1").value(hasItem(DEFAULT_VAL_1)))
            .andExpect(jsonPath("$.[*].val2").value(hasItem(DEFAULT_VAL_2)))
            .andExpect(jsonPath("$.[*].val3").value(hasItem(DEFAULT_VAL_3)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)));
    }

    @Test
    @Transactional
    void getParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.lib2").value(DEFAULT_LIB_2))
            .andExpect(jsonPath("$.lib3").value(DEFAULT_LIB_3))
            .andExpect(jsonPath("$.refExterne").value(DEFAULT_REF_EXTERNE))
            .andExpect(jsonPath("$.val1").value(DEFAULT_VAL_1))
            .andExpect(jsonPath("$.val2").value(DEFAULT_VAL_2))
            .andExpect(jsonPath("$.val3").value(DEFAULT_VAL_3))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE));
    }

    @Test
    @Transactional
    void getParametersByIdFiltering() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        Long id = parameter.getId();

        defaultParameterShouldBeFound("id.equals=" + id);
        defaultParameterShouldNotBeFound("id.notEquals=" + id);

        defaultParameterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParameterShouldNotBeFound("id.greaterThan=" + id);

        defaultParameterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParameterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParametersByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where label equals to DEFAULT_LABEL
        defaultParameterShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the parameterList where label equals to UPDATED_LABEL
        defaultParameterShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllParametersByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where label not equals to DEFAULT_LABEL
        defaultParameterShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the parameterList where label not equals to UPDATED_LABEL
        defaultParameterShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllParametersByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultParameterShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the parameterList where label equals to UPDATED_LABEL
        defaultParameterShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllParametersByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where label is not null
        defaultParameterShouldBeFound("label.specified=true");

        // Get all the parameterList where label is null
        defaultParameterShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByLabelContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where label contains DEFAULT_LABEL
        defaultParameterShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the parameterList where label contains UPDATED_LABEL
        defaultParameterShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllParametersByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where label does not contain DEFAULT_LABEL
        defaultParameterShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the parameterList where label does not contain UPDATED_LABEL
        defaultParameterShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    void getAllParametersByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where activated equals to DEFAULT_ACTIVATED
        defaultParameterShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the parameterList where activated equals to UPDATED_ACTIVATED
        defaultParameterShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllParametersByActivatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where activated not equals to DEFAULT_ACTIVATED
        defaultParameterShouldNotBeFound("activated.notEquals=" + DEFAULT_ACTIVATED);

        // Get all the parameterList where activated not equals to UPDATED_ACTIVATED
        defaultParameterShouldBeFound("activated.notEquals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllParametersByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultParameterShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the parameterList where activated equals to UPDATED_ACTIVATED
        defaultParameterShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllParametersByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where activated is not null
        defaultParameterShouldBeFound("activated.specified=true");

        // Get all the parameterList where activated is null
        defaultParameterShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByLib2IsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib2 equals to DEFAULT_LIB_2
        defaultParameterShouldBeFound("lib2.equals=" + DEFAULT_LIB_2);

        // Get all the parameterList where lib2 equals to UPDATED_LIB_2
        defaultParameterShouldNotBeFound("lib2.equals=" + UPDATED_LIB_2);
    }

    @Test
    @Transactional
    void getAllParametersByLib2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib2 not equals to DEFAULT_LIB_2
        defaultParameterShouldNotBeFound("lib2.notEquals=" + DEFAULT_LIB_2);

        // Get all the parameterList where lib2 not equals to UPDATED_LIB_2
        defaultParameterShouldBeFound("lib2.notEquals=" + UPDATED_LIB_2);
    }

    @Test
    @Transactional
    void getAllParametersByLib2IsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib2 in DEFAULT_LIB_2 or UPDATED_LIB_2
        defaultParameterShouldBeFound("lib2.in=" + DEFAULT_LIB_2 + "," + UPDATED_LIB_2);

        // Get all the parameterList where lib2 equals to UPDATED_LIB_2
        defaultParameterShouldNotBeFound("lib2.in=" + UPDATED_LIB_2);
    }

    @Test
    @Transactional
    void getAllParametersByLib2IsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib2 is not null
        defaultParameterShouldBeFound("lib2.specified=true");

        // Get all the parameterList where lib2 is null
        defaultParameterShouldNotBeFound("lib2.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByLib2ContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib2 contains DEFAULT_LIB_2
        defaultParameterShouldBeFound("lib2.contains=" + DEFAULT_LIB_2);

        // Get all the parameterList where lib2 contains UPDATED_LIB_2
        defaultParameterShouldNotBeFound("lib2.contains=" + UPDATED_LIB_2);
    }

    @Test
    @Transactional
    void getAllParametersByLib2NotContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib2 does not contain DEFAULT_LIB_2
        defaultParameterShouldNotBeFound("lib2.doesNotContain=" + DEFAULT_LIB_2);

        // Get all the parameterList where lib2 does not contain UPDATED_LIB_2
        defaultParameterShouldBeFound("lib2.doesNotContain=" + UPDATED_LIB_2);
    }

    @Test
    @Transactional
    void getAllParametersByLib3IsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib3 equals to DEFAULT_LIB_3
        defaultParameterShouldBeFound("lib3.equals=" + DEFAULT_LIB_3);

        // Get all the parameterList where lib3 equals to UPDATED_LIB_3
        defaultParameterShouldNotBeFound("lib3.equals=" + UPDATED_LIB_3);
    }

    @Test
    @Transactional
    void getAllParametersByLib3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib3 not equals to DEFAULT_LIB_3
        defaultParameterShouldNotBeFound("lib3.notEquals=" + DEFAULT_LIB_3);

        // Get all the parameterList where lib3 not equals to UPDATED_LIB_3
        defaultParameterShouldBeFound("lib3.notEquals=" + UPDATED_LIB_3);
    }

    @Test
    @Transactional
    void getAllParametersByLib3IsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib3 in DEFAULT_LIB_3 or UPDATED_LIB_3
        defaultParameterShouldBeFound("lib3.in=" + DEFAULT_LIB_3 + "," + UPDATED_LIB_3);

        // Get all the parameterList where lib3 equals to UPDATED_LIB_3
        defaultParameterShouldNotBeFound("lib3.in=" + UPDATED_LIB_3);
    }

    @Test
    @Transactional
    void getAllParametersByLib3IsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib3 is not null
        defaultParameterShouldBeFound("lib3.specified=true");

        // Get all the parameterList where lib3 is null
        defaultParameterShouldNotBeFound("lib3.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByLib3ContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib3 contains DEFAULT_LIB_3
        defaultParameterShouldBeFound("lib3.contains=" + DEFAULT_LIB_3);

        // Get all the parameterList where lib3 contains UPDATED_LIB_3
        defaultParameterShouldNotBeFound("lib3.contains=" + UPDATED_LIB_3);
    }

    @Test
    @Transactional
    void getAllParametersByLib3NotContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where lib3 does not contain DEFAULT_LIB_3
        defaultParameterShouldNotBeFound("lib3.doesNotContain=" + DEFAULT_LIB_3);

        // Get all the parameterList where lib3 does not contain UPDATED_LIB_3
        defaultParameterShouldBeFound("lib3.doesNotContain=" + UPDATED_LIB_3);
    }

    @Test
    @Transactional
    void getAllParametersByRefExterneIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where refExterne equals to DEFAULT_REF_EXTERNE
        defaultParameterShouldBeFound("refExterne.equals=" + DEFAULT_REF_EXTERNE);

        // Get all the parameterList where refExterne equals to UPDATED_REF_EXTERNE
        defaultParameterShouldNotBeFound("refExterne.equals=" + UPDATED_REF_EXTERNE);
    }

    @Test
    @Transactional
    void getAllParametersByRefExterneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where refExterne not equals to DEFAULT_REF_EXTERNE
        defaultParameterShouldNotBeFound("refExterne.notEquals=" + DEFAULT_REF_EXTERNE);

        // Get all the parameterList where refExterne not equals to UPDATED_REF_EXTERNE
        defaultParameterShouldBeFound("refExterne.notEquals=" + UPDATED_REF_EXTERNE);
    }

    @Test
    @Transactional
    void getAllParametersByRefExterneIsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where refExterne in DEFAULT_REF_EXTERNE or UPDATED_REF_EXTERNE
        defaultParameterShouldBeFound("refExterne.in=" + DEFAULT_REF_EXTERNE + "," + UPDATED_REF_EXTERNE);

        // Get all the parameterList where refExterne equals to UPDATED_REF_EXTERNE
        defaultParameterShouldNotBeFound("refExterne.in=" + UPDATED_REF_EXTERNE);
    }

    @Test
    @Transactional
    void getAllParametersByRefExterneIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where refExterne is not null
        defaultParameterShouldBeFound("refExterne.specified=true");

        // Get all the parameterList where refExterne is null
        defaultParameterShouldNotBeFound("refExterne.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByRefExterneContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where refExterne contains DEFAULT_REF_EXTERNE
        defaultParameterShouldBeFound("refExterne.contains=" + DEFAULT_REF_EXTERNE);

        // Get all the parameterList where refExterne contains UPDATED_REF_EXTERNE
        defaultParameterShouldNotBeFound("refExterne.contains=" + UPDATED_REF_EXTERNE);
    }

    @Test
    @Transactional
    void getAllParametersByRefExterneNotContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where refExterne does not contain DEFAULT_REF_EXTERNE
        defaultParameterShouldNotBeFound("refExterne.doesNotContain=" + DEFAULT_REF_EXTERNE);

        // Get all the parameterList where refExterne does not contain UPDATED_REF_EXTERNE
        defaultParameterShouldBeFound("refExterne.doesNotContain=" + UPDATED_REF_EXTERNE);
    }

    @Test
    @Transactional
    void getAllParametersByVal1IsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val1 equals to DEFAULT_VAL_1
        defaultParameterShouldBeFound("val1.equals=" + DEFAULT_VAL_1);

        // Get all the parameterList where val1 equals to UPDATED_VAL_1
        defaultParameterShouldNotBeFound("val1.equals=" + UPDATED_VAL_1);
    }

    @Test
    @Transactional
    void getAllParametersByVal1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val1 not equals to DEFAULT_VAL_1
        defaultParameterShouldNotBeFound("val1.notEquals=" + DEFAULT_VAL_1);

        // Get all the parameterList where val1 not equals to UPDATED_VAL_1
        defaultParameterShouldBeFound("val1.notEquals=" + UPDATED_VAL_1);
    }

    @Test
    @Transactional
    void getAllParametersByVal1IsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val1 in DEFAULT_VAL_1 or UPDATED_VAL_1
        defaultParameterShouldBeFound("val1.in=" + DEFAULT_VAL_1 + "," + UPDATED_VAL_1);

        // Get all the parameterList where val1 equals to UPDATED_VAL_1
        defaultParameterShouldNotBeFound("val1.in=" + UPDATED_VAL_1);
    }

    @Test
    @Transactional
    void getAllParametersByVal1IsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val1 is not null
        defaultParameterShouldBeFound("val1.specified=true");

        // Get all the parameterList where val1 is null
        defaultParameterShouldNotBeFound("val1.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByVal1ContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val1 contains DEFAULT_VAL_1
        defaultParameterShouldBeFound("val1.contains=" + DEFAULT_VAL_1);

        // Get all the parameterList where val1 contains UPDATED_VAL_1
        defaultParameterShouldNotBeFound("val1.contains=" + UPDATED_VAL_1);
    }

    @Test
    @Transactional
    void getAllParametersByVal1NotContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val1 does not contain DEFAULT_VAL_1
        defaultParameterShouldNotBeFound("val1.doesNotContain=" + DEFAULT_VAL_1);

        // Get all the parameterList where val1 does not contain UPDATED_VAL_1
        defaultParameterShouldBeFound("val1.doesNotContain=" + UPDATED_VAL_1);
    }

    @Test
    @Transactional
    void getAllParametersByVal2IsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val2 equals to DEFAULT_VAL_2
        defaultParameterShouldBeFound("val2.equals=" + DEFAULT_VAL_2);

        // Get all the parameterList where val2 equals to UPDATED_VAL_2
        defaultParameterShouldNotBeFound("val2.equals=" + UPDATED_VAL_2);
    }

    @Test
    @Transactional
    void getAllParametersByVal2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val2 not equals to DEFAULT_VAL_2
        defaultParameterShouldNotBeFound("val2.notEquals=" + DEFAULT_VAL_2);

        // Get all the parameterList where val2 not equals to UPDATED_VAL_2
        defaultParameterShouldBeFound("val2.notEquals=" + UPDATED_VAL_2);
    }

    @Test
    @Transactional
    void getAllParametersByVal2IsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val2 in DEFAULT_VAL_2 or UPDATED_VAL_2
        defaultParameterShouldBeFound("val2.in=" + DEFAULT_VAL_2 + "," + UPDATED_VAL_2);

        // Get all the parameterList where val2 equals to UPDATED_VAL_2
        defaultParameterShouldNotBeFound("val2.in=" + UPDATED_VAL_2);
    }

    @Test
    @Transactional
    void getAllParametersByVal2IsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val2 is not null
        defaultParameterShouldBeFound("val2.specified=true");

        // Get all the parameterList where val2 is null
        defaultParameterShouldNotBeFound("val2.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByVal2ContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val2 contains DEFAULT_VAL_2
        defaultParameterShouldBeFound("val2.contains=" + DEFAULT_VAL_2);

        // Get all the parameterList where val2 contains UPDATED_VAL_2
        defaultParameterShouldNotBeFound("val2.contains=" + UPDATED_VAL_2);
    }

    @Test
    @Transactional
    void getAllParametersByVal2NotContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val2 does not contain DEFAULT_VAL_2
        defaultParameterShouldNotBeFound("val2.doesNotContain=" + DEFAULT_VAL_2);

        // Get all the parameterList where val2 does not contain UPDATED_VAL_2
        defaultParameterShouldBeFound("val2.doesNotContain=" + UPDATED_VAL_2);
    }

    @Test
    @Transactional
    void getAllParametersByVal3IsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val3 equals to DEFAULT_VAL_3
        defaultParameterShouldBeFound("val3.equals=" + DEFAULT_VAL_3);

        // Get all the parameterList where val3 equals to UPDATED_VAL_3
        defaultParameterShouldNotBeFound("val3.equals=" + UPDATED_VAL_3);
    }

    @Test
    @Transactional
    void getAllParametersByVal3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val3 not equals to DEFAULT_VAL_3
        defaultParameterShouldNotBeFound("val3.notEquals=" + DEFAULT_VAL_3);

        // Get all the parameterList where val3 not equals to UPDATED_VAL_3
        defaultParameterShouldBeFound("val3.notEquals=" + UPDATED_VAL_3);
    }

    @Test
    @Transactional
    void getAllParametersByVal3IsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val3 in DEFAULT_VAL_3 or UPDATED_VAL_3
        defaultParameterShouldBeFound("val3.in=" + DEFAULT_VAL_3 + "," + UPDATED_VAL_3);

        // Get all the parameterList where val3 equals to UPDATED_VAL_3
        defaultParameterShouldNotBeFound("val3.in=" + UPDATED_VAL_3);
    }

    @Test
    @Transactional
    void getAllParametersByVal3IsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val3 is not null
        defaultParameterShouldBeFound("val3.specified=true");

        // Get all the parameterList where val3 is null
        defaultParameterShouldNotBeFound("val3.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByVal3ContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val3 contains DEFAULT_VAL_3
        defaultParameterShouldBeFound("val3.contains=" + DEFAULT_VAL_3);

        // Get all the parameterList where val3 contains UPDATED_VAL_3
        defaultParameterShouldNotBeFound("val3.contains=" + UPDATED_VAL_3);
    }

    @Test
    @Transactional
    void getAllParametersByVal3NotContainsSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where val3 does not contain DEFAULT_VAL_3
        defaultParameterShouldNotBeFound("val3.doesNotContain=" + DEFAULT_VAL_3);

        // Get all the parameterList where val3 does not contain UPDATED_VAL_3
        defaultParameterShouldBeFound("val3.doesNotContain=" + UPDATED_VAL_3);
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre equals to DEFAULT_ORDRE
        defaultParameterShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the parameterList where ordre equals to UPDATED_ORDRE
        defaultParameterShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre not equals to DEFAULT_ORDRE
        defaultParameterShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the parameterList where ordre not equals to UPDATED_ORDRE
        defaultParameterShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultParameterShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the parameterList where ordre equals to UPDATED_ORDRE
        defaultParameterShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre is not null
        defaultParameterShouldBeFound("ordre.specified=true");

        // Get all the parameterList where ordre is null
        defaultParameterShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre is greater than or equal to DEFAULT_ORDRE
        defaultParameterShouldBeFound("ordre.greaterThanOrEqual=" + DEFAULT_ORDRE);

        // Get all the parameterList where ordre is greater than or equal to UPDATED_ORDRE
        defaultParameterShouldNotBeFound("ordre.greaterThanOrEqual=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre is less than or equal to DEFAULT_ORDRE
        defaultParameterShouldBeFound("ordre.lessThanOrEqual=" + DEFAULT_ORDRE);

        // Get all the parameterList where ordre is less than or equal to SMALLER_ORDRE
        defaultParameterShouldNotBeFound("ordre.lessThanOrEqual=" + SMALLER_ORDRE);
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsLessThanSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre is less than DEFAULT_ORDRE
        defaultParameterShouldNotBeFound("ordre.lessThan=" + DEFAULT_ORDRE);

        // Get all the parameterList where ordre is less than UPDATED_ORDRE
        defaultParameterShouldBeFound("ordre.lessThan=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllParametersByOrdreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where ordre is greater than DEFAULT_ORDRE
        defaultParameterShouldNotBeFound("ordre.greaterThan=" + DEFAULT_ORDRE);

        // Get all the parameterList where ordre is greater than SMALLER_ORDRE
        defaultParameterShouldBeFound("ordre.greaterThan=" + SMALLER_ORDRE);
    }

    @Test
    @Transactional
    void getAllParametersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        Parameter type;
        if (TestUtil.findAll(em, Parameter.class).isEmpty()) {
            type = ParameterResourceIT.createEntity(em);
            em.persist(type);
            em.flush();
        } else {
            type = TestUtil.findAll(em, Parameter.class).get(0);
        }
        em.persist(type);
        em.flush();
        parameter.setType(type);
        parameterRepository.saveAndFlush(parameter);
        Long typeId = type.getId();

        // Get all the parameterList where type equals to typeId
        defaultParameterShouldBeFound("typeId.equals=" + typeId);

        // Get all the parameterList where type equals to (typeId + 1)
        defaultParameterShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    @Test
    @Transactional
    void getAllParametersByParaentIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);
        Parameter paraent;
        if (TestUtil.findAll(em, Parameter.class).isEmpty()) {
            paraent = ParameterResourceIT.createEntity(em);
            em.persist(paraent);
            em.flush();
        } else {
            paraent = TestUtil.findAll(em, Parameter.class).get(0);
        }
        em.persist(paraent);
        em.flush();
        parameter.setParaent(paraent);
        parameterRepository.saveAndFlush(parameter);
        Long paraentId = paraent.getId();

        // Get all the parameterList where paraent equals to paraentId
        defaultParameterShouldBeFound("paraentId.equals=" + paraentId);

        // Get all the parameterList where paraent equals to (paraentId + 1)
        defaultParameterShouldNotBeFound("paraentId.equals=" + (paraentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParameterShouldBeFound(String filter) throws Exception {
        restParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].lib2").value(hasItem(DEFAULT_LIB_2)))
            .andExpect(jsonPath("$.[*].lib3").value(hasItem(DEFAULT_LIB_3)))
            .andExpect(jsonPath("$.[*].refExterne").value(hasItem(DEFAULT_REF_EXTERNE)))
            .andExpect(jsonPath("$.[*].val1").value(hasItem(DEFAULT_VAL_1)))
            .andExpect(jsonPath("$.[*].val2").value(hasItem(DEFAULT_VAL_2)))
            .andExpect(jsonPath("$.[*].val3").value(hasItem(DEFAULT_VAL_3)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)));

        // Check, that the count call also returns 1
        restParameterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParameterShouldNotBeFound(String filter) throws Exception {
        restParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParameterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findById(parameter.getId()).get();
        // Disconnect from session so that the updates on updatedParameter are not directly saved in db
        em.detach(updatedParameter);
        updatedParameter
            .label(UPDATED_LABEL)
            .activated(UPDATED_ACTIVATED)
            .lib2(UPDATED_LIB_2)
            .lib3(UPDATED_LIB_3)
            .refExterne(UPDATED_REF_EXTERNE)
            .val1(UPDATED_VAL_1)
            .val2(UPDATED_VAL_2)
            .val3(UPDATED_VAL_3)
            .ordre(UPDATED_ORDRE);

        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testParameter.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testParameter.getLib2()).isEqualTo(UPDATED_LIB_2);
        assertThat(testParameter.getLib3()).isEqualTo(UPDATED_LIB_3);
        assertThat(testParameter.getRefExterne()).isEqualTo(UPDATED_REF_EXTERNE);
        assertThat(testParameter.getVal1()).isEqualTo(UPDATED_VAL_1);
        assertThat(testParameter.getVal2()).isEqualTo(UPDATED_VAL_2);
        assertThat(testParameter.getVal3()).isEqualTo(UPDATED_VAL_3);
        assertThat(testParameter.getOrdre()).isEqualTo(UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void putNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();
        parameter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();
        parameter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();
        parameter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter.activated(UPDATED_ACTIVATED).lib2(UPDATED_LIB_2).val1(UPDATED_VAL_1).val3(UPDATED_VAL_3);

        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testParameter.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testParameter.getLib2()).isEqualTo(UPDATED_LIB_2);
        assertThat(testParameter.getLib3()).isEqualTo(DEFAULT_LIB_3);
        assertThat(testParameter.getRefExterne()).isEqualTo(DEFAULT_REF_EXTERNE);
        assertThat(testParameter.getVal1()).isEqualTo(UPDATED_VAL_1);
        assertThat(testParameter.getVal2()).isEqualTo(DEFAULT_VAL_2);
        assertThat(testParameter.getVal3()).isEqualTo(UPDATED_VAL_3);
        assertThat(testParameter.getOrdre()).isEqualTo(DEFAULT_ORDRE);
    }

    @Test
    @Transactional
    void fullUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter
            .label(UPDATED_LABEL)
            .activated(UPDATED_ACTIVATED)
            .lib2(UPDATED_LIB_2)
            .lib3(UPDATED_LIB_3)
            .refExterne(UPDATED_REF_EXTERNE)
            .val1(UPDATED_VAL_1)
            .val2(UPDATED_VAL_2)
            .val3(UPDATED_VAL_3)
            .ordre(UPDATED_ORDRE);

        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testParameter.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testParameter.getLib2()).isEqualTo(UPDATED_LIB_2);
        assertThat(testParameter.getLib3()).isEqualTo(UPDATED_LIB_3);
        assertThat(testParameter.getRefExterne()).isEqualTo(UPDATED_REF_EXTERNE);
        assertThat(testParameter.getVal1()).isEqualTo(UPDATED_VAL_1);
        assertThat(testParameter.getVal2()).isEqualTo(UPDATED_VAL_2);
        assertThat(testParameter.getVal3()).isEqualTo(UPDATED_VAL_3);
        assertThat(testParameter.getOrdre()).isEqualTo(UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void patchNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();
        parameter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();
        parameter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();
        parameter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parameter))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        int databaseSizeBeforeDelete = parameterRepository.findAll().size();

        // Delete the parameter
        restParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, parameter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
