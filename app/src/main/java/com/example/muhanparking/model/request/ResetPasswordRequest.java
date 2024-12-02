package com.example.muhanparking.model.request;


public class ResetPasswordRequest {
    private String username;
    private String name;
    private Integer studentId;
    private String newPassword;

    public ResetPasswordRequest(String username, String name, Integer studentId, String newPassword) {
        this.username = username;
        this.name = name;
        this.studentId = studentId;
        this.newPassword = newPassword;
    }
}