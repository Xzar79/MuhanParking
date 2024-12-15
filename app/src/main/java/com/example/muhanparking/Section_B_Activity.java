package com.example.muhanparking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.IotInfoRequest;
import com.example.muhanparking.model.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Section_B_Activity.java
public class Section_B_Activity extends AppCompatActivity {
    private final Map<Integer, ImageView> parkingSpots = new HashMap<>();
    private static final String DEVICE_ID = "IoT-B";
    private Handler updateHandler;
    private static final int UPDATE_INTERVAL = 60000; // 1분

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_section_b);

        initializeViews();
        startPeriodicUpdates();
    }

    private void initializeViews() {
        // 뒤로가기 버튼
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Section_B_Activity.this, Empty_Parking_Activity.class);
            startActivity(intent);
            finish();
        });

        // 새로고침 버튼
        ImageView reload = findViewById(R.id.reload);
        reload.setOnClickListener(v -> loadParkingStatus());

        // B구역 주차공간 초기화
        for (int i = 1; i <= 27; i++) {
            int viewId = getResources().getIdentifier("car_" + i, "id", getPackageName());
            parkingSpots.put(i, findViewById(viewId));
        }
    }

    private void startPeriodicUpdates() {
        updateHandler = new Handler(Looper.getMainLooper());
        // 초기 데이터 로드
        loadParkingStatus();

        // 주기적 업데이트 시작
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadParkingStatus();
                updateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, UPDATE_INTERVAL);
    }

    private void loadParkingStatus() {
        RetrofitClient.getInstance().getApi().getIotInfo(DEVICE_ID)
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            updateParkingSpots(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        // 에러 처리
                        Toast.makeText(Section_B_Activity.this,
                                "데이터 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateParkingSpots(List<IotInfoRequest> spots) {
        runOnUiThread(() -> {
            int occupiedCount = 0; // 주차된 차량 수
            int totalSpots = parkingSpots.size(); // 전체 주차 공간 수

            for (IotInfoRequest spot : spots) {
                ImageView parkingSpot = parkingSpots.get(spot.getNumber());
                if (parkingSpot != null) {
                    int imageResource = spot.isOccupied() ?
                            R.drawable.ic_car_red : R.drawable.b_empty_car_img;
                    parkingSpot.setImageResource(imageResource);

                    if (spot.isOccupied()) {
                        occupiedCount++;
                    }
                }
            }

            // 주차량
            TextView txtNormal = findViewById(R.id.txt_normal);
            txtNormal.setText(String.format("일반 : %d / %d", occupiedCount, totalSpots));
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
        }
    }
}