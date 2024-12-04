
package com.example.muhanparking.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static RetrofitClient instance;
    private final Retrofit retrofit;

    private RetrofitClient() {
        // 여기에 LoggingInterceptor 추가
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ParkingApiService getApiService() {
        return retrofit.create(ParkingApiService.class);
    }


    public UserApi getUserApi() {
        return retrofit.create(UserApi.class);
    }
}