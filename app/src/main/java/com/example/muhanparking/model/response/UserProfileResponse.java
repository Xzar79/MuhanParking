package com.example.muhanparking.model.response;


public class UserProfileResponse {
    private String username;
    private String email;
    private Integer studentId;
    private String department;

    // Getters
    public String getName() { return username; }
    public String getEmail() { return email; }
    public Integer getStudentId() { return studentId; }
    public String getDepartment() { return department; }
}

