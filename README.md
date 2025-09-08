# BA Copilot — from notes to developer-ready deliverables

**BA Copilot** is an AI-assisted Spring Boot platform that turns messy notes into clear, traceable requirements and user stories, then packages them into BRD/FRD PDFs with diagrams - ready for engineering and stakeholders. Approvals, Jira sync, and secure sharing are built in. Optional subscription billing is supported.

---

## ✨ Why teams use BA Copilot

* **From raw notes to requirements** in minutes
* **Auto-generated user stories** with full traceability
* **One-click BRD/FRD PDFs** with embedded diagrams
* **Approvals that stick** (logged + emailed)
* **Sync to Jira** without copy-paste
* **Secure sharing** via short-lived links
* **Optional billing** for SaaS deployments

---

## 👥 Roles

* **Admin** — manages users, projects, and billing
* **Business Analyst (BA)** — creates projects and curates artifacts
* **Stakeholder** — reviews and approves

---

## 🔄 Workflow

1. Create a project and add members.
2. Paste notes → AI suggests **draft requirements** → review and accept/reject.
3. Generate **user stories** from accepted requirements.
4. Produce **BRD/FRD PDFs** and **AI-generated diagrams** (sequence/ER/Class).
5. Request **approvals**; decisions are logged and emailed.
6. **Sync to Jira** and **share securely** via presigned links.
7. (Optional) Manage **subscriptions and payments**.

---

## 🚀 Key Features

* **Auth & access**: JWT, BCrypt passwords, role checks
* **Requirements & stories**: AI-assisted drafting with traceability
* **Documents**: Markdown → HTML → PDF; diagrams linked in
* **Diagrams**: AI-generated sequence & ER from text
* **Approvals**: stateful workflow with email notifications
* **Jira**: create boards/issues and sync stories
* **Billing (optional)**: subscriptions and one-off payments via Moyasar
* **Secure sharing**: time-limited AWS S3 presigned URLs

---

## 🧰 Tech Stack
| Area                        | Stack                                                                 |
|-----------------------------| --------------------------------------------------------------------- |
| **Core Framework**          | Spring Boot                                                           |
| **Backend**                 | Spring MVC (REST); Spring Data JPA; Hibernate; Spring Security; MySQL |
| **Security**                | JSON Web Tokens (JWT)                                                 |
| **Artificial Intelligence** | Spring AI (OpenAI models)                                             |
| **Document & PDF**          | Adobe PDF Services; OpenPDF; OpenHTMLToPDF; CommonMark (Markdown)     |
| **Cloud & Services**        | Amazon S3 (object storage); Spring Boot Mail (SMTP)                   |
| **APIs & HTTP**             | Unirest (HTTP client)                                                 |
| **Developer Tools**         | Lombok; Jackson; JSON.org; Maven                                      |
| **Testing**                 | JUnit 5; Mockito; AssertJ                                             |
| Deployment                  | AWS (EC2, RDS, S3, IAM for deployment & hosting)
---

## ⚙️ Configuration

Set the following environment variables (or `application.properties`):

```properties
# App
APP_ENV=prod
SERVER_PORT=8080
JWT_SECRET=your-256-bit-secret
JWT_TTL_SECONDS=3600

# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bacopilot
SPRING_DATASOURCE_USERNAME=youruser
SPRING_DATASOURCE_PASSWORD=yourpass

# OpenAI / Spring AI
SPRING_AI_OPENAI_API_KEY=sk-xxxx

# AWS S3
AWS_REGION=your-region
AWS_S3_BUCKET=your-bucket
AWS_S3_PRESIGNED_TTL_SECONDS=900

# Email (SMTP)
MAIL_HOST=smtp.example.com
MAIL_PORT=587
MAIL_USERNAME=noreply@example.com
MAIL_PASSWORD=secret
MAIL_FROM=noreply@example.com

# Jira
JIRA_BASE_URL=https://your-domain.atlassian.net
JIRA_EMAIL=you@example.com
JIRA_API_TOKEN=atlassian-api-token
JIRA_PROJECT_KEY=PROJ

# Moyasar (optional)
MOYASAR_API_KEY=pk_test_xxx
MOYASAR_ENDPOINT=https://api.moyasar.com
```

---

## ▶️ Run locally

```bash
# 1) Build
mvn clean package

# 2) Run
java -jar target/ba-copilot-*.jar
# or
mvn spring-boot:run
```

> Requires Java 17+ and a running MySQL instance.

---

## 📁 Project Structure (high level)

```
src/
 ├─ main/java/.../config        # Security, JWT, mail, S3, AI, etc.
 ├─ main/java/.../controllers   # REST controllers
 ├─ main/java/.../services      # Business logic (Requirements, Stories, Docs, Diagrams, Approvals, Jira, Payments)
 ├─ main/java/.../repositories  # Spring Data JPA repositories
 ├─ main/java/.../mappers       # DTO/entity mappers
 ├─ main/java/.../domain        # Entities & DTOs
 └─ main/resources              # Templates, configs
```

