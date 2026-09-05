package com.registration.service;

import com.registration.exception.DuplicateRegistrationException;
import com.registration.exception.SeatUnavailableException;
import com.registration.model.Registration;

/**
 * Represents one student's registration attempt, run on its own thread.
 * This is what makes the registration process "concurrent" - many of
 * these tasks are submitted at once, all racing to grab seats in the
 * same course.
 */
public class StudentRegistrationTask implements Runnable {

    private final RegistrationService registrationService;
    private final String studentId;
    private final String courseId;

    public StudentRegistrationTask(RegistrationService registrationService,
                                    String studentId, String courseId) {
        this.registrationService = registrationService;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    @Override
    public void run() {
        try {
            Registration registration = registrationService.registerStudent(studentId, courseId);
            System.out.println(Thread.currentThread().getName() +
                    " -> SUCCESS: " + registration);
        } catch (SeatUnavailableException e) {
            System.out.println(Thread.currentThread().getName() +
                    " -> WAITLISTED: " + e.getMessage());
        } catch (DuplicateRegistrationException e) {
            System.out.println(Thread.currentThread().getName() +
                    " -> DUPLICATE: " + e.getMessage());
        }
    }
}
