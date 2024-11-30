package com.example.muhanparking;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Setting_Activity extends AppCompatActivity {

    private Switch notificationSwitch, darkModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Switches 초기화
        notificationSwitch = findViewById(R.id.notification_switch);
        darkModeSwitch = findViewById(R.id.dark_mode_switch);

        // SharedPreferences를 사용해 설정 저장
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 알림 스위치 상태 복원
        boolean isNotificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        notificationSwitch.setChecked(isNotificationsEnabled);

        // 다크 모드 상태 복원
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(isDarkModeEnabled);

        // 알림 스위치 상태 변경
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("notifications", isChecked);
            editor.apply();
            Toast.makeText(this, "알림이 " + (isChecked ? "활성화" : "비활성화") + "되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // 다크 모드 스위치 상태 변경
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            Toast.makeText(this, "다크 모드가 " + (isChecked ? "활성화" : "비활성화") + "되었습니다.", Toast.LENGTH_SHORT).show();
            // 다크 모드 적용 로직 추가 필요 (Optional)
        });

        // 로그아웃 버튼 동작
        findViewById(R.id.logout_button).setOnClickListener(v -> {
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            // 로그아웃 동작 추가 필요
            finish();
        });
    }
}