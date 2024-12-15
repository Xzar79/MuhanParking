
package com.example.muhanparking.api;

import android.content.Context;
import android.util.Log;

import com.example.muhanparking.util.PreferenceManager;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitClient {
    private static final String BASE_URL = "http://ceprj.gachon.ac.kr:60010/";
    private static RetrofitClient instance = null;
    private final Retrofit retrofit;
    private static Context context;  // Context 추가

    // Context를 설정하는 메소드 추가
    public static void setContext(Context ctx) {
        context = ctx;
    }

    private RetrofitClient() {
        // 로깅 인터셉터 설정
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Log.d("RetrofitClient", "API Call: " + message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    // Context를 사용하여 토큰 가져오기
                    String token = context != null ? PreferenceManager.getToken(context) : null;

                    Request.Builder builder = originalRequest.newBuilder();
                    if (token != null && !token.isEmpty()) {
                        builder.addHeader("Authorization", "Bearer " + token);
                    }
                    Request request = builder.build();

                    // 요청 로깅
                    Log.d("RetrofitClient", "Sending request: " + request.url());
                    Log.d("RetrofitClient", "Headers: " + request.headers());

                    Response response = chain.proceed(request);

                    // 응답 로깅
                    Log.d("RetrofitClient", "Response code: " + response.code());

                    return response;
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .client(client)
                .build();
    }


    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }


    public UserApi getUserApi() {
        return retrofit.create(UserApi.class);
    }

    public IotApi getApi() {
        return retrofit.create(IotApi.class);
    }
}