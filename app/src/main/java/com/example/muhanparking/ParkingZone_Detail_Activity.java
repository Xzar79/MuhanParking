package com.example.muhanparking;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ParkingZone_Detail_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_zone_detail);

        // 선택된 구역 정보 받기
        String zoneId = getIntent().getStringExtra("zone_id");

        // 뒤로가기 버튼
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        // 구역 제목 설정
        TextView zoneTitle = findViewById(R.id.zone_title);
        zoneTitle.setText(zoneId + "구역 주차장");

        // 구역별 주차장 이미지 설정
        ImageView parkingLayout = findViewById(R.id.parking_layout);
        switch (zoneId) {
            case "A":
                parkingLayout.setImageResource(R.drawable.zone_a_layout);
                break;
            case "B":
                parkingLayout.setImageResource(R.drawable.zone_b_layout);
                break;
            case "C":
                parkingLayout.setImageResource(R.drawable.zone_c_layout);
                break;
        }

        // 임시 데이터로 주차 현황 표시
        TextView normalCount = findViewById(R.id.txt_normal_count);
        TextView disabledCount = findViewById(R.id.txt_disabled_count);

        switch (zoneId) {
            case "A":
                normalCount.setText("35 / 50");
                disabledCount.setText("8 / 10");
                break;
            case "B":
                normalCount.setText("28 / 40");
                disabledCount.setText("5 / 8");
                break;
            case "C":
                normalCount.setText("20 / 30");
                disabledCount.setText("3 / 5");
                break;
        }
    }
}