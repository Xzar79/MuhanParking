package com.example.muhanparking;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
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

        // 기타 버튼
        Button btnMore = findViewById(R.id.btn_more);

        // 텍스트뷰들 폰트 적용
        TextView textMyInfo = findViewById(R.id.text_my_info);

        textMyInfo.setTypeface(pretendardBold);

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

    /* 주차 현황 업데이트 메서드
    private void updateParkingDisplay(int normalAvailable, int normalTotal,
                                      int disabledAvailable, int disabledTotal) {
        LinearLayout normalSpace = findViewById(R.id.normal_space_layout);
        LinearLayout disabledSpace = findViewById(R.id.disabled_space_layout);

        TextView normalText = normalSpace.findViewById(R.id.space_count);
        TextView disabledText = disabledSpace.findViewById(R.id.space_count);

        normalText.setText(String.format("%d / %d", normalAvailable, normalTotal));
        disabledText.setText(String.format("%d / %d", disabledAvailable, disabledTotal));
    }*/
}