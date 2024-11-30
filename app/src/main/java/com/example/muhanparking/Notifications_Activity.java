package com.example.muhanparking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Notifications_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view_notifications);

        // 알림 리스트 데이터 준비
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("알림 1", "알림 세부 내용 1"));
        notifications.add(new Notification("알림 2", "알림 세부 내용 2"));
        notifications.add(new Notification("알림 3", "알림 세부 내용 3"));

        // RecyclerView 어댑터 설정
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}