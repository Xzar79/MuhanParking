package com.example.muhanparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.SignUpRequest;
import com.example.muhanparking.model.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* 성별 스피너 */
        Spinner spinnerGender = findViewById(R.id.ctGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        /* 상단 툴바의 뒤로가기 */
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /* 이메일, 비밀번호 등 */
        EditText ctEmail = findViewById(R.id.ctEmail);
        EditText ctId = findViewById(R.id.ctId);
        EditText ctPassword = findViewById(R.id.ctPassword);
        EditText ctPassword2 = findViewById(R.id.ctPassword2);
        EditText ctName = findViewById(R.id.ctName);
        EditText ctPhone = findViewById(R.id.ctPhone);
        EditText ctAdd = findViewById(R.id.ctAddress);
        EditText ctStuId = findViewById(R.id.ctStudentId);
        EditText ctDepart = findViewById(R.id.ctDepartment);
        EditText ctBir = findViewById(R.id.ctBirthDate);


        Button btnSuccess = findViewById(R.id.btnSuccess);

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 입력값 가져오기
                    String username = ctId.getText().toString().trim();
                    String password = ctPassword.getText().toString();
                    String password2 = ctPassword2.getText().toString();
                    String name = ctName.getText().toString().trim();
                    String phone = ctPhone.getText().toString().trim();
                    String address = ctAdd.getText().toString().trim();
                    String gender = spinnerGender.getSelectedItem().toString();
                    String studentIdStr = ctStuId.getText().toString().trim();
                    String departmentStr = ctDepart.getText().toString().trim();
                    String birthDate = ctBir.getText().toString().trim();
                    String email = ctEmail.getText().toString().trim();

                    if (!isValidEmail(email)) {
                        Toast.makeText(Register_Activity.this,
                                "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 필수 입력값 검증
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                            TextUtils.isEmpty(name) || TextUtils.isEmpty(studentIdStr) ||
                            TextUtils.isEmpty(departmentStr)) {
                        Toast.makeText(Register_Activity.this,
                                "필수 항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 비밀번호 일치 검사
                    if (!password.equals(password2)) {
                        Toast.makeText(Register_Activity.this,
                                "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 학번 코드가 숫자인지 검사
                    if (!studentIdStr.matches("\\d+")) {
                        Toast.makeText(Register_Activity.this,
                                "학번과 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int studentId = Integer.parseInt(studentIdStr);
                    int department = Integer.parseInt(departmentStr);

                    // API 호출
                    SignUpRequest request = new SignUpRequest(
                            username, password, name, phone,
                            address, gender, studentId, department, birthDate, email
                    );

                    RetrofitClient.getInstance().getUserApi().signup(request)
                            .enqueue(new Callback<BaseResponse<Void>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if(response.body().isSuccess()) {
                                            Toast.makeText(Register_Activity.this,
                                                    "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Register_Activity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // 서버에서 보낸 실패 메시지 표시
                                            String errorMessage = response.body().getMessage();
                                            Toast.makeText(Register_Activity.this,
                                                    errorMessage != null ? errorMessage : "회원가입 실패",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        try {
                                            // 에러 응답 확인
                                            String errorBody = response.errorBody().string();
                                            Toast.makeText(Register_Activity.this,
                                                    "서버 오류: " + errorBody, Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(Register_Activity.this,
                                                    "서버 오류가 발생했습니다. (상태 코드: " + response.code() + ")",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                                    Log.e("API_ERROR", "회원가입 실패", t);  // 로그 추가
                                    Toast.makeText(Register_Activity.this,
                                            "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(Register_Activity.this,
                            "오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isValidEmail(String email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }

            //비번
            private boolean isValidPassword(String password) {
                return password.length() >= 8;
            }


            //네트워크 홛인 로직
            private boolean isNetworkAvailable() {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}