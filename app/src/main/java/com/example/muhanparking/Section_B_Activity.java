package com.example.muhanparking;

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

public class Section_B_Activity extends AppCompatActivity {
    private final Map<Integer, ImageView> parkingSpotsB = new HashMap<>();
    private final Map<Integer, ImageView> parkingSpotsD = new HashMap<>();
    private Handler updateHandler;
    private static final int UPDATE_INTERVAL = 60000; // 1분

    // 주차 상태를 추적하기 위한 변수 추가
    private int occupiedCountB = 0;
    private int occupiedCountD = 0;
    private final int totalSpotsB = 27; // B 구역 주차구역 수
    private final int totalSpotsD = 4;  // D 구역 주차구역 수 (장애인)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_section_b);

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
            Intent intent = new Intent(Section_B_Activity.this, Empty_Parking_Activity.class);
            startActivity(intent);
            finish();
        });

        // 새로고침 버튼
        ImageView reload = findViewById(R.id.reload);
        reload.setOnClickListener(v -> {
            loadParkingStatus("IoT-B");
            loadParkingStatus("IoT-D");
            Toast.makeText(Section_B_Activity.this, "B구역 주차 현황이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // 일반 주차구역 초기화 (1-27)
        for (int i = 1; i <= 27; i++) {
            int viewId = getResources().getIdentifier("car_" + i, "id", getPackageName());
            ImageView spot = findViewById(viewId);
            if (spot != null) {
                parkingSpotsB.put(i, spot);
                Log.d("Section_B_Activity", "Initialized normal parking spot " + i);
            } else {
                Log.e("Section_B_Activity", "Failed to find view for normal spot " + i);
            }
        }

        // 장애인 주차구역 초기화 (1-4)
        for (int i = 1; i <= 4; i++) {
            int viewId = getResources().getIdentifier("b_car_" + i, "id", getPackageName());
            ImageView spot = findViewById(viewId);
            if (spot != null) {
                parkingSpotsD.put(i, spot);
                Log.d("Section_B_Activity", "Initialized disabled parking spot " + i);
            } else {
                Log.e("Section_B_Activity", "Failed to find view for disabled spot " + i);
            }
        }
    }

    private void startPeriodicUpdates() {
        updateHandler = new Handler(Looper.getMainLooper());

        // 초기 데이터 로드
        loadParkingStatus("IoT-B");
        loadParkingStatus("IoT-D");

        // 주기적 업데이트
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadParkingStatus("IoT-B");
                loadParkingStatus("IoT-D");
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
                            List<IotInfoRequest> spots = response.body().getData();
                            Log.d("Section_B_Activity", deviceId + " received " + spots.size() + " spots data");
                            updateParkingSpots(spots, deviceId);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        Toast.makeText(Section_B_Activity.this,
                                deviceId + " 데이터 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateParkingSpots(List<IotInfoRequest> spots, String deviceId) {
        runOnUiThread(() -> {
            int occupiedCount = 0;
            Map<Integer, ImageView> targetSpots = deviceId.equals("IoT-B") ? parkingSpotsB : parkingSpotsD;
            int maxSpots = deviceId.equals("IoT-B") ? totalSpotsB : totalSpotsD;

            for (IotInfoRequest spot : spots) {
                int spotNumber = spot.getNumber();

                if (spotNumber > maxSpots) {
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

                    Log.d("Section_B_Activity",
                            deviceId + " Updating spot " + spotNumber +
                                    " occupied: " + isOccupied);
                }
            }

            // 주차량 업데이트
            if (deviceId.equals("IoT-B")) {
                occupiedCountB = occupiedCount;
            } else {
                occupiedCountD = occupiedCount;
            }

            // UI 업데이트
            TextView txtNormal = findViewById(R.id.txt_normal);
            TextView txtDisabled = findViewById(R.id.txt_disabled);

            txtNormal.setText(String.format("일반 : %d / %d", occupiedCountB, totalSpotsB));
            txtDisabled.setText(String.format("장애인 : %d / %d", occupiedCountD, totalSpotsD));

            Log.d("Section_B_Activity", deviceId + " occupied: " + occupiedCount);
            Log.d("Section_B_Activity",
                    String.format("Total occupied - Normal: %d/%d, Disabled: %d/%d",
                            occupiedCountB, totalSpotsB, occupiedCountD, totalSpotsD));
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