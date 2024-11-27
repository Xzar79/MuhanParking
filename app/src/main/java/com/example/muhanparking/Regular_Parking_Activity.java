package com.example.muhanparking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class Regular_Parking_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regular_parking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView back = findViewById(R.id.back_arrow);
        ImageView help = findViewById(R.id.help);

        TextView textView = findViewById(R.id.regular_parking_textview);

        Button appInquery = findViewById(R.id.btn_app_inquery);
        Button download = findViewById(R.id.btn_download_paper);
        Button upload = findViewById(R.id.btn_upload_paper);
        Button inqeuryPaper = findViewById(R.id.btn_inquery_paper);
        Button payment = findViewById(R.id.btn_payment_manage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Regular_Parking_Activity.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Regular_Parking_Activity.this, Help_Activity.class);
                startActivity(intent);
            }
        });

        appInquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Regular_Parking_Activity.this, "신청 및 조회하기", Toast.LENGTH_SHORT).show();
            }
        });
    }
}