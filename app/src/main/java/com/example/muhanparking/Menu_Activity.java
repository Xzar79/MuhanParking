package com.example.muhanparking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

public class Menu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        ImageView backIcon = findViewById(R.id.back_arrow);
        Button regularParking = findViewById(R.id.btn_regular_parking);
        Button inqueryApp = findViewById(R.id.btn_inquery_app);
        Button inqueryLoc = findViewById(R.id.btn_inquery_location);
        Button recommandParking = findViewById(R.id.btn_recommand_parking);
        Button simulation = findViewById(R.id.btn_simulation);
        Button emptyParking = findViewById(R.id.btn_empty_parking);
        Button mypage = findViewById(R.id.btn_mypage);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        regularParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Regular_Parking_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        inqueryApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "신청 조회 페이지", Toast.LENGTH_SHORT).show();
            }
        });

        inqueryLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "주차장 위치 조회 페이지", Toast.LENGTH_SHORT).show();
            }
        });

        recommandParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "실시간 주차장 추천", Toast.LENGTH_SHORT).show();
            }
        });

        simulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "실시간 주차 공간 조회", Toast.LENGTH_SHORT).show();
            }
        });

        emptyParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "남은 주차 자리 수 보기", Toast.LENGTH_SHORT).show();
            }
        });

        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "마이페이지", Toast.LENGTH_SHORT).show();
            }
        });
    }
}