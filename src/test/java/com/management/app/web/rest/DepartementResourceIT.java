package com.management.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.management.app.IntegrationTest;
import com.management.app.domain.Departement;
import com.management.app.repository.DepartementRepository;
import com.management.app.service.dto.DepartementDTO;
import com.management.app.service.mapper.DepartementMapper;
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
 * Integration tests for the {@link DepartementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartementResourceIT {

    private static final String DEFAULT_NAME_DEPARTEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DEPARTEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private DepartementMapper departementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartementMockMvc;

    private Departement departement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createEntity(EntityManager em) {
        Departement departement = new Departement().name_departement(DEFAULT_NAME_DEPARTEMENT);
        return departement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createUpdatedEntity(EntityManager em) {
        Departement departement = new Departement().name_departement(UPDATED_NAME_DEPARTEMENT);
        return departement;
    }

    @BeforeEach
    public void initTest() {
        departement = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartement() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().size();
        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);
        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate + 1);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getName_departement()).isEqualTo(DEFAULT_NAME_DEPARTEMENT);
    }

    @Test
    @Transactional
    void createDepartementWithExistingId() throws Exception {
        // Create the Departement with an existing ID
        departement.setId(1L);
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        int databaseSizeBeforeCreate = departementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartements() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name_departement").value(hasItem(DEFAULT_NAME_DEPARTEMENT)));
    }

    @Test
    @Transactional
    void getDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get the departement
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL_ID, departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departement.getId().intValue()))
            .andExpect(jsonPath("$.name_departement").value(DEFAULT_NAME_DEPARTEMENT));
    }

    @Test
    @Transactional
    void getNonExistingDepartement() throws Exception {
        // Get the departement
        restDepartementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement
        Departement updatedDepartement = departementRepository.findById(departement.getId()).get();
        // Disconnect from session so that the updates on updatedDepartement are not directly saved in db
        em.detach(updatedDepartement);
        updatedDepartement.name_departement(UPDATED_NAME_DEPARTEMENT);
        DepartementDTO departementDTO = departementMapper.toDto(updatedDepartement);

        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getName_departement()).isEqualTo(UPDATED_NAME_DEPARTEMENT);
    }

    @Test
    @Transactional
    void putNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.name_departement(UPDATED_NAME_DEPARTEMENT);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getName_departement()).isEqualTo(UPDATED_NAME_DEPARTEMENT);
    }

    @Test
    @Transactional
    void fullUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.name_departement(UPDATED_NAME_DEPARTEMENT);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getName_departement()).isEqualTo(UPDATED_NAME_DEPARTEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(departementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeDelete = departementRepository.findAll().size();

        // Delete the departement
        restDepartementMockMvc
            .perform(delete(ENTITY_API_URL_ID, departement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
