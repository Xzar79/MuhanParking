package com.example.muhanparking;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Empty_Parking_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_parking);

        // 뒤로가기 버튼
        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Empty_Parking_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        RelativeLayout zoneA = findViewById(R.id.zone_a);
        RelativeLayout zoneB = findViewById(R.id.zone_b);
        RelativeLayout zoneC = findViewById(R.id.zone_c);

        // 각 구역 클릭 리스너 설정
        zoneA.setOnClickListener(v -> {
            Intent intent = new Intent(Empty_Parking_Activity.this, Section_A_Activity.class);
            intent.putExtra("zone_id", "A");
            startActivity(intent);
        });

        zoneB.setOnClickListener(v -> {
            Intent intent = new Intent(Empty_Parking_Activity.this, Section_B_Activity.class);
            intent.putExtra("zone_id", "B");
            startActivity(intent);
        });

        zoneC.setOnClickListener(v -> {
            Intent intent = new Intent(Empty_Parking_Activity.this, Section_C_Activity.class);
            intent.putExtra("zone_id", "C");
            startActivity(intent);
        });
    }
    }