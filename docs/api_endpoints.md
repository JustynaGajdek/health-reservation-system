
# API Endpoints – Health Reservation System

This document lists the main REST API endpoints used in the backend application. Each endpoint includes its HTTP method, route, expected request/response, and access control.

---

## 🔐 Authentication & Registration

### `POST /auth/login`
Authenticate user and return JWT token.

**Request body `LoginDTO`:**
```json
{
  "email": "user@example.com",
  "password": "string"
}
```
**Response:**
```json
{
  "token": "<JWT_TOKEN>"
}
```
**Access:** Public

---

### `POST /auth/register`
Register a new user with role (default: `PATIENT`). User starts with status `PENDING`.

**Request body:** `SignUpDto`
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "123456789",
  "password": "pass123",
  "role": "PATIENT"
}
```
**Response:**
```text
Account created. Waiting for admin approval.
```
**Access:** Public

---

## 👤 User Management

### `GET /admin/users`
List all registered users.

**Response:**
```json
[
  {
    "id": 1,
    "email": "john@example.com",
    "role": "PATIENT",
    "status": "PENDING"
  },
  ...
]
```
**Access:** `ADMIN`



### `PUT /admin/users/{id}/approve`
Activate a user by ID.

**Access:** `ADMIN`



### `DELETE /admin/users/{id}`
Delete a user by ID.

**Access:** `ADMIN`

---

## 🧾 Receptionist Endpoints

### `POST /receptionist/appointments`
Create appointment for patient.  
**Request body:** `AppointmentCreateDTO`  
**Access:** RECEPTIONIST

### `PUT /receptionist/appointments/{id}/cancel`
Cancel appointment (confirmed by receptionist).  
**Access:** RECEPTIONIST
---

## 👤 Patient Endpoints

### `GET /patient/appointments`
List your own appointments.  
**Access:** PATIENT

### `POST /patient/appointments`
Request new appointment (status: PENDING).  
**Access:** PATIENT

### `DELETE /patient/appointments/{id}`
Cancel pending appointment.  
**Access:** PATIENT

---

## 👨‍⚕️ Doctor Endpoints

### `GET /doctor/appointments`
Get assigned appointments.  
**Access:** DOCTOR

### `PUT /doctor/appointments/{id}/complete`
Mark appointment as completed.  
**Access:** DOCTOR

---

## 📅 Shared Appointment Endpoints

### `GET /appointments`
List upcoming or past appointments for current user.
**Access:** `PATIENT`, `DOCTOR`

### `POST /appointments`
Book a new appointment.
**Access:** `PATIENT`

### `PUT /appointments/{id}/confirm`
Confirm a pending appointment.
**Access:** `DOCTOR`, `RECEPTIONIST`

### `DELETE /appointments/{id}`
Cancel an appointment.
**Access:** `PATIENT`, `RECEPTIONIST`

---

## 💊 Prescriptions 

### `POST /prescriptions`

Create a new prescription for the current patient.

**Access:** `PATIENT`, `DOCTOR`

### `GET /prescriptions/my`

List prescriptions of the current patient.

**Access:** `PATIENT`

### `GET /prescriptions/by-patient?patientId={id}`

List prescriptions by patient ID.

**Access:** `DOCTOR`, `RECEPTIONIST`


### `GET /prescriptions/by-appointment?appointmentId={id}`

List prescriptions associated with a specific appointment.

**Access:** `DOCTOR`, `RECEPTIONIST`

---

## 💉 Vaccinations 

### `POST /receptionist/vaccinations/appointments`
Schedule vaccination appointment for patient.  
**Request body:** `VaccinationAppointmentDTO`  
**Access:** RECEPTIONIST

### `PUT /doctor/vaccinations/appointments/{id}/approve`
Approve qualification for vaccination.  
**Access:** DOCTOR

### `POST /doctor/vaccinations`
Perform vaccination.  
**Request body:** `VaccinationCreateDTO`  
**Access:** DOCTOR

### `GET /patient/vaccinations`
List completed vaccinations.  
**Access:** PATIENT

---

## 🛑 Error Response Example
```json
{
  "timestamp": "2025-05-07T18:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials",
  "path": "/login"
}
```
```json
{
  "timestamp": "2025-05-20T12:34:56.789+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/admin/users"
}
```

---

> ℹ️ Note: Use this document as a contract between frontend and backend. Actual 
> implementations may evolve; always refer to the live Swagger UI 
> at http://localhost:8080/swagger-ui/index.html for up-to-date specifications.
> 
---
_Generated on 2025-05-20_  
_API version: v1.0_


