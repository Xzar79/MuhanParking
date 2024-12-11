package com.example.muhanparking.model.request;

public class SignUpRequest {
        private String username;
        private String password;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String gender;
        private Integer studentId;
        private Integer department;
        private String birthDate;

    public SignUpRequest(String username, String password, String name,
                         String phone, String address, String gender,
                         Integer studentId, Integer department, String birthDate,
                         String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.studentId = studentId;
        this.department = department;
        this.birthDate = birthDate;
        this.email = email;
    }
    }
