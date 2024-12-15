package com.example.muhanparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.muhanparking.api.RetrofitClient;
import com.example.muhanparking.model.request.FindUsernameRequest;
import com.example.muhanparking.model.response.BaseResponse;
import com.example.muhanparking.model.response.FindUsernameResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Find_ID_Activity extends AppCompatActivity {
    private EditText nameInput;
    private EditText studentIdInput;
    private EditText birthDateInput;
    private Button findIdButton;
    private TextView idResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_id);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.name_input);
        studentIdInput = findViewById(R.id.student_id_input);
        birthDateInput = findViewById(R.id.birth_date_input);
        findIdButton = findViewById(R.id.find_id_button);
        idResult = findViewById(R.id.id_result);
        ImageView back = findViewById(R.id.back_arrow);

        findIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String studentIdStr = studentIdInput.getText().toString();
                String birthDate = birthDateInput.getText().toString();

                // Validation
                if (name.isEmpty()) {
                    nameInput.setError("이름을 입력해주세요");
                    return;
                }
                if (studentIdStr.isEmpty()) {
                    studentIdInput.setError("학번을 입력해주세요");
                    return;
                }
                if (birthDate.isEmpty()) {
                    birthDateInput.setError("생년월일을 입력해주세요");
                    return;
                }

                Integer studentId = Integer.parseInt(studentIdStr);
                FindUsernameRequest request = new FindUsernameRequest(name, studentId, birthDate);
                findUsername(request);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Find_ID_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findUsername(FindUsernameRequest request) {
        RetrofitClient.getInstance().getUserApi().findUsername(request)
                .enqueue(new Callback<BaseResponse<FindUsernameResponse>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<FindUsernameResponse>> call,
                                           Response<BaseResponse<FindUsernameResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String maskedUsername = response.body().getData().getMaskedUsername();
                            idResult.setText("찾은 아이디: " + maskedUsername);
                            idResult.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(Find_ID_Activity.this,
                                    "아이디를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<FindUsernameResponse>> call, Throwable t) {
                        Toast.makeText(Find_ID_Activity.this,
                                "네트워크 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}