package com.example.muhanparking;

import android.media.Image;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
    private final int totalSpotsA1 = 41; // A1 구역 주차구역 수
    private final int totalSpotsA2 = 8;  // A2 구역 주차우경 수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_section_a);

        initializeViews();
        startPeriodicUpdates();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
            Toast.makeText(Section_A_Activity.this, "A구역 주차 현황이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // A1 구역 주차공간 초기화 (1-26)
        for (int i = 1; i <= 26; i++) {
            int viewId = getResources().getIdentifier("car_" + i, "id", getPackageName());
            ImageView spot = findViewById(viewId);
            if (spot != null) {
                parkingSpotsA1.put(i, spot);
                // 단순한 로그
                Log.d("Section_A", "A1 spot " + i + " 초기화");
            }
        }

        // A2 구역 주차공간 초기화 (1-9를 27-35에 매핑)
        for (int i = 1; i <= 9; i++) {  // A2 구역은 1-9번
            int viewId = getResources().getIdentifier("car_" + (i + 26), "id", getPackageName());
            ImageView spot = findViewById(viewId);
            if (spot != null) {
                parkingSpotsA2.put(i, spot);
                Log.d("Section_A_Activity", "Initialized A2 parking spot " + i);
            } else {
                Log.e("Section_A_Activity", "Failed to find view for A2 spot " + i);
            }
        }
    }


    private void startPeriodicUpdates() {
        updateHandler = new Handler(Looper.getMainLooper());
        loadParkingStatus("IoT-A1");
        loadParkingStatus("IoT-A2");

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
                        if (response.isSuccessful() && response.body() != null) {
                            updateParkingSpots(response.body().getData(), deviceId);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        Toast.makeText(Section_A_Activity.this,
                                "업데이트 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateParkingSpots(List<IotInfoRequest> spots, String deviceId) {
        runOnUiThread(() -> {
            int occupiedCount = 0;
            Map<Integer, ImageView> targetSpots = deviceId.equals("IoT-A1") ? parkingSpotsA1 : parkingSpotsA2;

            for (IotInfoRequest spot : spots) {
                int spotNumber = spot.getNumber();

                // A1 구역은 1-26, A2 구역은 1-9만 처리
                if ((deviceId.equals("IoT-A1") && spotNumber > 26) ||
                        (deviceId.equals("IoT-A2") && spotNumber > 9)) {
                    continue;
                }

                ImageView parkingSpot = targetSpots.get(spotNumber);
                if (parkingSpot != null) {
                    boolean isOccupied = spot.isOccupied();
                    int imageResource = isOccupied ?
                            R.drawable.ic_car_red : R.drawable.b_empty_car_img;
                    parkingSpot.setImageResource(imageResource);

                    if (isOccupied) {
                        occupiedCount++;
                    }

                    Log.d("Section_A_Activity",
                            deviceId + " Updating spot " + spotNumber +
                                    " occupied: " + isOccupied);
                }
            }

            // 주차량 전체 합 업데이트
            if (deviceId.equals("IoT-A1")) {
                occupiedCountA1 = occupiedCount;
            } else {
                occupiedCountA2 = occupiedCount;
            }

            // UI 업데이트
            TextView txtNormal = findViewById(R.id.txt_normal);
            int totalOccupied = occupiedCountA1 + occupiedCountA2;
            int totalSpots = 35;  // A1(26) + A2(9)

            Log.d("Section_A_Activity", deviceId + " occupied: " + occupiedCount);
            Log.d("Section_A_Activity", "Total occupied: " + totalOccupied + "/" + totalSpots);

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