package com.example.muhanparking;

import android.media.Image;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
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
public class Section_A_Activity extends AppCompatActivity {
    private final Map<Integer, ImageView> parkingSpotsA1 = new HashMap<>();
    private final Map<Integer, ImageView> parkingSpotsA2 = new HashMap<>();
    private Handler updateHandler;
    private static final int UPDATE_INTERVAL = 60000; // 1분

    // 주차 상태를 추적하기 위한 변수 추가
    private int occupiedCountA1 = 0;
    private int occupiedCountA2 = 0;
    private final int totalSpotsA1 = 26; // A1 구역 주차구역 수
    private final int totalSpotsA2 = 8;  // A2 구역 주차우경 수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_section_a);

        initializeViews();
        startPeriodicUpdates();
    }

    private void initializeViews() {
        // 뒤로가기 버튼
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Section_A_Activity.this, Empty_Parking_Activity.class);
            startActivity(intent);
            finish();
        });

        // 새로고침 버튼
        ImageView reload = findViewById(R.id.reload);
        reload.setOnClickListener(v -> {
            loadParkingStatus("IoT-A1");
            loadParkingStatus("IoT-A2");
        });


        // A1 구역 주차공간 초기화 (1-26)
        for (int i = 1; i <= 26; i++) {
            int viewId = getResources().getIdentifier("car_" + i, "id", getPackageName());
            parkingSpotsA1.put(i, findViewById(viewId));
        }

        // A2 구역 주차공간 초기화 (27-34)
        for (int i = 26; i <= 33; i++) {
            int viewId = getResources().getIdentifier("car_" + i, "id", getPackageName());
            parkingSpotsA2.put(i, findViewById(viewId));
        }
    }

    private void startPeriodicUpdates() {
        updateHandler = new Handler(Looper.getMainLooper());

        // 초기 데이터 로드
        loadParkingStatus("IoT-A1");
        loadParkingStatus("IoT-A2");

        // 주기적 업데이트
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadParkingStatus("IoT-A1");
                loadParkingStatus("IoT-A2");
                updateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, UPDATE_INTERVAL);
    }

    private void loadParkingStatus(String deviceId) {
        RetrofitClient.getInstance().getApi().getIotInfo(deviceId)
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Map<Integer, ImageView> targetSpots =
                                    deviceId.equals("IoT-A1") ? parkingSpotsA1 : parkingSpotsA2;
                            updateParkingSpots(response.body().getData(), targetSpots, deviceId);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        Toast.makeText(Section_A_Activity.this,
                                deviceId + " 데이터 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateParkingSpots(List<IotInfoRequest> spots, Map<Integer, ImageView> parkingSpots, String deviceId) {
        runOnUiThread(() -> {
            int occupiedCount = 0;

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

            // 주차량 전체 합 (a1,a2)
            TextView txtNormal = findViewById(R.id.txt_normal);
            int totalOccupied = 0;
            int totalSpots = parkingSpotsA1.size() + parkingSpotsA2.size();

            if (deviceId.equals("IoT-A1")) {
                totalOccupied = occupiedCount + getOccupiedCountA2();
            } else {
                totalOccupied = getOccupiedCountA1() + occupiedCount;
            }

            txtNormal.setText(String.format("일반 : %d / %d", totalOccupied, totalSpots));
        });
    }

    private int getOccupiedCountA1() {
        int count = 0;
        for (ImageView iv : parkingSpotsA1.values()) {
            if (iv.getDrawable().getConstantState() ==
                    getResources().getDrawable(R.drawable.ic_car_red).getConstantState()) {
                count++;
            }
        }
        return count;
    }

    private int getOccupiedCountA2() {
        int count = 0;
        for (ImageView iv : parkingSpotsA2.values()) {
            if (iv.getDrawable().getConstantState() ==
                    getResources().getDrawable(R.drawable.ic_car_red).getConstantState()) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
        }
    }
}