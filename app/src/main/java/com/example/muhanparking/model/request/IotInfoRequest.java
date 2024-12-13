package com.example.muhanparking.model.request;

public class IotInfoRequest {
    private int number;
    private boolean occupied;

    public IotInfoRequest() {
    }

    public IotInfoRequest(int number, boolean occupied) {
        this.number = number;
        this.occupied = occupied;
    }

    // Getters and Setters
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}