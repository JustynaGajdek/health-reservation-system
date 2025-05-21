# Health Reservation System

> This project was developed as part of the **Tech Leaders mentoring program** and showcases a role-based medical appointment system.

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
* React 18 (Vite)
* React Router v6+
* Context API (auth state)
* Axios (API layer with token handling)
* Bootstrap + custom CSS
* Role-based routing (PrivateRoute)

**Dev & Tools**
* Maven
* Docker & Docker Compose
* GitHub Actions (CI)
* Postman (testing)
---
## 🩺 Features

* User registration with account status (PENDING → ACTIVE)
* Role-based access: PATIENT, DOCTOR, RECEPTIONIST, ADMIN
* JWT-based login and authorization
* Appointment booking and confirmation
* Prescription requests and access control
* Vaccination tracking with qualification and execution flow
* Admin user management (view, approve, delete users)
* Swagger UI documentation at `/swagger-ui/index.html`
* Fullstack Docker support (backend + frontend + PostgreSQL via Docker Compose)
* GitHub Actions CI/CD for backend and frontend
---
## 📁 Project Structure
```
health-reservation-system/
├── backend/ # Spring Boot backend
│   ├── Dockerfile
│   ├── .env (not committed)
│   ├── src/
│   │ ├── main/
│   │ │ ├── java/com/justynagajdek/healthreservationsystem/
│   │ │ │  ├── config/
│   │ │ │  ├── controller/
│   │ │ │  ├── dto/
│   │ │ │  ├── entity/
│   │ │ │  ├── enums/
│   │ │ │  ├── jwt/
│   │ │ │  ├── mapper/
│   │ │ │  ├── payload/
│   │ │ │  ├── repository/
│   │ │ │  └── service/
│   │ │ └── resources/ # application.properties, Liquibase changelogs
│   │ └── test/ # Unit and integration tests
│   └── resources/
│   │  ├── db/
│   │  ├── static/
│   │  └── templates/
│   ├── pom.xml
│   └── mvnw, mvnw.cmd # Maven wrapper
│
├── frontend/ # React frontend
│ ├── Dockerfile
│ ├── .dockerignore
│ ├── nginx.conf
│ ├── public/
│ └── src/
│      ├── pages/
│      │    ├── HomePage/
│      │    ├── LoginPage/
│      │    ├── RegisterPage/
│      │    └── DashboardPage/
│      ├── components/
│      │    └── PrivateRoute.jsx
│      ├── context/
│      │    └── AuthContext.jsx
│      ├── hooks/
│      │    └── useAuth.js
│      ├── services/
│      │    └── api.js, auth.service.js
│      └── main.jsx, App.jsx
│
├── .github/
│   └── workflows/
│       ├── backend.yml
│       └── frontend-ci.yml
│ 
├── docs/ # Technical documentation
│ ├── ARCHITECTURE.md
│ ├── api_endpoints.md
│ ├── database-schema.md
│ └── db_schema_diagram.png
│ 
├── docker-compose.yml          # Multi-service deployment
└── README.md
```
---
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
cd ..
docker compose up --build
```
_Backend runs on `http://localhost:8080`_

_Frontend (React + Nginx): `http://localhost:3000`_

_PostgreSQL on port `5433`._

---
### ⚙️ Local dev without Docker

**Run backend:**
```bash
cd backend
./mvnw spring-boot:run
```
**Run frontend (in a separate terminal):**
```bash
cd frontend
npm install
npm run dev

```
_Frontend runs on `http://localhost:3000` and proxies API requests to backend._

**Database setup**
You must have `PostgreSQL` running locally on `localhost:5432 `
with user and DB configured as in `application.properties.`

Run Liquibase migrations:
```
cd backend
./mvnw liquibase:update
```
---
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

---
## 🧪 Tests

The project includes both unit and integration tests, covering:

**✅ Unit tests:**

- Controllers: auth, admin, patient, doctor, receptionist, appointment, prescriptions, vaccinations
- Services: user, staff, patient, doctor, prescription, appointment, vaccination
- Mappers: user, patient, appointment, prescription, vaccination
- Security: JWT token utils and authentication filter

**🔄 Integration tests:**
- Authentication & registration flows
- Appointment and vaccination scenarios
- Spring context bootstrapping

All tests run via **Maven Surefire** in CI/CD.

You can run them locally with:
```
cd backend
./mvnw test
```
---
## 🧪 CI/CD

- `backend.yml`: runs on pull requests and pushes to `main`
    - builds backend with Maven
    - runs unit & integration tests
    - supports env variables in CI only
- `frontend-ci.yml`: runs on pull requests and pushes to `main`
    - installs dependencies
    - builds the React app
    - runs linting and basic frontend checks

Workflow file:
- `.github/workflows/backend.yml`
- `.github/workflows/frontend-ci.yml`
---

## 📖 API Docs (Swagger UI)

The backend uses [Springdoc OpenAPI](https://springdoc.org/) to automatically generate and serve interactive API documentation.

To access the documentation locally, start the backend  (`./mvnw spring-boot:run` or via Docker) and go to:

`http://localhost:8080/swagger-ui.html`

You can test all available endpoints directly in the browser, including:
- `POST /login`
- `POST /register`
- `GET /admin/users`
- `PUT /admin/users/approve/{id}`
- ...and more.

Authentication-required endpoints support `Bearer` JWT tokens.

The Swagger configuration can be found in:
- `OpenApiConfig.java` – metadata (title, version, description)
- `SecurityConfig.java` – public access granted to `/swagger-ui/**` and `/v3/api-docs/**`

---

## ⚙️ Configuration: `application.yml`
This project uses `application.yml` instead of `application.properties` for cleaner, structured configuration.

```yaml
Main configuration values:

spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}

  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml

app:
  jwtSecret: ${JWT_SECRET}
  jwtExpirationMs: 3600000
```
Secrets and credentials are loaded from `.env` or system environment variables (e.g. in Docker Compose or CI pipelines).

## 📚 Documentation
- [Architecture Overview](docs/ARCHITECTURE.md)
- [API Endpoints](docs/api_endpoints.md)
- [Full Database Documentation (PL)](docs/database-schema.md)
- [ER Diagram](docs/db_schema_diagram.png.png)
- [Swagger UI – interactive API docs](http://localhost:8080/swagger-ui.html)


## 📌 Status
✅ MVP completed
🚧 Planned: email notifications, calendar integration, audit logs

## 🧑‍💻 Author
Justyna Gajdek

## 📄 License
MIT (to be added)
