# Integration Test Plan – Health Reservation System

## Purpose
This document outlines the strategy and scope of **integration testing** for the Health Reservation System. Integration tests verify the correctness of interactions between controllers, services, repositories, security layers, and persistence.

---

## Testing Strategy
All integration tests:
- Use the full Spring Boot context.
- May use embedded PostgreSQL/Testcontainers.
- Include authentication where needed (JWT, user roles).
- Validate both successful and erroneous flows.

---

## Module Coverage Overview

| Module        | Controller Class            | Key Use Cases                         | Priority |
|---------------|-----------------------------|---------------------------------------|----------|
| Auth          | AuthController              | Login, registration, JWT              | High     |
| User          | UserController (via Auth)   | Registration, duplicate prevention    | High     |
| Doctor        | DoctorController            | CRUD operations, availability         | Medium   |
| Receptionist  | ReceptionistController      | Accepting users, creating patients    | High     |
| Appointment   | AppointmentController       | Booking, cancelling, viewing          | High     |
| Prescription  | PrescriptionController      | Issuing prescriptions                 | Medium   |
| Vaccination   | VaccinationController       | Booking, qualification, execution     | Medium   |
| Public Info   | PublicInfoController        | Public endpoints for services         | Low      |
| Patient       | PatientController           | Viewing/updating patient data         | Medium   |
| Admin         | AdminController             | Managing user roles and access        | Medium   |

---

## Example Test Scenarios Per Module

### ✈ AuthController
- ✅ Login with valid credentials → 200 OK + JWT
- ❌ Login with wrong password → 401 Unauthorized
- ❌ Login with inactive account → 403 Forbidden

### 👩‍⚕️ DoctorController
- [ ] GET `/doctors` returns list
- [ ] POST `/doctors` by admin adds new doctor
- [ ] GET `/doctors/{id}` returns doctor data
- [ ] DELETE `/doctors/{id}` allowed only for admin

### 🗓 AppointmentController
- [ ] Patient creates appointment
- [ ] Cannot double-book same time slot
- [ ] Doctor views their appointments
- [ ] Receptionist can cancel appointments

### 💊 PrescriptionController
- [ ] Doctor issues prescription for a patient
- [ ] Unauthorized user → 401
- [ ] Missing fields → 400
- [ ] Valid prescription is persisted

### 🧠 VaccinationController (V1)
- [ ] Receptionist schedules appointment
- [ ] Doctor qualifies patient
- [ ] Doctor executes vaccination
- [ ] Patient sees only completed ones

### 🧑‍💼 ReceptionistController
- [ ] Accepts new user account (sets ACTIVE)
- [ ] Creates patient profile for unregistered visitor

---

## Folder Structure for Tests
```
src/test/java
└── com.justynagajdek.healthreservationsystem
    └── integration
        ├── BaseIntegrationTest.java
        ├── AuthIntegrationTest.java
        ├── DoctorIntegrationTest.java
        ├── AppointmentIntegrationTest.java
        ├── ReceptionistIntegrationTest.java
        ├── VaccinationIntegrationTest.java
        └── PrescriptionIntegrationTest.java
```

---

## Good Practices
- Each test covers at least 1 happy path and 1 failure path.
- Test names describe expected behavior: `shouldReturn403WhenInactiveUserTriesToLogin()`
- Group tests by module and keep assertions clear.
- Use `@WithMockUser` or actual JWTs for role-based testing.

---

## Next Steps
- ✅ Tests for Auth and Registration completed
- ⏳ Start with Doctor or Appointment modules next
- ✏ Document found bugs and limitations in `BUGS.md`
- ⇲ Add Postman or REST Assured-based API regression later

---

For every new integration module:
> Create a branch like `test/integration/doctor`, commit frequently, and open PR for review.

---

_Last updated: 2025-05-16_
