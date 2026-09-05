package com.registration.model;

/**
 * Represents an administrator who manages courses and seats.
 */
public class Admin {

    private final String adminId;
    private final String name;

    public Admin(String adminId, String name) {
        this.adminId = adminId;
        this.name = name;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Admin{id='" + adminId + "', name='" + name + "'}";
    }
}