---

## 🔐 Security Notes

* Stateless **JWT** auth with **BCrypt** password hashing
* **Role-based checks** on project membership and mutations
* **Short-lived S3 presigned URLs** for artifact access

---

## ✅ Testing

```bash
mvn test
```

* Unit tests: **JUnit 5**, **Mockito**, **AssertJ**


---

# API Endpoints Documentation (with Person & Numbered)



### ApprovalController

| # | Method | Endpoint | Person |
|---|--------|----------|--------|
| 1 | POST | `/api/v1/approvals/send` | Ali    |
| 2 | GET | `/api/v1/approvals/pending` | Ali    |
| 3 | PUT | `/api/v1/approvals/approve` | Ali    |
| 4 | PUT | `/api/v1/approvals/reject` | Ali    |
| 5 | GET | `/api/v1/approvals/get` | Ali    |
| 6 | GET | `/api/v1/approvals/get/{id}` | Ali    |
| 7 | PUT | `/api/v1/approvals/update/{id}` | Ali    |
| 8 | DELETE | `/api/v1/approvals/delete/{id}` | Ali    |

### AuthController

| # | Method | Endpoint | Person |
|---|--------|----------|--------|
| 9 | POST | `/api/v1/auth/login` | Faisal |

### BAController

| # | Method | Endpoint | Person |
|---|--------|----------|--------|
| 10 | POST | `/api/v1/ba/register` | Faisal |
| 11 | GET | `/api/v1/ba/projects` | Faisal |
| 12 | PUT | `/api/v1/ba/update` | Faisal |
| 13 | DELETE | `/api/v1/ba/delete` | Faisal |
| 14 | GET | `/api/v1/ba/get` | Faisal |
| 15 | GET | `/api/v1/ba/get/{id}` | Faisal |

### DiagramController

| # | Method | Endpoint | Person |
|---|--------|----------|--------|
| 16 | POST | `/api/v1/diagrams/generate/class/{project_id}` | Faisal |
| 17 | POST | `/api/v1/diagrams/generate/sequence/{project_id}` | Faisal |
| 18 | POST | `/api/v1/diagrams/generate/erd/{project_id}` | Faisal |
| 19 | GET | `/api/v1/diagrams/get` | Ali    |
| 20 | GET | `/api/v1/diagrams/get/{id}` | Ali    |
| 21 | DELETE | `/api/v1/diagrams/delete/{id}` | Ali    |

### DocumentController

| # | Method | Endpoint | Person   |
|---|--------|----------|----------|
| 22 | POST | `/api/v1/documents/generate/brd/{project_id}` | Faisal   |
| 23 | POST | `/api/v1/documents/generate/frd/{project_id}` | Ali      |
| 24 | POST | `/api/v1/documents/generate/feasibility/{project_id}` | Abdullah |
| 25 | GET | `/api/v1/documents/get` | Abdullah |
| 26 | GET | `/api/v1/documents/get/{id}` | Abullah  |
| 27 | DELETE | `/api/v1/documents/delete/{id}` | Abdullah |

### JiraController

| # | Method | Endpoint | Person   |
|---|--------|----------|----------|
| 28 | POST | `/api/v1/jira/issue-from-userstory/{userStoryId}` | Ali      |
| 29 | POST | `/api/v1/jira/issue-from-project/{projectId}` | Ali      |
| 30 | POST | `/api/v1/jira/project/create/example` | Abdullah |

### PaymentController

| # | Method | Endpoint | Person   |
|---|--------|----------|----------|
| 31 | GET | `/api/v1/payment/status/{paymentId}` | Abdullah |
| 32 | POST | `/api/v1/payment/monthly` | Abdullah |
| 33 | POST | `/api/v1/payment/yearly` | Abdullah |
| 34 | GET | `/api/v1/payment/callback/yearly/{baID}` | Abdullah |
| 35 | GET | `/api/v1/payment/callback/monthly/{baID}` | Abdullah |
| 36 | GET | `/api/v1/payment/subscription/status` | Abdullah |
| 37 | PUT | `/api/v1/payment/subscription/cancel` | Abdullah |

### ProjectController

