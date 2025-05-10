# Health Reservation System

A full-stack medical appointment booking system that allows patients to register, schedule appointments (stationary or teleconsultation), request prescriptions, and track vaccination history. Receptionists and doctors confirm appointments, and administrators manage user approvals.

## 🚀 Tech Stack

**Backend**
* Java 17
* Spring Boot 3
* Spring Security + JWT authentication
* PostgreSQL
* Liquibase (DB migrations)
* Docker Compose – backend & database

**Frontend**
* React 18
* React Router
* Axios
* Bootstrap (styling)

**Dev & Tools**
* Maven
* Docker & Docker Compose
* GitHub Actions (CI)
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
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── .env (not committed)
│   ├── src/
│   │ ├── main/
│   │ │ ├── java/com/... # Controllers, services, entities, etc.
│   │ │ └── resources/ # application.properties, Liquibase changelogs
│   │ └── test/ # Unit and integration tests
│   ├── pom.xml
│   └── mvnw, mvnw.cmd # Maven wrapper
│
├── frontend/ # React frontend
│ ├── public/
│ └── src/
│      ├── HomePage.jsx
│      ├── LoginForm.jsx
│      └── ...
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
*  Docker & Docker Compose

### 🐳 Dockerized Run (backend + database)

1. Build JAR manually:
```bash
cd backend
./mvnw clean package -DskipTests
```
2. Start containers:
```bash
cd backend
docker compose up --build
```
_Backend runs on `http://localhost:8080`, PostgreSQL on port `5433`._


**Frontend**
```
cd frontend
npm install
npm run dev
```

### ⚙️ Local dev without Docker
```bash
cd backend
./mvnw spring-boot:run
```

**Database setup**
Run Liquibase migrations:
```
cd backend
./mvnw liquibase:update
```

## ✅ Environment
Create `.env` file inside `/backend`:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/health-reservation-system
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=asd
SPRING_JPA_HIBERNATE_DDL_AUTO=update
JWT_SECRET=your-test-jwt-secret
```
Use `.env.example` as a base template.


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

## 🧪 CI/CD

GitHub Actions workflow:
- runs on pull requests and pushes to main
- builds backend with Maven
- runs unit & integration tests
- supports env variables in CI only

Workflow file: `.github/workflows/backend.yml`


## 📚 Documentation
- [Architecture Overview](docs/ARCHITECTURE.md)
- [API Endpoints](docs/api_endpoints.md)
- [Full Database Documentation (PL)](docs/database-schema.md)
- [ER Diagram](docs/db_schema_diagram.png.png)

## 📌 Status
✅ MVP in progress — login, registration, appointment booking complete.
🔜 Coming soon — patient panel, admin dashboard, email notifications.

## 🧑‍💻 Author
Justyna Gajdek

## 📄 License
MIT (to be added)
