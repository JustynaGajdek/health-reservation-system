INSERT INTO users (email, password_hash, first_name, last_name, phone_number, role)
VALUES
  ('john.doe@example.com', 'hashed_password1', 'John', 'Doe', '123456789', 'PATIENT'),
  ('alice.johnson@example.com', 'hashed_password2', 'Alice', 'Johnson', '234567890', 'PATIENT'),
  ('bob.williams@example.com', 'hashed_password3', 'Bob', 'Williams', '345678901', 'PATIENT'),
  ('emily.davis@example.com', 'hashed_password4', 'Emily', 'Davis', '456789012', 'PATIENT'),
  ('child.davis@example.com', 'hashed_password5', 'Tommy', 'Davis', '567890123', 'PATIENT'),
  ('jane.smith@example.com', 'hashed_password6', 'Jane', 'Smith', '678901234', 'DOCTOR'),
  ('robert.miller@example.com', 'hashed_password7', 'Robert', 'Miller', '789012345', 'DOCTOR'),
  ('linda.wilson@example.com', 'hashed_password8', 'Linda', 'Wilson', '890123456', 'DOCTOR'),
  ('michael.brown@example.com', 'hashed_password9', 'Michael', 'Brown', '901234567', 'RECEPTIONIST'),
  ('sarah.lee@example.com', 'hashed_password10', 'Sarah', 'Lee', '012345678', 'RECEPTIONIST'),
  ('admin@example.com', 'hashed_password11', 'Admin', 'User', '000000000', 'ADMIN');

INSERT INTO patients (user_id, pesel, birth_date)
VALUES 
  ((SELECT id FROM users WHERE email = 'john.doe@example.com'), '12345678901', '1985-05-20'),
  ((SELECT id FROM users WHERE email = 'alice.johnson@example.com'), '23456789012', '1990-08-15'),
  ((SELECT id FROM users WHERE email = 'bob.williams@example.com'), '34567890123', '1978-12-30'),
  ((SELECT id FROM users WHERE email = 'emily.davis@example.com'), '45678901234', '2002-03-10');

INSERT INTO patients (user_id, pesel, birth_date, guardian_id)
VALUES (
  (SELECT id FROM users WHERE email = 'child.davis@example.com'),
  '56789012345',
  '2010-06-01',
  (SELECT id FROM patients WHERE pesel = '45678901234')
);

INSERT INTO doctors (user_id, specialization, office_number, working_hours, bio)
VALUES 
  (
    (SELECT id FROM users WHERE email = 'jane.smith@example.com'),
    'Cardiology',
    '101',
    '{"Monday": "09:00-17:00", "Tuesday": "09:00-17:00", "Wednesday": "09:00-17:00"}'::jsonb,
    'Experienced cardiologist with over 10 years of practice, focused on preventive care.'
  ),
  (
    (SELECT id FROM users WHERE email = 'robert.miller@example.com'),
    'Dermatology',
    '102',
    '{"Tuesday": "10:00-16:00", "Thursday": "10:00-16:00", "Friday": "10:00-16:00"}'::jsonb,
    'Skilled dermatologist specializing in skin treatments and cosmetic procedures.'
  ),
  (
    (SELECT id FROM users WHERE email = 'linda.wilson@example.com'),
    'Pediatrics',
    '103',
    '{"Monday": "08:00-14:00", "Wednesday": "08:00-14:00", "Friday": "08:00-14:00"}'::jsonb,
    'Compassionate pediatrician with a focus on child health and preventive medicine.'
  );

INSERT INTO receptionists (user_id)
VALUES 
  ((SELECT id FROM users WHERE email = 'michael.brown@example.com')),
  ((SELECT id FROM users WHERE email = 'sarah.lee@example.com'));

INSERT INTO appointments (patient_id, doctor_id, appointment_type, appointment_date)
VALUES 
  (
    (SELECT id FROM patients WHERE pesel = '12345678901'),
    (SELECT id FROM doctors WHERE user_id = (SELECT id FROM users WHERE email = 'jane.smith@example.com')),
    'STATIONARY',
    '2025-03-15 10:00:00'
  ),
  (
    (SELECT id FROM patients WHERE pesel = '23456789012'),
    (SELECT id FROM doctors WHERE user_id = (SELECT id FROM users WHERE email = 'robert.miller@example.com')),
    'TELECONSULTATION',
    '2025-04-01 14:30:00'
  );

INSERT INTO prescriptions (patient_id, doctor_id, prescription_details)
VALUES 
  (
    (SELECT id FROM patients WHERE pesel = '12345678901'),
    (SELECT id FROM doctors WHERE user_id = (SELECT id FROM users WHERE email = 'jane.smith@example.com')),
    'Take one 100mg Aspirin daily after meals.'
  ),
  (
    (SELECT id FROM patients WHERE pesel = '23456789012'),
    (SELECT id FROM doctors WHERE user_id = (SELECT id FROM users WHERE email = 'robert.miller@example.com')),
    'Apply topical cream twice daily on affected areas.'
  );

INSERT INTO vaccinations (patient_id, vaccine_name, vaccination_date, is_mandatory)
VALUES 
  (
    (SELECT id FROM patients WHERE pesel = '12345678901'),
    'Influenza',
    '2024-11-01',
    TRUE
  ),
  (
    (SELECT id FROM patients WHERE pesel = '23456789012'),
    'COVID-19',
    '2023-12-15',
    TRUE
  );