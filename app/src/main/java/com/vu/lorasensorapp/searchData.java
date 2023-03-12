package com.vu.lorasensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
        adapter = new myAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);





        lastWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String searchTimestamp = timestampEditText.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = dateFormat.parse(searchTimestamp);
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
                    String urlString = "https://api.thingspeak.com/channels/2054190/feeds.json?api_key=0ESB1QG9MBNFQRGD&start="
                            + startDate.getTime() + "&end=" + endDate.getTime();
                    new GetDataTask(urlString).execute();
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
                    List<String> field1List = new ArrayList<>();
                    List<String> field2List = new ArrayList<>();
                    List<String> field3List = new ArrayList<>();
                    List<String> field4List = new ArrayList<>();
                    List<String> field5List = new ArrayList<>();
                    List<String> field6List = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject feed = jsonArray.getJSONObject(i);
                        String timeStampDB = feed.getString("created_at");
                        String field1DB = feed.getString("field1");
                        String field2DB = feed.getString("field2");
                        String field3DB = feed.getString("field3");
                        String field4DB = feed.getString("field4");
                        String field5DB = feed.getString("field5");
                        String field6DB = feed.getString("field6");
                        Log.d("RetrieveData", ", TimeStamp: " + timeStampDB + ", Field1: " + field1DB + ", Field2: " + field2DB + ", Field3: " + field3DB + ", Field4: " + field4DB + ", Field5: " + field5DB + ", Field6: " + field6DB);

                        field1List.add(field1DB);
                        field2List.add(field2DB);
                        field3List.add(field3DB);
                        field4List.add(field4DB);
                        field5List.add(field5DB);
                        field6List.add(field6DB);
                    }
                    adapter.setDataList(field1List);


                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("RetrieveData", e.getMessage());
                }
            } else {
                Log.e("RetrieveData", "Error retrieving data");
            }
        }

    }
}

