package com.example.muhanparking.api;

import com.example.muhanparking.model.request.IotInfoRequest;
import com.example.muhanparking.model.response.BaseResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface IotApi {
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("/api/v1/iot/info")
    Call<BaseResponse<List<IotInfoRequest>>> getIotInfo(@Header("Device-ID") String deviceId);
}