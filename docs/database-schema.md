
# Database Documentation – Health Reservation System

## 1. System Overview

The Health Reservation System allows patients to register and schedule medical appointments at a clinic.
It supports stationary visits, teleconsultations, specialist appointments, prescription requests, and vaccination history tracking.

Appointments requested online must be confirmed by a receptionist or doctor for better time slot control.

---

## 2. Database Design Assumptions

* **Patient registration** can be done online (self-registration) or in-person by a receptionist.
* **Appointment confirmations** are required before final acceptance.
* **User roles** include: `PATIENT`, `DOCTOR`, `RECEPTIONIST`, and `ADMIN`.
* **Prescription requests** can be submitted without booking a visit.
* **Vaccination history** includes both mandatory and optional vaccinations.
* **Security**: Passwords are hashed, and patient medical data is separated from user credentials.

---

## 3. Database Structure

### 1️⃣ `users` table

Stores login credentials and user metadata.

| Column         | Description                      |
| -------------- | -------------------------------- |
| id             | Primary key                      |
| email          | Unique email                     |
| password_hash  | Encrypted password               |
| first_name     | First name                       |
| last_name      | Last name                        |
| phone_number   | Optional phone                   |
| role           | User role (enum)                 |
| status         | Account status (PENDING, ACTIVE) |
| created_at     | Timestamp                        |

---

### 2️⃣ `patients` table

Contains personal medical info linked to `users`.

| Column       | Description                 |
| ------------ | --------------------------- |
| id           | Primary key                 |
| user_id      | FK → users(id)              |
| pesel        | National ID number (unique) |
| birth_date   | Date of birth               |
| guardian_id  | FK → patients(id), optional |
| created_at   | Timestamp                   |

---

### 3️⃣ `doctors` table

List of available doctors (e.g., GP, pediatrician, specialist).

| Column         | Description                 |
| -------------- | --------------------------- |
| id             | Primary key                 |
| user_id        | FK → users(id)              |
| specialization | Medical specialization      |
| office_number  | Room number                 |
| working_hours  | JSON with availability      |
| bio            | Description, qualifications |

---

### 4️⃣ `receptionists` table

| Column   | Description    |
| -------- | -------------- |
| id       | Primary key    |
| user_id  | FK → users(id) |

---

### 5️⃣ `appointments` table

Holds visit details booked by patients.

| Column            | Description                         |
| ----------------- | ----------------------------------- |
| id                | Primary key                         |
| patient_id        | FK → patients(id)                   |
| doctor_id         | FK → doctors(id)                    |
| appointment_type  | Enum: STATIONARY, TELECONSULTATION  |
| appointment_date  | Date & time of the appointment      |
| status            | Enum: PENDING, CONFIRMED, CANCELLED |
| created_at        | Timestamp                           |

---

### 6️⃣ `prescriptions` table

Stores issued prescriptions.

| Column                | Description               |
| --------------------- | ------------------------- |
| id                    | Primary key               |
| patient_id            | FK → patients(id)         |
| doctor_id             | FK → doctors(id)          |
| issue_date            | Date of issue             |
| prescription_details  | Medication list           |
| status                | PENDING, READY, COMPLETED |

---

### 7️⃣ `vaccinations` table

Vaccination history of each patient.

| Column            | Description                    |
| ----------------- | ------------------------------ |
| id                | Primary key                    |
| patient_id        | FK → patients(id)              |
| vaccine_name      | Name of vaccine                |
| vaccination_date  | Date of administration         |
| is_mandatory      | Boolean: mandatory or optional |

---

## 4. Key Relationships

✅ `users` ↔ `patients` / `doctors` / `receptionists` – one-to-one identity per role

✅ `patients` ↔ `appointments` – one-to-many, a patient can have many appointments

✅ `doctors` ↔ `appointments` – each appointment is assigned to one doctor

✅ `patients` ↔ `prescriptions` – one-to-many prescriptions per patient

✅ `patients` ↔ `vaccinations` – one-to-many record of administered vaccines
