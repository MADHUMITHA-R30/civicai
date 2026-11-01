# CivicAI — AI-Powered Citizen Issue Reporting & Resolution Management Platform

![CivicAI Platform](https://img.shields.io/badge/CivicAI-v1.0.0-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)
![Angular](https://img.shields.io/badge/Angular-17-red.svg)
![FastAPI](https://img.shields.io/badge/FastAPI-0.100+-teal.svg)
![Python](https://img.shields.io/badge/Python-3.12-yellow.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)

**CivicAI** is an intelligent, full-stack civic issue reporting and resolution platform that empowers citizens to report civic problems through text or voice, and equips municipal departments with automated AI classification, priority scoring, duplicate detection, and end-to-end resolution tracking.

---

## 🌟 Key Features

### 👤 Citizen Portal
* **Multimodal Issue Reporting**: Report issues via standard web forms or **voice-based reporting** with audio processing.
* **Smart Auto-Filling**: AI extracts category, location/address, and contact information from description.
* **Photo Proof**: Upload evidence photos with issue reports and inspect before/after photos upon resolution.
* **Real-Time Issue Tracking**: Monitor status updates from `SUBMITTED` → `IN_PROGRESS` → `RESOLVED`.
* **Citizen Verification**: Verify resolution quality or reopen/dispute resolved complaints.

### 🏢 Department Admin Portal
* **Automated Routing**: Complaints automatically assigned to the responsible department (Electrical, Roads, Water, Sanitation, Public Safety, Environment).
* **Prioritized Queue**: Criticality engine calculates priority scores (Low, Medium, High, Critical) based on public safety hazards.
* **Duplicate Detection**: TF-IDF & Cosine Similarity detect cluster complaints and prevent redundant dispatching.
* **Resolution Workflow**: Upload resolution evidence photos, submit remarks, and mark complaints as resolved.

### 🛡️ Super Admin Portal
* **Civic Analytics**: Live metrics across departments, resolution rates, average resolution time, and satisfaction scores.
* **Hotspot & Trend Detection**: Geographic tracking of recurring infrastructure issues.
* **Audit & Moderation**: Risk scoring, spam detection, complaint flagging, and user management.

---

## 🏗️ Repository Architecture

This repository is structured as a modular monorepo containing three core services:

```
civicai/
├── civicai-backend/           # Spring Boot 3.2.3 REST API & Unified Static SPA Host
│   ├── src/main/java/         # Java 17 backend source code
│   │   └── com/civicai/       # Controllers, Entities, Services, Security, Repositories
│   ├── src/main/resources/    # Configuration (application.properties, application-mysql.properties)
│   │   └── static/            # Pre-compiled Angular production SPA bundle
│   ├── Dockerfile             # Multi-stage Maven + Temurin JDK 17 Dockerfile
│   └── pom.xml                # Maven project definition
│
├── civicai-frontend/          # Angular 17 Standalone SPA
│   ├── src/app/               # Pages, Components, Guards, Models, and Services
│   ├── src/environments/      # Environment configurations (relative /api pathing)
│   ├── Dockerfile             # Standalone Nginx Dockerfile
│   ├── nginx.conf             # Nginx reverse proxy configuration
│   ├── proxy.conf.json        # Dev proxy configuration
│   └── package.json           # Angular npm dependencies
│
├── civicai-ai-service/        # Python FastAPI Microservice
│   ├── main.py                # AI routing, deduplication, NLP extraction, priority engine
│   ├── requirements.txt       # Python dependencies (FastAPI, Uvicorn, Scikit-learn, Pydantic)
│   └── Dockerfile             # Python 3.12-slim Dockerfile
│
├── docker-compose.yml         # Multi-service container orchestration (MySQL, AI Service, Backend)
├── .env.example               # Environment variables configuration template
├── .gitignore                 # Comprehensive repository ignore rules
└── README.md                  # Project documentation
```

---

## 🚀 Quick Start & Local Execution

You can run CivicAI using either **Docker Compose** (one command) or **Local Native Execution**.

### Option A: One-Command Docker Run (Recommended)

Requires [Docker Desktop](https://www.docker.com/).

```bash
# Clone the repository
git clone https://github.com/your-username/civicai.git
cd civicai

# Start all services (MySQL, FastAPI AI Service, Spring Boot Full-Stack)
docker-compose up --build
```

Access the application:
* **Main Web Platform (Full-Stack)**: [http://localhost:8080](http://localhost:8080)
* **FastAPI AI Docs**: [http://localhost:8000/docs](http://localhost:8000/docs)

---

### Option B: Unified Single-Port Execution (Native)

Spring Boot serves both the backend REST APIs and the pre-built Angular frontend directly from port `8080`.

#### 1. Start Python AI Microservice
```bash
cd civicai-ai-service
pip install -r requirements.txt
python main.py
# Runs on http://localhost:8000
```

#### 2. Start Spring Boot Backend
```bash
cd civicai-backend
mvn spring-boot:run
# Runs on http://localhost:8080 (Uses in-memory H2 database with seed data)
```

Open your browser to: **`http://localhost:8080`**

---

### Option C: Development Mode (Live Frontend Reload)

If actively modifying Angular components:

```bash
# In a 3rd terminal:
cd civicai-frontend
npm install
npm start
# Runs on http://localhost:4200 (proxies /api to http://localhost:8080)
```

---

## 🔑 Demo Accounts (Pre-Seeded)

When running for the first time, demo accounts and sample complaints are automatically seeded:

| Role | Email / Identifier | Password | Dashboard Features |
|---|---|---|---|
| **Citizen** | `citizen@civicai.org` | `citizen123` | Submit issues, voice complaints, view status, verify resolution |
| **Department Admin** | `dept@civicai.org` | `dept123` | Manage department queue, update progress, resolve with photo |
| **Super Admin** | `admin@civicai.org` | `admin123` | Department analytics, SLA monitoring, user risk control |

---

## ⚙️ Environment Variables & Cloud Configuration

Copy `.env.example` to `.env` when deploying to production:

```env
# Application Ports
PORT=8080
AI_SERVICE_URL=http://ai-service:8000

# Active Database Profile (dev for H2, mysql for Cloud MySQL)
SPRING_PROFILES_ACTIVE=mysql
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/civicai_db?useSSL=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=civic_user
SPRING_DATASOURCE_PASSWORD=your_strong_password

# JWT Security
JWT_SECRET=your_256bit_secure_random_key_string_for_production
JWT_EXPIRATION_MS=86400000

# File Storage & CORS
UPLOAD_DIR=/app/uploads
CORS_ALLOWED_ORIGINS=https://your-domain.com
```

---

## 📡 API Overview

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register citizen account |
| `POST` | `/api/auth/login` | Login and receive JWT Bearer token |
| `GET` | `/api/categories` | Retrieve 15 civic issue categories |
| `GET` | `/api/departments` | Retrieve 7 municipal departments |
| `POST` | `/api/complaints` | Submit new civic complaint with photo |
| `POST` | `/api/complaints/voice-process` | Process voice recording |
| `POST` | `/api/complaints/check-duplicate` | Check similarity against existing complaints |
| `GET` | `/api/complaints/my` | Retrieve logged-in citizen's complaints |
| `GET` | `/api/complaints/department` | Retrieve department queue |
| `PUT` | `/api/complaints/{id}/status` | Update complaint status |
| `POST` | `/api/complaints/{id}/resolve` | Submit resolution remarks & after-photo |
| `POST` | `/api/complaints/{id}/verify` | Citizen verification / dispute feedback |
| `GET` | `/api/analytics/department` | Department resolution metrics & SLA stats |

---

## 📄 License
This project is licensed under the Apache 2.0 License.
