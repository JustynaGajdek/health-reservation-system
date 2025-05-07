
# API Endpoints – Health Reservation System

This document lists the main REST API endpoints used in the backend application. Each endpoint includes its HTTP method, route, expected request/response, and access control.

---

## 🔐 Authentication & Registration

### `POST /login`
Authenticate user and return JWT token.

**Request body (JSON):**
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

### `POST /register`
Register a new user with role `PATIENT`, `DOCTOR`, or `RECEPTIONIST`. User starts with status `PENDING`.

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

---

### `PUT /admin/users/{id}/activate`
Activate a user by ID.

**Access:** `ADMIN`

---

### `DELETE /admin/users/{id}`
Delete a user by ID.

**Access:** `ADMIN`

---

## 📅 Appointments (planned)

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

## 💊 Prescriptions (planned)

### `POST /prescriptions/request`
Request a new prescription.
**Access:** `PATIENT`

### `GET /prescriptions`
List prescriptions for current patient.
**Access:** `PATIENT`, `DOCTOR`

---

## 💉 Vaccinations (planned)

### `GET /vaccinations`
Get vaccination history.
**Access:** `PATIENT`, `DOCTOR`

### `POST /vaccinations`
Add new vaccination (administered).
**Access:** `DOCTOR`, `RECEPTIONIST`

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

---

> ℹ️ Note: Actual endpoints might vary depending on how your controllers evolve. Use this document as a working contract between backend and frontend.
