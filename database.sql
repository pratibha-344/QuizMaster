CREATE DATABASE IF NOT EXISTS quizmaster
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE quizmaster;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS quizzes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(160) NOT NULL,
  description TEXT NOT NULL,
  time_limit_minutes INT NOT NULL DEFAULT 15,
  is_published BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_quiz_time_limit CHECK (time_limit_minutes BETWEEN 1 AND 240)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS questions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  quiz_id BIGINT NOT NULL,
  question_text TEXT NOT NULL,
  option_a VARCHAR(500) NOT NULL,
  option_b VARCHAR(500) NOT NULL,
  option_c VARCHAR(500) NOT NULL,
  option_d VARCHAR(500) NOT NULL,
  correct_option CHAR(1) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_correct_option CHECK (correct_option IN ('A', 'B', 'C', 'D')),
  CONSTRAINT fk_questions_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  INDEX idx_questions_quiz_id (quiz_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS results (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  quiz_id BIGINT NOT NULL,
  score INT NOT NULL,
  total_questions INT NOT NULL,
  duration_seconds INT NOT NULL DEFAULT 0,
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_result_score CHECK (score >= 0 AND score <= total_questions),
  CONSTRAINT fk_results_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_results_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  INDEX idx_results_user_submitted (user_id, submitted_at),
  INDEX idx_results_quiz_score (quiz_id, score)
) ENGINE=InnoDB;

INSERT INTO users (id, full_name, email, password_hash, role)
VALUES
  (1, 'QuizMaster Admin', 'admin@quizmaster.com',
   'pbkdf2_sha256$120000$UXVpek1hc3RlckFkbWluU2VlZFNhbHQx$CQnBgSJyjaPsqunA5xyTzFi-MpYAseaET4zMK7poh4I', 'ADMIN'),
  (2, 'Sample Student', 'student@quizmaster.com',
   'pbkdf2_sha256$120000$UXVpek1hc3RlclN0dWRlbnRTZWVkU2FsdDE$z4kKSdgEmmG-kHK3bxM2F2bLVED4B-xqUkbmvBDdD9M', 'STUDENT')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO quizzes (id, title, description, time_limit_minutes, is_published)
VALUES
  (1, 'Java Fundamentals', 'Test your understanding of Java syntax, classes, and core language concepts.', 10, TRUE),
  (2, 'Database Essentials', 'Review relational databases, SQL, keys, and prepared statements.', 10, TRUE),
  (3, 'Web Technology Basics', 'A quick review of HTML, CSS, JavaScript, and HTTP.', 10, TRUE)
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO questions
  (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
  (1, 1, 'Which keyword is used to create a subclass in Java?', 'implements', 'extends', 'inherits', 'instanceof', 'B'),
  (2, 1, 'Which method is the entry point of a standard Java application?', 'start()', 'run()', 'main()', 'init()', 'C'),
  (3, 1, 'Which type stores a true or false value?', 'boolean', 'char', 'String', 'double', 'A'),
  (4, 1, 'What does the "new" keyword do?', 'Deletes an object', 'Creates an object', 'Imports a package', 'Ends a method', 'B'),
  (5, 2, 'Which SQL command retrieves rows from a table?', 'FETCH', 'READ', 'SELECT', 'OPEN', 'C'),
  (6, 2, 'What is a primary key used for?', 'Encrypting a table', 'Uniquely identifying a row', 'Sorting every query', 'Connecting to a server', 'B'),
  (7, 2, 'Which JDBC feature helps bind values safely into SQL?', 'PreparedStatement', 'ResultSetMetaData', 'DriverManager', 'Savepoint', 'A'),
  (8, 2, 'Which normal form first requires each field to hold one value?', 'First normal form', 'Second normal form', 'Third normal form', 'Boyce-Codd normal form', 'A'),
  (9, 3, 'What does HTML primarily describe?', 'Page structure', 'Database indexes', 'Server hardware', 'Network routing', 'A'),
  (10, 3, 'Which CSS property changes text color?', 'font-style', 'background-image', 'color', 'text-decoration', 'C'),
  (11, 3, 'Which HTTP method is commonly used to retrieve a page?', 'POST', 'GET', 'PATCH', 'DELETE', 'B'),
  (12, 3, 'Which language adds browser-side interactivity?', 'SQL', 'Java', 'CSS', 'JavaScript', 'D')
ON DUPLICATE KEY UPDATE id = id;
