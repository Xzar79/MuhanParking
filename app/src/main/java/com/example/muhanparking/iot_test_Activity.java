package com.example.muhanparking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.muhanparking.api.IotApi;
import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.IotInfoRequest;
import com.example.muhanparking.model.response.BaseResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class iot_test_Activity extends AppCompatActivity {
    private IotApi iotApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iot_test);

        // 뒤로가기 버튼
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(iot_test_Activity.this, Empty_Parking_Activity.class);
            startActivity(intent);
            finish();
        });

        iotApi = RetrofitClient.getInstance().getApi();

        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(v -> fetchData());

        // 전달받은 zone_id 확인
        String zoneId = getIntent().getStringExtra("zone_id");
        Log.d("IOT_TEST", "Selected Zone: " + zoneId);
    }

    private void fetchData() {
        iotApi.getIotInfo().enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                   Response<BaseResponse<List<IotInfoRequest>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<IotInfoRequest> iotInfoList = response.body().getData();
                    Log.d("IOT_DATA", "Response: " + iotInfoList.toString());
                    Toast.makeText(iot_test_Activity.this, "데이터 수신 성공", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("IOT_DATA", "Error Body: " + errorBody);
                        Log.e("IOT_DATA", "Error Code: " + response.code());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(iot_test_Activity.this, "데이터 수신 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                Log.e("IOT_DATA", "Network Error: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(iot_test_Activity.this, "네트워크 에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}