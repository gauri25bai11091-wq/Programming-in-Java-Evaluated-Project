package com.registration.exception;

/**
 * Thrown when a student tries to register for a course
 * that has no seats left (student is added to waitlist instead).
 */
public class SeatUnavailableException extends Exception {

    public SeatUnavailableException(String message) {
        super(message);
    }
}
