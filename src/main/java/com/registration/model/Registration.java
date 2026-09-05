package com.registration.model;

import java.time.LocalDateTime;

/**
 * Represents one successful registration record
 * (a student allocated to a seat in a course).
 */
public class Registration {

    private final String studentId;
    private final String courseId;
    private final int seatNumber;
    private final LocalDateTime timestamp;

    public Registration(String studentId, String courseId, int seatNumber) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.seatNumber = seatNumber;
        this.timestamp = LocalDateTime.now();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Registration{student=" + studentId + ", course=" + courseId +
                ", seat=" + seatNumber + ", at=" + timestamp + "}";
    }
}
