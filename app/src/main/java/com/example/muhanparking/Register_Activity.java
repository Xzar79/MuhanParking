package com.example.muhanparking;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.text.TextUtils;
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
        EditText ctAcc = findViewById(R.id.ctAccountNumber);
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

                    // 학번과 학과 코드가 숫자인지 검사
                    if (!studentIdStr.matches("\\d+") || !departmentStr.matches("\\d+")) {
                        Toast.makeText(Register_Activity.this,
                                "학번과 학과 코드는 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int studentId = Integer.parseInt(studentIdStr);
                    int department = Integer.parseInt(departmentStr);

                    // API 호출
                    SignUpRequest request = new SignUpRequest(
                            username, password, name, phone,
                            address, gender, studentId, department, birthDate
                    );

                    RetrofitClient.getInstance().getUserApi().signup(request)
                            .enqueue(new Callback<BaseResponse<Void>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<Void>> call,
                                                       Response<BaseResponse<Void>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if(response.body().isSuccess()) {
                                            Toast.makeText(Register_Activity.this,
                                                    "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Register_Activity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Register_Activity.this,
                                                    response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Register_Activity.this,
                                                "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                                    Toast.makeText(Register_Activity.this,
                                            "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(Register_Activity.this,
                            "오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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