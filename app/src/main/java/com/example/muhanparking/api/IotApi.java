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
    Call<BaseResponse<List<IotInfoRequest>>> getIotInfo();

    @GET("/api/v1/{id}/status")
    Call<BaseResponse<IotDevice>> getStatus(@Path("id") int id);

    @PUT("/api/v1/{id}/status")
    Call<BaseResponse<String>> updateStatus(
            @Path("id") int id,
            @Body IotStateRequest statusRequest
    );
}