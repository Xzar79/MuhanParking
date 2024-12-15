package com.example.muhanparking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.response.BaseResponse;
import com.example.muhanparking.model.response.UserProfileResponse;
import com.example.muhanparking.util.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mypage_Activity extends AppCompatActivity {
    private TextView userName, userId, userMajor, userEmail, userCarNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mypage);
        RetrofitClient.setContext(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //
        userName = findViewById(R.id.user_name);
        userId = findViewById(R.id.user_id);
        userMajor = findViewById(R.id.user_major);
        userEmail = findViewById(R.id.user_email);

        // 사용자 프로필 가져오기
        loadUserProfile();

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mypage_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        RelativeLayout logoutButton = findViewById(R.id.logout_button);
        RelativeLayout editButton = findViewById(R.id.btn_edit_button);
        RelativeLayout deleteButton = findViewById(R.id.btn_delete_account);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 확인 다이얼로그 표시
                new AlertDialog.Builder(Mypage_Activity.this)
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", (dialog, which) -> {
                            // 저장된 사용자 데이터 삭제
                            PreferenceManager.clearToken(Mypage_Activity.this);
                            PreferenceManager.clearUsername(Mypage_Activity.this);

                            // 로그인 화면으로 이동
                            Intent intent = new Intent(Mypage_Activity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(Mypage_Activity.this, User_Edit_Activity.class);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("회원 탈퇴")
                    .setMessage("정말 탈퇴하시겠습니까?")
                    .setPositiveButton("확인", (dialog, which) -> {
                        // 회원 탈퇴 처리
                        Intent intent = new Intent(Mypage_Activity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });
    }
        private void loadUserProfile() {

            String token = PreferenceManager.getToken(this);
            Log.d("Mypage", "Token: " + token);  // 토큰 확인 로그 추가


            RetrofitClient.getInstance().getUserApi().getUserProfile()
                    .enqueue(new Callback<BaseResponse<UserProfileResponse>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<UserProfileResponse>> call,
                                               Response<BaseResponse<UserProfileResponse>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                UserProfileResponse profile = response.body().getData();
                                Log.d("Mypage", "Email from server: '" + profile.getEmail() + "'");  // 이메일 값 확인

                                userName.setText(profile.getName());
                                userId.setText(String.valueOf(profile.getStudentId()));
                                userMajor.setText(profile.getDepartment());
                                userEmail.setText(profile.getEmail() != null && !profile.getEmail().isEmpty()
                                        ? profile.getEmail()
                                        : "이메일 정보 없음");  // 빈 값일 경우 대체 텍스트 표시
                            } else {
                                Toast.makeText(Mypage_Activity.this,
                                        "프로필 정보를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<UserProfileResponse>> call, Throwable t) {
                            Toast.makeText(Mypage_Activity.this,
                                    "네트워크 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                        }


                    });
        }
    }
