-- Database: health-reservation-system

-- DROP DATABASE IF EXISTS "health-reservation-system";
/*
CREATE DATABASE "health-reservation-system"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'nb-NO'
    LC_CTYPE = 'nb-NO'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
 */

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(50) CHECK (role IN ('PATIENT', 'DOCTOR', 'RECEPTIONIST', 'ADMIN')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    pesel VARCHAR(11) UNIQUE NOT NULL,
    birth_date DATE NOT NULL,
    guardian_id INT REFERENCES patients(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    specialization VARCHAR(100) NOT NULL,
    office_number VARCHAR(10),
    working_hours JSONB NOT NULL,
	bio TEXT
);

CREATE TABLE receptionists (
    id SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    patient_id INT REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id INT REFERENCES doctors(id) ON DELETE CASCADE,
    appointment_type VARCHAR(50) CHECK (appointment_type IN ('STATIONARY', 'TELECONSULTATION')) NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    status VARCHAR(50) CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED')) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE prescriptions (
    id SERIAL PRIMARY KEY,
    patient_id INT REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id INT REFERENCES doctors(id) ON DELETE CASCADE,
    issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    prescription_details TEXT NOT NULL,
    status VARCHAR(50) CHECK (status IN ('PENDING', 'READY', 'COMPLETED')) DEFAULT 'PENDING'
);

CREATE TABLE vaccinations (
    id SERIAL PRIMARY KEY,
    patient_id INT REFERENCES patients(id) ON DELETE CASCADE,
    vaccine_name VARCHAR(255) NOT NULL,
    vaccination_date DATE NOT NULL,
    is_mandatory BOOLEAN NOT NULL
);

-- Indeksy dla optymalizacji zapytań
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_patients_pesel ON patients(pesel);
