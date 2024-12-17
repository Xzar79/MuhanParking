package com.example.muhanparking.model.request;

import java.util.List;

public class IotInfoRequest {
    private int number;
    private List<List<Integer>> points;  // 좌표 데이터
    private boolean occupied;

    public IotInfoRequest() {
    }

    public IotInfoRequest(int number, List<List<Integer>> points, boolean occupied) {
        this.number = number;
        this.points = points;
        this.occupied = occupied;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<List<Integer>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Integer>> points) {
        this.points = points;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}