package com.example.project;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.project.databinding.ActivityVisualiseDataBinding;

public class visualiseData extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityVisualiseDataBinding binding;

    private LatLng sensor;

    String timeFromDB, altitudeFromDB, deviceFromDB, algorithmFromDB;
    double longitudeFromDB, latitudeFromDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = getIntent();
         timeFromDB = intent.getStringExtra("timeFromDB");
         longitudeFromDB = intent.getDoubleExtra("longitudeFromDB", 0.0);
         latitudeFromDB = intent.getDoubleExtra("latitudeFromDB", 0.0);
         altitudeFromDB = intent.getStringExtra("altitudeFromDB");
         deviceFromDB = intent.getStringExtra("deviceFromDB");
         algorithmFromDB = intent.getStringExtra("algorithmFromDB");



        binding = ActivityVisualiseDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        sensor = new LatLng(latitudeFromDB, longitudeFromDB);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(sensor).title("Longitude: " + longitudeFromDB +
                " Latitude: " + latitudeFromDB ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sensor));



    }
}