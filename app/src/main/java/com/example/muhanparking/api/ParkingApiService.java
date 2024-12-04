package com.example.muhanparking.api;

public interface ParkingApiService {
    @POST("api/v1/parking/regular/applications")
    Call<RegularParkingResponse> applyParking(@Body ParkingApplicationRequest request);
}