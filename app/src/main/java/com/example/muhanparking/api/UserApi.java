// api/UserApi.java
package com.example.muhanparking.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Body;
import com.example.muhanparking.model.request.LoginRequest;
import com.example.muhanparking.model.request.SignUpRequest;
import com.example.muhanparking.model.request.FindUsernameRequest;
import com.example.muhanparking.model.request.ResetPasswordRequest;
import com.example.muhanparking.model.response.BaseResponse;
import com.example.muhanparking.model.response.LoginResponse;
import com.example.muhanparking.model.response.FindUsernameResponse;

public interface UserApi {
    @POST("api/v1/user/auth/signup")
    Call<BaseResponse<Void>> signup(@Body SignUpRequest request);

    @POST("api/v1/user/auth/login")
    Call<BaseResponse<LoginResponse>> login(@Body LoginRequest request);

    @DELETE("api/v1/user/profile")
    Call<BaseResponse<Void>> withdrawUser();

    @POST("api/v1/user/auth/find-username")
    Call<BaseResponse<FindUsernameResponse>> findUsername(@Body FindUsernameRequest request);

    @POST("api/v1/user/auth/reset-password")
    Call<BaseResponse<Void>> resetPassword(@Body ResetPasswordRequest request);
}