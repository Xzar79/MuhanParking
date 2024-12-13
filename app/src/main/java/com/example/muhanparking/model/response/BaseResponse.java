package com.example.muhanparking.model.response;

public class BaseResponse<T> {
    private boolean success;
    private String message;
    private int status;
    private T data;

    public BaseResponse(boolean success, String message, int status, T data) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}


