package com.justynagajdek.healthreservationsystem.integration;

import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import com.justynagajdek.healthreservationsystem.integration.util.BaseIntegrationTest;
import com.justynagajdek.healthreservationsystem.integration.util.TestEntityFactory;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.repository.VaccinationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VaccinationControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityFactory testEntityFactory;

    @Autowired
    private VaccinationRepository vaccinationRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;


    @Test
    void shouldReturnCompletedVaccinationsForPatient() throws Exception {
        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);

        String token = jwtTokenUtil.generateJwtToken(patient.getUser().getEmail());

        VaccinationEntity vaccination = new VaccinationEntity();
        vaccination.setPatient(patient);
        vaccination.setVaccineName("MMR");
        vaccination.setVaccinationDate(LocalDate.now().minusDays(14));
        vaccination.setMandatory(true);
        vaccinationRepository.save(vaccination);

        mockMvc.perform(get("/vaccinations/patient")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].vaccineName").value("MMR"));
    }


}
