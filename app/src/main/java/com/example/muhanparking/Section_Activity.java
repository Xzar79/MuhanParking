package com.example.muhanparking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.IotInfoRequest;
import com.example.muhanparking.model.response.BaseResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Section_Activity extends AppCompatActivity {
    private String deviceId;  // "A1", "A2", "B", "C" 중 하나
    private final Map<Integer, ImageView> parkingSpots = new HashMap<>();
    private Handler updateHandler;
    private Runnable updateRunnable;
    private static final int UPDATE_INTERVAL = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 먼저 deviceId를 받아옵니다
        deviceId = getIntent().getStringExtra("device_id");
        Log.d("Section_Activity", "Received device_id: " + deviceId);

        if (deviceId == null) {
            Log.e("Section_Activity", "No device_id received!");
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        deviceId = formatDeviceId(deviceId);
        Log.d("Section_Activity", "Formatted device_id: " + deviceId);


        // deviceId에 따라 레이아웃 설정
        switch (deviceId) {
            case "IoT-A1":
                Log.d("Section_Activity", "Setting layout for section A1");
                setContentView(R.layout.activity_section_b);
                break;
            case "IoT-A2":
                Log.d("Section_Activity", "Setting layout for section A2");
                setContentView(R.layout.activity_section_b);
                break;
            case "IoT-B":
                Log.d("Section_Activity", "Setting layout for section B");
                setContentView(R.layout.activity_section_b);
                break;
            case "IoT-C":
                Log.d("Section_Activity", "Setting layout for section C");
                setContentView(R.layout.activity_section_c);
                break;
            default:
                Log.e("Section_Activity", "Unknown device_id: " + deviceId);
                Toast.makeText(this, "알 수 없는 구역입니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
        }

        // 레이아웃 설정 후 뷰 초기화
        initializeViews();
        startRealtimeUpdates();
    }
    private String formatDeviceId(String originalId) {
        if (originalId == null) return null;
        if (originalId.startsWith("IoT-")) return originalId;  // 이미 포맷된 경우

        return "IoT-" + originalId;
    }
    private void startRealtimeUpdates() {
        updateHandler = new Handler(Looper.getMainLooper());
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                loadParkingStatus();
                updateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
        updateHandler.post(updateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (updateHandler != null && updateRunnable != null) {
            updateHandler.removeCallbacks(updateRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (updateHandler != null && updateRunnable != null) {
            updateHandler.post(updateRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
        }
    }

    private void initializeViews() {
        // 뒤로가기 버튼
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        // 구역별 주차 공간 초기화
        switch (deviceId) {
            case "IoT-A1":
                initSectionA1Spots();
                break;
            case "IoT-A2":
                initSectionA2Spots();
                break;
            case "IoT-B":
                initSectionBSpots();
                break;
            case "IoT-C":
                initSectionCSpots();
                break;
        }

        // 초기화된 주차 공간 수 확인
        Log.d("Section_Activity", "Total initialized spots: " + parkingSpots.size());
    }

    private void loadParkingStatus() {
        Log.d("Section_Activity", "Requesting parking data for device: " + deviceId);

        RetrofitClient.getInstance().getApi().getIotInfo(deviceId)
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        Log.d("Section_Activity", "Raw response: " + response.raw().toString());

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<IotInfoRequest> spots = response.body().getData();
                            Log.d("Section_Activity", "Received spots count: " + spots.size());
                            Log.d("Section_Activity", "Response data: " + spots);
                            updateParkingSpots(spots);
                        } else {
                            Log.e("Section_Activity", "Response not successful. Code: " + response.code());
                            if (response.errorBody() != null) {
                                try {
                                    Log.e("Section_Activity", "Error body: " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        Log.e("Section_Activity", "Network error", t);
                    }
                });
    }
    private void loadIotData() {
        RetrofitClient.getInstance().getApi().getIotInfo(deviceId)  // deviceId 전달
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<IotInfoRequest> parkingSpots = response.body().getData();
                            Log.d("Section_Activity", "Received parking data: " + parkingSpots.size() + " spots");
                            updateParkingSpots(parkingSpots);
                        } else {
                            Log.e("Section_Activity", "Failed to get parking data");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        Log.e("Section_Activity", "Network error: " + t.getMessage());
                    }
                });
    }

    private void updateParkingSpots(List<IotInfoRequest> spots) {
        runOnUiThread(() -> {
            for (IotInfoRequest spot : spots) {
                ImageView parkingSpot = parkingSpots.get(spot.getNumber());
                if (parkingSpot != null) {
                    Log.d("Section_Activity", "Updating spot " + spot.getNumber() +
                            " occupied: " + spot.isOccupied());

                    // 이미지 리소스 설정을 더 명확하게
                    int imageResource = spot.isOccupied() ?
                            R.drawable.ic_car_red : R.drawable.b_empty_car_img;
                    parkingSpot.setImageResource(imageResource);

                    Log.d("Section_Activity", "Setting " +
                            (spot.isOccupied() ? "RED" : "GREEN") +
                            " for spot: " + spot.getNumber());

                    parkingSpot.setVisibility(View.VISIBLE);
                    parkingSpot.invalidate();
                } else {
                    Log.w("Section_Activity", "ImageView not found for spot: " + spot.getNumber());
                }
            }
        });
    }
    // 구역별 주차 공간 초기화 메서드들
    private void initSectionA1Spots() {
        int[] spotNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        for (int number : spotNumbers) {
            int viewId = getResources().getIdentifier("car_" + number, "id", getPackageName());
            parkingSpots.put(number, findViewById(viewId));
        }
    }

    private void initSectionA2Spots() {
        int[] spotNumbers = {1, 2, 3, 4};
        for (int number : spotNumbers) {
            int viewId = getResources().getIdentifier("car_" + number, "id", getPackageName());
            parkingSpots.put(number, findViewById(viewId));
        }
    }

    private void initSectionBSpots() {
        for (int i = 1; i <= 27; i++) {
            int viewId = getResources().getIdentifier("car_" + i, "id", getPackageName());
            parkingSpots.put(i, findViewById(viewId));
        }
        for (int i = 1; i <= 4; i++) {
            int viewId = getResources().getIdentifier("b_car_" + i, "id", getPackageName());
            parkingSpots.put(100 + i, findViewById(viewId));
        }
    }

    private void initSectionCSpots() {
        for (int i = 1; i <= 11; i++) {
            int viewId = getResources().getIdentifier("car_" + i, "id", getPackageName());
            parkingSpots.put(i, findViewById(viewId));
        }
    }
}