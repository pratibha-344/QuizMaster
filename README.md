# QuizMaster – Online Quiz Platform

A Java MVC quiz application for students and quiz administrators. It uses JSP for pages, Jakarta Servlets for requests and sessions, JDBC DAOs for MySQL, and Maven to build and run.

## What works

- Student registration, login, logout, dashboard, profile editing, and role-protected pages.
- Published quizzes, multiple-choice questions, a server-enforced countdown, automatic scoring, saved results, result history, and leaderboard.
- Admin dashboard, quiz create/edit/delete, question create/edit/delete, student list, and result list.
- Passwords are stored as salted PBKDF2 hashes. Forms include CSRF protection, user text is escaped in JSPs, and database operations use prepared statements.
- The first three sample quizzes, sample accounts, and questions are loaded by `database.sql`.

## Requirements

- Java 17 or newer
- Maven 3.8 or newer
- MySQL 8.0 or newer

## 1. Create the database

Start MySQL, then run the included script from a terminal:

```bash
mysql -u root -p < database.sql
```

The script creates the `quizmaster` database, its four tables, sample accounts, and three sample quizzes.

If your MySQL root account has no password, press Enter at the prompt. Otherwise, enter your own MySQL password. The sample QuizMaster login passwords are separate from your MySQL password.

### Running the demo in this Replit workspace

The **QuizMaster MySQL** workflow starts a local MySQL 8.4 server, applies `database.sql`, and stores its data in the ignored `quizmaster/.mysql-data/` directory. The server listens only on `127.0.0.1`; this empty-password root account is for this isolated development workspace only, not production use. Keep the MySQL workflow running while using QuizMaster.

## 2. Set the database connection

The defaults are:

```text
JDBC_URL=jdbc:mysql://127.0.0.1:3306/quizmaster?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=
```

Set `DB_USER` and `DB_PASSWORD` in your terminal if your local MySQL account uses different credentials. Do not put a real database password in source code or commit it to version control.

Linux/macOS:

```bash
export DB_USER=root
export DB_PASSWORD='your-mysql-password'
```

Windows PowerShell:

```powershell
$env:DB_USER = "root"
$env:DB_PASSWORD = "your-mysql-password"
```

## 3. Run QuizMaster

From this folder:

```bash
mvn clean package exec:java
```

Open [http://localhost:8080](http://localhost:8080). Use `Ctrl+C` in the terminal to stop the app.

If port 8080 is already in use, set `PORT` before starting. For example, on Linux/macOS:

```bash
PORT=8081 mvn clean package exec:java
```

## Sample QuizMaster accounts

| Role | Email | Password |
| --- | --- | --- |
| Admin | `admin@quizmaster.com` | `admin123` |
| Student | `student@quizmaster.com` | `student123` |

Change or remove these demo accounts before using this project with real student data.

## Project structure

```text
src/main/java/com/quizmaster/
  dao/          JDBC data access
  filter/       Login, role, and CSRF protection
  model/        Domain objects
  service/      Validation, quiz attempts, and scoring
  servlet/      MVC controllers
  util/         Database, password, and web helpers
src/main/webapp/
  assets/       Responsive CSS and browser-side quiz timer
  WEB-INF/views JSP views (not directly accessible by URL)
database.sql    MySQL database and demo data
pom.xml         Maven build and server dependencies
```

## Troubleshooting

- **Cannot connect to MySQL:** Make sure the MySQL server is running, `quizmaster` was created with `database.sql`, and `DB_USER` / `DB_PASSWORD` match a MySQL account that can access the database.
- **Unknown database:** Re-run `mysql -u root -p < database.sql`.
- **Address already in use:** Choose an unused port with `PORT=8081`.
- **Maven cannot download dependencies:** Check your internet connection and run the Maven command again.

The app does not create its database automatically. It reports a helpful database error rather than silently switching to another database.
