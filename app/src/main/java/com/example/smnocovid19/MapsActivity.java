package com.example.smnocovid19;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Intent intent;
    double x,y;
    String buildingName, buildingFloor, dateTime;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        intent=getIntent();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildingName = intent.getStringExtra("buildingName");       //건물 이름
        buildingFloor = intent.getStringExtra("buildingFloor");     //건물 층수
        dateTime = intent.getStringExtra("dateTime");               //방문 시간

        if (buildingName.equals("본관")){
            x = 36.80028;
            y = 127.07485;
        } else if (buildingName.equals("원화관")){
            x = 36.80016;
            y = 127.07713;
        } else if (buildingName.equals("자연관")){
            x = 36.79874;
            y = 127.07402;
        } else if (buildingName.equals("인문관")){
            x = 36.79879;
            y = 127.07584;
        } else if (buildingName.equals("공학관")){
            x = 36.80014;
            y = 127.07265;
        } else if (buildingName.equals("도서관")){
            x = 36.797593;
            y = 127.075920;
        } else if (buildingName.equals("보건관")){
            x = 36.799114;
            y = 127.078301;
        } else if (buildingName.equals("학생회관")){
            x = 36.79754;
            y = 127.07734;
        } else if (buildingName.equals("기숙사신관")){
            x = 36.79596;
            y = 127.06973;
        } else if (buildingName.equals("기숙사구관")){
            x = 36.79616;
            y = 127.07075;
        } else if (buildingName.equals("스포츠과학관")){
            x = 36.801119;
            y = 127.070314;
        }

        LatLng target = new LatLng(x, y);   //좌표 위치 지정
        mMap.addMarker(new MarkerOptions().position(target).title(buildingName + " " + buildingFloor + "  방문 시간 : " + dateTime));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(target));
    }
}