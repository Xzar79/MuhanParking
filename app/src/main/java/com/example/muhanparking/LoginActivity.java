package com.example.muhanparking;

import android.os.Bundle;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.LoginRequest;
import com.example.muhanparking.model.response.BaseResponse;
import com.example.muhanparking.model.response.LoginResponse;
import com.example.muhanparking.util.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText Id = findViewById(R.id.Id);
        EditText Password = findViewById(R.id.Password);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Id.getText().toString();
                String password = Password.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                LoginRequest loginRequest = new LoginRequest(username, password);
                RetrofitClient.getInstance().getUserApi().login(loginRequest)
                        .enqueue(new Callback<BaseResponse<LoginResponse>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<LoginResponse>> call, Response<BaseResponse<LoginResponse>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    // 토큰과 사용자 이름 저장
                                    String token = response.body().getData().getToken();
                                    String username = response.body().getData().getUsername();
                                    PreferenceManager.saveToken(LoginActivity.this, token);
                                    PreferenceManager.saveUsername(LoginActivity.this, username);

                                    // 메인 화면으로 이동
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }


                            @Override
                            public void onFailure(Call<BaseResponse<LoginResponse>> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        Button btnFindID = findViewById(R.id.btnFindID);
        btnFindID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Find_ID_Activity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register_Activity.class);
                startActivity(intent);
            }
        });
    }
}