package com.example.muhanparking.model.response;

import com.google.gson.annotations.SerializedName;

public class UpdateUserResponse {
    @SerializedName("phone")
    private String phone;

    @SerializedName("department")
    private String department;

    @SerializedName("email")
    private String email;

    // Getters and Setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}