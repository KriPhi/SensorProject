package com.vu.lorasensorapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vu.lorasensorapp.databinding.ActivityVisualiseDataBinding;


public class visualiseData extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityVisualiseDataBinding binding;

    private LatLng sensor;

    String timeStampDB, altitudeDB, deviceIDDB, algorithmDB;
    double longitudeDB, latitudeDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = getIntent();
        timeStampDB = intent.getStringExtra("timeStampDB");
        longitudeDB = intent.getDoubleExtra("longitudeDB", 0.0);
        latitudeDB = intent.getDoubleExtra("latitudeDB", 0.0);
        altitudeDB = intent.getStringExtra("altitudeDB");
        deviceIDDB = intent.getStringExtra("deviceIDDB");
        algorithmDB = intent.getStringExtra("algorithmDB");



        binding = ActivityVisualiseDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        sensor = new LatLng(latitudeDB, longitudeDB);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(sensor).title("Longitude: " + longitudeDB +
                " Latitude: " + latitudeDB ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sensor));



    }
}