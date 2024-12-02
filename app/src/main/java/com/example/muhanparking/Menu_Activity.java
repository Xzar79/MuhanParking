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
        Button simulation = findViewById(R.id.btn_simulation);
        Button emptyParking = findViewById(R.id.btn_empty_parking);
        Button mypage = findViewById(R.id.btn_mypage);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, MainActivity.class);
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
                Toast.makeText(Menu_Activity.this, "신청이 되었는지 안되었는지 모릅니다.", Toast.LENGTH_SHORT).show();
            }
        });

        inqueryLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Parking_Location_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        simulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Simulation_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        emptyParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Empty_Parking_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Mypage_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}