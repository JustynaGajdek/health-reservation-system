package com.justynagajdek.healthreservationsystem.integration;

import com.justynagajdek.healthreservationsystem.dto.VaccinationDto;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.entity.VaccinationEntity;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.integration.util.BaseIntegrationTest;
import com.justynagajdek.healthreservationsystem.integration.util.TestEntityFactory;
import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.repository.VaccinationRepository;
import com.justynagajdek.healthreservationsystem.service.VaccinationService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


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
    @Autowired
    private VaccinationService vaccinationService;


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

    @Test
    void shouldAddVaccinationAsDoctor() throws Exception {
        // given
        vaccinationRepository.deleteAll();

        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);
        UserEntity doctor = TestEntityFactory.createUser("doctor", Role.DOCTOR, userRepository);
        String token = jwtTokenUtil.generateJwtToken(doctor.getEmail());

        String payload = """
        {
            "vaccineName": "Tetanus",
            "vaccinationDate": "%s",
            "mandatory": true
        }
        """.formatted(LocalDate.now().minusDays(1).toString());

        // when + then
        mockMvc.perform(post("/vaccinations/patient/" + patient.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        // verify
        var all = vaccinationRepository.findAll();
        assertThat(all).hasSize(1);
        VaccinationEntity saved = all.get(0);
        assertThat(saved.getVaccineName()).isEqualTo("Tetanus");
        assertThat(saved.isMandatory()).isTrue();
        assertThat(saved.getPatient().getId()).isEqualTo(patient.getId());
    }

    @Test
    void shouldRejectVaccinationCreationWhenUnauthorizedRole() throws Exception {
        vaccinationRepository.deleteAll();

        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);
        String token = jwtTokenUtil.generateJwtToken(patient.getUser().getEmail());

        String payload = """
        {
            "vaccineName": "Hepatitis B",
            "vaccinationDate": "%s",
            "mandatory": false
        }
        """.formatted(LocalDate.now().minusWeeks(2).toString());

        // when + then
        mockMvc.perform(post("/vaccinations/patient/" + patient.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());

        assertThat(vaccinationRepository.findAll()).isEmpty();
    }

    @Test
    void shouldAllowNurseToAddVaccination() throws Exception {
        // given
        vaccinationRepository.deleteAll();

        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);
        UserEntity nurse = TestEntityFactory.createUser("nurse", Role.NURSE, userRepository);
        String token = jwtTokenUtil.generateJwtToken(nurse.getEmail());

        String payload = """
        {
            "vaccineName": "Polio",
            "vaccinationDate": "%s",
            "mandatory": true
        }
        """.formatted(LocalDate.now().minusDays(3));

        // when
        mockMvc.perform(post("/vaccinations/patient/" + patient.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        // then
        List<VaccinationEntity> list = vaccinationRepository.findByPatientId(patient.getId());
        assertThat(list)
                .hasSize(1)
                .extracting(VaccinationEntity::getVaccineName, VaccinationEntity::isMandatory)
                .containsExactly(tuple("Polio", true));
    }

    @Test
    void shouldRejectGetMyVaccinationsForDoctorRole() throws Exception {
        // given
        UserEntity doctor = TestEntityFactory.createUser("doc", Role.DOCTOR, userRepository);
        String token = jwtTokenUtil.generateJwtToken(doctor.getEmail());

        // when + then
        mockMvc.perform(get("/vaccinations/patient")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectVaccinationRetrievalWithoutAuth() throws Exception {
        // when + then
        mockMvc.perform(get("/vaccinations/patient")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectVaccinationWhenInvalidDateFormat() throws Exception {
        // given
        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);
        UserEntity doctor = TestEntityFactory.createUser("doctor", Role.DOCTOR, userRepository);
        String token = jwtTokenUtil.generateJwtToken(doctor.getEmail());

        String invalidPayload = """
    {
        "vaccineName": "MMR",
        "vaccinationDate": "2025/05/20",
        "mandatory": false
    }
    """;

        // when + then
        mockMvc.perform(post("/vaccinations/patient/" + patient.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
        // given
        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);
        UserEntity doctor = TestEntityFactory.createUser("doctor", Role.DOCTOR, userRepository);
        String token = jwtTokenUtil.generateJwtToken(doctor.getEmail());

        String incompletePayload = """
    {
        "mandatory": true
    }
    """;

        // when + then
        mockMvc.perform(post("/vaccinations/patient/" + patient.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompletePayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnForbiddenWhenPatientTriesToAddVaccination() throws Exception {
        // given
        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);
        String token = jwtTokenUtil.generateJwtToken(patient.getUser().getEmail());

        String payload = """
        {
          "vaccineName": "Flu",
          "vaccinationDate": "%s",
          "mandatory": true
        }
        """.formatted(LocalDate.now());

        // when + then
        mockMvc.perform(post("/vaccinations/patient/" + patient.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnNotFoundWhenPatientDoesNotExist() throws Exception {
        // given
        long nonExistentPatientId = 999_999L;
        UserEntity doctor = TestEntityFactory.createUser("doctor404", Role.DOCTOR, userRepository);
        String token = jwtTokenUtil.generateJwtToken(doctor.getEmail());

        String payload = """
        {
            "vaccineName": "Influenza",
            "vaccinationDate": "%s",
            "mandatory": false
        }
        """.formatted(LocalDate.now().toString());

        // when + then
        mockMvc.perform(post("/vaccinations/patient/" + nonExistentPatientId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnMyVaccinationsAsPatient() throws Exception {
        // given
        vaccinationRepository.deleteAll();
        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);

        vaccinationService.addVaccination(patient.getId(), new VaccinationDto("A", LocalDate.now().minusDays(10), true));
        vaccinationService.addVaccination(patient.getId(), new VaccinationDto("B", LocalDate.now().minusDays(5), false));
        String token = jwtTokenUtil.generateJwtToken(patient.getUser().getEmail());

        // when + then
        mockMvc.perform(get("/vaccinations/patient")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].vaccineName").value("A"))
                .andExpect(jsonPath("$[1].vaccineName").value("B"));
    }

    @Test
    void shouldRejectGetMyVaccinationsWhenUnauthorized() throws Exception {

        UserEntity doctor = TestEntityFactory.createUser("doc", Role.DOCTOR, userRepository);
        String token = jwtTokenUtil.generateJwtToken(doctor.getEmail());

        // when + then
        mockMvc.perform(get("/vaccinations/patient")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectAddVaccinationWithoutAuth() throws Exception {
        // given
        PatientEntity patient = TestEntityFactory.createPatientWithUser(userRepository, patientRepository);
        String payload = """
    {
        "vaccineName": "Varicella",
        "vaccinationDate": "%s",
        "mandatory": false
    }
    """.formatted(LocalDate.now().minusDays(7));

        // when + then
        mockMvc.perform(post("/vaccinations/patient/" + patient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }



}
