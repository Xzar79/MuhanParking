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
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                String email = ctEmail.getText().toString();
                String password1 = ctPassword.getText().toString();
                String password2 = ctPassword2.getText().toString();
                String id = ctId.getText().toString();
                String name = ctName.getText().toString();
                String tel = ctPhone.getText().toString();
                String add = ctAdd.getText().toString();
                String acc = ctAcc.getText().toString();
                String stuId = ctStuId.getText().toString();
                String dep = ctDepart.getText().toString();
                String birth = ctBir.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
                    Toast.makeText(Register_Activity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    if(password1.equals(password2)) {
                        Intent intent = new Intent(Register_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(Register_Activity.this, "Invalid Password.", Toast.LENGTH_SHORT).show();
                    }
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