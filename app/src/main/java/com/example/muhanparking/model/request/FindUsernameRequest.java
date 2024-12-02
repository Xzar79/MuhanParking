
package com.example.muhanparking.model.request;

public class FindUsernameRequest {
    private String name;
    private Integer studentId;
    private String birthDate;

    public FindUsernameRequest(String name, Integer studentId, String birthDate) {
        this.name = name;
        this.studentId = studentId;
        this.birthDate = birthDate;
    }
}