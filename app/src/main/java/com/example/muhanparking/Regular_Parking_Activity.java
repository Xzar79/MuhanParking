package com.example.muhanparking;

import android.net.Uri;
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
                Intent intent = new Intent(Regular_Parking_Activity.this, MainActivity.class);
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
                // 가천대 포털 사이트 이동
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sso.gachon.ac.kr/svc/tk/Auth.do?ac=Y&ifa=N&id=portal&"));
                startActivity(intent);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Regular_Parking_Activity.this, Payment_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}