package com.justynagajdek.healthreservationsystem.integration.util;


import com.justynagajdek.healthreservationsystem.entity.DoctorEntity;
import com.justynagajdek.healthreservationsystem.entity.PatientEntity;
import com.justynagajdek.healthreservationsystem.entity.UserEntity;
import com.justynagajdek.healthreservationsystem.enums.AccountStatus;
import com.justynagajdek.healthreservationsystem.repository.DoctorRepository;
import com.justynagajdek.healthreservationsystem.repository.PatientRepository;
import com.justynagajdek.healthreservationsystem.repository.UserRepository;
import com.justynagajdek.healthreservationsystem.enums.Role;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Component
@Transactional
public class TestEntityFactory {

    public static PatientEntity createPatientWithUser(UserRepository userRepo, PatientRepository patientRepo) {
        UserEntity user = new UserEntity();
        user.setEmail("jan.kowalski@example.com");
        user.setPasswordHash("test1234");
        user.setRole(Role.PATIENT);
        user.setStatus(AccountStatus.ACTIVE);
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPhoneNumber("123456789");
        user = userRepo.save(user);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Jan");
        patient.setLastName("Kowalski");
        patient.setPesel("90010112345");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setUser(user);

        return patientRepo.save(patient);
    }


    public static PatientEntity createPatientWithUser(String email, String pesel, UserRepository userRepo, PatientRepository patientRepo) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setPasswordHash("hashed-password");
        user.setRole(Role.PATIENT);
        user.setStatus(AccountStatus.ACTIVE);
        user = userRepo.save(user);

        PatientEntity patient = new PatientEntity();
        patient.setUser(user);
        patient.setPesel(pesel);;
        patient.setAddress("Test Street 1");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        user.setPatient(patient);

        return patientRepo.save(patient);
    }

    public static DoctorEntity createDoctorWithUser(String email, UserRepository userRepo, DoctorRepository doctorRepo) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("Anna");
        user.setLastName("Nowak");
        user.setPasswordHash("pass123");
        user.setRole(Role.DOCTOR);
        user.setStatus(AccountStatus.ACTIVE);
        user = userRepo.save(user);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setUser(user);
        doctor.setSpecialization("General Practitioner");
        doctor.setOfficeNumber("101");
        return doctorRepo.save(doctor);
    }

    public static UserEntity createUser(String baseEmailPrefix, Role role, UserRepository userRepo) {
        String uniqueEmail = baseEmailPrefix + "+" + UUID.randomUUID() + "@example.com";

        UserEntity user = new UserEntity();
        user.setEmail(uniqueEmail);
        user.setRole(role);
        user.setPasswordHash("test"); // dummy
        user.setFirstName("Test");
        user.setLastName("User");
        user.setStatus(AccountStatus.ACTIVE);
        return userRepo.save(user);
    }

    public static UserEntity createReceptionistWithUser(String email, UserRepository userRepo) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("Recep");
        user.setLastName("Tionist");
        user.setPasswordHash("test123");
        user.setRole(Role.RECEPTIONIST);
        user.setStatus(AccountStatus.ACTIVE);
        return userRepo.save(user);
    }

    public static DoctorEntity createDoctorWithUser(UserRepository userRepo, DoctorRepository doctorRepo) {
        String email = "doctor+" + UUID.randomUUID() + "@example.com";
        return createDoctorWithUser(email, userRepo, doctorRepo);
    }

}
