CREATE DATABASE IF NOT EXISTS SMS CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE USER 'sms'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON sms.* TO 'sms'@'localhost' WITH GRANT OPTION;

USE SMS;

CREATE TABLE USERS(
	USER_NAME VARCHAR(10) NOT NULL PRIMARY KEY,
	USER_PASS VARCHAR(255) NOT NULL,
	PERSON_ID INT
);

CREATE TABLE USER_ROLES(
	USER_NAME VARCHAR(10) NOT NULL,
	ROLE_NAME VARCHAR(50) NOT NULL,
	PRIMARY KEY (USER_NAME, ROLE_NAME)
);

CREATE TABLE IF NOT EXISTS DEPARTMENTS(
	DEPARTMENT_ID int AUTO_INCREMENT PRIMARY KEY,
	TITLE VARCHAR(255) NOT NULL,
	SEMESTERS INT NOT NULL DEFAULT 8,
	GRADUATION_UNITS INT NOT NULL DEFAULT 58
);

CREATE TABLE IF NOT EXISTS COURSES(
	COURSE_ID int AUTO_INCREMENT PRIMARY KEY,
	TITLE VARCHAR(255) NOT NULL,
	DEPARTMENT_ID int NOT NULL,
	SEMESTER VARCHAR(1) NOT NULL,
	REQUIRED TINYINT(1) NOT NULL DEFAULT 1,
	DIDACTIC_UNITS INT NOT NULL DEFAULT 1,
	FOREIGN KEY (DEPARTMENT_ID) REFERENCES DEPARTMENTS(DEPARTMENT_ID)
);

CREATE TABLE IF NOT EXISTS EMPLOYEES(
	EMPLOYEE_ID int AUTO_INCREMENT PRIMARY KEY,
	FIRST_NAME VARCHAR(255) NOT NULL,
	LAST_NAME VARCHAR(255) NOT NULL,
	GENDER_CODE VARCHAR(1) NOT NULL,
	DESIGNATION VARCHAR(255) NOT NULL,
	DEPARTMENT_ID INT,
	ACTIVE_FLAG TINYINT(1) NOT NULL,
	BIRTH_DATE DATE,
	OFFICE_PHONE_NUMBER VARCHAR(20),
	MOBILE_NUMBER VARCHAR(20),
	HIRE_DATE DATE,
	ADDRESS VARCHAR(255),
	FOREIGN KEY (DEPARTMENT_ID) REFERENCES DEPARTMENTS(DEPARTMENT_ID)
);

CREATE TABLE IF NOT EXISTS COURSE_TEACHERS(
	COURSE_ID int NOT NULL,
	EMPLOYEE_ID int NOT NULL,
	PRIMARY KEY (COURSE_ID, EMPLOYEE_ID)
);

ALTER TABLE COURSE_TEACHERS
ADD FOREIGN KEY (COURSE_ID)
REFERENCES COURSES(COURSE_ID)
ON DELETE CASCADE;

ALTER TABLE COURSE_TEACHERS
ADD FOREIGN KEY (EMPLOYEE_ID)
REFERENCES EMPLOYEES(EMPLOYEE_ID)
ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS STUDENTS(
	STUDENT_ID int AUTO_INCREMENT PRIMARY KEY,
	CODE VARCHAR(50) NOT NULL,
	FIRST_NAME VARCHAR(255) NOT NULL,
	LAST_NAME VARCHAR(255) NOT NULL,
	GENDER_CODE VARCHAR(1) NOT NULL,
	ENTRY_DATE DATE NOT NULL,
	ACTIVE_FLAG TINYINT(1) NOT NULL,
	DEPARTMENT_ID int NOT NULL,
	BIRTH_DATE DATE,
	BIRTH_CITY_NAME VARCHAR(255),
	BIRTH_COUNTRY_CODE VARCHAR(10),
	MOBILE_NUMBER VARCHAR(20),
	ADDRESS VARCHAR(255),
	MOTHER_NAME VARCHAR(255),
	FATHER_NAME VARCHAR(255),
	FOREIGN KEY (DEPARTMENT_ID) REFERENCES DEPARTMENTS(DEPARTMENT_ID)
);

CREATE TABLE COURSE_ENROLLMENTS (
	STUDENT_ID int NOT NULL,
	COURSE_ID int NOT NULL,
	PRIMARY KEY (STUDENT_ID, COURSE_ID),
	FOREIGN KEY (STUDENT_ID) REFERENCES STUDENTS(STUDENT_ID),
	FOREIGN KEY (COURSE_ID) REFERENCES COURSES(COURSE_ID)
);

CREATE TABLE GRADES (
	RECORD_ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	STUDENT_ID int NOT NULL,
	COURSE_ID int NOT NULL,
	EXAM_PERIOD DATE NOT NULL,
	GRADE DECIMAL(2,1),
	FOREIGN KEY (STUDENT_ID) REFERENCES STUDENTS(STUDENT_ID),
	FOREIGN KEY (COURSE_ID) REFERENCES COURSES(COURSE_ID)
);