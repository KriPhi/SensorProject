package com.vu.lorasensorapp;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class liveTrackData extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_track_data);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng center = new LatLng(0, 0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 1));
        getData();
    }

    private void getData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thingspeak.com/channels/2054190/feeds.json?api_key=0ESB1QG9MBNFQRGD?results=1")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        JSONArray jsonArray = jsonObject.getJSONArray("feeds");
                        if (jsonArray.length() > 0) {
                            JSONObject feed = jsonArray.getJSONObject(0);
                            double longitude = feed.getDouble("field2");
                            double latitude = feed.getDouble("field3");
                            LatLng location = new LatLng(latitude, longitude);
                            runOnUiThread(() -> {
                                if (marker == null) {
                                    MarkerOptions options = new MarkerOptions()
                                            .position(location)
                                            .title("Latest Entry");
                                    marker = mMap.addMarker(options);
                                } else {
                                    marker.setPosition(location);
                                }
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Schedule the next call to getData() after 5 seconds
                new android.os.Handler().postDelayed(() -> getData(), 5000);
            }
        });
    }
}
