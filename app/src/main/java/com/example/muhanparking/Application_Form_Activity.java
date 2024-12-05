package com.example.muhanparking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.muhanparking.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Application_Form_Activity extends AppCompatActivity {
    private EditText etStudentYear, etDepartureStation, etStationTime;
    private RadioGroup rgTransType;
    private EditText etPublicTime, etCarTime;
    private Button btnSubmit;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_form);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        // 뷰 초기화
        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        etStudentYear = findViewById(R.id.et_student_year);
        rgTransType = findViewById(R.id.rg_trans_type);
        etDepartureStation = findViewById(R.id.et_departure_station);
        etStationTime = findViewById(R.id.et_station_time);
        etPublicTime = findViewById(R.id.et_public_time);
        etCarTime = findViewById(R.id.et_car_time);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void setClickListeners() {
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> submitApplication());
    }

    private void submitApplication() {
        // 로그인된 사용자 ID 가져오기
        Long userId = sharedPreferences.getLong("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
            // 로그인 화면으로 이동
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        ParkingApplicationRequest request = new ParkingApplicationRequest();
        try {
            request.setUserId(userId);  // 실제 로그인한 사용자 ID 사용
            request.setStudentYear(Integer.parseInt(etStudentYear.getText().toString()));
            request.setTransportationType(
                    rgTransType.getCheckedRadioButtonId() == R.id.rb_subway ? "SUBWAY" : "BUS"
            );
            request.setDepartureStation(etDepartureStation.getText().toString());
            request.setStationTimeMinutes(Integer.parseInt(etStationTime.getText().toString()));
            request.setPublicTransportTime(Integer.parseInt(etPublicTime.getText().toString()));
            request.setCarTime(Integer.parseInt(etCarTime.getText().toString()));
        } catch(NumberFormatException e) {
            Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 입력값 유효성 검사
        if (!validateInput(request)) {
            return;
        }

        RetrofitClient.getInstance()
                .getApiService()
                .applyParking(request)
                .enqueue(new Callback<RegularParkingResponse>() {
                    @Override
                    public void onResponse(Call<RegularParkingResponse> call,
                                           Response<RegularParkingResponse> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(Application_Form_Activity.this,
                                    "정기주차 신청이 완료되었습니다!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            handleError(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegularParkingResponse> call, Throwable t) {
                        Toast.makeText(Application_Form_Activity.this,
                                "네트워크 오류가 발생했습니다: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInput(ParkingApplicationRequest request) {
        if (request.getDepartureStation().trim().isEmpty()) {
            Toast.makeText(this, "출발역/정류장을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (request.getStationTimeMinutes() <= 0) {
            Toast.makeText(this, "정류장까지 걸리는 시간을 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (request.getPublicTransportTime() <= 0) {
            Toast.makeText(this, "대중교통 소요시간을 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (request.getCarTime() <= 0) {
            Toast.makeText(this, "자가용 소요시간을 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void handleError(Response<RegularParkingResponse> response) {
        try {
            String errorBody = response.errorBody().string();
            Toast.makeText(Application_Form_Activity.this,
                    "신청 실패: " + errorBody, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(Application_Form_Activity.this,
                    "알 수 없는 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}