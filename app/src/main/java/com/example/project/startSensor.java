package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class startSensor extends AppCompatActivity {


    Button backBtn2, visualiseDataBtn;
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

        Intent intent = getIntent();
        String timeFromDB = intent.getStringExtra("timeFromDB");
        double longitudeFromDB = intent.getDoubleExtra("longitudeFromDB", 0.0);
        double latitudeFromDB = intent.getDoubleExtra("latitudeFromDB", 0.0);
        String altitudeFromDB = intent.getStringExtra("altitudeFromDB");
        String deviceFromDB = intent.getStringExtra("deviceFromDB");
        String algorithmFromDB = intent.getStringExtra("algorithmFromDB");

        dispTimeStamp.setText(timeFromDB);
        dispLongitude.setText(String.format(String.valueOf(longitudeFromDB)));
        dispLatitude.setText(String.format(String.valueOf(latitudeFromDB)));
        dispAltitude.setText(altitudeFromDB);
        dispDevice.setText(deviceFromDB);
        dispAlgorithm.setText(algorithmFromDB);





        visualiseDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(startSensor.this, visualiseData.class);
                intent.putExtra("timeFromDB", timeFromDB);
                intent.putExtra("longitudeFromDB", longitudeFromDB);
                intent.putExtra("latitudeFromDB", latitudeFromDB);
                intent.putExtra("altitudeFromDB", altitudeFromDB);
                intent.putExtra("deviceFromDB", deviceFromDB);
                intent.putExtra("algorithmFromDB", algorithmFromDB);
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