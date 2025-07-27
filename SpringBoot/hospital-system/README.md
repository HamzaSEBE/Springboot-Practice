# Hospital Management System

This is a simple hospital management system built with Spring Boot.

## Features

* Add new patients.
* View a list of patients.
* Secure patient data by encrypting phone numbers.
* User authentication and authorization (admin role).

## Getting Started

1.  **Prerequisites:**
    * Java 17 or later
    * Maven
    * MySQL database

2.  **Database Setup:**
    * Use the `docker-compose.yml` file to set up the MySQL database.
    * Ensure the database connection details in `SecurityConfig.java` are correct.
    * Create the `users` and `authorities` tables in the database (see `SecurityConfig.java` comments).
    * Insert the initial "admin" user (see `SecurityConfig.java` comments).
    * Set the `ENCRYPTION_KEY` environment variable.

3.  **Build and Run:**
    * Use Maven to build the project: `mvn clean install`
    * Run the application: `java -jar target/hospital-system-0.0.1-SNAPSHOT.jar`

## Security

* Phone numbers are encrypted using AES.
* User authentication is handled by Spring Security.
* Authorization is role-based (admin users have access to /admin/**).

## Further Improvements

* Implement more robust error handling.
* Add comprehensive logging.
* Create a detailed threat model and risk analysis document.
* Implement more granular authorization.
* Add unit and integration tests.
* Improve the user interface.
* /actuator/auditevents