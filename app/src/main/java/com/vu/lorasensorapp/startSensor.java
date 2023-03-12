package com.vu.lorasensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class startSensor extends AppCompatActivity {


    Button backBtn2, visualiseDataBtn, visualiseDataBtn2;
    TextView dispTimeStamp, dispLongitude, dispLatitude, dispAltitude, dispDevice, dispAlgorithm;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_sensor);
        dispTimeStamp = findViewById(R.id.dispTimeStamp);
        dispLongitude = findViewById(R.id.dispLongitude);
        dispLatitude = findViewById(R.id.dispLatitude);
        dispAltitude = findViewById(R.id.dispAltitude);
        dispDevice= findViewById(R.id.dispDevice);
        dispAlgorithm= findViewById(R.id.dispAlgorithm);
        backBtn2 = findViewById(R.id.backBtn2);
        visualiseDataBtn = findViewById(R.id.visualiseDataBtn);
        visualiseDataBtn2 = findViewById(R.id.liveTrackingBtn);

        Intent intent = getIntent();
        String timeStampDB = intent.getStringExtra("timeStampDB");
        double longitudeDB = intent.getDoubleExtra("longitudeDB", 0.0);
        double latitudeDB = intent.getDoubleExtra("latitudeDB", 0.0);
        double altitudeDB = intent.getDoubleExtra("altitudeDB", 0.0);
        String deviceIDDB = intent.getStringExtra("deviceIDDB");
        String algorithmDB = intent.getStringExtra("algorithmDB");

        dispTimeStamp.setText("Time Stamp: " +timeStampDB);
        dispLongitude.setText(String.format(String.valueOf("Longitude: "+longitudeDB)));
        dispLatitude.setText(String.format(String.valueOf("Latitude: "+latitudeDB)));
        dispAltitude.setText("Altitude: "+altitudeDB);
        dispDevice.setText("Device ID: "+deviceIDDB);
        dispAlgorithm.setText("Algorithm Type: "+algorithmDB);








        visualiseDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(startSensor.this, visualiseData.class);
                intent.putExtra("timeStampDB", timeStampDB);
                intent.putExtra("longitudeDB", longitudeDB);
                intent.putExtra("latitudeDB", latitudeDB);
                intent.putExtra("altitudeDB", altitudeDB);
                intent.putExtra("deviceIDDB", deviceIDDB);
                intent.putExtra("algorithmDB", algorithmDB);
                startActivity(intent);
            }
        });

        visualiseDataBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(startSensor.this, liveTrackData.class);
                intent.putExtra("timeStampDB", timeStampDB);
                intent.putExtra("longitudeDB", longitudeDB);
                intent.putExtra("latitudeDB", latitudeDB);
                intent.putExtra("altitudeDB", altitudeDB);
                intent.putExtra("deviceIDDB", deviceIDDB);
                intent.putExtra("algorithmDB", algorithmDB);
                startActivity(intent);
            }
        });






        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(startSensor.this, adminHome.class);
                startActivity(intent);
            }
        });
    }
}