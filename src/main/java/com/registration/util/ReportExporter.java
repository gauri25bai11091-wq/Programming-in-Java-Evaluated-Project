package com.registration.util;

import com.registration.model.Course;
import com.registration.model.Registration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Exports registration and seat-availability reports to a text file
 * using character-oriented streams (BufferedWriter / FileWriter).
 */
public class ReportExporter {

    public void exportRegistrationReport(String filePath, List<Registration> registrations) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("student_id,course_id,seat_number,timestamp");
            writer.newLine();
            for (Registration r : registrations) {
                writer.write(r.getStudentId() + "," + r.getCourseId() + "," +
                        r.getSeatNumber() + "," + r.getTimestamp());
                writer.newLine();
            }
            System.out.println("Registration report written to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to write registration report: " + e.getMessage());
        }
    }

    public void exportSeatAvailabilityReport(String filePath, List<Course> courses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("course_id,course_name,available_seats,total_seats");
            writer.newLine();
            for (Course c : courses) {
                writer.write(c.getCourseId() + "," + c.getCourseName() + "," +
                        c.getAvailableSeatCount() + "," + c.getTotalSeats());
                writer.newLine();
            }
            System.out.println("Seat availability report written to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to write seat availability report: " + e.getMessage());
        }
    }
}
