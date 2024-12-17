package com.example.muhanparking;

import static kotlin.text.Typography.section;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.IotInfoRequest;
import com.example.muhanparking.model.response.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Handler updateHandler;
    private static final int UPDATE_INTERVAL = 60000; // 1분
    private TextView disabledSpaceText;  // 장애인 주차공간 TextView 추가


    private int occupiedA1 = 0, totalA1 = 32;
    private int occupiedA2 = 0, totalA2 = 9;
    private int occupiedB = 0, totalB = 27;
    private int occupiedC = 0, totalC = 11;
    private int occupiedD = 0, totalD = 4;  // B구역 장애인 구역

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 폰트 초기화
        Typeface pretendardBold = ResourcesCompat.getFont(this, R.font.pretendard_bold);
        Typeface pretendardMedium = ResourcesCompat.getFont(this, R.font.pretendard_medium);

        // 네비게이션 바 버튼들
        ImageView btnHome = findViewById(R.id.btn_home);
        ImageView btnParking = findViewById(R.id.btn_parking);
        ImageView btnMyInfo = findViewById(R.id.btn_myinfo);
        ImageView btnHelp = findViewById(R.id.help);

        // help 버튼
        Button btnMore = findViewById(R.id.btn_more);
        
        //새로 고침 버튼
        ImageView btnReload = findViewById(R.id.reload);
        btnReload.setOnClickListener(v -> loadAllParkingStatus());

        // 텍스트뷰들 폰트 적용
        TextView textMyInfo = findViewById(R.id.text_my_info);

        textMyInfo.setTypeface(pretendardBold);

        // 주차공간 TextView 초기화
        disabledSpaceText = findViewById(R.id.normal_space_text);

        // 주기적 업데이트 시작
        startPeriodicUpdates();

        // 새로고침 버튼 녠
        btnReload.setOnClickListener(v -> {
            loadAllParkingStatus();
            Toast.makeText(MainActivity.this, "주차 현황이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // 네비게이션 바 클릭 이벤트 처리
        btnHome.setOnClickListener(v -> {
            // 이미 홈 화면이므로 동작 안함
        });

        btnParking.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Parking_Location_Activity.class);
            startActivity(intent);
        });

        btnMyInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Mypage_Activity.class);
            startActivity(intent);
        });


        btnMore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Empty_Parking_Activity.class);
            startActivity(intent);
        });

        btnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Help_Activity.class);
            startActivity(intent);
        });
    }

    private void startPeriodicUpdates() {
        updateHandler = new Handler(Looper.getMainLooper());
        // 초기 데이터 로드
        loadAllParkingStatus();

        // 주기적 업데이트
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAllParkingStatus();
                updateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, UPDATE_INTERVAL);
    }
    private void loadAllParkingStatus() {
        // A1 구역 데이터 로드
        RetrofitClient.getInstance().getApi().getIotInfo("IoT-A1")
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            updateTotalCount("A1", response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        // 에러 처리
                    }
                });

        // A2 구역 데이터 로드
        RetrofitClient.getInstance().getApi().getIotInfo("IoT-A2")
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            updateTotalCount("A2", response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        // 에러 처리
                    }
                });

        // B 구역 데이터 로드
        RetrofitClient.getInstance().getApi().getIotInfo("IoT-B")
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            updateTotalCount("B", response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        // 에러 처리
                    }
                });

        // C 구역 데이터 로드
        RetrofitClient.getInstance().getApi().getIotInfo("IoT-C")
                .enqueue(new Callback<BaseResponse<List<IotInfoRequest>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<IotInfoRequest>>> call,
                                           Response<BaseResponse<List<IotInfoRequest>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            updateTotalCount("C", response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<IotInfoRequest>>> call, Throwable t) {
                        // 에러 처리
                    }
                });
    }
    private void updateTotalCount(String section, List<IotInfoRequest> spots) {
        int occupied = 0;
        for (IotInfoRequest spot : spots) {
            if (spot.isOccupied()) {
                occupied++;
            }
        }

        // 각 구역별 주차량 업데이트
        switch (section) {
            case "A1":
                occupiedA1 = occupied;
                break;
            case "A2":
                occupiedA2 = occupied;
                break;
            case "B":
                occupiedB = occupied;
                break;
            case "C":
                occupiedC = occupied;
                break;
        }

        // 전체 합계 계산
        int totalOccupied = occupiedA1 + occupiedA2 + occupiedB + occupiedC;
        int totalSpots = totalA1 + totalA2 + totalB + totalC;

        // UI 업데이트 - 주차된 차량 수 표시
        runOnUiThread(() -> {
            disabledSpaceText.setText(String.format("%d / %d", totalOccupied, totalSpots));
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