| # | Method | Endpoint | Person   |
|---|--------|----------|----------|
| 38 | GET | `/api/v1/projects/get` | Ali      |
| 39 | GET | `/api/v1/projects/get/{id}` | Ali      |
| 40 | PUT | `/api/v1/projects/update/{id}` | Ali      |
| 41 | POST | `/api/v1/projects/add` | Ali      |
| 42 | DELETE | `/api/v1/projects/delete/{id}` | Ali      |
| 43 | POST | `/api/v1/projects/{projectId}/market-benchmark` | Ali      |
| 44 | POST | `/api/v1/projects/assign/stakeholder/{stakeholder_id}/{project_id}` | Faisal   |
| 45 | POST | `/api/v1/projects/assign/ba/{ba_id}/{project_id}` | Faisal   |
| 46 | PUT | `/api/v1/projects/exit/ba/{project_id}` | Abdullah |
| 47 | PUT | `/api/v1/projects/exit/stakeholder/{project_id}` | Abdullah |
| 48 | PUT | `/api/v1/projects/reassign/owner/{project_id}/{nextOwnerId}` | Abdullah |
| 49 | POST | `/api/v1/projects/recommend-tools/{projectId}` | Ali      |

### RequirementController

| # | Method | Endpoint | Person |
|---|--------|----------|--------|
| 50 | GET | `/api/v1/requirement/get` | Faisal |
| 51 | GET | `/api/v1/requirement/draft/project/{project_id}` | Faisal |
| 52 | GET | `/api/v1/requirement/get/{id}` | Faisal |
| 53 | POST | `/api/v1/requirement/add` | Faisal |
| 54 | GET | `/api/v1/requirement/by-project/{project_id}` |   Faisal     |
| 55 | POST | `/api/v1/requirement/generate/{project_id}` |   Faisal     |
| 56 | GET | `/api/v1/requirement/draft/get/{draft_id}` |    Faisal    |
| 57 | POST | `/api/v1/requirement/draft/accept/{draft_id}` |  Faisal      |
| 58 | DELETE | `/api/v1/requirement/draft/reject/{draft_id}` |    Faisal    |
| 59 | PUT | `/api/v1/requirement/update/{id}` |   Faisal     |
| 60 | DELETE | `/api/v1/requirement/delete/{id}` |   Faisal     |

### StakeholderController

| # | Method | Endpoint | Person   |
|---|--------|----------|----------|
| 61 | GET | `/api/v1/stakeholder/get` | Abdullah |
| 62 | GET | `/api/v1/stakeholder/get/{id}` | Abdullah |
| 63 | POST | `/api/v1/stakeholder/register` | Abdullah |
| 64 | PUT | `/api/v1/stakeholder/update` | Abdullah |
| 65 | DELETE | `/api/v1/stakeholder/delete` | Abdullah |
| 66 | GET | `/api/v1/stakeholder/projects/{stakeholder_id}` | Abdullah |

### UserController

| # | Method | Endpoint | Person |
|---|--------|----------|--------|
| 67 | GET | `/api/v1/users/get` | Ali    |
| 68 | GET | `/api/v1/users/get/{id}` | Ali    |
| 69 | POST | `/api/v1/users/register` | Ali    |
| 70 | PUT | `/api/v1/users/update/{id}` | Ali    |
| 71 | DELETE | `/api/v1/users/delete/{id}` | Ali    |

### UserStoryController

| # | Method | Endpoint | Person |
|---|--------|----------|--------|
| 72 | GET | `/api/v1/user-stories/get` | Faisal |
| 73 | GET | `/api/v1/user-stories/get/{id}` | Faisal |
| 74 | POST | `/api/v1/user-stories/add` | Faisal |
| 75 | PUT | `/api/v1/user-stories/update/{id}` | Faisal |
| 76 | DELETE | `/api/v1/user-stories/delete/{id}` | Faisal |
| 77 | POST | `/api/v1/user-stories/generate/{requirement_id}` | Faisal |
| 78 | GET | `/api/v1/user-stories/draft/requirement/{requirement_id}` | Faisal |
| 79 | GET | `/api/v1/user-stories/draft/get/{draft_id}` | Faisal |
| 80 | POST | `/api/v1/user-stories/draft/accept/{draft_id}` | Faisal |
| 81 | DELETE | `/api/v1/user-stories/draft/reject/{draft_id}` | Faisal |

---
##  - Figma:
- https://www.figma.com/proto/Dhp3puaU3VGk5kQSQBtjrI/Java-Web-Final-Project?node-id=7-528

## - Postman Documentation:
- https://documenter.getpostman.com/view/32615945/2sB3HnJKaz

## - API URL:
- http://ba-copilot.eu-central-1.elasticbeanstalk.com

---
## - Class Diagram
- <img width="800" alt="BA Copilot - Class Diagram" src="https://github.com/user-attachments/assets/5d18b850-be4e-479d-99a2-585aa22cc45c" />


## - Use Case Diagram
- <img width="800" alt="BA Copilot - Use Case Diagram" src="https://github.com/user-attachments/assets/e899624d-0ffa-42fd-a4d7-ab15d71b4ce9" />


---

## 👤 Credits

Built by **Ali Abumansour**, **Faisal Almansour**, and **Abdullah AlWaal**.

