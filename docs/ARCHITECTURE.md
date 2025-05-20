
# Architecture Overview – Health Reservation System

## 🧩 System Overview

The Health Reservation System is a full-stack application that allows users to register, book appointments (stationary or teleconsultation), request prescriptions, and view vaccination history. The system includes multiple roles (patient, doctor, receptionist, admin), JWT-based authentication, and persistent PostgreSQL storage managed with Liquibase.

---

## 🏗️ Core Components

### Backend (Spring Boot)
- **Controller Layer** – exposes REST API endpoints for login, registration, user management, and appointments
- **Service Layer** – contains business logic and security-related processing
- **Repository Layer** – uses Spring Data JPA to access the database
- **DTOs** – data transfer objects used for request/response (e.g., `SignUpDto`, `AppointmentCreateDTO`)
- **Mappers** – convert between entities and DTOs using `MapStruct`
- **Exception Handling** – global exception handler with @ControllerAdvice, structured error responses (timestamp, status, message)
- **JWT Authentication** – stateless login with `Authorization: Bearer <token>`
- **SecurityConfig** – configures access per role, public/private endpoints, and filters
- **Liquibase** – manages schema and test data

### Frontend (React)
- **Pages** – `HomePage`, `LoginForm`, `Dashboard`
- **React Router** – client-side navigation
- **Axios** – connects to backend APIs
- **State** – token stored in `localStorage`, redirect after login

### Database (PostgreSQL)
- Tables: `users`, `patients`, `doctors`, `receptionists`, `appointments`, `prescriptions`, `vaccinations`
- All schema changes and initial data handled with Liquibase

---

## 👤 User Roles and Access Flow

- **PATIENT** – registers via `/auth/register`, starts with `PENDING` status; after activation can log in, view/book appointments, request prescriptions, and view vaccination history.
- **RECEPTIONIST** – reviews and activates patient accounts, confirms or cancels appointment requests, schedules vaccinations.
- **DOCTOR** – views assigned appointments, approves qualifications for vaccinations, and issues prescriptions.
- **NURSE** – supports vaccination execution and possibly monitoring patient history.
- **ADMIN** – creates and manages medical staff accounts (`DOCTOR`, `RECEPTIONIST`, `NURSE`), oversees system users and roles.


### Registration flow:
1. User registers account → assigned role `PATIENT` (status: `PENDING`)
2. Receptionist reviews and activates patient account → status becomes `ACTIVE`
3. Patient can log in and use the system (e.g. book appointments)
4. Administrator manually creates accounts for medical staff (`DOCTOR`, `RECEPTIONIST`,` NURSE`)

---

## 🔐 Security

- JWT token is generated on `/login` and attached to `Authorization` header
- Token is verified on every request by `JwtAuthenticationFilter`
- Role-based access is enforced with `.hasRole(...)` and `@PreAuthorize`

Example:
```
.requestMatchers("/admin/**").hasRole("ADMIN")
```

---

## 🗂️ Entity Diagram

```mermaid
classDiagram
  class UserEntity {
    Long id
    String email
    String passwordHash
    String role
    AccountStatus status
  }

  class PatientEntity {
    Long id
    String pesel
    LocalDate birthDate
  }

  class DoctorEntity {
    Long id
    String specialization
    String officeNumber
    String workingHours
  }

  class ReceptionistEntity {
    Long id
  }

  class AppointmentEntity {
    Long id
    AppointmentType appointmentType
    LocalDateTime appointmentDate
    AppointmentStatus status
  }

  class PrescriptionEntity {
    Long id
    String prescriptionDetails
    PrescriptionStatus status
  }

  class VaccinationEntity {
    Long id
    String vaccineName
    Date vaccinationDate
    boolean isMandatory
  }

  UserEntity --> PatientEntity
  UserEntity --> DoctorEntity
  UserEntity --> ReceptionistEntity
  PatientEntity --> AppointmentEntity
  DoctorEntity --> AppointmentEntity
  PatientEntity --> PrescriptionEntity
  PatientEntity --> VaccinationEntity
```
---

## 🧪 Testing Strategy

The backend includes unit and integration tests, covering core business logic and API behavior:

**✅ Unit Tests**

- **Service layer logic** – tested with `@MockBean` and `Mockito` (e.g.,`UserServiceTest`,`PrescriptionServiceTest`)
- **JWT generation and parsing** – (`JwtTokenUtilTest`)
- **Mapper correctness** – (`UserMapperTest`)

**🔄 Integration Tests** 
- End-to-end tests of registration, authentication, appointments, and vaccination flows using `@SpringBootTest` and `MockMvc`
- `Testcontainers` used to spin up real **PostgreSQL** instances during integration tests
- Liquibase migrations automatically applied in test containers

```bash
cd backend
./mvnw test
```
Test results are reported by Maven Surefire and run automatically in CI.
---

## 🚀 Scalability (Future Ideas)

- Move modules (auth, appointments, notifications) to microservices
- Add email notifications (account approval, appointment reminder)
- Add audit logging for user actions
- Introduce frontend state management (Redux or Context API)
- Containerize with Docker and add GitHub Actions CI/CD

---

## 📁 Related Files
- `README.md` – general project description and setup
- `database-schema.md` – detailed database documentation
- `api_endpoints.md` – list of available REST endpoints with request/response structure


