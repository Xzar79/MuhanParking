package com.example.muhanparking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.example.muhanparking.util.PreferenceManager;

public class Mypage_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mypage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
}