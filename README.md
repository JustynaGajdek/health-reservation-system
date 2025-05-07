# Health Reservation System

A full-stack medical appointment booking system that allows patients to register, schedule appointments (stationary or teleconsultation), request prescriptions, and track vaccination history. Receptionists and doctors confirm appointments, and administrators manage user approvals.

## 🚀 Tech Stack

**Backend**
* Java 17
* Spring Boot 3
* Spring Security + JWT authentication
* PostgreSQL
* Liquibase (DB migrations)

**Frontend**
* React 18
* React Router
* Axios
* Bootstrap (styling)

**Dev & Tools**
* Maven
* Docker (planned)
* Postman (testing)

## 🩺 Features

* User registration with account status (PENDING → ACTIVE)
* Role-based access: PATIENT, DOCTOR, RECEPTIONIST, ADMIN
* JWT-based login and authorization
* Appointment booking and confirmation
* Prescription requests
* Vaccination tracking (mandatory & optional)
* Admin panel (planned)

## 📁 Project Structure
```
health-reservation-system/
├── backend/ # Spring Boot backend
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/... # Controllers, services, entities, etc.
│ │ │ └── resources/ # application.properties, Liquibase changelogs
│ │ └── test/ # Unit and integration tests
│ ├── pom.xml
│ └── mvnw, mvnw.cmd # Maven wrapper
│
├── frontend/ # React frontend
│ ├── public/
│ └── src/
│ ├── HomePage.jsx
│ ├── LoginForm.jsx
│ └── ...
│
├── docs/ # Technical documentation
│ ├── ARCHITECTURE.md
│ ├── api_endpoints.md
│ ├── database-schema.md
│ └── db_schema_diagram.png
│
└── README.md
```

## 🧪 Run Locally

**Prerequisites**
* Java 17+
* Node.js (for frontend)
* PostgreSQL (or Docker)

**Backend**
```
cd backend
./mvnw spring-boot:run
```
**Frontend**
```
cd frontend
npm install
npm run dev
```
**Database setup**
Run Liquibase migrations:
```
cd backend
./mvnw liquibase:update
```

## 🧪 Tests

The project includes both unit and integration tests for key components:

- User registration & login (JWT)
- Authentication filter logic
- Token generation and validation
- Mapper and service layer logic

To run tests:
```
cd backend
./mvnw test
```
H2 in-memory database is used for integration testing.

## 📚 Documentation
- [Architecture Overview](docs/ARCHITECTURE.md)
- [API Endpoints](docs/api_endpoints.md)
- [Full Database Documentation (PL)](docs/database-schema.md)
- [ER Diagram](docs/db_schema_diagram.png)

## 📌 Status
✅ MVP in progress — login, registration, appointment booking and vaccine records complete.
🔜 Coming soon — patient panel, admin dashboard, email notifications.

## 🧑‍💻 Author
Justyna Gajdek

## 📄 License
MIT (to be added)
