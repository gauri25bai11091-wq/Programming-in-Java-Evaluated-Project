package com.registration.exception;

/**
 * Thrown when a student tries to register for the
 * same course more than once.
 */
public class DuplicateRegistrationException extends Exception {

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
