package com.example.muhanparking;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.ImageView;

public class Parking_Location_Activity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ImageView back = findViewById(R.id.back_arrow);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Parking_Location_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 가천대학교 AI공학관 위치
        LatLng aiBuilding = new LatLng(37.455485, 127.133648);  // AI공학관의 실제 좌표

        // 기본 마커 추가
        MarkerOptions markerOptions = new MarkerOptions()
                .position(aiBuilding)
                .title("AI공학관 주차장")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mMap.addMarker(markerOptions);

        // 카메라를 AI공학관 위치로 이동 (줌 레벨 17)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(aiBuilding, 17));
    }
}