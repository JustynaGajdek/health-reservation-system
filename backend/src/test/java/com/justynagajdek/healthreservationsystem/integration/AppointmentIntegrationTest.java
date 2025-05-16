package com.justynagajdek.healthreservationsystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justynagajdek.healthreservationsystem.dto.AppointmentCreationDto;
import com.justynagajdek.healthreservationsystem.dto.AppointmentRequestDto;
import com.justynagajdek.healthreservationsystem.entity.AppointmentEntity;
import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.enums.AppointmentStatus;
import com.justynagajdek.healthreservationsystem.enums.AppointmentType;
import com.justynagajdek.healthreservationsystem.integration.util.BaseIntegrationTest;
import com.justynagajdek.healthreservationsystem.integration.util.TestEntityFactory;
import com.justynagajdek.healthreservationsystem.repository.AppointmentRepository;
import com.justynagajdek.healthreservationsystem.repository.DoctorRepository;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static com.justynagajdek.healthreservationsystem.integration.util.TestEntityFactory.createPatientWithUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
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


    @Test
    @WithMockUser(username = "jan.kowalski@example.com", roles = "PATIENT")
    void shouldCreateAppointmentWhenSlotIsFree() throws Exception {
        // given: a patient and a doctor exist in the database
        PatientEntity patient = createPatientWithUser(userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);

        AppointmentRequestDto request = new AppointmentRequestDto();
        request.setDoctorId(doctor.getId());
        request.setPreferredDateTime(LocalDateTime.of(2025, 5, 20, 10, 0));
        request.setAppointmentType(AppointmentType.STATIONARY);

        // when: POST /appointments/request
        mockMvc.perform(post("/appointments/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.appointmentDate").value("2025-05-20T10:00:00"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "jan.kowalski@example.com", roles = "PATIENT")
    void shouldReturnConflictWhenDoubleBooking() throws Exception {
        // given
        PatientEntity patient = createPatientWithUser(userRepo, patientRepo);
        DoctorEntity doctor = TestEntityFactory.createDoctorWithUser(userRepo, doctorRepo);
        String datetime = "2025-05-20T10:00:00";

        AppointmentRequestDto request = new AppointmentRequestDto();
        request.setDoctorId(doctor.getId());
        request.setPreferredDateTime(LocalDateTime.parse("2025-05-20T10:00:00"));
        request.setAppointmentType(AppointmentType.TELECONSULTATION);

        // when: first booking succeeds
        mockMvc.perform(post("/appointments/request")
                        .with(user("jan.kowalski@example.com").roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // then: second booking gets 409 Conflict
        mockMvc.perform(post("/appointments/request")
                        .with(user("jan.kowalski@example.com").roles("PATIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andDo(print());
    }
    @Test
    @WithMockUser(username = "jan.kowalski@example.com", roles = "PATIENT")
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

        PatientEntity patient2 = TestEntityFactory.createPatientWithUser("ania@example.com", "12345678902", userRepo, patientRepo);

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







}