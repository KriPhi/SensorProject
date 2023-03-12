package com.vu.lorasensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.vu.lorasensorapp.myAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class searchData extends AppCompatActivity {
    private Button lastWeekButton;
    private Button lastMonthButton;
    private Button searchButton, bckBtnSearch;
    private EditText timestampEditText;
    private RecyclerView recyclerView;



    private List<String> timeStampList = new ArrayList<>();
    private List<String> longitudeList = new ArrayList<>();
    private List<String> latitudeList = new ArrayList<>();
    private List<String> altitudeList = new ArrayList<>();
    private List<String> deviceIDList = new ArrayList<>();
    private List<String> algorithmList = new ArrayList<>();

    private myAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);

        // Initialize views
        lastWeekButton = findViewById(R.id.last_week_button);
        lastMonthButton = findViewById(R.id.last_month_button);
        bckBtnSearch = findViewById(R.id.bckBtnSearch);
        searchButton = findViewById(R.id.searchBtn);
        timestampEditText = findViewById(R.id.timestamp_edit_text);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new myAdapter(new ArrayList<>(), new myAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String field1, int position) {
                String timeStamp = timeStampList.get(position);
                double longitude = Double.parseDouble(longitudeList.get(position));
                double latitude = Double.parseDouble(latitudeList.get(position));
                double altitude = Double.parseDouble(altitudeList.get(position));
                String deviceID = deviceIDList.get(position);
                String algorithm = algorithmList.get(position);

                Intent intent = new Intent(searchData.this, startSensor.class);
                intent.putExtra("timeStampDB", longitude);
                intent.putExtra("longitudeDB", longitude);
                intent.putExtra("latitudeDB", latitude);
                intent.putExtra("altitudeDB", altitude);
                intent.putExtra("deviceIDDB", deviceID);
                intent.putExtra("algorithmDB", algorithm);
                startActivity(intent);




            }
        });
        recyclerView.setAdapter(adapter);







        lastWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                adapter.notifyDataSetChanged();
                Calendar calendar = Calendar.getInstance();
                Date endDate = calendar.getTime();
                calendar.add(Calendar.DATE, -7);
                Date startDate = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startDateString = format.format(startDate);
                String endDateString = format.format(endDate);

                new GetDataTask("https://api.thingspeak.com/channels/2054190/feeds.json?api_key=0ESB1QG9MBNFQRGD&start="
                        + startDateString + "&end=" + endDateString).execute();
            }
        });




        lastMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                Calendar calendar = Calendar.getInstance();
                Date endDate = calendar.getTime();
                calendar.add(Calendar.MONTH, -1);
                Date startDate = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startDateString = format.format(startDate);
                String endDateString = format.format(endDate);

                new GetDataTask("https://api.thingspeak.com/channels/2054190/feeds.json?api_key=0ESB1QG9MBNFQRGD" +
                        "&start="
                        + startDateString + "&end=" + endDateString).execute();
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
                String keyword = timestampEditText.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = format.parse(keyword);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Date startDate = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    Date endDate = calendar.getTime();
                    String startDateString = format.format(startDate);
                    String endDateString = format.format(endDate);

                    String urlString = "https://api.thingspeak.com/channels/2054190/feeds.json?api_key=0ESB1QG9MBNFQRGD&start="
                            + startDateString + "&end=" + endDateString;
                    new GetDataTaskByKeyword(urlString, keyword,true).execute();

                } else {
                    String urlString = "https://api.thingspeak.com/channels/2054190/feeds.json?api_key=0ESB1QG9MBNFQRGD&field5=" + keyword;
                    new GetDataTaskByKeyword(urlString, keyword, false).execute();
                }
            }
        });

        bckBtnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(searchData.this, adminHome.class);
                startActivity(intent);
            }
        });
    }


    private class GetDataTask extends AsyncTask<String, Void, String> {
        private String urlString;

        public GetDataTask(String url) {
            this.urlString = url;
        }

        protected String doInBackground(String... params) {
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

        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("feeds");




                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject feed = jsonArray.getJSONObject(i);
                        String timeStamp = feed.getString("field1");
                        String longitude = feed.getString("field2");
                        String latitude = feed.getString("field3");
                        String altitude = feed.getString("field4");
                        String deviceID = feed.getString("field5");
                        String algorithm = feed.getString("field6");
                        Log.d("RetrieveData", ", Field1: " + timeStamp + ", Field2: " + longitude + ", Field3: " + latitude + ", Field4: " + altitude + ", Field5: " + deviceID + ", Field6: " + algorithm);

                        timeStampList.add(timeStamp);
                        longitudeList.add(longitude);
                        latitudeList.add(latitude);
                        altitudeList.add(altitude);
                        deviceIDList.add(deviceID);
                        algorithmList.add(algorithm);



                    }
                    adapter.setDataList(timeStampList);


                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("RetrieveData", e.getMessage());
                }
            } else {
                Log.e("RetrieveData", "Error retrieving data");
            }
        }

    }



    private class GetDataTaskByKeyword extends AsyncTask<String, Void, String> {
        private boolean isDate;
        private String urlString;
        private String keyword;

        public GetDataTaskByKeyword(String url, String keyword, boolean isDate) {
            this.urlString = url;
            this.keyword = keyword;
            this.isDate = isDate;
        }

        protected String doInBackground(String... params) {
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

        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("feeds");

                    List<String> matchingTimeStamps = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject feed = jsonArray.getJSONObject(i);
                        String timeStamp = feed.getString("field1");
                        String longitude = feed.getString("field2");
                        String latitude = feed.getString("field3");
                        String altitude = feed.getString("field4");
                        String deviceID = feed.getString("field5");
                        String algorithm = feed.getString("field6");
                        Log.d("RetrieveData", ", Field1: " + timeStamp + ", Field2: " + longitude + ", Field3: " + latitude + ", Field4: " + altitude + ", Field5: " + deviceID + ", Field6: " + algorithm);

                        timeStampList.add(timeStamp);
                        longitudeList.add(longitude);
                        latitudeList.add(latitude);
                        altitudeList.add(altitude);
                        deviceIDList.add(deviceID);
                        algorithmList.add(algorithm);



                        if (deviceID.contains(keyword)) {
                            matchingTimeStamps.add(timeStamp);
                        }
                    }

                    if (isDate) {
                        adapter.setDataList(timeStampList);
                    } else {
                        adapter.setDataList(matchingTimeStamps);
                    }




                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("RetrieveData", e.getMessage());
                }
            } else {
                Log.e("RetrieveData", "Error retrieving data");
            }
        }

    }

    private void clearData() {
        timeStampList.clear();
        longitudeList.clear();
        latitudeList.clear();
        altitudeList.clear();
        deviceIDList.clear();
        algorithmList.clear();
    }
}