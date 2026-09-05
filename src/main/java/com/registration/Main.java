package com.registration;

import com.registration.db.DBConnector;
import com.registration.model.Course;
import com.registration.service.RegistrationService;
import com.registration.service.StudentRegistrationTask;
import com.registration.util.ReportExporter;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Entry point that demonstrates the Concurrent Course Registration
 * and Seat Allocation System end to end:
 *   1. Set up courses with limited seats
 *   2. Fire multiple student registration threads at the same course
 *   3. Show that seat allocation stays correct under concurrency
 *   4. Persist data via JDBC
 *   5. Export reports via file I/O
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        RegistrationService registrationService = new RegistrationService();

        // 1. Set up a course with only 3 seats but 6 competing students
        Course javaCourse = new Course("CSE101", "Core Java Programming", 3);
        registrationService.addCourse(javaCourse);

        Course dbCourse = new Course("CSE102", "Database Systems", 2);
        registrationService.addCourse(dbCourse);

        // 2. Create a thread pool to simulate many students registering at once
        ExecutorService executor = Executors.newFixedThreadPool(6);

        String[] studentIds = {"S1", "S2", "S3", "S4", "S5", "S6"};

        System.out.println("=== Simulating concurrent registration for CSE101 (3 seats, 6 students) ===");
        for (String studentId : studentIds) {
            executor.execute(new StudentRegistrationTask(registrationService, studentId, "CSE101"));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println();
        System.out.println("Final seat status for CSE101: " + javaCourse);
        System.out.println("Waitlist for CSE101: " + javaCourse.getWaitlist());

        // 3. Simulate a drop -> waitlisted student should be auto-promoted
        System.out.println();
        System.out.println("=== S1 drops CSE101 ===");
        registrationService.dropCourse("S1", "CSE101");
        System.out.println("Seat status after drop: " + javaCourse);
        System.out.println("Waitlist after promotion: " + javaCourse.getWaitlist());

        // 4. Persist everything via JDBC
        DBConnector db = new DBConnector();
        try {
            db.connect();
            db.saveCourse(javaCourse.getCourseId(), javaCourse.getCourseName(), javaCourse.getTotalSeats());
            db.saveCourse(dbCourse.getCourseId(), dbCourse.getCourseName(), dbCourse.getTotalSeats());
            for (var registration : registrationService.getRegistrations()) {
                db.saveRegistration(registration);
            }
            System.out.println();
            System.out.println("All registrations saved to database (registration.db)");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } finally {
            try {
                db.close();
            } catch (SQLException ignored) {
            }
        }

        // 5. Export reports using File I/O
        ReportExporter exporter = new ReportExporter();
        exporter.exportRegistrationReport("registration_report.csv", registrationService.getRegistrations());
        exporter.exportSeatAvailabilityReport("seat_availability_report.csv", registrationService.listCourses());
    }
}
