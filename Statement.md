# Problem Statement

## Problem Statement

College course registration periods often involve hundreds of students trying to register for a limited number of seats in popular courses within a very short window of time. When multiple registration requests are processed at the same time without proper safeguards, it can lead to race conditions — for example, two students both being allocated the same last seat, resulting in over-enrollment and data inconsistency.

This project builds a **Concurrent Course Registration & Seat Allocation System** that solves this problem by handling multiple simultaneous registration attempts safely, ensuring that seat allocation is always accurate, no student is double-booked, and students who miss out on a seat are automatically placed on a waitlist.

## Scope of the Project

The system covers:
- Defining courses with a fixed number of seats
- Allowing multiple students to attempt registration concurrently
- Thread-safe seat allocation logic
- Waitlist management with automatic promotion when a seat is freed
- Persisting registration and course data using JDBC
- Exporting registration and seat-availability reports
- Automated testing of the concurrency guarantees

The scope does **not** include a graphical user interface or web front end — the system is a console-based demonstration of the core registration and concurrency logic, in line with the Core Java syllabus.

## Target Users

- **Students** — represented as concurrent registration requests (simulated via threads) attempting to secure a seat in a course
- **Administrators** — responsible for defining courses and the number of available seats (modeled in the system, extendable to an interactive admin flow)
- **College evaluators / instructors** — reviewing the project to see a correct, real-world application of Java concurrency, OOP, exception handling, collections, and JDBC

## High-Level Features

1. Course and seat setup with a fixed capacity
2. Concurrent student registration simulation using multiple threads
3. Thread-safe seat allocation (no double-booking, verified via unit tests)
4. Custom exceptions for duplicate registration and seat unavailability
5. Waitlist with automatic promotion on seat availability
6. Persistent storage of registrations and courses via JDBC (SQLite)
7. CSV report export for registrations and seat availability
