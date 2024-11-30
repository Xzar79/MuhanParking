package com.example.muhanparking;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView myInfo = findViewById(R.id.btn_myinfo);
        ImageView parking = findViewById(R.id.btn_parking);
        ImageView regular = findViewById(R.id.btn_regular_parking);
        ImageView menuIcon = findViewById(R.id.menu_icon);
        ImageView settingsIcon = findViewById(R.id.settings_icon);
        ImageView notiIcon = findViewById(R.id.noti_icon);
        Button more = findViewById(R.id.btn_more);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 햄버거 메뉴 클릭 시 실행될 동작
                Intent intent = new Intent(MainActivity2.this, Menu_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        notiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, Notifications_Activity.class);
                startActivity(intent);
                finish();

            }
        });

        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, Simulation_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 설정 버튼 클릭 시 실행될 동작
                Intent intent = new Intent(MainActivity2.this, Setting_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        regular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, Regular_Parking_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, Mypage_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //자세히 보기 버튼
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, Empty_Parking_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}