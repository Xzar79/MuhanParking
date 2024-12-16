package com.example.muhanparking;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.UpdateUserRequest;
import com.example.muhanparking.model.response.BaseResponse;
import com.example.muhanparking.model.response.UpdateUserResponse;
import com.example.muhanparking.model.response.UserProfileResponse;
import com.example.muhanparking.util.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class User_Edit_Activity extends AppCompatActivity {
    private EditText phoneEditText, departmentsEditText, emailEditText, passwordEditText;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_edit);

        preferenceManager = new PreferenceManager(this);

        // EditText 초기화
        phoneEditText = findViewById(R.id.edit_user_Phone);
        departmentsEditText = findViewById(R.id.edit_user_department);
        emailEditText = findViewById(R.id.edit_user_email);
        passwordEditText = findViewById(R.id.edit_password);

        // 현재 사용자 정보 불러오기
        loadUserInfo();

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        Button saveButton = findViewById(R.id.save_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(v -> updateUserInfo());

        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadUserInfo() {
        String token = preferenceManager.getToken(User_Edit_Activity.this);

        RetrofitClient.getInstance().getUserApi().UpdateUserResponse("Bearer " + token)
                .enqueue(new Callback<BaseResponse<UpdateUserResponse>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<UpdateUserResponse>> call,
                                           Response<BaseResponse<UpdateUserResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            UpdateUserResponse userInfo = response.body().getData();

                            // EditText에 현재 정보 설정
                            phoneEditText.setText(userInfo.getPhone());
                            departmentsEditText.setText(userInfo.getDepartment());
                            emailEditText.setText(userInfo.getEmail());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<UpdateUserResponse>> call, Throwable t) {
                        Toast.makeText(User_Edit_Activity.this,
                                "사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        String phone = phoneEditText.getText().toString().trim();
        String departments = departmentsEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 입력값 검증
        if (phone.isEmpty() || departments.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = preferenceManager.getToken(User_Edit_Activity.this);

        // UpdateUserRequest 객체 생성
        UpdateUserRequest request = new UpdateUserRequest();
        request.setPhone(phone);
        request.setDepartment(departments);
        request.setEmail(email);
        if (!password.isEmpty()) {
            request.setPassword(password);
        }

        // 서버에 업데이트 요청
        RetrofitClient.getInstance().getUserApi().updateUserProfile("Bearer " + token, request)
                .enqueue(new Callback<BaseResponse<UpdateUserResponse>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<UpdateUserResponse>> call,
                                           Response<BaseResponse<UpdateUserResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(User_Edit_Activity.this,
                                    "정보가 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(User_Edit_Activity.this,
                                    "정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<UpdateUserResponse>> call, Throwable t) {
                        Toast.makeText(User_Edit_Activity.this,
                                "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}