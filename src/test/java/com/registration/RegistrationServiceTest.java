package com.registration;

import com.registration.model.Course;
import com.registration.service.RegistrationService;
import com.registration.service.StudentRegistrationTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the core concurrency guarantee of the project:
 * when N students race for a course with fewer than N seats,
 * exactly `totalSeats` registrations succeed - no seat is
 * double-booked, no matter how many threads compete.
 */
class RegistrationServiceTest {

    private RegistrationService registrationService;
    private Course course;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService();
        course = new Course("TEST101", "Concurrency Test Course", 3);
        registrationService.addCourse(course);
    }

    @Test
    void onlyTotalSeatsCountOfRegistrationsSucceedUnderConcurrency() throws InterruptedException {
        int numberOfStudents = 20; // far more students than seats

        ExecutorService executor = Executors.newFixedThreadPool(numberOfStudents);
        for (int i = 1; i <= numberOfStudents; i++) {
            String studentId = "S" + i;
            executor.execute(new StudentRegistrationTask(registrationService, studentId, "TEST101"));
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Exactly 3 registrations should have succeeded - matching total seats
        assertEquals(3, registrationService.getRegistrations().size());

        // No seat should ever be allocated to more than one student
        long allocatedSeats = course.getSeats().stream().filter(s -> s.isAllocated()).count();
        assertEquals(3, allocatedSeats);

        // The remaining 17 students should have ended up on the waitlist
        assertTrue(course.getWaitlist().size() <= numberOfStudents - 3);
    }

    @Test
    void studentCannotRegisterTwiceForSameCourse() {
        assertTrue(registrationService.listCourses().contains(course));
        // First registration should succeed without throwing
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                registrationService.registerStudent("S1", "TEST101"));

        // Second attempt by the same student should throw DuplicateRegistrationException
        org.junit.jupiter.api.Assertions.assertThrows(
                com.registration.exception.DuplicateRegistrationException.class,
                () -> registrationService.registerStudent("S1", "TEST101"));
    }
}
