# QuizMaster – Online Quiz Platform

**Learn. Play. Improve.**

QuizMaster is a Java-based web application for conducting multiple-choice quizzes. It provides separate Student and Admin workflows. Students can take published quizzes and review results, while administrators can manage quizzes and questions.

> **Project status:** College review/demo project. The application runs locally; it is not deployed as a public live website.

**GitHub repository:** https://github.com/pratibha-344/QuizMaster

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Objectives](#2-objectives)
3. [Features](#3-features)
4. [Technology Stack and Dependencies](#4-technology-stack-and-dependencies)
5. [Application Flow](#5-application-flow)
6. [Architecture](#6-architecture)
7. [Database Design](#7-database-design)
8. [Requirements](#8-requirements)
9. [Setup and Run in VS Code](#9-setup-and-run-in-vs-code)
10. [Demo Login Accounts](#10-demo-login-accounts)
11. [Project Structure](#11-project-structure)
12. [Important Configuration Details](#12-important-configuration-details)
13. [Troubleshooting](#13-troubleshooting)
14. [Limitations and Future Enhancements](#14-limitations-and-future-enhancements)

---

## 1. Project Overview

QuizMaster is designed to make quiz management and quiz attempts easier to organize. It uses a Java MVC-style structure, JSP pages for the interface, Jakarta Servlets for request handling, JDBC for database access, and MySQL for persistent data.

The project is intended for learning and college demonstration. It can be run on a developer's own computer after installing the required tools and configuring the database.

## 2. Objectives

- Provide separate access for students and administrators.
- Allow administrators to create, update, publish, and manage quizzes and questions.
- Let students attempt published multiple-choice quizzes.
- Calculate and save quiz results automatically.
- Let students review their previous attempts.
- Store application data in a relational database.

## 3. Features

### Student features
- Student registration, login, and logout.
- Student dashboard and profile editing.
- View published quizzes.
- Attempt multiple-choice questions with a countdown timer.
- Submit answers and receive an automatically calculated score.
- View saved results, attempt history, and leaderboard.

### Admin features
- Admin login and protected admin area.
- Create, edit, and delete quizzes.
- Create, edit, and delete questions and answer options.
- Publish quizzes for students.
- View student and result lists.

### Security-related implementation
- Passwords are stored as salted PBKDF2 hashes rather than plain-text passwords.
- Forms include CSRF protection.
- JSP output escapes user-provided text.
- Database access uses prepared statements.
- Login and role checks protect restricted areas.

These controls are included for the project demo; the application has not been independently security-audited and should not be treated as production-ready.

## 4. Technology Stack and Dependencies

| Technology / package | Version in project | Purpose |
|---|---|---|
| Java | 17 language target | Application code |
| Maven | 3.8+ recommended | Dependency management and build |
| JSP | Provided through embedded Tomcat/Jasper | Server-rendered web pages |
| Jakarta Servlet API | 6.0.0 | HTTP request/response handling |
| Apache Tomcat Embed Core | 10.1.44 | Runs the web application locally |
| Apache Tomcat Embed Jasper | 10.1.44 | JSP compilation and rendering |
| Apache Tomcat Embed EL | 10.1.44 | Expression Language support |
| JSTL implementation | 3.0.1 | JSP tag-library support |
| JSTL API | 3.0.0 | JSP standard tag API |
| MySQL | 8.0+ recommended | Persistent storage |
| MySQL Connector/J | 9.3.0 | Java-to-MySQL JDBC driver |
| JDBC / DAO pattern | Java SQL API | Database operations and data access |
| HTML, CSS, JavaScript | Browser technologies | Page layout, styling, and client-side timer |

The exact dependency declarations and versions are maintained in [`pom.xml`](pom.xml).

## 5. Application Flow

### Student workflow
1. A student registers or logs in.
2. The application validates the account and opens the student area.
3. The student chooses a published quiz.
4. The application displays its multiple-choice questions and countdown.
5. The student submits answers; the server validates the attempt and calculates the score.
6. The result is stored in MySQL and can be viewed later in the student's results/history.

### Admin workflow
1. The administrator logs in with an Admin account.
2. The application opens the protected admin area.
3. The administrator creates a quiz and adds questions with answer options.
4. The administrator publishes the quiz.
5. The published quiz becomes available to students.
6. The administrator can review student and result lists.

### Simplified request flow

```text
Student / Admin
      |
      v
JSP pages (user interface)
      |
      v
Jakarta Servlets (request handling and sessions)
      |
      v
Services / validation / scoring
      |
      v
DAO classes using JDBC
      |
      v
MySQL database
```

## 6. Architecture

QuizMaster follows a layered Java web application structure:

- **View layer:** JSP pages and web assets display the interface.
- **Controller layer:** Jakarta Servlets receive requests and coordinate application actions.
- **Service layer:** validation, quiz attempt handling, and scoring logic.
- **DAO layer:** JDBC-based classes read and write database records.
- **Model layer:** Java classes represent application data.
- **Filter layer:** login, role, and CSRF checks.
- **Database layer:** MySQL stores users, quizzes, questions, and results.

The project is packaged as a WAR file by Maven. The run command below launches the application using the embedded Tomcat configuration in the project; a separate Tomcat installation is not required for this run method.

## 7. Database Design

The SQL setup file is [`database.sql`](database.sql). It creates the `quizmaster` database and four core tables.

| Table | Purpose | Important fields / relationships |
|---|---|---|
| `users` | Stores accounts and roles | `id` primary key, `full_name`, unique `email`, `password_hash`, `role` (`STUDENT` or `ADMIN`) |
| `quizzes` | Stores quiz details and publication state | `id` primary key, `title`, `description`, `time_limit_minutes`, `is_published` |
| `questions` | Stores questions and four answer options | `id` primary key, `quiz_id` foreign key, `question_text`, options A–D, `correct_option` |
| `results` | Stores submitted attempts and scores | `id` primary key, `user_id` and `quiz_id` foreign keys, `score`, `total_questions`, `duration_seconds`, `submitted_at` |

### Relationships
- One quiz can contain multiple questions.
- Each question belongs to a quiz.
- One student can have multiple result records.
- Each result belongs to one user and one quiz.
- Foreign keys maintain the links between related records.

### Sample data
The script inserts two demo accounts and three sample quizzes with questions. The sample inserts use `ON DUPLICATE KEY UPDATE` so rerunning the script does not intentionally duplicate records with the same primary keys.

## 8. Requirements

Install the following before running the project:

- Java JDK 17 or newer.
- Maven 3.8 or newer.
- MySQL Server 8.0 or newer.
- Git.
- Visual Studio Code (recommended editor).
- Internet access for Maven to download dependencies on the first build.

Verify Java and Maven in the VS Code terminal:

```bash
java -version
mvn -version
git --version
```

The `mysql` client command must also be available in the terminal to import the database script. If it is not available, configure the MySQL command-line client/PATH for your operating system or import `database.sql` using MySQL Workbench.

## 9. Setup and Run in VS Code

These commands are for a local development/demo setup. Database credentials differ by computer; do not copy another person's MySQL password.

### Clone the repository

Open VS Code, select **Terminal → New Terminal**, and run:

```bash
git clone https://github.com/pratibha-344/QuizMaster.git
cd QuizMaster
code .
```

If `code .` is not available, open the cloned `QuizMaster` folder using **File → Open Folder** in VS Code.

If you have already cloned the repository, do not clone it again each time. Open the existing folder and use `git pull` only when you need updates from GitHub.

### Create and seed the database

1. Start your local MySQL Server.
2. Open a terminal in the repository root (the folder containing `database.sql`).
3. Import the SQL file.

**macOS / Linux terminal:**

```bash
mysql -u root -p < database.sql
```

**Windows PowerShell:**

```powershell
Get-Content .\database.sql | mysql -u root -p
```

When prompted, enter the password for your local MySQL `root` account. If that account has no password, follow the prompt for your local setup.

Alternatively, open `database.sql` in MySQL Workbench and execute the script using a connection to your local MySQL Server.

### Configure database credentials

By default, the application uses:

```text
JDBC_URL=jdbc:mysql://127.0.0.1:3306/quizmaster?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=
```

The application reads these environment variables in `src/main/java/com/quizmaster/util/Database.java`. If your MySQL account uses a password, set `DB_USER` and `DB_PASSWORD` in the VS Code terminal before starting the app.

**macOS / Linux (bash or zsh):**

```bash
export DB_USER=root
export DB_PASSWORD='your-local-mysql-password'
```

**Windows PowerShell:**

```powershell
$env:DB_USER = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
```

Replace the example value with your own local MySQL password. Do not commit real passwords to GitHub. These environment variables apply to the current terminal session; set them again in a new terminal if needed.

If your database uses a different host, port, or database name, set `JDBC_URL` to the correct JDBC URL as well.

### Run the application

Make sure MySQL is running, then from the project root run:

```bash
mvn clean package exec:java
```

Maven builds the WAR and launches the embedded Tomcat server. Keep this terminal open while using the website.

Open the URL printed by the application. The default is:

**http://localhost:8080**

To stop the application, press **Ctrl+C** in the terminal.

### Everyday run command

After the initial clone, prerequisite installation, database import, and credential setup, open the existing project folder in VS Code and run:

```bash
mvn clean package exec:java
```

Do not clone the repository or import the SQL file every time. Re-import `database.sql` only when you intentionally need to initialize/reset the demo database; take care because existing records may be affected by schema changes.

## 10. Demo Login Accounts

These accounts are inserted by `database.sql` for demonstration:

| Role | Email | Password |
|---|---|---|
| Admin | `admin@quizmaster.com` | `admin123` |
| Student | `student@quizmaster.com` | `student123` |

Use these credentials only with a local demo database initialized from the supplied SQL script. They are public sample credentials, not private accounts. Do not use them for real student data or a production deployment.

## 11. Project Structure

```text
QuizMaster/
├── src/
│   └── main/
│       ├── java/com/quizmaster/
│       │   ├── dao/          # JDBC data access objects
│       │   ├── filter/       # Authentication, role and CSRF filters
│       │   ├── model/        # Application/domain models
│       │   ├── service/      # Validation, quiz attempts and scoring
│       │   ├── servlet/      # Web controllers
│       │   ├── util/         # Database and helper classes
│       │   └── Server.java   # Embedded Tomcat launcher
│       └── webapp/
│           ├── assets/       # CSS and browser-side assets
│           └── WEB-INF/views/# JSP views
├── scripts/                  # Development/setup helper scripts
├── database.sql              # Schema and sample data
├── pom.xml                   # Maven build and dependencies
├── README.md                 # Project documentation
└── .gitignore                # Excludes generated/local files
```

Generated build output is placed in `target/` and should not be committed.

## 12. Important Configuration Details

- **Database connection code:** `src/main/java/com/quizmaster/util/Database.java`
- **Default database:** `quizmaster`
- **Default database host/port:** `127.0.0.1:3306`
- **Default app port:** `8080`; the `PORT` environment variable can override it.
- **Build output:** `target/quizmaster.war`
- **Main launcher:** `com.quizmaster.Server`
- **Maven configuration:** `pom.xml`
- **Schema and demo seed data:** `database.sql`

The application expects the database to be created before it starts. It does not automatically create the database if it is missing.

## 13. Troubleshooting

| Problem | What to check |
|---|---|
| `mvn: command not found` | Install Maven and ensure it is on `PATH`; reopen the terminal after installation. |
| `java: command not found` or wrong Java version | Install JDK 17+ and verify with `java -version` and `mvn -version`. |
| `mysql: command not found` | Install/configure the MySQL command-line client or import the SQL through MySQL Workbench. |
| Cannot connect to MySQL | Confirm MySQL Server is running and the JDBC URL, username, and password match your local setup. |
| `Unknown database 'quizmaster'` | Run the database import steps from the repository root. |
| Access denied for MySQL user | Check your local `DB_USER` and `DB_PASSWORD` values and database privileges. |
| Port 8080 already in use | Choose another free port, for example `PORT=8081 mvn clean package exec:java` on macOS/Linux or `$env:PORT="8081"; mvn clean package exec:java` in PowerShell. Then open `http://localhost:8081`. |
| Maven dependency download fails | Check internet access and rerun the Maven command. |
| Login does not accept demo account | Ensure the local database was initialized with the included `database.sql` and that the sample users exist. |

## 14. Limitations and Future Enhancements

### Current scope
- Local application run for review/demo.
- Student and Admin workflows.
- Quiz creation, publishing, attempts, scoring, and saved results.
- MySQL-backed persistence.

### Possible future enhancements
- Deployment to a suitable Java hosting environment with a managed database.
- Automated unit and integration tests.
- More detailed reporting and administrative analytics.
- Production-oriented secret management, security review, and operational monitoring.
- Further accessibility and responsive-interface testing.

---

## Author / Project

**Project:** QuizMaster – Online Quiz Platform  
**Repository:** https://github.com/pratibha-344/QuizMaster  
**Purpose:** College project review and demonstration
