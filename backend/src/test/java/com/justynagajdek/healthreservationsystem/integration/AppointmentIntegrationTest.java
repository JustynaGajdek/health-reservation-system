package com.justynagajdek.healthreservationsystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.AppointmentRequestDto;
import com.justynagajdek.healthreservationsystem.entity.*;
import com.justynagajdek.healthreservationsystem.enums.AppointmentStatus;
import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import com.justynagajdek.healthreservationsystem.enums.Role;
import com.justynagajdek.healthreservationsystem.integration.util.BaseIntegrationTest;
import com.justynagajdek.healthreservationsystem.integration.util.TestEntityFactory;
import com.justynagajdek.healthreservationsystem.repository.*;
import com.justynagajdek.healthreservationsystem.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.justynagajdek.healthreservationsystem.integration.util.TestEntityFactory.createPatientWithUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AppointmentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;
    @Autowired
    private ReceptionistRepository receptionistRepo;

    @Autowired
    private AppointmentService appointmentService;


    @Test
    void shouldCreateAppointmentWhenSlotIsFree() throws Exception {
        // given: a patient and a doctor exist in the database
        String email = "jan.kowalski+" + UUID.randomUUID() + "@example.com";
        String pesel = UUID.randomUUID().toString().substring(0, 11);
        PatientEntity patient = TestEntityFactory.createPatientWithUser(email, pesel, userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);

        AppointmentRequestDto request = new AppointmentRequestDto();
        request.setDoctorId(doctor.getId());
        request.setPreferredDateTime(LocalDateTime.of(2025, 5, 20, 10, 0));
        request.setAppointmentType(AppointmentType.STATIONARY);

        // when: POST /appointments/request
        mockMvc.perform(post("/appointments/request")
                        .with(user(email).roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.appointmentDate").value("2025-05-20T10:00:00"))
                .andDo(print());
    }

    @Test
    void shouldReturnConflictWhenDoubleBooking() throws Exception {
        // given
        String email = "jan.kowalski+" + UUID.randomUUID() + "@example.com";
        String pesel = UUID.randomUUID().toString().substring(0, 11);

        PatientEntity patient = TestEntityFactory.createPatientWithUser(email, pesel, userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);

        AppointmentRequestDto request = new AppointmentRequestDto();
        request.setDoctorId(doctor.getId());
        request.setPreferredDateTime(LocalDateTime.parse("2025-05-20T10:00:00"));
        request.setAppointmentType(AppointmentType.TELECONSULTATION);

        // when: first booking succeeds
        mockMvc.perform(post("/appointments/request")
                        .with(user(email).roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // then: second booking gets 409 Conflict
        mockMvc.perform(post("/appointments/request")
                        .with(user(email).roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    void shouldReturnAppointmentsForPatient() throws Exception {
        // given
        PatientEntity patient = createPatientWithUser(userRepo, patientRepo);
        DoctorEntity doctor1 = TestEntityFactory.createDoctorWithUser("anna1@example.com", userRepo, doctorRepo);
        DoctorEntity doctor2 = TestEntityFactory.createDoctorWithUser("anna2@example.com", userRepo, doctorRepo);

        AppointmentEntity a1 = new AppointmentEntity();
        a1.setAppointmentDate(LocalDateTime.of(2025, 5, 20, 10, 0));
        a1.setDoctor(doctor1);
        a1.setPatient(patient);
        a1.setAppointmentType(AppointmentType.STATIONARY);
        a1.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepo.save(a1);

        AppointmentEntity a2 = new AppointmentEntity();
        a2.setAppointmentDate(LocalDateTime.of(2025, 5, 21, 11, 0));
        a2.setDoctor(doctor2);
        a2.setPatient(patient);
        a2.setAppointmentType(AppointmentType.TELECONSULTATION);
        a2.setStatus(AppointmentStatus.PENDING);
        appointmentRepo.save(a2);

        PatientEntity patient2 = TestEntityFactory.createPatientWithUser("ania@example.com", "12345678936", userRepo, patientRepo);

        AppointmentEntity a3 = new AppointmentEntity();
        a3.setAppointmentDate(LocalDateTime.of(2025, 5, 22, 12, 0));
        a3.setDoctor(doctor1);
        a3.setPatient(patient2);
        a3.setAppointmentType(AppointmentType.STATIONARY);
        a3.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepo.save(a3);

        // when + then
        mockMvc.perform(get("/appointments/mine")
                        .with(user("jan.kowalski@example.com").roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].patientName").value("Jan Kowalski"))
                .andExpect(jsonPath("$[1].patientName").value("Jan Kowalski"))
                .andDo(print());
    }

    @Test
    void shouldReturnAppointmentsForDoctor() throws Exception {
        String email = "doctor+" + UUID.randomUUID() + "@example.com";
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(email, userRepo, doctorRepo);
        PatientEntity patient = TestEntityFactory.createPatientWithUser("patient@example.com", "12345678958", userRepo, patientRepo);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));
        appointment.setAppointmentType(AppointmentType.STATIONARY);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepo.save(appointment);

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.getName()).thenReturn("doctor@example.com");
        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        // when
        mockMvc.perform(get("/appointments/mine")
                        .with(user(email).roles("DOCTOR"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    void shouldReturnForbiddenForUnauthorizedRole() throws Exception {
        // given
        String email = "reception+" + UUID.randomUUID() + "@example.com";
        TestEntityFactory.createReceptionistWithUser(email, userRepo);


        // when + then
        mockMvc.perform(get("/appointments/mine")
                        .with(user(email).roles("RECEPTIONIST"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }


    @Test
    void shouldReturnUnauthorizedIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/appointments/mine")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "reception@example.com", roles = "RECEPTIONIST")
    void shouldCancelAppointmentAsReceptionist() throws Exception {
        // given
        String email = "reception+" + UUID.randomUUID() + "@example.com";
        UserEntity receptionistUser = TestEntityFactory.createUser(email, Role.RECEPTIONIST, userRepo);

        ReceptionistEntity receptionist = new ReceptionistEntity();
        receptionist.setUser(receptionistUser);
        receptionistRepo.save(receptionist);

        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser("doc@example.com", userRepo, doctorRepo);
        PatientEntity patient = TestEntityFactory.createPatientWithUser("patient@example.com", "12345678909", userRepo, patientRepo);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setAppointmentType(AppointmentType.STATIONARY);
        appointmentRepo.save(appointment);

        // when + then
        mockMvc.perform(delete("/reception/appointments/" + appointment.getId())
                        .with(user("reception@example.com").roles("RECEPTIONIST"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());


        // verify
        Optional<AppointmentEntity> maybeAppointment = appointmentRepo.findById(appointment.getId());
        assertThat(maybeAppointment).isPresent();
        assertThat(maybeAppointment.get().getStatus()).isEqualTo(AppointmentStatus.CANCELED);

    }

    @Test
    void shouldReturnBadRequestWhenRequestMissingDoctorOrDate() throws Exception {
        // given
        String email = "jan.kowalski+" + UUID.randomUUID() + "@example.com";
        String pesel = UUID.randomUUID().toString().substring(0, 11);

        TestEntityFactory.createPatientWithUser(email, pesel, userRepo, patientRepo);

        AppointmentRequestDto invalidRequest = new AppointmentRequestDto();
        invalidRequest.setAppointmentType(AppointmentType.STATIONARY);

        // when + then
        mockMvc.perform(post("/appointments/request")
                        .with(user(email).roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void shouldAllowPatientToCancelOwnAppointment() throws Exception {
        String email = "patient+" + UUID.randomUUID() + "@example.com";
        PatientEntity patient = TestEntityFactory.createPatientWithUser(email, "12345678971", userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setAppointmentType(AppointmentType.STATIONARY);
        appointmentRepo.save(appointment);

        mockMvc.perform(patch("/appointments/" + appointment.getId() + "/cancel-request")
                        .with(user(email).roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(appointmentRepo.findById(appointment.getId()).get().getStatus())
                .isEqualTo(AppointmentStatus.CANCEL_REQUESTED);
    }
    @Test
    void shouldReturnNotFoundWhenCancelingNonExistentAppointment() throws Exception {
        mockMvc.perform(delete("/appointments/999999")
                        .with(user("patient@example.com").roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectAppointmentInThePast() throws Exception {
        String email = "jan+" + UUID.randomUUID() + "@example.com";
        TestEntityFactory.createPatientWithUser(email, "12345678981", userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);

        AppointmentRequestDto request = new AppointmentRequestDto();
        request.setDoctorId(doctor.getId());
        request.setPreferredDateTime(LocalDateTime.now().minusDays(1));
        request.setAppointmentType(AppointmentType.TELECONSULTATION);

        mockMvc.perform(post("/appointments/request")
                        .with(user(email).roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectCancelRequestForOthersAppointment() throws Exception {
        // given
        PatientEntity owner = TestEntityFactory.createPatientWithUser("owner@example.com", "12345678999", userRepo, patientRepo);
        PatientEntity attacker = TestEntityFactory.createPatientWithUser("attacker@example.com", "98765432109", userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setDoctor(doctor);
        appointment.setPatient(owner);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(2));
        appointment.setAppointmentType(AppointmentType.TELECONSULTATION);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepo.save(appointment);

        // when + then
        mockMvc.perform(patch("/appointments/" + appointment.getId() + "/cancel-request")
                        .with(user("attacker@example.com").roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectCancelRequestIfNotConfirmed() throws Exception {
        String email = "jan+" + UUID.randomUUID() + "@example.com";
        String pesel = UUID.randomUUID().toString().substring(0, 11);
        PatientEntity patient = TestEntityFactory.createPatientWithUser(email, pesel, userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);

        for (AppointmentStatus status : List.of(AppointmentStatus.PENDING, AppointmentStatus.CANCEL_REQUESTED)) {
            AppointmentEntity appointment = new AppointmentEntity();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));
            appointment.setAppointmentType(AppointmentType.STATIONARY);
            appointment.setStatus(status);
            appointmentRepo.save(appointment);

            mockMvc.perform(patch("/appointments/" + appointment.getId() + "/cancel-request")
                            .with(user(email).roles("PATIENT"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

}
