package com.registration.service;

import com.registration.exception.DuplicateRegistrationException;
import com.registration.exception.SeatUnavailableException;
import com.registration.model.Course;
import com.registration.model.Registration;
import com.registration.model.Seat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central service responsible for allocating seats to students.
 *
 * This is the concurrency-critical part of the project: when several
 * threads (students) call registerStudent() for the same course at the
 * same time, only one of them may successfully claim a given seat.
 * The "synchronized" keyword on registerStudent() makes seat allocation
 * atomic and prevents two threads from grabbing the same last seat
 * (a classic race condition).
 */
public class RegistrationService {

    private final Map<String, Course> courseCatalog;      // courseId -> Course
    private final List<Registration> registrations;       // all successful registrations
    private final Map<String, List<String>> studentCourses; // studentId -> list of courseIds registered

    public RegistrationService() {
        this.courseCatalog = new HashMap<>();
        this.registrations = new ArrayList<>();
        this.studentCourses = new HashMap<>();
    }

    public void addCourse(Course course) {
        courseCatalog.put(course.getCourseId(), course);
    }

    public Course getCourse(String courseId) {
        return courseCatalog.get(courseId);
    }

    public List<Course> listCourses() {
        return new ArrayList<>(courseCatalog.values());
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    /**
     * Attempts to register a student for a course.
     * Synchronized on the course object itself, so that registration
     * attempts for DIFFERENT courses can still run in parallel, but
     * attempts for the SAME course are serialized safely.
     */
    public Registration registerStudent(String studentId, String courseId)
            throws SeatUnavailableException, DuplicateRegistrationException {

        Course course = courseCatalog.get(courseId);
        if (course == null) {
            throw new IllegalArgumentException("No such course: " + courseId);
        }

        synchronized (course) {
            // Prevent duplicate registration for the same course
            List<String> alreadyTaken = studentCourses.getOrDefault(studentId, new ArrayList<>());
            if (alreadyTaken.contains(courseId)) {
                throw new DuplicateRegistrationException(
                        "Student " + studentId + " is already registered for " + courseId);
            }

            Seat freeSeat = findFreeSeat(course);
            if (freeSeat == null) {
                // No seat available -> add to waitlist instead of failing silently
                if (!course.getWaitlist().contains(studentId)) {
                    course.getWaitlist().add(studentId);
                }
                throw new SeatUnavailableException(
                        "No seats left in " + courseId + ". Student " + studentId + " added to waitlist.");
            }

            // Critical section: allocate the seat
            freeSeat.allocate(studentId);
            Registration registration = new Registration(studentId, courseId, freeSeat.getSeatNumber());
            registrations.add(registration);

            studentCourses.computeIfAbsent(studentId, k -> new ArrayList<>()).add(courseId);

            return registration;
        }
    }

    /**
     * Called when a student drops a course - frees the seat and
     * automatically promotes the next student on the waitlist, if any.
     */
    public void dropCourse(String studentId, String courseId) {
        Course course = courseCatalog.get(courseId);
        if (course == null) return;

        synchronized (course) {
            for (Seat seat : course.getSeats()) {
                if (seat.isAllocated() && studentId.equals(seat.getAllocatedTo())) {
                    seat.release();
                    studentCourses.getOrDefault(studentId, new ArrayList<>()).remove(courseId);

                    // Auto-promote next waitlisted student
                    if (!course.getWaitlist().isEmpty()) {
                        String nextStudent = course.getWaitlist().remove(0);
                        seat.allocate(nextStudent);
                        studentCourses.computeIfAbsent(nextStudent, k -> new ArrayList<>()).add(courseId);
                        registrations.add(new Registration(nextStudent, courseId, seat.getSeatNumber()));
                    }
                    return;
                }
            }
        }
    }

    private Seat findFreeSeat(Course course) {
        for (Seat seat : course.getSeats()) {
            if (!seat.isAllocated()) {
                return seat;
            }
        }
        return null;
    }
}
