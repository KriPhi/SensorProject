package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminHome extends AppCompatActivity {

    Button startSensorBtn, searchDataBtn, userSettingsBtn, logOutBtn;
    Double latitudeFromDB, longitudeFromDB;



    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        startSensorBtn = findViewById(R.id.startSensorBtn);
        searchDataBtn = findViewById(R.id.searchDataBtn);
        userSettingsBtn = findViewById(R.id.userSettingsBtn);
        logOutBtn = findViewById(R.id.logOutBtn);

        Intent intent = getIntent();
        String roleFromDB = intent.getStringExtra("roleFromDB");




        if (!"admin".equals(roleFromDB)){
            userSettingsBtn.setVisibility(View.GONE);
        } else {
            userSettingsBtn.setVisibility(View.VISIBLE);
        }


        startSensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("sensor");

                Query query = reference.orderByChild("date").limitToLast(1);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot recentSnapshot = snapshot.getChildren().iterator().next();

                            String timeFromDB = recentSnapshot.child("date").getValue(String.class);
                            longitudeFromDB = Double.parseDouble(recentSnapshot.child("longi").getValue(String.class));
                            latitudeFromDB = Double.parseDouble(recentSnapshot.child("lati").getValue(String.class));
                            String altitudeFromDB = recentSnapshot.child("alti").getValue(String.class);
                            String deviceFromDB = recentSnapshot.child("device").getValue(String.class);
                            String algorithmFromDB = recentSnapshot.child("algo").getValue(String.class);

                            Intent intent = new Intent(adminHome.this, startSensor.class);
                            intent.putExtra("timeFromDB", timeFromDB);
                            intent.putExtra("longitudeFromDB", longitudeFromDB);
                            intent.putExtra("latitudeFromDB", latitudeFromDB);
                            intent.putExtra("altitudeFromDB", altitudeFromDB);
                            intent.putExtra("deviceFromDB", deviceFromDB);
                            intent.putExtra("algorithmFromDB", algorithmFromDB);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


        searchDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminHome.this, searchData.class);
                startActivity(intent);
            }
        });

        userSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminHome.this, adminSettings.class);
                startActivity(intent);
            }
        });


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminHome.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}