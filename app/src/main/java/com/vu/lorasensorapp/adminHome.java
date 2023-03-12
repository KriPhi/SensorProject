package com.vu.lorasensorapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class adminHome extends AppCompatActivity {

    Button startSensorBtn, searchDataBtn, userSettingsBtn, logOutBtn;

    Boolean isButton2Enabled = false, isButton3Enabled = false, isButton4Enabled = false;
    SharedPreferences sharedPreferences;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        startSensorBtn = findViewById(R.id.startSensorBtn);
        searchDataBtn = findViewById(R.id.searchDataBtn);
        userSettingsBtn = findViewById(R.id.userSettingsBtn);
        logOutBtn = findViewById(R.id.logOutBtn);



        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        isButton2Enabled = sharedPreferences.getBoolean("isButton2Enabled", false);
        isButton3Enabled = sharedPreferences.getBoolean("isButton3Enabled", false);
        isButton4Enabled = sharedPreferences.getBoolean("isButton4Enabled", false);


        if (adminPreference.isAdmin(this)){
            startSensorBtn.setEnabled(true);
            searchDataBtn.setEnabled(true);
            userSettingsBtn.setEnabled(true);
        } else {
            startSensorBtn.setEnabled(isButton2Enabled);
            if (isButton2Enabled) {
                startSensorBtn.setBackgroundColor(getResources().getColor(R.color.mayablue));
            } else {
                startSensorBtn.setBackgroundColor(getResources().getColor(R.color.grey));
            }

            searchDataBtn.setEnabled(isButton3Enabled);
            if (isButton3Enabled) {
                searchDataBtn.setBackgroundColor(getResources().getColor(R.color.mayablue));
            } else {
                searchDataBtn.setBackgroundColor(getResources().getColor(R.color.grey));
            }

            userSettingsBtn.setEnabled(isButton4Enabled);
            if (isButton4Enabled) {
                userSettingsBtn.setBackgroundColor(getResources().getColor(R.color.mayablue));
            } else {
                userSettingsBtn.setBackgroundColor(getResources().getColor(R.color.grey));
            }


        }


        startSensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = "https://api.thingspeak.com/channels/2054190/feeds.json?api_key=0ESB1QG9MBNFQRGD";
                new GetDataTask().execute(urlString);

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
                Intent intent = new Intent(adminHome.this, userSettings.class);
                intent.putExtra("isButton2Enabled", isButton2Enabled);
                intent.putExtra("isButton3Enabled", isButton3Enabled);
                intent.putExtra("isButton4Enabled", isButton4Enabled);
                startActivityForResult(intent, 1);



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                isButton2Enabled = data.getBooleanExtra("isButton2Enabled", false);
                isButton3Enabled = data.getBooleanExtra("isButton3Enabled", false);
                isButton4Enabled = data.getBooleanExtra("isButton4Enabled", false);

                if (adminPreference.isAdmin(this)) {
                    startSensorBtn.setEnabled(true);
                    searchDataBtn.setEnabled(true);
                    userSettingsBtn.setEnabled(true);
                } else {
                    startSensorBtn.setEnabled(isButton2Enabled);
                    if (isButton2Enabled) {
                        startSensorBtn.setBackgroundColor(getResources().getColor(R.color.mayablue));
                    } else {
                        startSensorBtn.setBackgroundColor(getResources().getColor(R.color.grey));
                    }

                    searchDataBtn.setEnabled(isButton3Enabled);
                    if (isButton3Enabled) {
                        searchDataBtn.setBackgroundColor(getResources().getColor(R.color.mayablue));
                    } else {
                        searchDataBtn.setBackgroundColor(getResources().getColor(R.color.grey));
                    }

                    userSettingsBtn.setEnabled(isButton4Enabled);
                    if (isButton4Enabled) {
                        userSettingsBtn.setBackgroundColor(getResources().getColor(R.color.mayablue));
                    } else {
                        userSettingsBtn.setBackgroundColor(getResources().getColor(R.color.grey));
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isButton2Enabled", isButton2Enabled);
                    editor.putBoolean("isButton3Enabled", isButton3Enabled);
                    editor.putBoolean("isButton4Enabled", isButton4Enabled);
                    editor.apply();
                }
            } else {
            }
        } else {
        }


    }



    private class GetDataTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params){
            String urlString = params[0] + "&results=1";
            String result = "";

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.e("RetrieveData", e.getMessage());
                result = null;
            }
            return result;
        }

        protected void onPostExecute(String result){
            if (result != null){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("feeds");
                    Log.d("RetrieveData", "Result string: " + result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject feed = jsonArray.getJSONObject(i);
                        String timeStampDB = feed.optString("field1");
                        double longitudeDB = Double.parseDouble(feed.getString("field2"));
                        double latitudeDB = Double.parseDouble(feed.getString("field3"));
                        double altitudeDB = Double.parseDouble(feed.getString("field4"));
                        String deviceIDDB = feed.getString("field5");
                        String algorithmDB = feed.getString("field6");
                        Log.d("RetrieveData", "Field 1 value: " + timeStampDB + ", Field 2 value: " + longitudeDB + ", Field 3 value: " + latitudeDB + ", Field 4 value: " + altitudeDB + ", Field 5 value: " + deviceIDDB + ", Field 6 value: " + algorithmDB);

                      /*  switch (algorithmDB) {
                            case "01":
                                int decryptedLongitude = TEAdecrypt.decrypt(longitudeDB);
                                double decryptLongitude = (double) decryptedLongitude;
                                int decryptedLatitude = TEAdecrypt.decrypt(latitudeDB);
                                double decryptLatitude = (double) decryptedLatitude;
                                int decryptedAltitude = TEAdecrypt.decrypt(altitudeDB);
                                double decryptAltitude = (double) decryptedAltitude;
                                break;
                            case "02":
                                int decryptedLongitudeXTEA = XTEAdecrypt.decrypt(longitudeDB);
                                double decryptLongitudeXTEA = (double) decryptedLongitudeXTEA;
                                int decryptedLatitudeXTEA = XTEAdecrypt.decrypt(latitudeDB);
                                double decryptLatitudeXTEA = (double) decryptedLatitudeXTEA;
                                int decryptedAltitudeXTEA = XTEAdecrypt.decrypt(altitudeDB);
                                double decryptAltitudeXTEA = (double) decryptedAltitudeXTEA;
                                break;
                            case "03":
                                int decryptedLongitudeXXTEA = XXTEAdecrypt.decrypt(longitudeDB);
                                double decryptLongitudeXXTEA = (double) decryptedLongitudeXXTEA;
                                int decryptedLatitudeXXTEA = XXTEAdecrypt.decrypt(latitudeDB);
                                double decryptLatitudeXXTEA = (double) decryptedLatitudeXXTEA;
                                int decryptedAltitudeXXTEA = XXTEAdecrypt.decrypt(altitudeDB);
                                double decryptAltitudeXXTEA = (double) decryptedAltitudeXXTEA;
                                break;
                            case "04":
                                String decryptedLongitudeVigenere = VigenereDecrypt.decrypt(longitudeDB);
                                double decryptLongitudeVigenere = Double.parseDouble(decryptedLongitudeVigenere);
                                String decryptedLatitudeVigenere = VigenereDecrypt.decrypt(latitudeDB);
                                double decryptLatitudeVigenere = Double.parseDouble(decryptedLatitudeVigenere);
                                String decryptedAltitudeVigenere = VigenereDecrypt.decrypt(altitudeDB);
                                double decryptAltitudeVigenere = Double.parseDouble(decryptedAltitudeVigenere);
                                break;
                            default:
                                Log.e("RetrieveData", "Invalid algorithm name");
                                break;
                        }
*/

                        Intent intent = new Intent(adminHome.this, startSensor.class);
                        intent.putExtra("timeStampDB", timeStampDB);
                        intent.putExtra("longitudeDB", longitudeDB);
                        intent.putExtra("latitudeDB", latitudeDB);
                        intent.putExtra("altitudeDB", altitudeDB);
                        intent.putExtra("deviceIDDB", deviceIDDB);
                        intent.putExtra("algorithmDB", algorithmDB);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("RetrieveData", e.getMessage());
                }
            } else {
                Log.e("RetrieveData", "Error retrieving data");
            }
        }
    }
}