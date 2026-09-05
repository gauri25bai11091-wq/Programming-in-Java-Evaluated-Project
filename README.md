# Concurrent Course Registration & Seat Allocation System

A Java-based console application that simulates real-world course registration, where multiple students may try to register for the same course at the same time. The system guarantees that seats are never double-booked, even under heavy concurrent load, by using thread-safe (synchronized) seat allocation.

## Overview

Traditional course registration systems can suffer from race conditions when many students try to grab the same limited seats at once. This project demonstrates how Java's concurrency tools (`synchronized`, `ExecutorService`, `Runnable`) can be used to build a registration engine that stays correct no matter how many students register simultaneously, while also persisting data through JDBC and exporting reports via file I/O.

## Features

- **Course & Seat Management** — define courses with a fixed number of seats
- **Concurrent Registration Engine** — multiple students (threads) can attempt to register for the same course at once; seat allocation is thread-safe
- **Waitlist Management** — students who miss out on a seat are automatically waitlisted, and auto-promoted when a seat becomes free
- **Custom Exception Handling** — `SeatUnavailableException` and `DuplicateRegistrationException` for clear, meaningful error handling
- **Persistent Storage (JDBC)** — courses and registrations are saved to a SQLite database
- **Report Export (File I/O)** — registration and seat-availability reports are exported as CSV files
- **Unit Tested** — JUnit 5 tests verify that seat allocation remains correct under concurrent access

## Technologies / Tools Used

- Java 17+ (Core Java, OOP, Multithreading, Collections)
- JDBC with SQLite (`sqlite-jdbc`)
- Maven (build & dependency management)
- JUnit 5 (unit testing)


## Project Structure

```
CourseRegistrationSystem/
│
├── pom.xml
│
├── src/
│   ├── main/java/com/registration/
│   │   ├── Main.java
│   │   │
│   │   ├── model/
│   │   │   ├── Student.java
│   │   │   ├── Admin.java
│   │   │   ├── Course.java
│   │   │   ├── Seat.java
│   │   │   └── Registration.java
│   │   │
│   │   ├── exception/
│   │   │   ├── SeatUnavailableException.java
│   │   │   └── DuplicateRegistrationException.java
│   │   │
│   │   ├── service/
│   │   │   ├── RegistrationService.java
│   │   │   └── StudentRegistrationTask.java
│   │   │
│   │   ├── db/
│   │   │   └── DBConnector.java
│   │   │
│   │   └── util/
│   │       └── ReportExporter.java
│   │
│   └── test/java/com/registration/
│       └── RegistrationServiceTest.java
│
└── screenshots/
    ├── registration_output.png
    └── test_results.png
```


## Steps to Install & Run the Project

### Prerequisites
- Java JDK 17 or higher installed
- Maven (or an IDE like VS Code / IntelliJ with Maven support)

### 1. Clone the repository
```bash
git clone https://github.com/gauri25bai11091-wq/Programming-in-Java-Evaluated-Project.git
cd Programming-in-Java-Evaluated-Project
```

### 2. Open in your IDE
Open the folder in VS Code or IntelliJ IDEA as a **Maven project**. Dependencies (SQLite JDBC driver, JUnit 5) will be downloaded automatically from `pom.xml`.

### 3. Run the application
Open `src/main/java/com/registration/Main.java` and run the `main` method.

This will:
- Set up sample courses with limited seats
- Simulate multiple students registering concurrently
- Print registration results (SUCCESS / WAITLISTED) to the console
- Save all data to a local SQLite database (`registration.db`)
- Export CSV reports (`registration_report.csv`, `seat_availability_report.csv`)

## Instructions for Testing

Open `src/test/java/com/registration/RegistrationServiceTest.java` and run the tests (via your IDE's "Run Test" option, or `mvn test` from the terminal).

The tests verify:
- Exactly `totalSeats` registrations succeed when more students than seats attempt to register concurrently (no double-booking)
- A student cannot register twice for the same course

## Screenshots

#### Registration output
<img width="975" height="426" alt="image" src="https://github.com/user-attachments/assets/8d4b2a41-1401-4304-a47a-5d59c4c52767" />


#### Concurrent Registration
<img width="975" height="691" alt="image" src="https://github.com/user-attachments/assets/6e0dc84c-952f-4d70-8b56-5e4bb93ae6b1" />



## Future Enhancements

- Add a simple CLI menu for interactive use
- Support course capacity changes and multiple admins
- Add a REST API layer for web-based access
