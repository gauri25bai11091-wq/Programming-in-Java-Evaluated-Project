package com.registration.db;

import com.registration.model.Registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles all JDBC interactions with the database.
 * Uses SQLite by default so the project can run without a separate
 * database server - swap the URL for MySQL/PostgreSQL if needed.
 */
public class DBConnector {

    // Change this to your MySQL URL if you prefer, e.g.:
    // "jdbc:mysql://localhost:3306/course_registration"
    private static final String DB_URL = "jdbc:sqlite:registration.db";

    private Connection connection;

    public Connection connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        createTablesIfNotExists();
        return connection;
    }

    private void createTablesIfNotExists() throws SQLException {
        String createRegistrations = "CREATE TABLE IF NOT EXISTS registrations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_id TEXT NOT NULL," +
                "course_id TEXT NOT NULL," +
                "seat_number INTEGER NOT NULL," +
                "timestamp TEXT NOT NULL" +
                ")";

        String createCourses = "CREATE TABLE IF NOT EXISTS courses (" +
                "course_id TEXT PRIMARY KEY," +
                "course_name TEXT NOT NULL," +
                "total_seats INTEGER NOT NULL" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCourses);
            stmt.execute(createRegistrations);
        }
    }

    public void saveRegistration(Registration registration) throws SQLException {
        String sql = "INSERT INTO registrations (student_id, course_id, seat_number, timestamp) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, registration.getStudentId());
            ps.setString(2, registration.getCourseId());
            ps.setInt(3, registration.getSeatNumber());
            ps.setString(4, registration.getTimestamp().toString());
            ps.executeUpdate();
        }
    }

    public void saveCourse(String courseId, String courseName, int totalSeats) throws SQLException {
        String sql = "INSERT OR IGNORE INTO courses (course_id, course_name, total_seats) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseId);
            ps.setString(2, courseName);
            ps.setInt(3, totalSeats);
            ps.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
