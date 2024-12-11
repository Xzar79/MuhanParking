package com.example.muhanparking.api;

import com.example.muhanparking.model.request.IotInfoRequest;
import com.example.muhanparking.model.response.BaseResponse;
import com.example.muhanparking.model.response.IotInfoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IotApi {
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("/api/v1/iot/info")
    Call<BaseResponse<List<IotInfoRequest>>> getIotInfo();
}