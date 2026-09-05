package com.registration.model;

/**
 * Represents a single seat inside a course.
 * The "allocated" flag is the shared mutable state that must be
 * protected when multiple threads try to register at the same time.
 */
public class Seat {

    private final int seatNumber;
    private boolean allocated;
    private String allocatedTo; // studentId

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.allocated = false;
        this.allocatedTo = null;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public String getAllocatedTo() {
        return allocatedTo;
    }

    public void allocate(String studentId) {
        this.allocated = true;
        this.allocatedTo = studentId;
    }

    public void release() {
        this.allocated = false;
        this.allocatedTo = null;
    }

    @Override
    public String toString() {
        return "Seat{" + seatNumber + ", allocated=" + allocated +
                (allocated ? ", to=" + allocatedTo : "") + "}";
    }
}
