package com.registration.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a course with a fixed number of seats.
 */
public class Course {

    private final String courseId;
    private final String courseName;
    private final int totalSeats;
    private final List<Seat> seats;
    private final List<String> waitlist; // studentIds waiting for a seat

    public Course(String courseId, String courseName, int totalSeats) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalSeats = totalSeats;
        this.seats = new ArrayList<>();
        this.waitlist = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<String> getWaitlist() {
        return waitlist;
    }

    public long getAvailableSeatCount() {
        return seats.stream().filter(s -> !s.isAllocated()).count();
    }

    @Override
    public String toString() {
        return "Course{" + courseId + " - " + courseName +
                ", availableSeats=" + getAvailableSeatCount() + "/" + totalSeats + "}";
    }
}
